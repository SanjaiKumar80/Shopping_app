package com.example.shoppingapp.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.shoppingapp.R;
import com.example.shoppingapp.apiUrl.Api;
import com.example.shoppingapp.apiUrl.ApiClient;
import com.example.shoppingapp.connectivityReceiver.ConnectivityReceiver;
import com.example.shoppingapp.utils.TextUtilsInput;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

import static com.example.shoppingapp.config.Config.noConnection;

public class ChangePassword extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    private   static  final String TAG = ChangePassword.class.getSimpleName();
    private String mobile;

    @BindView(R.id.til_new_password)
    TextInputLayout textInputnewPassword;

    @BindView(R.id.tie_new_password)
    TextInputEditText editTextnewPassword;

    @BindView(R.id.til_reenter_password)
    TextInputLayout textInputreenterPassword;

    @BindView(R.id.tie_re_enter_password)
    TextInputEditText editTextreenterPassword;

    @BindView(R.id.toolbar)
    Toolbar mtoolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        ButterKnife.bind(this);
        checkConnection();
        setSupportActionBar(mtoolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        Intent i = getIntent();
        if (i!=null) {
            mobile = getIntent().getStringExtra("userMobile");
        }
        TextUtilsInput.textUtilsMethod(editTextnewPassword,textInputnewPassword);
        TextUtilsInput.textUtilsMethod(editTextreenterPassword,textInputreenterPassword);

    }
    @OnClick(R.id.toolbar_backbutton)
    void loginBackButton(){
        Intent i = new Intent(ChangePassword.this, OtpVerify.class);
        startActivity(i);
        finish();


    }

    @OnClick(R.id.button_change_password)
    void changePassword() {
        checkConnection();
        Log.d(TAG, "changePassword: "+TextUtilsInput.getString(editTextnewPassword));
        Log.d(TAG, "passwordmobile: "+mobile);
        if (TextUtilsInput.getString(editTextnewPassword).isEmpty()) {
            textInputnewPassword.setError("please Enter a Password");
            return;
        } else if (TextUtilsInput.getString(editTextnewPassword).length() < 6) {
            textInputnewPassword.setError("Password should  atleast 6 characters");
            return;
        }

        if (TextUtilsInput.getString(editTextreenterPassword).isEmpty()) {
            textInputreenterPassword.setError("Please Enter a Password");
        }

        else if (!TextUtilsInput.getString(editTextnewPassword).equals(TextUtilsInput.getString(editTextreenterPassword))) {
            textInputreenterPassword.setError("Password mismatch");


        } else if (ConnectivityReceiver.isConnected(this)) {
            Api apiService = ApiClient.getClient().create(Api.class);


            Call<ResponseBody> call = apiService.resetPassword(mobile, TextUtilsInput.getString(editTextnewPassword));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NotNull Call<ResponseBody> call, @NotNull retrofit2.Response<ResponseBody> response) {
                    Log.d(TAG, "onResponse: ");
                    try {
                        if (response.body() != null) {
                            String res = response.body().string();
                            Log.d(TAG, "changepasswordresponse: " + res);
                            JSONObject object = new JSONObject(res);
                            if (object.getBoolean("status")) {
                                Intent i = new Intent(ChangePassword.this, LoginActivity.class);
                                startActivity(i);
                                finish();


                            }
                            TextUtilsInput.backgroundThreadShortToast(ChangePassword.this,object.getString("message"));


                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }


                @Override
                public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                    Log.d(TAG, "onFailure: " + t.getMessage());

                }
            });


        }
    }
    private void checkConnection()
    {
        boolean isConnected = ConnectivityReceiver.isConnected(this);
        showSnack(isConnected);
    }

    // Showing the status in Snackbar
    private void showSnack(boolean isConnected) {

        if (!isConnected) {
            noConnection(ChangePassword.this);
        }
    }



    @Override
    public void onBackPressed() {
        Intent i = new Intent(ChangePassword.this,OtpVerify.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();
    }


    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);

    }
}
