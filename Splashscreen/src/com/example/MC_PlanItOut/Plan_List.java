package com.example.MC_PlanItOut;

import java.util.ArrayList;
import java.util.List;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class Plan_List extends ListActivity {

	ArrayList<PLan_List_Data> places;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
	    setContentView(R.layout.plan_list);
	    
	    places = new ArrayList<PLan_List_Data>();
	    placesList();
	
	}
	
	void populate()
	{
		
	    Myadap adap = new Myadap(this, places);
	    setListAdapter(adap);
	}
	
	private void placesList() {
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("DataBase");
		setProgressBarIndeterminateVisibility(true);
		query.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(List<ParseObject> pl_list, ParseException e) {
				setProgressBarIndeterminateVisibility(false);
				// TODO Auto-generated method stub
				if (e == null) {
	                // If there are results, update the list of posts
	                // and notify the adapter
	                places.clear();
	                for (int i = 0; i < pl_list.size(); i++) {


	                        PLan_List_Data myObject = new PLan_List_Data();
	                        ParseObject object = pl_list.get(i);

	                        myObject.id = object.getObjectId();
	                        myObject.name = (String) object.get("Name");
	                        myObject.address = (String) object.get("Address");
	                        myObject.phone = (String) object.get("Phone");
	                        myObject.distance = (String) object.get("Distance");
	                        places.add(myObject);
	                }
	                
	                populate();
	                
	                //((ArrayAdapter<PLan_List_Data>) getListAdapter()).notifyDataSetChanged();
	                
	            } else {
	                Log.d(getClass().getSimpleName(), "Error: " + e.getMessage());
	            }
			}
		});
	    
		
	}
	
	
	
	
}




class Myadap extends BaseAdapter{
	ArrayList<PLan_List_Data> places;
	Context con;
	//LayoutInflater inflater;
	public Myadap(Context c, ArrayList<PLan_List_Data> places){
		this.con=c;
		this.places=places;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return places.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return places.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row=convertView;
		if( row == null ){
	        //We must create a View:
			LayoutInflater inflater = (LayoutInflater) con.getSystemService(con.LAYOUT_INFLATER_SERVICE);
	        row = inflater.inflate(R.layout.plan_list_item, parent, false);
	    }
	    //Here we can do changes to the convertView, such as set a text on a TextView 
	    //or an image on an ImageView.
		
		TextView title = (TextView) row.findViewById(R.id.mytitle);
		TextView addr = (TextView) row.findViewById(R.id.myaddr);
		TextView ph = (TextView) row.findViewById(R.id.myph);
		TextView dist = (TextView) row.findViewById(R.id.mydist);
		title.setText(places.get(position).name);
		addr.setText(places.get(position).address);
		ph.setText(places.get(position).phone);
		dist.setText(places.get(position).distance);
	    return row;
	}
}
