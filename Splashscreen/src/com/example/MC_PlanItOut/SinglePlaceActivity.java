package com.example.MC_PlanItOut;

import java.util.List;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SinglePlaceActivity extends Activity {
	// flag for Internet connection status
	Boolean isInternetPresent = false;
	
	Button addPlan;
	
	Button remove;
	// Connection detector class
	ConnectionDetector cd;
	
	// Alert Dialog Manager
	AlertDialogManager alert = new AlertDialogManager();

	// Google Places
	GooglePlaces googlePlaces;
	
	// Place Details
	PlaceDetails placeDetails;
	
	// Progress dialog
	ProgressDialog pDialog;
	
	TextView lbl_name,lbl_address,lbl_phone,distance;
	// KEY Strings
	public static String KEY_REFERENCE = "reference"; // id of the place

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.single_place);
		
		lbl_name = (TextView) findViewById(R.id.name);
		lbl_address = (TextView) findViewById(R.id.address);
		lbl_phone = (TextView) findViewById(R.id.phone);
	//TextView lbl_location = (TextView) findViewById(R.id.location);
		distance = (TextView) findViewById(R.id.dis);
		final ParseObject testObject = new ParseObject("DataBase");
		//testObject.put("foo", "bar");
	    //testObject.saveInBackground();
		Intent i = getIntent();
		
		addPlan = (Button) findViewById(R.id.button1);
		remove = (Button) findViewById(R.id.button2);
		// Place referece id
		String reference = i.getStringExtra(KEY_REFERENCE);
		String user_latitude = i.getStringExtra("user_latitude");
		String user_longitude = i.getStringExtra("user_longitude");
	
		
		// Calling a Async Background thread
		new LoadSinglePlaceDetails().execute(reference);
		
		addPlan.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Log.d("Plan Button ", lbl_name.getText().toString());
				addPlan.setVisibility(View.GONE);
				testObject.put("Name", lbl_name.getText().toString());
				testObject.put("Address", lbl_address.getText().toString());
				testObject.put("Phone", lbl_phone.getText().toString());
				testObject.put("Distance", distance.getText().toString());
				testObject.saveInBackground();
				Toast.makeText(getApplicationContext(), "Successfully Added" ,
	 				  Toast.LENGTH_LONG).show();
				remove.setVisibility(View.VISIBLE);
			}
		});
		
		remove.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				ParseQuery<ParseObject> query = ParseQuery.getQuery("TestObject");
				query.findInBackground(new FindCallback<ParseObject>() {

					@Override
					public void done(List<ParseObject> pl_list, ParseException e) {
						
						// TODO Auto-generated method stub
						
						if (e == null) {
			                // If there are results, update the list of posts
			                // and notify the adapter

							PLan_List_Data myObject = new PLan_List_Data();
							int i=0;
							while(i< pl_list.size())
							{
								i++;
							}
							
								
							pl_list.get(i-1).deleteInBackground(new DeleteCallback() {
                                @Override
                                public void done(ParseException e) {
                                    // TODO Auto-generated method stub
                                    if(e==null)
                                    {
                                        Toast.makeText(getBaseContext(),"Deleted Successfully!", Toast.LENGTH_LONG).show();
                                          //pwindo.dismiss();
                                        addPlan.setVisibility(View.VISIBLE);
                                        remove.setVisibility(View.GONE);
                                    }
                                    else
                                    {
                                        Toast.makeText(getBaseContext(),"Cant Delete Your Entry!"+e.toString(), Toast.LENGTH_LONG).show();
                                    }

                                }
                            });
			                
			            } else {
			                Log.d(getClass().getSimpleName(), "Error: " + e.getMessage());
			            }
					}
				});
				
			}
		});
	}
	
	
	/**
	 * Background Async Task to Load Google places
	 * */
	class LoadSinglePlaceDetails extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(SinglePlaceActivity.this);
			pDialog.setMessage("Loading profile ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		/**
		 * getting Profile JSON
		 * */
		protected String doInBackground(String... args) {
			String reference = args[0];
			
			// creating Places class object
			googlePlaces = new GooglePlaces();

			// Check if used is connected to Internet
			try {
				placeDetails = googlePlaces.getPlaceDetails(reference);

			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
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
					if(placeDetails != null){
						String status = placeDetails.status;
						
						// check place deatils status
						// Check for all possible status
						if(status.equals("OK")){
							if (placeDetails.result != null) {
								String name = placeDetails.result.name;
								String address = placeDetails.result.formatted_address;
								Intent i = getIntent();
								
								// Place referece id
								String reference = i.getStringExtra(KEY_REFERENCE);
								String user_latitude = i.getStringExtra("user_latitude");
								String user_longitude = i.getStringExtra("user_longitude");
								
								
							
								String phone = placeDetails.result.formatted_phone_number;
								String latitude = Double.toString(placeDetails.result.geometry.location.lat);
								String longitude = Double.toString(placeDetails.result.geometry.location.lng);
								double d1=Double.parseDouble(latitude);
								double d2=Double.parseDouble(longitude);
								double d3=Double.parseDouble(user_latitude);
								double d4=Double.parseDouble(user_longitude);
								
								Location loc1 = new Location("");
							      loc1.setLatitude(d1);
							      loc1.setLongitude(d2);

							      Location loc2 = new Location("");
							      loc2.setLatitude(d3);
							      loc2.setLongitude(d4);	
							     double d =loc1.distanceTo(loc2);
							     d=d/1000;
								
								
								Log.d("Place ", name + address + phone + latitude + longitude);
								//Toast.makeText(getApplicationContext(), "Distance is :" +d+ " Km" ,
						 			//	  Toast.LENGTH_LONG).show();		
								
								// Displaying all the details in the view
								// single_place.xml
								
								
								
								// Check for null data from google
								// Sometimes place details might missing
								name = name == null ? "Not present" : name; // if name is null display as "Not present"
								address = address == null ? "Not present" : address;
								phone = phone == null ? "Not present" : phone;
								latitude = latitude == null ? "Not present" : latitude;
								longitude = longitude == null ? "Not present" : longitude;
								
								lbl_name.setText(name);
								lbl_address.setText("Address:"+address);
								lbl_phone.setText(Html.fromHtml("<b>Phone:</b> " + phone));
								//lbl_location.setText(Html.fromHtml("<b>Latitude:</b> " + latitude + ", <b>Longitude:</b> " + longitude));
								distance.setText( String.format( "Distance: %.1f Km", d ) );
							}
						}
						else if(status.equals("ZERO_RESULTS")){
							alert.showAlertDialog(SinglePlaceActivity.this, "Near Places",
									"Sorry no place found.",
									false);
						}
						else if(status.equals("UNKNOWN_ERROR"))
						{
							alert.showAlertDialog(SinglePlaceActivity.this, "Places Error",
									"Sorry unknown error occured.",
									false);
						}
						else if(status.equals("OVER_QUERY_LIMIT"))
						{
							alert.showAlertDialog(SinglePlaceActivity.this, "Places Error",
									"Sorry query limit to google places is reached",
									false);
						}
						else if(status.equals("REQUEST_DENIED"))
						{
							alert.showAlertDialog(SinglePlaceActivity.this, "Places Error",
									"Sorry error occured. Request is denied",
									false);
						}
						else if(status.equals("INVALID_REQUEST"))
						{
							alert.showAlertDialog(SinglePlaceActivity.this, "Places Error",
									"Sorry error occured. Invalid Request",
									false);
						}
						else
						{
							alert.showAlertDialog(SinglePlaceActivity.this, "Places Error",
									"Sorry error occured.",
									false);
						}
					}else{
						alert.showAlertDialog(SinglePlaceActivity.this, "Places Error",
								"Sorry error occured.",
								false);
					}
					
					
				}
			});

		}

	}
	
	
	

}
