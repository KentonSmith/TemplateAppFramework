package com.example.kentons.templateapp;

import android.app.Application;

import com.google.android.gms.common.api.GoogleApiClient;

/*
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
 */

/**
 * Created by admin on 9/13/2015.
 */
public class GlobalClass extends Application {

    public GoogleApiClient getKentonGoogleApiClient() {
        return kentonGoogleApiClient;
    }

    public void setKentonGoogleApiClient(GoogleApiClient kentonGoogleApiClient) {
        this.kentonGoogleApiClient = kentonGoogleApiClient;
    }

    private GoogleApiClient kentonGoogleApiClient;



}
