package com.example.kentons.templateapp;

  /*
        SOLUTION TO NOT STARTING UP
        http://www.androidhive.info/2014/02/android-login-with-google-plus-account-1/
             AmitSinghTomar • 5 months ago
            hello ,

            Please replace this code mGoogleApiClient = new GoogleApiClient.Builder(this)

            .addConnectionCallbacks(this)

            .addOnConnectionFailedListener(this).addApi(Plus.API)

            .addScope(Plus.SCOPE_PLUS_LOGIN).build();

            with

            mGoogleApiClient = new GoogleApiClient.Builder(this)

            .addConnectionCallbacks(this)

            .addOnConnectionFailedListener(this).addApi(Plus.API)

            .addScope(Plus.SCOPE_PLUS_PROFILE).build();

            and it will work fine .

         */


import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;


//Sources http://www.androidhive.info/2014/02/android-login-with-google-plus-account-1/
//Sources http://www.deviantart.com/art/DotA-2------Morphling-325037584
public class MainActivity extends FragmentActivity implements OnClickListener,
        ConnectionCallbacks, OnConnectionFailedListener, FacebookLoginButtonFragment.OnFragmentInteractionListener {


    private Bitmap Google_Bitmap;

    private static final int RC_SIGN_IN = 0;
    // Logcat tag
    private static final String TAG = "MainActivity";

    // Profile pic image size in pixels
    private static final int PROFILE_PIC_SIZE = 400;

    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;

    /**
     * A flag indicating that a PendingIntent is in progress and prevents us
     * from starting further intents.
     */
    private boolean mIntentInProgress;

    private boolean mSignInClicked;

    private ConnectionResult mConnectionResult;

    private SignInButton btnSignIn;
    private Button btnSignOut, btnRevokeAccess;
    private ImageView imgProfilePic;
    private TextView txtName, txtEmail;
    private LinearLayout llProfileLayout;

    private String facebookEmail;

    public String getFacebookEmail() {
        return facebookEmail;
    }

    public void setFacebookEmail(String facebookEmail) {
        this.facebookEmail = facebookEmail;
    }


    private LoginButton loginButton;
    //private FacebookLoginButtonFragment myFragment = new FacebookLoginButtonFragment();;

    private CallbackManager callbackManager;

    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            AccessToken accessToken = loginResult.getAccessToken();
            Profile.fetchProfileForCurrentAccessToken();
            final Profile profile = Profile.getCurrentProfile();




            GraphRequest request = GraphRequest.newMeRequest( AccessToken.getCurrentAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object,GraphResponse response) {
                            try {

                                Intent intent = new Intent(getApplicationContext(), NavigationDrawerActivity.class);
                               // Intent intent = new Intent(getApplicationContext(), MainMenuAfterLogin.class);
                                String temp = object.getString("email");
                                //Toast.makeText(getApplicationContext(), temp, Toast.LENGTH_LONG).show();
                                intent.putExtra("Email", temp);
                                intent.putExtra("Name", profile.getFirstName() + " " + profile.getLastName());

                                //Toast.makeText(getApplicationContext(), "Outside GraphRequest " + getFacebookEmail(), Toast.LENGTH_LONG).show();


                                intent.putExtra("From", "Facebook");

                                Uri profile_pic = profile.getProfilePictureUri(64, 64);
                                intent.putExtra("imageUri", profile_pic);
                                startActivity(intent);




                                //Toast.makeText(getApplicationContext(), getFacebookEmail(), Toast.LENGTH_LONG).show();

                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                        }

                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "email");
            request.setParameters(parameters);
            request.executeAsync();





            //for(String s:accessToken.getPermissions())
            //{
                //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            //}


            //displayMessage(profile);
        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException e) {

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());   //NEED TO THIS BEFORE UI
        showHashKey(this); //for facebook


        //ATTEMPTING TO GET RID OF POP UP
        //setTheme(android.R.style.Theme_Black_NoTitleBar);


        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getActionBar().hide();
        setContentView(R.layout.activity_main);

        loginButton = (LoginButton) findViewById(R.id.fb_login_button);


        float fbIconScale = 1.45F;
        Drawable drawable = this.getResources().getDrawable(
                com.facebook.R.drawable.com_facebook_button_icon);
        drawable.setBounds(0, 0, (int) (drawable.getIntrinsicWidth() * fbIconScale),
                (int) (drawable.getIntrinsicHeight() * fbIconScale));
        loginButton.setCompoundDrawables(drawable, null, null, null);
        loginButton.setCompoundDrawablePadding(this.getResources().
                getDimensionPixelSize(R.dimen.fb_margin_override_textpadding));
        loginButton.setPadding(
                this.getResources().getDimensionPixelSize(
                        R.dimen.fb_margin_override_lr),
                this.getResources().getDimensionPixelSize(
                        R.dimen.fb_margin_override_top),
                0,
                this.getResources().getDimensionPixelSize(
                        R.dimen.fb_margin_override_bottom));



        callbackManager = CallbackManager.Factory.create();

        List<String> permissions = new ArrayList<String>();
        permissions.add("public_profile");
        permissions.add("email");
        loginButton.setReadPermissions(permissions);



        // Callback registration
        loginButton.registerCallback(callbackManager, callback);



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






        mGoogleApiClient = new GoogleApiClient.Builder(this)

                .addConnectionCallbacks(this)

                .addOnConnectionFailedListener(this).addApi(Plus.API)

                .addScope(Plus.SCOPE_PLUS_PROFILE).build();


        //Global variable to store mGoogleApiClient
        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        globalVariable.setKentonGoogleApiClient(mGoogleApiClient);
    }


    public static void showHashKey(Context context) {
        try {

            PackageInfo info = context.getPackageManager().getPackageInfo(
                    "com.example.kentons.templateapp", -PackageManager.GET_SIGNATURES);
            //Your            package name here

            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.i("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        }
         catch (Exception e) {
        }
    }

    protected void onStart() {
        super.onStart();

        mGoogleApiClient.connect();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        //int fb_width = loginButton.getWidth();
        //int goog_width = btnSignIn.getWidth();
        //Toast.makeText(getApplicationContext(), fb_width + " " + goog_width, Toast.LENGTH_LONG).show();
    }

    protected void onStop() {
        //Toast.makeText(getApplicationContext(), "Going to main menu (onStop)", Toast.LENGTH_SHORT).show();
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            //KENTON ADDED
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient)
                    .setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status arg0) {
                            Toast.makeText(getApplicationContext(), "Going to main menu", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "User access revoked!");
                            //mGoogleApiClient.connect();
                            //updateUI(false);
                        }

                    });

            //KENTON ADDED
            mGoogleApiClient.disconnect();
            //Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);  //Not Here Original
        }
    }

    /**
     * Method to resolve any ----signin errors
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
            // Store the ConnectionResult for later usage
            mConnectionResult = result;

            if (mSignInClicked) {
                // The user has already clicked 'sign-in' so we attempt to
                // resolve all
                // errors until the user is signed in, or they cancel.
                resolveSignInError();
            }
        }

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        //Toast.makeText(this, "POST CREATE", Toast.LENGTH_LONG).show();




    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode,
                                    Intent intent) {


        //FACEBOOK
        callbackManager.onActivityResult(requestCode, responseCode, intent);

        //GOOGLE
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
        getProfileInformation();    //Original

        /*

            private SignInButton btnSignIn;
            private Button btnSignOut, btnRevokeAccess;
            private ImageView imgProfilePic;
            private TextView txtName, txtEmail;
            private LinearLayout llProfileLayout;

        */
        Intent intent = new Intent(this, NavigationDrawerActivity.class);
        //Intent intent = new Intent(this, MainMenuAfterLogin.class);
        intent.putExtra("Name", txtName.getText().toString());
        intent.putExtra("Email", txtEmail.getText().toString());
        intent.putExtra("From", "Google");
        Bundle extras = new Bundle();
        //Google_Bitmap NEED TO PASS TO OTHER ACTIVITY AS BITMAP
        imgProfilePic.buildDrawingCache();
        extras.putParcelable("Bitmap", imgProfilePic.getDrawingCache());
        intent.putExtras(extras);
        startActivity(intent);



        // Update the UI after ----signin
        //updateUI(true);                       //ORIGINAL

    }

    /**
     * Updating the UI, showing/hiding buttons and profile layout
     * */
    private void updateUI(boolean isSignedIn) {
        if (isSignedIn) {
            btnSignIn.setVisibility(View.GONE);
            btnSignOut.setVisibility(View.VISIBLE);
            btnRevokeAccess.setVisibility(View.VISIBLE);
            llProfileLayout.setVisibility(View.VISIBLE);
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
                //Toast.makeText(getApplicationContext(), "gettingProfileInformation in good statement", Toast.LENGTH_SHORT).show();

                Person currentPerson = Plus.PeopleApi
                        .getCurrentPerson(mGoogleApiClient);
                String personName = currentPerson.getDisplayName();
                String personPhotoUrl = currentPerson.getImage().getUrl();
                String personGooglePlusProfile = currentPerson.getUrl();
                String email = Plus.AccountApi.getAccountName(mGoogleApiClient);

                Log.e(TAG, "Name: " + personName + ", plusProfile: "
                        + personGooglePlusProfile + ", email: " + email
                        + ", Image: " + personPhotoUrl);




                txtName.setText(personName);
                txtEmail.setText(email);

                // by default the profile url gives 50x50 px image only
                // we can replace the value with whatever dimension we want by
                // replacing sz=X
                personPhotoUrl = personPhotoUrl.substring(0,
                        personPhotoUrl.length() - 2)
                        + PROFILE_PIC_SIZE;

                new LoadProfileImage(imgProfilePic).execute(personPhotoUrl);

            } else {
                        //Toast.makeText(getApplicationContext(), "gettingProfileInformation in bad statement", Toast.LENGTH_SHORT).show();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.standard_menu, menu);
        return true;
    }

    /**
     * Button on click listener
     * */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sign_in:
                // ----Signin button clicked
                signInWithGplus();
                break;
            case R.id.btn_sign_out:
                // ----Signout button clicked
                signOutFromGplus();
                break;
            case R.id.btn_revoke_access:
                // Revoke access button clicked
                revokeGplusAccess();
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
    private void revokeGplusAccess() {
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient)
                    .setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status arg0) {
                            Log.e(TAG, "User access revoked!");
                            mGoogleApiClient.connect();
                            updateUI(false);
                        }

                    });
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    /**
     * Background Async task to load user profile picture from url
     * */
    private class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public LoadProfileImage(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
            Google_Bitmap = result;
        }
    }

}