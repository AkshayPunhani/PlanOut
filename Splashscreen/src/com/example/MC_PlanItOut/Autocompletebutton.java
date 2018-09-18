package com.example.MC_PlanItOut;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Autocompletebutton extends Activity {

	private Button b1, b2;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.autocompletebutton);
      Button b1=(Button)findViewById(R.id.button1);
      Button b2=(Button)findViewById(R.id.button2);
      b1.setOnClickListener(new View.OnClickListener() {

        public void onClick(View v) {
            Intent myintent1 = new Intent(Autocompletebutton.this,Autocomplete.class);
            startActivity(myintent1);
        	//Toast.makeText(getApplicationContext(), "Successfully Added" ,
	 			//	  Toast.LENGTH_LONG).show();
        }
    });
      b2.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
            	String str = null;
                Intent myintent2 = new Intent(Autocompletebutton.this,User.class);
                myintent2.putExtra("straddress", str);
                startActivity(myintent2);

            }
        }); 
}
}
