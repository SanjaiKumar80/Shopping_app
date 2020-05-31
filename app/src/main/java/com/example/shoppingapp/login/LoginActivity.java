package com.example.shoppingapp.login;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.shoppingapp.MainActivity;
import com.example.shoppingapp.R;
import com.example.shoppingapp.apiUrl.Api;
import com.example.shoppingapp.apiUrl.ApiClient;
import com.example.shoppingapp.connectivityReceiver.ConnectivityReceiver;
import com.example.shoppingapp.connectivityReceiver.MyApplication;
import com.example.shoppingapp.sessionManagement.SessionManagement;
import com.example.shoppingapp.utils.TextUtilsInput;
import com.facebook.CallbackManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
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

public class LoginActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener, GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = LoginActivity.class.getSimpleName();
    private SessionManagement sessionManagement;

  /*  @BindView(R.id.login_button)
    LoginButton loginButton;*/

    CallbackManager callbackManager;
    @BindView(R.id.sign_in_google)
    SignInButton signInButton;
    private GoogleApiClient googleApiClient;
    private static final int RC_SIGN_IN = 1;

    @BindView(R.id.til_login_number)
    TextInputLayout textInputLayoutMobileNumber;

    @BindView(R.id.til_login_password)
    TextInputLayout textInputLayoutPassword;

    @BindView(R.id.tie_login_number)
    TextInputEditText loginMobileNumber;

    @BindView(R.id.tie_login_password)
    TextInputEditText loginPassword;

    @BindView(R.id.btn_forgotpassword)
    Button buttonForgotPassword;

    @BindView(R.id.login_toolbar)
    Toolbar mtoolbar;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkConnection();
        sessionManagement = new SessionManagement(getApplicationContext());
//        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        setSupportActionBar(mtoolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        if (sessionManagement.isLoggedIn()) {
            Log.d(TAG, "loggedin: " + sessionManagement.isLoggedIn());
            Intent intent3 = new Intent(this, MainActivity.class);
            startActivity(intent3);
            this.finish();
        }
        TextUtilsInput.textUtilsMethod(loginMobileNumber,textInputLayoutMobileNumber);
        TextUtilsInput.textUtilsMethod(loginPassword,textInputLayoutPassword);
        GoogleSignInOptions gso =  new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleApiClient=new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();



    }
    @OnClick(R.id.sign_in_google)
    void signInGoogle(){
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent,RC_SIGN_IN);
    }

/*
  @OnClick(R.id.login_button)
    void facebookLogin(){
        boolean loggedOut = AccessToken.getCurrentAccessToken() == null;


        loginButton.setReadPermissions(Arrays.asList("email", "public_profile"));
        callbackManager = CallbackManager.Factory.create();

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                //loginResult.getAccessToken();
                //loginResult.getRecentlyDeniedPermissions()
                //loginResult.getRecentlyGrantedPermissions()
                boolean loggedIn = AccessToken.getCurrentAccessToken() == null;
                Log.d("API123", loggedIn + " ??");

            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });

    }
*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       // callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RC_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result){
        if(result.isSuccess()){
        }else{
            Toast.makeText(getApplicationContext(),"Sign in cancel",Toast.LENGTH_LONG).show();
        }
    }





    @OnClick(R.id.btn_forgotpassword)
    void forgotPassword(){
        Intent i = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
        startActivity(i);
    }
    @OnClick(R.id.btn_sign_up)
    void signUpButton(){
        Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(i);
    }
    @OnClick(R.id.button_login)
    void login() {
        checkConnection();
        Log.d(TAG, "loginbutton: ");
        Log.d(TAG, "loginmobile: "+TextUtilsInput.getString(loginMobileNumber));
        if (TextUtilsInput.getString(loginMobileNumber).isEmpty() ) {
            textInputLayoutMobileNumber.setError("Please Enter mobile number or email");
        } else if (TextUtilsInput.getString(loginMobileNumber).length() < 10 ) {
            textInputLayoutMobileNumber.setError("Invalid Number or email");


        }
        Log.d(TAG, "loginpassword: "+TextUtilsInput.getString(loginPassword));

        if (TextUtilsInput.getString(loginPassword).isEmpty()) {
            textInputLayoutPassword.setError("Please Enter a Password");
        } else if (TextUtilsInput.getString(loginPassword).length() < 6) {
            textInputLayoutPassword.setError("Password should  atleast 6 characters");
        } else if (ConnectivityReceiver.isConnected(this)){
            Api apiService = ApiClient.getClient().create(Api.class);
            Call<ResponseBody> call = apiService.userAuth(TextUtilsInput.getString(loginMobileNumber), TextUtilsInput.getString(loginPassword));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NotNull Call<ResponseBody> call, @NotNull retrofit2.Response<ResponseBody> response) {
                    Log.d(TAG, "onResponse: ");
                    try {
                        if (response.body() != null) {
                            String res = response.body().string();
                            Log.d(TAG, "loginResponse: "+res);
                            JSONObject object = new JSONObject(res);

                            if (object.getBoolean("status")){
                                JSONObject obj = object.getJSONObject("data");
                                String idLogin = obj.getString("id");
                                String nameLogin = obj.getString("name");
                                String codeLogin = obj.getString("country_code");
                                String mobileLogin = obj.getString("mobile_no");
                                String emailLogin = obj.getString("email");
                                String passwordLogin = obj.getString("password");
                                String addressLogin = obj.getString("address");
                                String pincodeLogin = obj.getString("pincode");
                                String occupationLogin = obj.getString("occupation");
                                String websiteLogin = obj.getString("website");
                                String locationLogin = obj.getString("location");
                                String facebookLogin = obj.getString("facebook");
                                String pinterestLogin = obj.getString("pinterest");
                                String twitterLogin = obj.getString("twitter");
                                String instragramLogin = obj.getString("instagram");
                                String youtubeLogin = obj.getString("youtube");
                                String bioLogin = obj.getString("bio");
                                String userimage = obj.getString("image");
                                String coverimage = obj.getString("cover_image");
                                String usertypelogin = obj.getString("usertype");
                                sessionManagement.createProfileUpdateSession(idLogin, usertypelogin, nameLogin, userimage, coverimage, codeLogin, mobileLogin, emailLogin, passwordLogin, addressLogin, pincodeLogin, occupationLogin, websiteLogin,
                                        locationLogin, facebookLogin, pinterestLogin, twitterLogin, instragramLogin, youtubeLogin, bioLogin);



                                TextUtilsInput.backgroundThreadShortToast(LoginActivity.this,object.getString("message"));

                                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(i);
                                finish();




                            }
                            else {


                                TextUtilsInput.backgroundThreadShortToast(LoginActivity.this,"Invalid email or password!");
                            }


                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                    Log.d(TAG, "onFailurelogin: "+t.getMessage());

                }
            });



        }

    }
    public boolean checkConnection()
    {
        boolean isConnected = ConnectivityReceiver.isConnected(this);
        showSnack(isConnected);
        return isConnected;

    }

    // Showing the status in Snackbar
    private void showSnack(boolean isConnected) {

        if (!isConnected) {
            noConnection(LoginActivity.this);
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.getInstance().setConnectivityListener(this::showSnack);

    }

    @Override
    public void onBackPressed() {
        if (!sessionManagement.isLoggedIn()) {
            Log.d(TAG, "onBackPressed: "+sessionManagement.isLoggedIn());
            sessionManagement.checkLogin();
            finish();
        } else {
            finish();
        }

    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
