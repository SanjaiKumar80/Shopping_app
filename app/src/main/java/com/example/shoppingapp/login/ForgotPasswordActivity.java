package com.example.shoppingapp.login;

import android.app.Activity;
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
import com.example.shoppingapp.connectivityReceiver.MyApplication;
import com.example.shoppingapp.sessionManagement.SessionManagement;
import com.example.shoppingapp.utils.TextUtilsInput;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.shoppingapp.config.Config.noConnection;

public class ForgotPasswordActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener{
    private static final String TAG = ForgotPasswordActivity.class.getSimpleName();

    @BindView(R.id.til_forgot_password)
    TextInputLayout forgotPasswordNumber;

    @BindView(R.id.tie_forgot_password_number)
    TextInputEditText editTextforgotPassword;

    @BindView(R.id.toolbar)
    Toolbar mtoolbar;

    SessionManagement session;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ButterKnife.bind(this);
        checkConnection();
        TextUtilsInput.textUtilsMethod(editTextforgotPassword, forgotPasswordNumber);

        setSupportActionBar(mtoolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        session = new SessionManagement(this);




    }
    @OnClick(R.id.toolbar_backbutton)
    void loginBackButton(){
        Intent i = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
        startActivity(i);
        finish();


    }


    @OnClick(R.id.button_forgot_password)
    void forgotPassword() {
        checkConnection();

        if (TextUtilsInput.getString(editTextforgotPassword).isEmpty()) {
            forgotPasswordNumber.setError("Please Enter mobile number");

        } else if (TextUtilsInput.getString(editTextforgotPassword).length() < 10) {
            forgotPasswordNumber.setError("Invalid Number");


        } else if (ConnectivityReceiver.isConnected(this)) {

            Api apiService = ApiClient.getClient().create(Api.class);

            Call<ResponseBody> call = apiService.forgotPassword(TextUtilsInput.getString(editTextforgotPassword));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                    Log.d(TAG, "onResponse: ");
                    try {
                        if (response.body() != null) {
                            String res = response.body().string();
                            Log.d(TAG, "forgotpasswordResponse: " + res);
                            JSONObject object = new JSONObject(res);

                            if (object.getBoolean("status")) {
                                String receivedOtp = object.getString("data");
                                Intent i = new Intent(ForgotPasswordActivity.this, OtpVerify.class);
                                i.putExtra("userMobile", TextUtilsInput.getString(editTextforgotPassword));
                                Log.d(TAG, "usermobileresponse: "+TextUtilsInput.getString(editTextforgotPassword));
                                i.putExtra("receivedOtp",receivedOtp);
                                startActivity(i);
                            }

                            TextUtilsInput.backgroundThreadShortToast(ForgotPasswordActivity.this,object.getString("message"));


                        }
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                    Log.d(TAG, "onFailure: "+t.getMessage());

                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode== Activity.RESULT_OK){
            if (data!=null){
                Credential credential = data.getParcelableExtra(Credential.EXTRA_KEY);
                assert credential != null;
                editTextforgotPassword.setText(credential.getId());
            }
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
            noConnection(ForgotPasswordActivity.this);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.getInstance().setConnectivityListener(this::showSnack);

    }



    @Override
    public void onBackPressed() {
        Intent i = new Intent(ForgotPasswordActivity.this,LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();

    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);

    }
}

