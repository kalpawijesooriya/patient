package com.project.kalpawijesooriya.patient;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by Kalpa Wijesooriya on 3/11/2018.
 */

public class Fireapp extends Application {

    @Override
    public void onCreate(){
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
