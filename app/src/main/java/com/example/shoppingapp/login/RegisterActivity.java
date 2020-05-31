package com.example.shoppingapp.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.shoppingapp.R;
import com.example.shoppingapp.adapter.SpinAdapter;
import com.example.shoppingapp.apiUrl.Api;
import com.example.shoppingapp.apiUrl.ApiClient;
import com.example.shoppingapp.connectivityReceiver.ConnectivityReceiver;
import com.example.shoppingapp.connectivityReceiver.MyApplication;
import com.example.shoppingapp.model.SpinnerModel;
import com.example.shoppingapp.sessionManagement.SessionManagement;
import com.example.shoppingapp.utils.TextUtilsInput;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.shoppingapp.config.Config.noConnection;


public class RegisterActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    private static String TAG = RegisterActivity.class.getSimpleName();
    private ArrayList<SpinnerModel> countryCodeSpinnerList;
    SessionManagement sessionManagement;
    public  static int selectedItem = -1;

    @BindView(R.id.tie_register_email)
    TextInputEditText registerEmail;

    @BindView(R.id.til_register_email)
    TextInputLayout inputLayoutEmail;

    @BindView(R.id.tie_register_password)
    TextInputEditText registerPassword;

    @BindView(R.id.til_register_password)
    TextInputLayout inputLayoutPassword;

    @BindView(R.id.til_register_number)
    TextInputLayout inputLayoutNumber;

    @BindView(R.id.tie_register_number)
    TextInputEditText registerNumber;

    @BindView(R.id.tie_register_name)
    TextInputEditText registerName;

    @BindView(R.id.til_register_name)
    TextInputLayout inputLayoutName;

    @BindView(R.id.spin_country_code)
    Spinner spinnerCountry;

    @BindView(R.id.button_register)
    Button buttonRegister;


    private  SpinAdapter spinAdapter;

   private String countryIdvalue,countryId ,countryCode,countryName;


    @BindView(R.id.toolbar)
    Toolbar mtoolbar;

    public RegisterActivity() {
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        spinningCode();
        checkConnection();
        sessionManagement = new SessionManagement(this);
        TextUtilsInput.textUtilsMethod(registerEmail,inputLayoutEmail);
        TextUtilsInput.textUtilsMethod(registerPassword,inputLayoutPassword);
        TextUtilsInput.textUtilsMethod(registerNumber,inputLayoutNumber);
        TextUtilsInput.textUtilsMethod(registerName,inputLayoutName);
        setSupportActionBar(mtoolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);


    }

    @OnClick(R.id.toolbar_backbutton)
    void loginBackButton(){
        Intent i = new Intent(RegisterActivity.this,LoginActivity.class);
        startActivity(i);
        finish();


    }
//to select countrycode

    void spinningCode(){
        checkConnection();
        Log.d(TAG, "spinningCode: ");
        Api apiService = ApiClient.getClient().create(Api.class);
        Call<ResponseBody> call = apiService.countrycode(countryId,countryCode,countryName);
        call.enqueue(new Callback<ResponseBody>() {



            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                try {
                    assert response.body() != null;
                    String   res = response.body().string();
                    Log.d(TAG, "onResponsecountry: "+res);
                    JSONObject object = new JSONObject(res);
                    JSONArray object2 = object.getJSONArray("country");
                    countryCodeSpinnerList = new ArrayList<>();
                    int country = object2.length();
                    for (int i=0;i<country;i++) {
                        SpinnerModel model = new SpinnerModel();
                        JSONObject dataObject = object2.getJSONObject(i);
                        model.setCountryId(dataObject.getString("country_id"));
                        model.setCountryName(dataObject.getString("country_name"));
                        model.setCountrycode(dataObject.getString("country_code"));

                        countryCodeSpinnerList.add(model);



                    }
                    Log.d(TAG, "spinadapter: "+countryCodeSpinnerList);
                    spinAdapter = new SpinAdapter(RegisterActivity.this,R.layout.layout_spinner_values,countryCodeSpinnerList);
                    spinAdapter.setDropDownViewResource(R.layout.layout_spinner); // The drop down view
                    spinnerCountry.setAdapter(spinAdapter);
                    spinnerCountry.setSelection(98);

                    spinnerCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Log.d(TAG, "onItemClickposition: "+position);
                            countryIdvalue = spinAdapter.getCountryId(position);
                            Log.d(TAG, "onItemClickcountryId: "+countryIdvalue);
                            Log.d(TAG, "selectedcountrycode: "+countryIdvalue);
                            selectedItem = position;

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });




                } catch (Exception e) {
                    e.printStackTrace();
                }



            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                Log.d(TAG, "onFailure: "+t.getMessage());

            }
        });


    }
    @OnClick(R.id.btn_sign_in)
    void buttonLogin(){
        Intent i = new Intent(RegisterActivity.this,LoginActivity.class);
        startActivity(i);
    }




    @OnClick(R.id.button_register)
    void register() {
        checkConnection();
        Log.d(TAG, "registerbutton: ");

        if (TextUtilsInput.getString(registerName).isEmpty()) {
            inputLayoutName.setError("Please Enter the name");
        }
        Log.d(TAG, "registermobile: " + TextUtilsInput.getString(registerNumber));

        if (TextUtilsInput.getString(registerNumber).isEmpty()) {
            inputLayoutNumber.setError("Please Enter mobile number");
        } else if (TextUtilsInput.getString(registerNumber).length() < 10) {
            inputLayoutNumber.setError("Invalid Number");


        }

        Log.d(TAG, "registeremail: " + TextUtilsInput.getString(registerEmail));
        String email = TextUtilsInput.getString(registerEmail);
        if (TextUtilsInput.getString(registerEmail).isEmpty() || !TextUtilsInput.isValidEmail(email)) {
            inputLayoutEmail.setError("Enter valid Email Address");
        }

        Log.d(TAG, "registerpassword: " + TextUtilsInput.getString(registerPassword));
        if (TextUtilsInput.getString(registerPassword).isEmpty()) {
            inputLayoutPassword.setError("Please Enter a Password");
        } else if (TextUtilsInput.getString(registerPassword).length() < 6) {
            inputLayoutPassword.setError("Password should  atleast 6 characters");
        } else if (ConnectivityReceiver.isConnected(this)) {

            Api apiService = ApiClient.getClient().create(Api.class);
            Call<ResponseBody> call = apiService.registerstation(TextUtilsInput.getString(registerName), countryIdvalue, TextUtilsInput.getString(registerNumber), TextUtilsInput.getString(registerEmail), TextUtilsInput.getString(registerPassword));
            call.enqueue(new Callback<ResponseBody>() {

                @Override
                public void onResponse(@NotNull Call<ResponseBody> call, @NotNull retrofit2.Response<ResponseBody> response) {
                    try {
                        if (response.body() != null) {
                            String   res = response.body().string();
                            Log.d(TAG, "registerresponse: "+res);
                            JSONObject obj = new JSONObject(res);
                            if (obj.getBoolean("status")) {
                                String otp = obj.getString("data");
                                Intent i = new Intent(RegisterActivity.this, RegisterOtpverify.class);
                                i.putExtra("userEmail", TextUtilsInput.getString(registerEmail));
                                i.putExtra("userMobile", TextUtilsInput.getString(registerNumber));
                                Log.d(TAG, "onResponseusermobile: "+TextUtilsInput.getString(registerNumber));
                                i.putExtra("userCountry", countryIdvalue);
                                i.putExtra("userName", TextUtilsInput.getString(registerName));
                                i.putExtra("userPassword", TextUtilsInput.getString(registerPassword));
                                i.putExtra("userReceivedOtp",otp);

                                startActivity(i);
                                finish();


                            }
                            TextUtilsInput.backgroundThreadShortToast(RegisterActivity.this,obj.getString("message"));



                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                    Log.d(TAG, "onFailure" + t.getMessage());

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
            noConnection(RegisterActivity.this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.getInstance().setConnectivityListener(this::showSnack);

    }




    @Override
    public void onBackPressed() {
        Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();

    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }
}
