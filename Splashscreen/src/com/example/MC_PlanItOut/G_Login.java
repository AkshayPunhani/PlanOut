package com.example.MC_PlanItOut;

import android.app.Activity;

import java.io.InputStream;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
 



import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
 
public class G_Login extends Activity implements OnClickListener,
        ConnectionCallbacks, OnConnectionFailedListener {
 
    private static final int RC_SIGN_IN = 0;
    // Logcat tag
    private static final String TAG = "MainActivity";
 
    // Profile pic image size in pixels
    private static final int PROFILE_PIC_SIZE = 400;
 
    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;
    String email;
 
  
    private boolean mIntentInProgress;
 
    private boolean mSignInClicked;
    private int locate = 0;
 
    private ConnectionResult mConnectionResult;
 
    private SignInButton btnSignIn;
    private Button btnSignOut, btnRevokeAccess;
    private ImageView imgProfilePic;
    private TextView txtName, txtEmail;
    private LinearLayout llProfileLayout;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.g_login);
 
        btnSignIn = (SignInButton) findViewById(R.id.btn_sign_in);
        btnSignOut = (Button) findViewById(R.id.btn_sign_out);
        btnRevokeAccess = (Button) findViewById(R.id.btn_revoke_access);
        imgProfilePic = (ImageView) findViewById(R.id.imgProfilePic);
        txtName = (TextView) findViewById(R.id.txtName);
        txtEmail = (TextView) findViewById(R.id.txtEmail);
        llProfileLayout = (LinearLayout) findViewById(R.id.llProfile);
 
        // Button click listeners
        btnSignIn.setOnClickListener(this);
        btnSignOut.setOnClickListener(this);
        btnRevokeAccess.setOnClickListener(this);
        
        Intent intent = new Intent(this, Autocompletebutton.class); 
        startActivity(intent);
        finish();
        
        mGoogleApiClient = new GoogleApiClient.Builder(this)

		.addConnectionCallbacks(this)

		.addOnConnectionFailedListener(this).addApi(Plus.API)

		.addScope(Plus.SCOPE_PLUS_PROFILE).build();
    }
 
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }
 
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }
 
    /**
     * Method to resolve any signin errors
     * */
    private void resolveSignInError() {
        if (mConnectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
            } catch (SendIntentException e) {
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }
 
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (!result.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this,
                    0).show();
            return;
        }
 
        if (!mIntentInProgress) {
           
            mConnectionResult = result;
 
            if (mSignInClicked) {
                
                resolveSignInError();
            }
        }
 
    }
 
    @Override
    protected void onActivityResult(int requestCode, int responseCode,
            Intent intent) {
        if (requestCode == RC_SIGN_IN) {
            if (responseCode != RESULT_OK) {
                mSignInClicked = false;
            }
 
            mIntentInProgress = false;
 
            if (!mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }
        }
    }
 
    @Override
    public void onConnected(Bundle arg0) {
        mSignInClicked = false;
        Toast.makeText(this, "User is connected!", Toast.LENGTH_LONG).show();
 
        // Get user's information
        getProfileInformation();
 
        // Update the UI after signin
        updateUI(true);
        
        if(locate == 1)
        {
        	Intent intent = new Intent(this, User.class); 
            startActivity(intent);
            finish();
        }
        
        // Intent intent = new Intent(this, Second.class); 
        //  intent.putExtra("email", email);
       //  startActivity(intent);

        //sendEmail();
    }
 
    /**
     * Updating the UI, showing/hiding buttons and profile layout
     * */
    private void updateUI(boolean isSignedIn) {
        if (isSignedIn) {
            btnSignIn.setVisibility(View.GONE);
            //btnSignOut.setVisibility(View.VISIBLE);
            locate = 1;
            //btnRevokeAccess.setVisibility(View.VISIBLE);
           // llProfileLayout.setVisibility(View.VISIBLE);
        } else {
            btnSignIn.setVisibility(View.VISIBLE);
            btnSignOut.setVisibility(View.GONE);
            btnRevokeAccess.setVisibility(View.GONE);
            llProfileLayout.setVisibility(View.GONE);
        }
    }
 
    /**
     * Fetching user's information name, email, profile pic
     * */
    private void getProfileInformation() {
        try {
            if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                Person currentPerson = Plus.PeopleApi
                        .getCurrentPerson(mGoogleApiClient);
                String personName = currentPerson.getDisplayName();
            //    String personPhotoUrl = currentPerson.getImage().getUrl();
                String personGooglePlusProfile = currentPerson.getUrl();
                 email = Plus.AccountApi.getAccountName(mGoogleApiClient);
 
               
 
                txtName.setText(personName);
                txtEmail.setText(email);
 
              
             
 
            } else {
                /*Toast.makeText(getApplicationContext(),
                        "Person information is null", Toast.LENGTH_LONG).show();*/
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
        updateUI(false);
    }
 
   
   
 
    /**
     * Button on click listener
     * */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.btn_sign_in:
            // Signin button clicked
            signInWithGplus();
           // Intent intent = new Intent(this, Second.class); 
          //  intent.putExtra("email", email);
           

         //   startActivity(intent);

            break;
        case R.id.btn_sign_out:
            // Signout button clicked
            signOutFromGplus();
            break;
        case R.id.btn_revoke_access:
            // Revoke access button clicked
            GplusAccess();
            break;
        }
    }
 
    /**
     * Sign-in into google
     * */
    private void signInWithGplus() {
        if (!mGoogleApiClient.isConnecting()) {
            mSignInClicked = true;
            resolveSignInError();
        }
    }
 
    /**
     * Sign-out from google
     * */
    private void signOutFromGplus() {
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
            mGoogleApiClient.connect();
            updateUI(false);
        }
    }
 
   
     
    /**
     * Revoking access from google
     * */
    private void GplusAccess() {
     
    	
    	//  Intent intent = new Intent(this, Second.class); 
    	 /* Intent intent = new Intent(this, MainActivity.class); 
          startActivity(intent);
          finish();*/
    }
 
    
 
}