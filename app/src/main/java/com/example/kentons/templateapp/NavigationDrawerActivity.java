package com.example.kentons.templateapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
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

//Used to extend Activity instead of AppCompatActivity
public class NavigationDrawerActivity extends Activity {
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    };

    private GoogleApiClient mGoogleApiClient;

    private TextView fromIntentTextView;
    private TextView userNameTextView;
    private TextView userEmailTextView;

    private ShareActionProvider shareActionProvider;
    private String[] titles;
    private ListView drawerList;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private int currentPosition = 0;


    private Bundle myBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myBundle = savedInstanceState;

        FacebookSdk.sdkInitialize(getApplicationContext());   //NEED TO THIS BEFORE UI


        setContentView(R.layout.activity_navigation_drawer);

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        mGoogleApiClient = globalVariable.getKentonGoogleApiClient();

        titles = getResources().getStringArray(R.array.drawer_list_options);
        drawerList = (ListView)findViewById(R.id.drawer);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        //Populate the ListView
        drawerList.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_activated_1, titles));
        drawerList.setOnItemClickListener(new DrawerItemClickListener());
        //Display the correct fragment.

        Log.e("NavigationDrawer", "Got past onCreate stuff above this line");


        if (savedInstanceState != null) {
            currentPosition = savedInstanceState.getInt("position");
            setActionBarTitle(currentPosition);
        } else {
            Log.e("NavigationDrawer", "Before selectItem(0) in else statement");

            selectItem(0);
            Log.e("NavigationDrawer", "After selectItem(0) in else statement");
        }



        //Create the ActionBarDrawerToggle
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.string.drawer_open, R.string.drawer_close) {
            //Called when a drawer has settled in a completely closed state
            @Override
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }
            //Called when a drawer has settled in a completely open state.
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setTitle("TemplateApp");  ///SET TITLE TO BLANK STRING
        getFragmentManager().addOnBackStackChangedListener(
                new FragmentManager.OnBackStackChangedListener() {
                    public void onBackStackChanged() {
                        FragmentManager fragMan = getFragmentManager();
                        Fragment fragment = fragMan.findFragmentByTag("visible_fragment");
                        if (fragment instanceof HomeFragment) {
                            currentPosition = 0;
                        }
                        if (fragment instanceof WorkoutLogFragment) {
                            currentPosition = 1;
                        }
                        if (fragment instanceof AnalyticsFragment) {
                            currentPosition = 2;
                        }
                        if (fragment instanceof BuyPremiumFragment) {
                            currentPosition = 3;
                        }
                        setActionBarTitle(currentPosition);
                        drawerList.setItemChecked(currentPosition, true);
                    }
                }
        );
    }

    private void selectItem(int position) {
        // update the main content by replacing fragments
        Log.e("NavigationDrawer", "int position = " + position);
        currentPosition = position;
        Fragment fragment;
        Log.e("NavigationDrawer", "Before switch in selectItem");
        switch(position) {
            case 1:
                fragment = new WorkoutLogFragment();
                break;
            case 2:
                fragment = new AnalyticsFragment();
                break;
            case 3:
                fragment = new BuyPremiumFragment();
                break;
            default:
                //Toast.makeText(this, "In beginning default switch for selectItem", Toast.LENGTH_SHORT).show();

                fragment = new HomeFragment();

        }
        Log.e("NavigationDrawer", "After switch in selectItem");

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if((fragment instanceof HomeFragment) && (myBundle == null))
        {
            //Toast.makeText(this, "In null of if statement for fragment", Toast.LENGTH_SHORT).show();
            fragment.setArguments(getIntent().getExtras());
        }



        ft.replace(R.id.content_frame, fragment, "visible_fragment");
        ft.addToBackStack(null);




        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
        Log.e("NavigationDrawer", "After ft.commit() and BEFORE setActionBarTitle()");
        //Set the action bar title
        setActionBarTitle(position);


        Log.e("NavigationDrawer", "After setActionBarTitle(position)");

        //Close drawer
        drawerLayout.closeDrawer(drawerList);
    }


    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() == 1) {
            //Toast.makeText(getApplicationContext(), "onBackPressed() method stack size 1", Toast.LENGTH_SHORT).show();
            logout_dialog();
            //this.finish();
        } else {
            //Toast.makeText(getApplicationContext(), "onBackPressed() method stack size greater than 1", Toast.LENGTH_SHORT).show();

            getFragmentManager().popBackStack();
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the drawer is open, hide action items related to the content view
        boolean drawerOpen = drawerLayout.isDrawerOpen(drawerList);
        menu.findItem(R.id.action_share).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("position", currentPosition);
    }

    private void setActionBarTitle(int position) {

        //Log.e("NavigationDrawer", "Begining of setActionBarTitle");
        String title;
        if (position == 0) {
            title = getResources().getString(R.string.app_name);
            //getActionBar().setTitle(title);

        } else {
            title = titles[position];
            //getActionBar().setTitle(title);

        }
        //Log.e("NavigationDrawer", "After if/else block in setActionBarTitle");


        //Log.e("NavigationDrawer", "End of setActionBarTitle after  getActionBar().setTitle(title)");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.standard_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.action_share);
        shareActionProvider = (ShareActionProvider) menuItem.getActionProvider();
        setIntent("This is example text");
        return super.onCreateOptionsMenu(menu);
    }

    private void setIntent(String text) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        shareActionProvider.setShareIntent(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.action_settings:
                Toast.makeText(getApplicationContext(), "Settings was pressed!", Toast.LENGTH_SHORT).show();

                //Code to run when the settings item is clicked
                return true;
            case R.id.logout_menu_option:

                Toast.makeText(getApplicationContext(), "Logout was pressed!", Toast.LENGTH_SHORT).show();

                logout_dialog();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void logout_dialog()
    {
        //DIALOG FROM http://www.tutorialspoint.com/android/android_alert_dialoges.htm

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want to log out?");

        alertDialogBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Toast.makeText(getApplicationContext(), "Logout Successful!", Toast.LENGTH_LONG).show();


                //LOGOUT FOR FACEBOOK AND GOOGLE


                LoginManager.getInstance().logOut();

                if (mGoogleApiClient.isConnected()) {
                    //KENTON ADDED
                    Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
                    Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient)
                            .setResultCallback(new ResultCallback<Status>() {
                                @Override
                                public void onResult(Status arg0) {
                                    //Toast.makeText(getApplicationContext(), "Going to main menu", Toast.LENGTH_SHORT).show();
                                    //mGoogleApiClient.connect();
                                    //updateUI(false);
                                }

                            });

                    //KENTON ADDED
                    mGoogleApiClient.disconnect();
                    //Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);  //Not Here Original
                }

                Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(myIntent);


            }
        });

        alertDialogBuilder.setNegativeButton("NO",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "You clicked no button", Toast.LENGTH_LONG).show();
                //finish();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }


}