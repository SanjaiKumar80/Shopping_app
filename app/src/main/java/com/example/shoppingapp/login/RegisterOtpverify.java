package com.example.shoppingapp.login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.shoppingapp.R;
import com.example.shoppingapp.apiUrl.Api;
import com.example.shoppingapp.apiUrl.ApiClient;
import com.example.shoppingapp.connectivityReceiver.ConnectivityReceiver;
import com.example.shoppingapp.connectivityReceiver.MyApplication;
import com.example.shoppingapp.connectivityReceiver.SmsBroadcastReceiver;
import com.example.shoppingapp.utils.TextUtilsInput;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.shoppingapp.config.Config.noConnection;

public class RegisterOtpverify extends AppCompatActivity implements SmsBroadcastReceiver.OTPReceiveListener, ConnectivityReceiver.ConnectivityReceiverListener {
    private static final String TAG = RegisterOtpverify.class.getSimpleName();

    @BindView(R.id.otp_num)
    TextView otpText;

    @BindView(R.id.til_inputlayout_otp)
    TextInputLayout textInputLayoutotp;

    @BindView(R.id.tie_otp_verify)
    TextInputEditText edittextOtpVerify;

    @BindView(R.id.button_resend_verify)
    Button resend;

    @BindView(R.id.count_timer)
    TextView countTimer;

    @BindView(R.id.toolbar)
    Toolbar mtoolbar;



    public int counter;

    private SmsBroadcastReceiver mSmsBroadcastReceiver;
    private String mobile;
    SmsRetrieverClient client;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);
        ButterKnife.bind(this);
        checkConnection();
        setSupportActionBar(mtoolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        Intent i = getIntent();
        if (i!=null) {
            mobile = getIntent().getStringExtra("userMobile");
            String receivedotp = getIntent().getStringExtra("userReceivedOtp");
            Log.d(TAG, "receivedotp: " + receivedotp);
        }



        TextUtilsInput.textUtilsMethod(edittextOtpVerify,textInputLayoutotp);

        startSMSListener();

    }
    private void startSMSListener() {
        try {
            mSmsBroadcastReceiver = new SmsBroadcastReceiver();
            mSmsBroadcastReceiver.setOTPListener(this);

            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION);
            this.registerReceiver(mSmsBroadcastReceiver, intentFilter);

            client = SmsRetriever.getClient(this);

            Task<Void> task = client.startSmsRetriever();
            task.addOnSuccessListener(aVoid -> {
                // API successfully started
                Log.d(TAG, "startSMSListener: addOnSuccessListener ");


            });

            task.addOnFailureListener(e -> {
                Log.d(TAG, "startSMSListener: addOnFailureListener ");

                // Fail to start API
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.toolbar_backbutton)
    void loginBackButton(){
        Intent i = new Intent(RegisterOtpverify.this, RegisterActivity.class);
        startActivity(i);
        finish();


    }

    @OnClick(R.id.button_otp_verify)
    void otpVerify(){
        checkConnection();
        Log.d(TAG, "otpverifybutton: ");
        Log.d(TAG, "otpverifyotp: "+TextUtilsInput.getString(edittextOtpVerify));
        Log.d(TAG, "otpverifynumber: "+mobile);

        if (TextUtilsInput.getString(edittextOtpVerify).isEmpty()) {

            textInputLayoutotp.setError("Please Enter the otp");
        }

        /*else if (!receivedotp.equals(otp)){
            textInputLayoutotp.setError("Enter Valid Otp");
        }

         */
        else if (ConnectivityReceiver.isConnected(this)){
            Api apiService = ApiClient.getClient().create(Api.class);
            Call<ResponseBody> call = apiService.registerVerify( mobile,TextUtilsInput.getString(edittextOtpVerify));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                    if (response.body() != null) {
                        try {
                            String   res = response.body().string();
                            Log.d(TAG, "userdetailsregisterverify: " + res);
                            JSONObject obj = new JSONObject(res);
                            if (obj.getBoolean("status")) {
                                Intent i = new Intent(RegisterOtpverify.this, LoginActivity.class);
                                i.putExtra("userMobile",mobile);
                                startActivity(i);
                                finish();


                            }

                            TextUtilsInput.backgroundThreadShortToast(RegisterOtpverify.this,obj.getString("message"));


                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }


                @Override
                public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                    Log.d(TAG, "onFailure: "+t.getMessage());
                }
            });
        }



    }
    //resend otp
    @OnClick(R.id.button_resend_verify)
    void resendVerify(){
        checkConnection();
        startSMSListener();
        edittextOtpVerify.setText(" ");
        countTimer.setVisibility(View.VISIBLE);
        Log.d(TAG, "otpVerificationmobile: "+mobile);

        Api apiService = ApiClient.getClient().create(Api.class);
        Call<ResponseBody> call = apiService.resendotp(mobile);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                Log.d(TAG, "onResponse: ");

                try {
                    if (response.body()!=null) {
                        String res = response.body().string();
                        Log.d(TAG, "registerverifyresponse: " + res);
                        JSONObject object = new JSONObject(res);
                        TextUtilsInput.backgroundThreadShortToast(RegisterOtpverify.this,object.getString("message"));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
        resend.setEnabled(false);
        resend.setTextColor(Color.LTGRAY);

        new CountDownTimer(120000,500) {

            @Override
            public void onTick(long millisUntilFinished) {
                //Convert milliseconds into hour,minute and seconds
                String text = String.format(Locale.getDefault(), "%02d:%02d ",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60,
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60);
                countTimer.setText(text);//set text

                counter++;
            }

            @SuppressLint("ResourceAsColor")
            @Override
            public void onFinish() {
                Log.d(TAG,"resend1");
                countTimer.setVisibility(View.GONE);
                resend.setEnabled(true);
                resend.setTextColor(getResources().getColor(R.color.red));


            }
        }.start();




    }




    @Override
    public void onBackPressed() {
        Intent i = new Intent(RegisterOtpverify.this, RegisterActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();
    }

    @Override
    public void onOTPReceived(String otp) {
        Log.d(TAG, "onOTPReceived: "+otp);
        edittextOtpVerify.setText(otp);
        edittextOtpVerify.setSelection(Objects.requireNonNull(edittextOtpVerify.getText()).length());

        if (mSmsBroadcastReceiver != null) {
            unregisterReceiver(mSmsBroadcastReceiver);
            mSmsBroadcastReceiver = null;
        }
        Log.d(TAG, "onOTPReceivedmessage: "+edittextOtpVerify.getText().toString());


    }

    @Override
    public void onOTPTimeOut() {
        Log.d(TAG, "onOTPTimeOut: ");

    }

    @Override
    public void onOTPReceivedError(String error) {
        Log.d(TAG, "onOTPReceivedError: ");

    }
    private void checkConnection()
    {
        boolean isConnected = ConnectivityReceiver.isConnected(this);
        showSnack(isConnected);
    }

    // Showing the status in Snackbar
    private void showSnack(boolean isConnected) {

        if (!isConnected) {
            noConnection(RegisterOtpverify.this);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSmsBroadcastReceiver != null) {
            unregisterReceiver(mSmsBroadcastReceiver);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.getInstance().setConnectivityListener(this::showSnack);

    }


    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }
}
