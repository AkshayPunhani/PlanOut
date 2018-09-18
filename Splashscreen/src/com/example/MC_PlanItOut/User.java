package com.example.MC_PlanItOut;


import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.vision.barcode.Barcode.GeoPoint;
import com.google.api.client.http.HttpResponse;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class User extends Activity {
	
	Button btn;
	CheckBox ch1,ch2,ch4,ch5,ch6,ch8,ch9,ch10,ch12;
	EditText budget,radius;
	 double lat=0;
		double longi=0;
	boolean[] array = new boolean[4];
	String m = null, r = null, t = null, nc = null, bak = null, am = null,cas = null, zo=null;
	String shop = null, art = null,li =null,bar = null;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_input);
        
        ch1=(CheckBox)findViewById(R.id.checkBox1);
        ch2=(CheckBox)findViewById(R.id.checkBox2);
        ch4=(CheckBox)findViewById(R.id.checkBox4);
        ch5=(CheckBox)findViewById(R.id.checkBox5);
        ch6=(CheckBox)findViewById(R.id.checkBox6);
        ch8=(CheckBox)findViewById(R.id.checkBox8);
        ch9=(CheckBox)findViewById(R.id.checkBox9);
        ch10=(CheckBox)findViewById(R.id.checkBox10);
        ch12=(CheckBox)findViewById(R.id.checkBox12);
        btn=(Button)findViewById(R.id.button1);
        budget=(EditText)findViewById(R.id.editText1);
        radius=(EditText)findViewById(R.id.editText2);
        
        Intent intent = getIntent();
        final String str = intent.getExtras().getString("stringaddress");
  /////////////////////////////////
        

        
        btn.setOnClickListener(new OnClickListener() {
			
			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!(ch1.isChecked() || ch2.isChecked()  || ch4.isChecked() || ch5.isChecked() || ch6.isChecked()  || ch8.isChecked() || ch9.isChecked() || ch10.isChecked()  || ch12.isChecked() ))
				{
					Toast.makeText(getApplicationContext(), "Please select any one category",
							   Toast.LENGTH_LONG).show();
				}
				else
				{
					/*if(budget.getText().toString().isEmpty() || radius.getText().toString().isEmpty())
					{
						Toast.makeText(getApplicationContext(), "Please fill budget and radius both",
								   Toast.LENGTH_LONG).show();
					}*/
					//else
					{
						Intent i;
						i = new Intent(getApplicationContext(), MainActivity.class); 
						i.putExtra("mus", m);
						i.putExtra("rest", r);
						//i.putExtra("caf", t);
						i.putExtra("night", nc);
						i.putExtra("bake", bak);
						i.putExtra("amu",am);
						//i.putExtra("casi",cas);
						i.putExtra("zoo",zo);
						i.putExtra("sho",shop);
						i.putExtra("art",art);
						//i.putExtra("lib",li);
						i.putExtra("bar",bar);
						//i.putExtra("rad", radius.getText().toString());
						i.putExtra("straddress", str);
						startActivity(i);
			            finish();
					}
				}
			}
			
			 
		     
			
		});
        
        
          
    }
	
	



	
	
	public void onCheckboxClicked(View view) 
    {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();
        
        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.checkBox1:
                if (checked)
                {
                	m = "museum";
    				//Log.d("Your message is: ",m);
                }
                    // Put some meat on the sandwich
                else
                {
                	m= "";
                }
                    // Remove the meat
                break;
            case R.id.checkBox2:
                if (checked)
                {
                	r = "restaurant";
                }
                    // Cheese me
                else
                {
                	r="";
                }
                    // I'm lactose intolerant
                break;
            /*case R.id.checkBox3:
                if (checked)
                {
                	t = "cafe";
                }
                    // Put some meat on the sandwich
                else
                {
                	t="";
                }
                    // Remove the meat
                break;*/
            case R.id.checkBox4:
                if (checked)
                {
                	nc="night_club";
                }
                    // Put some meat on the sandwich
                else
                {
                	nc="";
                }
                    // Remove the meat
                break;
            case R.id.checkBox5:
                if (checked)
                {
                	bak="bakery";
                }
                    // Put some meat on the sandwich
                else
                {
                	bak="";
                }
                    // Remove the meat
                break;
            case R.id.checkBox6:
                if (checked)
                {
                	am="amusement_park";
    				//Log.d("Your message is: ",m);
                }
                    // Put some meat on the sandwich
                else
                {
                	am= "";
                }
                    // Remove the meat
                break;
            /*case R.id.checkBox7:
                if (checked)
                {
                	cas="casino";
                }
                    // Cheese me
                else
                {
                	cas="";
                }
                    // I'm lactose intolerant
                break;*/
            case R.id.checkBox8:
                if (checked)
                {
                	zo = "zoo";
                }
                    // Put some meat on the sandwich
                else
                {
                	zo="";
                }
                    // Remove the meat
                break;
            case R.id.checkBox9:
                if (checked)
                {
                	shop="shopping_mall";
                }
                    // Put some meat on the sandwich
                else
                {
                	shop="";
                }
                    // Remove the meat
                break;
            case R.id.checkBox10:
                if (checked)
                {
                	art="art_gallery";
                }
                    // Put some meat on the sandwich
                else
                {
                	art="";
                }
                    // Remove the meat
                break;
            /*case R.id.checkBox11:
                if (checked)
                {
                	li="library";
                }
                    // Put some meat on the sandwich
                else
                {
                	li="";
                }
                    // Remove the meat
                break;*/
            case R.id.checkBox12:
                if (checked)
                {
                	bar="bar";
                }
                    // Put some meat on the sandwich
                else
                {
                	bar="";
                }
                    // Remove the meat
                break;
            // TODO: Veggie sandwich
        }
    }

}
