package com.example.shoppingapp.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.shoppingapp.apiUrl.Api;
import com.example.shoppingapp.apiUrl.ApiClient;
import com.example.shoppingapp.connectivityReceiver.ConnectivityReceiver;
import com.example.shoppingapp.connectivityReceiver.MyApplication;
import com.example.shoppingapp.MainActivity;
import com.example.shoppingapp.R;
import com.example.shoppingapp.sessionManagement.SessionManagement;
import com.example.shoppingapp.utils.TextUtilsInput;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;


import static com.example.shoppingapp.config.Config.noConnection;

public class ProfileActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    private static final String TAG = ProfileActivity.class.getSimpleName();

    private SessionManagement management;


    @BindView(R.id.civ_profile_image)
    ImageView imageProfile;

    @BindView(R.id.txt_profile_name)
    TextView textProfileName;

    @BindView(R.id.txt_profile_email)
    TextView textProfileEmail;

    @BindView(R.id.txt_profile_number)
    TextView textProfileNumber;

    @BindView(R.id.txt_profile_location)
    TextView textProfileLocation;

    @BindView(R.id.txt_bio_profile)
    TextView textProfileBio;

    @BindView(R.id.txt_profile_occupation)
    TextView textProfileOccupation;

    @BindView(R.id.txt_profile_website)
    TextView textProfileWebsite;

    @BindView(R.id.txt_profile_facebook)
    TextView textProfileFacebook;

    @BindView(R.id.txt_profile_pinterest)
    TextView textProfilePinterest;

    @BindView(R.id.txt_profile_twitter)
    TextView textProfileTwitter;

    @BindView(R.id.txt_profile_instagram)
    TextView textProfileInstagram;

    @BindView(R.id.txt_profile_youtube)
    TextView textProfileYoutube;

    @BindView(R.id.txt_profile_address)
    TextView textProfileAddress;

    @BindView(R.id.txt_profile_pincode)
    TextView textProfilePincode;



    @BindView(R.id.iv_cover_profile_image)
    ImageView imageViewCover;

    @BindView(R.id.toolbar)
    Toolbar mtoolbar;

    private String coverImage, profileImage;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        setSupportActionBar(mtoolbar);
        checkConnection();
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        management = new SessionManagement(getApplicationContext());
        setProfileDetails();

        showprofile();
    }


    private void setProfileDetails(){
        HashMap<String, String> userdetails = management.getSessionDetails();


        textProfileEmail.setText(userdetails.get(SessionManagement.EMAIL));
        Log.d(TAG, "profileemail: " + userdetails.get(SessionManagement.EMAIL));

        textProfileNumber.setText(userdetails.get(SessionManagement.MOBILE));
        Log.d(TAG, "profilenumber: " + userdetails.get(SessionManagement.MOBILE));


        textProfileName.setText(userdetails.get(SessionManagement.NAME));

        textProfileLocation.setText(userdetails.get(SessionManagement.LOCATION));

        textProfileAddress.setText(userdetails.get(SessionManagement.ADDRESS));
        Log.d(TAG, "profileAddress: "+userdetails.get(SessionManagement.ADDRESS));



        textProfilePincode.setText(userdetails.get(SessionManagement.YOUTUBE));
        Log.d(TAG, "profilePincode: "+userdetails.get(SessionManagement.YOUTUBE));



        textProfileBio.setText(userdetails.get(SessionManagement.BIO));
        Log.d(TAG, "profilebio: "+userdetails.get(SessionManagement.BIO));


        textProfileOccupation.setText(userdetails.get(SessionManagement.OCCUPATION));
        Log.d(TAG, "profileoccupation: "+userdetails.get(SessionManagement.OCCUPATION));


        textProfileWebsite.setText(userdetails.get(SessionManagement.WEBSITE));
        Log.d(TAG, "profilewebsite: "+userdetails.get(SessionManagement.WEBSITE));


        textProfileFacebook.setText(userdetails.get(SessionManagement.FACEBOOK));
        Log.d(TAG, "profilefacebook: "+userdetails.get(SessionManagement.FACEBOOK));


        textProfilePinterest.setText(userdetails.get(SessionManagement.PINTEREST));
        Log.d(TAG, "profilepinterest: "+userdetails.get(SessionManagement.PINTEREST));


        textProfileTwitter.setText(userdetails.get(SessionManagement.TWITTER));
        Log.d(TAG, "profiletwitter: "+userdetails.get(SessionManagement.TWITTER));


        textProfileInstagram.setText(userdetails.get(SessionManagement.INSTAGRAM));
        Log.d(TAG, "profileinstagram: "+userdetails.get(SessionManagement.INSTAGRAM));


        textProfileYoutube.setText(userdetails.get(SessionManagement.YOUTUBE));




    }


    private void showprofile(){
        checkConnection();
        Log.d(TAG, "showprofile: ");
        Log.d(TAG, "profilenumber: "+TextUtilsInput.getString(textProfileNumber));


        Api apiService = ApiClient.getClient().create(Api.class);
        Call<ResponseBody> call = apiService.profile(TextUtilsInput.getString(textProfileNumber));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull retrofit2.Response<ResponseBody> response) {
                Log.d(TAG, "onResponse: ");
                try {
                    if (response.body()!=null){
                        String res = response.body().string();
                        Log.d(TAG, "profileonResponse: "+res);
                        JSONObject object = new JSONObject(res);

                        JSONObject obj = object.getJSONObject("data");

                        textProfileName.setText(obj.getString("name"));

                        textProfileNumber.setText(obj.getString("mobile_no"));

                        textProfileEmail.setText(obj.getString("email"));

                        textProfileLocation.setText(obj.getString("location"));

                        textProfileBio.setText(obj.getString("bio"));

                        textProfileOccupation.setText(obj.getString("occupation"));

                        textProfileWebsite.setText(obj.getString("website"));

                        textProfileFacebook.setText(obj.getString("facebook"));

                        textProfilePinterest.setText(obj.getString("pinterest"));
                        Log.d(TAG, "useerpinterest: "+obj.getString("pinterest"));

                        textProfileTwitter.setText(obj.getString("twitter"));
                        Log.d(TAG, "usertwitter: "+obj.getString("twitter"));


                        textProfileInstagram.setText(obj.getString("instagram"));

                        textProfileYoutube.setText(obj.getString("youtube"));

                        textProfileAddress.setText(obj.getString("address"));

                        textProfilePincode.setText(obj.getString("pincode"));


                        profileImage = object.getString("path1");
                        Log.d(TAG, "onResponseimage: "+profileImage);

                        Glide.with(ProfileActivity.this).load(profileImage)
                                .placeholder(R.drawable.defauleimage)
                                .into(imageProfile);
                        imageProfile.setColorFilter(ContextCompat.getColor(ProfileActivity.this, android.R.color.transparent));


                        coverImage = object.getString("path2");
                        Log.d(TAG, "onResponseimagepath: "+coverImage);

                        Glide.with(ProfileActivity.this)
                                .load(coverImage)
                                .placeholder(R.drawable.backgroundimage)
                                .override(600,200)
                                .fitCenter()
                                .into(imageViewCover);
                        imageViewCover.setColorFilter(ContextCompat.getColor(ProfileActivity.this, android.R.color.transparent));





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

    }

    @OnClick(R.id.button_logout)
    void logoutButton(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Are You Sure ?")
                .setCancelable(false)
                .setPositiveButton("Logout", (dialog, id) -> {
                    management.logoutUser();
                    finish();

                })
                .setNegativeButton("No", (dialog, id) -> dialog.dismiss());
        AlertDialog alert = builder.create();
        alert.show();

    }
    @OnClick(R.id.toolbar_backbutton)
    void loginBackButton(){
        Intent i = new Intent(ProfileActivity.this, MainActivity.class);
        startActivity(i);
        finish();


    }
    @OnClick(R.id.txt_bio_profile)
    void bioEditProfile(){
        textProfileBio.setVisibility(View.INVISIBLE);
        Intent  i = new Intent(ProfileActivity.this, ProfileUpdateActivity.class);
        i.putExtra("profileuserNumber",TextUtilsInput.getString(textProfileNumber));
        startActivity(i);
        finish();

    }

    @OnClick(R.id.txt_profile_location)
    void locationEditProfile(){
        textProfileBio.setVisibility(View.INVISIBLE);
        Intent  i = new Intent(ProfileActivity.this, ProfileUpdateActivity.class);
        i.putExtra("profileuserNumber",TextUtilsInput.getString(textProfileNumber));
        startActivity(i);
        finish();

    }

    @OnClick(R.id.txt_profile_occupation)
    void occupationEditProfile(){
        textProfileOccupation.setVisibility(View.INVISIBLE);
        Intent  i = new Intent(ProfileActivity.this, ProfileUpdateActivity.class);
        i.putExtra("profileuserNumber",TextUtilsInput.getString(textProfileNumber));
        startActivity(i);
        finish();

    }


    @OnClick(R.id.btn_change_password)
    void profilePasswordChange(){
        Intent i = new Intent(ProfileActivity.this, ProfileChangePassword.class);
        startActivity(i);


    }





    @OnClick({R.id.button_profile_update,R.id.civ_profile_image,R.id.iv_cover_profile_image})
    void editProfile(){
        Intent i = new Intent(ProfileActivity.this,ProfileUpdateActivity.class);
        i.putExtra("profileuserNumber",TextUtilsInput.getString(textProfileNumber));
        startActivity(i);
        finish();
    }
    private void checkConnection()
    {
        boolean isConnected = ConnectivityReceiver.isConnected(this);
        showSnack(isConnected);
    }

    // Showing the status in Snackbar
    private void showSnack(boolean isConnected) {

        if (!isConnected) {
            Log.d(TAG, "connectivitynetwork: ");
            noConnection(ProfileActivity.this);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.getInstance().setConnectivityListener(this::showSnack);

    }





    @Override
    public void onBackPressed() {
        Intent i = new Intent(ProfileActivity.this,MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }
}
