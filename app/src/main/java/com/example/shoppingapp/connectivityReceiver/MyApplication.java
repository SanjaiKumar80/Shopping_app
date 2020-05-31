package com.example.shoppingapp.connectivityReceiver;

import android.app.Application;

import com.example.shoppingapp.sessionManagement.SessionManagement;

public class MyApplication extends Application  {
    public static MyApplication mInstance;
     private static SessionManagement management;



     @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
         management = new SessionManagement(this);


     }


     public static synchronized SessionManagement getSessionInstance(){
         return management;
     }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }


    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }


 }




