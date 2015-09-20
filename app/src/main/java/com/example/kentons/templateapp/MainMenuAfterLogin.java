package com.example.kentons.templateapp;

/*
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainMenuAfterLogin extends AppCompatActivity {

    private TextView fromIntentTextView;
    private TextView userNameTextView;
    private TextView userEmailTextView;
    private ImageView userProfilePictureImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu_after_login);
        Bundle mybundle = getIntent().getExtras();

        String from = (String) mybundle.get("From");

        fromIntentTextView = (TextView) findViewById(R.id.from_intent);
        userNameTextView = (TextView) findViewById(R.id.user_name);
        userEmailTextView = (TextView) findViewById(R.id.user_email);
        userProfilePictureImageView = (ImageView) findViewById(R.id.user_profile_picture);


        fromIntentTextView.setText(from);

        if(from.equals("Facebook"))
        {
            String name = (String) mybundle.get("Name");
            String email = (String) mybundle.get("Email");


            Uri profile_pic = (Uri) mybundle.get("imageUri");

            if(profile_pic == null)
            {
                Toast.makeText(getApplicationContext(), "Profile Pic Facebook is Null" , Toast.LENGTH_SHORT).show();
            }


            userNameTextView.setText(name);
            userEmailTextView.setText(email);

            try
            {
                userProfilePictureImageView.setImageURI(profile_pic);

            }
            catch(Exception e)
            {
                Log.i("MainMenuAfterLogin", e.toString());
            }

        }

        if(from.equals("Google"))
        {

            String name = (String) mybundle.get("Name");
            String email = (String) mybundle.get("Email");
            Bitmap bitmap = (Bitmap) getIntent().getParcelableExtra("Bitmap");

            userNameTextView.setText(name);
            userEmailTextView.setText(email);

            if(bitmap == null)
            {
                Toast.makeText(getApplicationContext(), "Bitmap Google is Null" , Toast.LENGTH_SHORT).show();
            }


            try
            {
                userProfilePictureImageView.setImageBitmap(bitmap);

            }
            catch(Exception e)
            {
                Log.i("MainMenuAfterLogin", e.toString());
            }

        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.standard_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (item.getItemId()){
            case R.id.menu_settings_1:
                Toast.makeText(getApplicationContext(), "Google Info Pressed", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_settings_2:
                Toast.makeText(getApplicationContext(), "Facebook Info Pressed", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_settings_3:
                Toast.makeText(getApplicationContext(), "Action Settings Pressed", Toast.LENGTH_SHORT).show();
                break;
            default:
                return false;
        }


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
*/