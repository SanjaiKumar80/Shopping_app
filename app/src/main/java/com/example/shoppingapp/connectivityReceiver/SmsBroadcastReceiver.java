package com.example.shoppingapp.connectivityReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SmsBroadcastReceiver extends BroadcastReceiver {
    OTPReceiveListener otpListener;
    private static final String TAG = SmsBroadcastReceiver.class.getSimpleName();

    public void setOTPListener(OTPReceiveListener otpListener) {
        this.otpListener = otpListener;
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "smsreceived: ");
        if (SmsRetriever.SMS_RETRIEVED_ACTION.equals(intent.getAction())) {
            Bundle extras = intent.getExtras();
            assert extras != null;
            Status status = (Status) extras.get(SmsRetriever.EXTRA_STATUS);
            assert status != null;
            switch (status.getStatusCode()) {
                case CommonStatusCodes.SUCCESS:

                    //This is the full message
                    String message = (String) extras.get(SmsRetriever.EXTRA_SMS_MESSAGE);
                    assert message != null;

                    //Extract the OTP code and send to the listener
                    Pattern mPattern = Pattern.compile("(|^)\\d{6}");


                    if (otpListener != null) {
                        Log.d(TAG, "otpListener: ");
                        Matcher mMatcher = mPattern.matcher(message);
                        if (mMatcher.find()) {
                            String otp = mMatcher.group(0);
                            Log.i(TAG,"Final OTP: "+ otp);
                            otpListener.onOTPReceived(otp);

                        }else {
                            //something went wrong
                            Log.e(TAG,"Failed to extract the OTP!! ");
                        }

                    }
                    break;
                case CommonStatusCodes.TIMEOUT:
                    // Waiting for SMS timed out (5 minutes)
                    Log.d(TAG, "onReceivetimeout: ");
                    if (otpListener != null) {
                        otpListener.onOTPTimeOut();
                    }
                    break;

                case CommonStatusCodes.API_NOT_CONNECTED:

                    Log.d(TAG, "onReceive: API_NOT_CONNECTED ");

                    if (otpListener != null) {
                        otpListener.onOTPReceivedError("API NOT CONNECTED");
                    }

                    break;

                case CommonStatusCodes.NETWORK_ERROR:
                    Log.d(TAG, "networkerror: ");
                    if (otpListener != null) {
                        otpListener.onOTPReceivedError("NETWORK ERROR");
                    }

                    break;

                case CommonStatusCodes.ERROR:
                    Log.d(TAG, "onReceiveerror: ");
                    if (otpListener != null) {
                        otpListener.onOTPReceivedError("SOME THING WENT WRONG");
                    }

                    break;

            }
        }
    }

    /**
     *
     */
    public interface OTPReceiveListener {

        void onOTPReceived(String otp);

        void onOTPTimeOut();

        void onOTPReceivedError(String error);
    }
}