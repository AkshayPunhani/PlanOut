package com.example.MC_PlanItOut;

import java.util.ArrayList;
import java.util.HashMap;

//import com.example.helloworld.FloatingActionButton;





import java.util.List;

import com.google.android.gms.maps.model.LatLng;
import com.parse.Parse;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {

	// flag for Internet connection status
	Boolean isInternetPresent = false;

	// Connection detector class
	ConnectionDetector cd;
	
	// Alert Dialog Manager
	AlertDialogManager alert = new AlertDialogManager();

	// Google Places
	GooglePlaces googlePlaces;
	final String str1=null;
	double lat =  0.0;
	double longi = 0.0;
	
	// Places List
	PlacesList nearPlaces;

	// GPS Location
	GPSTracker gps;

	// Button
	Button btnShowOnMap;

	// Progress dialog
	ProgressDialog pDialog;
	
	// Places Listview
	ListView lv;
	 EditText inputSearch;
	
	// ListItems data
	ArrayList<HashMap<String, String>> placesListItems = new ArrayList<HashMap<String,String>>();
	
	FloatingActionButton fabButton;


	// KEY Strings
	public static String KEY_REFERENCE = "reference"; // id of the place
	public static String KEY_NAME = "name"; // name of the place
	public static String KEY_VICINITY = "vicinity"; // Place area name

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		
		fabButton = new FloatingActionButton.Builder(this)
        .withDrawable(getResources().getDrawable(R.drawable.action))
        .withButtonColor(Color.TRANSPARENT)
        .withGravity(Gravity.BOTTOM | Gravity.RIGHT)
        .withMargins(0, 0, 20, 20)
        .create();
        
		Intent intent = getIntent();
        final String str = intent.getExtras().getString("straddress");
        //Log.d("Address",str);
        
        
		
        fabButton.setOnClickListener(new View.OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		Log.d("Button","Pressed");
        		//Toast.makeText(this, "Activity opened", Toast.LENGTH_LONG).show();
        		Intent intent = new Intent(getApplicationContext(),Plan_List.class);
        		startActivity(intent);
        	}
        });
		
		
		cd = new ConnectionDetector(getApplicationContext());

		// Check if Internet present
		isInternetPresent = cd.isConnectingToInternet();
		if (!isInternetPresent) {
			// Internet Connection is not present
			alert.showAlertDialog(MainActivity.this, "Internet Connection Error",
					"Please connect to working Internet connection", false);
			// stop executing code by return
			return;
		}

		// creating GPS Class object
		gps = new GPSTracker(this);

		// check if GPS location can get
		if (gps.canGetLocation()) {
			Log.d("Your Location", "latitude:" + gps.getLatitude() + ", longitude: " + gps.getLongitude());
		} else {
			// Can't get user's current location
			alert.showAlertDialog(MainActivity.this, "GPS Status",
					"Couldn't get location information. Please enable GPS",
					false);
			// stop executing code by return
			return;
		}
		if(str==null)
        {
        	lat = gps.getLatitude();
        	longi = gps.getLongitude();
        }
		else
		{
			LatLng x= getLocationFromAddress(this,str);
		}
		// Getting listview
		lv = (ListView) findViewById(R.id.list);
	//	 inputSearch = (EditText) findViewById(R.id.inputSearch);
		
		// button show on map
		btnShowOnMap = (Button) findViewById(R.id.btn_show_map);

		// calling background Async task to load Google Places
		// After getting places from Google all the data is shown in listview
		new LoadPlaces().execute();

		/** Button click event for shown on map */
		btnShowOnMap.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(getApplicationContext(),
						PlacesMapActivity.class);
				// Sending user current geo location
				i.putExtra("user_latitude", Double.toString(lat));
				i.putExtra("user_longitude", Double.toString(longi));
				
				// passing near places to map activity
				i.putExtra("near_places", nearPlaces);
				// staring activity
				startActivity(i);
			}
		});
		
		
		/**
		 * ListItem click event
		 * On selecting a listitem SinglePlaceActivity is launched
		 * */
		lv.setOnItemClickListener(new OnItemClickListener() {
 
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
            	// getting values from selected ListItem
                String reference = ((TextView) view.findViewById(R.id.reference)).getText().toString();
                
                // Starting new intent
                Intent in = new Intent(getApplicationContext(),
                        SinglePlaceActivity.class);
                in.putExtra("user_latitude", Double.toString(gps.getLatitude()));
				in.putExtra("user_longitude", Double.toString(gps.getLongitude()));

                
                // Sending place refrence id to single place activity
                // place refrence id used to get "Place full details"
                in.putExtra(KEY_REFERENCE, reference);
                startActivity(in);
            }
        });
	}

	/**
	 * Background Async Task to Load Google places
	 * */
	class LoadPlaces extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(MainActivity.this);
			pDialog.setMessage(Html.fromHtml("<b>Search</b><br/>Loading Places..."));
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		/**
		 * getting Places JSON
		 * */
		protected String doInBackground(String... args) {
			// creating Places class object
			googlePlaces = new GooglePlaces();
			int temp=0;
			Bundle bun= getIntent().getExtras();
		
			String m = null, r = null, t = null, nc = null, bak = null,am = null,cas = null, zo=null;
			String pre = null;
			String shop = null, art = null,li =null,bar = null;
			m = bun.getString("mus");
			r = bun.getString("rest");
			nc = bun.getString("night");
			bak = bun.getString("bake");
			am = bun.getString("amu");
			zo = bun.getString("zoo");
			shop = bun.getString("sho");
			art = bun.getString("art");
			bar = bun.getString("bar");
			
			try {
				// Separeate your place types by PIPE symbol "|"
				// If you want all types places make it as null
				// Check list of types supported by google
				// 
				if(m!=null)
				{
						pre=m;
						temp=1;
				}
				if(r!=null)
				{
					if(temp==0)
					{
						pre=r;
						temp=1;
					}
					else
						pre=pre+"|"+r;
				}
				
				
				if(nc!=null)
				{
					if(temp==0)
					{
						pre=nc;
						temp=1;
					}
					else
						pre=pre+"|"+nc;
				}
				if(bak!=null)
				{
					if(temp==0)
					{
						pre=bak;
						temp=1;
					}
					else
						pre=pre+"|"+bak;
				}
				if(am!=null)
				{
					if(temp==0)
					{
						pre=am;
						temp=1;
					}
					else
						pre=pre+"|"+am;
				}
				
				
				if(zo!=null)
				{
					if(temp==0)
					{
						pre=zo;
						temp=1;
					}
					else
						pre=pre+"|"+zo;
				}
				if(shop!=null)
				{
					if(temp==0)
					{
						pre=shop;
						temp=1;
					}
					else
						pre=pre+"|"+shop;
				}
				if(art!=null)
				{
					if(temp==0)
					{
						pre=art;
						temp=1;
					}
					else
						pre=pre+"|"+art;
				}
				
				if(bar!=null)
				{
					if(temp==0)
					{
						pre=bar;
						temp=1;
					}
					else
						pre=pre+"|"+bar;
				}
				String types = pre;   
				
				// Radius in meters - increase this value if you don't find any places
				double radius = 5000;//Double.valueOf(bun.getString("rad")); // 1000 meters 
				
				// get nearest places
				nearPlaces = googlePlaces.search(lat,
						longi, radius*1000, types);
				

			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * and show the data in UI
		 * Always use runOnUiThread(new Runnable()) to update UI from background
		 * thread, otherwise you will get error
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog after getting all products
			pDialog.dismiss();
			// updating UI from Background Thread
			runOnUiThread(new Runnable() {
				public void run() {
					/**
					 * Updating parsed Places into LISTVIEW
					 * */
					// Get json response status
					String status = nearPlaces.status;
					
					// Check for all possible status
					if(status.equals("OK")){
						// Successfully got places details
						if (nearPlaces.results != null) {
							// loop through each place
							for (Place p : nearPlaces.results) {
								HashMap<String, String> map = new HashMap<String, String>();
								
								// Place reference won't display in listview - it will be hidden
								// Place reference is used to get "place full details"
								map.put(KEY_REFERENCE, p.reference);
								
								// Place name
								map.put(KEY_NAME, p.name);
								
								
								// adding HashMap to ArrayList
								placesListItems.add(map);
							}
							// list adapter
							ListAdapter adapter = new SimpleAdapter(MainActivity.this, placesListItems,
					                R.layout.list_item,
					                new String[] { KEY_REFERENCE, KEY_NAME}, new int[] {
					                        R.id.reference, R.id.name });
							
							// Adding data into listview
							lv.setAdapter(adapter);
							
							
						}
					}
					else if(status.equals("ZERO_RESULTS")){
						// Zero results found
						alert.showAlertDialog(MainActivity.this, "Near Places",
								"Sorry no places found. Try to change the types of places",
								false);
					}
					else if(status.equals("UNKNOWN_ERROR"))
					{
						alert.showAlertDialog(MainActivity.this, "Places Error",
								"Sorry unknown error occured.",
								false);
					}
					else if(status.equals("OVER_QUERY_LIMIT"))
					{
						alert.showAlertDialog(MainActivity.this, "Places Error",
								"Sorry query limit to google places is reached",
								false);
					}
					else if(status.equals("REQUEST_DENIED"))
					{
						alert.showAlertDialog(MainActivity.this, "Places Error",
								"Sorry error occured. Request is denied",
								false);
					}
					else if(status.equals("INVALID_REQUEST"))
					{
						alert.showAlertDialog(MainActivity.this, "Places Error",
								"Sorry error occured. Invalid Request",
								false);
					}
					else
					{
						alert.showAlertDialog(MainActivity.this, "Places Error",
								"Sorry error occured.",
								false);
					}
				}
			});

		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	/*@Override
    public void onBackPressed() {
     // save data first

      Intent MainActivityIntent = new Intent(getApplicationContext(), Autocompletebutton.class); 
      startActivity(MainActivityIntent);
      super.onBackPressed();
}*/

	public LatLng getLocationFromAddress(Context context,String strAddress) {

	    Geocoder coder = new Geocoder(context);
	    List<Address> address;
	    LatLng p1 = null;

	    try {
	        address = coder.getFromLocationName(strAddress, 5);
	        if (address == null) {
	            return null;
	        }
	        Address location = address.get(0);
	      lat=  location.getLatitude();
	      longi=  location.getLongitude();
	      //Toast.makeText(getApplicationContext(), "Your latitude is " + lat,  Toast.LENGTH_LONG).show();
	      //Toast.makeText(getApplicationContext(), "Your longitude is " + longi,  Toast.LENGTH_LONG).show();
	      Log.d("Longi", String.valueOf(longi));
	      Log.d("Lati", String.valueOf(lat));

	        p1 = new LatLng(location.getLatitude(), location.getLongitude() );

	    } catch (Exception ex) {

	        ex.printStackTrace();
	    }

	    return p1;
	}
	
}
