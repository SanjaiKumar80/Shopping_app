package com.example.shoppingapp.login;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.shoppingapp.R;
import com.example.shoppingapp.apiUrl.Api;
import com.example.shoppingapp.camera.FilePath;
import com.example.shoppingapp.camera.ImagePickerActivity;
import com.example.shoppingapp.connectivityReceiver.ConnectivityReceiver;
import com.example.shoppingapp.connectivityReceiver.MyApplication;
import com.example.shoppingapp.sessionManagement.SessionManagement;
import com.example.shoppingapp.utils.TextUtilsInput;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static com.example.shoppingapp.apiUrl.ApiClient.BASE_URL;
import static com.example.shoppingapp.config.Config.noConnection;
import static okhttp3.MediaType.parse;
import static okhttp3.RequestBody.create;

public class ProfileUpdateActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener{
    private static final String TAG = ProfileUpdateActivity.class.getSimpleName();
    private static final  int AVATAR = 200;
    private static final  int COVER = 250;

    private static final int USERAVATAR = 100;
    private    boolean isStatus;
    private static final int USERAVATARGALLERY = 120;
    private static  final int COVERIMAGECAMERA = 50;
    private static  final int COVERIMAGEGALLERY = 150;
    SessionManagement management;

    @BindView(R.id.toolbar)
    Toolbar mtoolbar;
    private boolean iscoverstatus;


    @BindView(R.id.button_save)
    Button buttonSave;

    @BindView(R.id.tie_edit_profile_name)
    TextInputEditText textInputEditTextEditName;

    @BindView(R.id.til_edit_profile_name)
    TextInputLayout textInputLayoutName;

    @BindView(R.id.tie_edit_profile_email)
    TextInputEditText textInputEditTextEditEmail;

    @BindView(R.id.til_edit_profile_email)
    TextInputLayout textInputLayoutEmail;

    @BindView(R.id.tie_edit_profile_bio)
    TextInputEditText textInputEditTextEditBio;

    @BindView(R.id.tie_edit_profile_occupation)
    TextInputEditText textInputEditTextEditOccupation;

    @BindView(R.id.tie_edit_profile_website)
    TextInputEditText textInputEditTextEditWebsite;

    @BindView(R.id.tie_edit_profile_location)
    TextInputEditText textInputEditTextEditLocation;

    @BindView(R.id.tie_edit_profile_facebook)
    TextInputEditText textInputEditTextEditFacebook;

    @BindView(R.id.tie_edit_profile_pinterest)
    TextInputEditText textInputEditTextEditPinterest;

    @BindView(R.id.tie_edit_profile_twitter)
    TextInputEditText textInputEditTextTwitter;

    @BindView(R.id.tie_edit_profile_instagram)
    TextInputEditText textInputEditTextinstagram;

    @BindView(R.id.tie_edit_profile_youtube)
    TextInputEditText textInputEditTextYoutube;

    @BindView(R.id.civ_edit_profile_image)
    ImageView civCircularImage;

    @BindView(R.id.iv_profile_cover_image)
    ImageView imageViewCover;

    @BindView(R.id.ib_profile_click)
    ImageButton imageButtonProfile;

    @BindView(R.id.tie_edit_profile_address)
    TextInputEditText textInputEditTextAddress;

    @BindView(R.id.tie_edit_profile_pincode)
    TextInputEditText textInputEditTextPincode;




    private   Bitmap bitmap;

    private MultipartBody.Part image;

    private  MultipartBody.Part coverImage;

    private String profilenumber,uripath,uricoverpath;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_update);
        ButterKnife.bind(this);
        checkConnection();
        management = new SessionManagement(getApplicationContext());
        HashMap<String, String> userdetails = management.getSessionDetails();

          Intent i = getIntent();
        if (i!=null) {

            profilenumber = getIntent().getStringExtra("profileuserNumber");
            Log.d(TAG, "profilenumber: "+profilenumber);

        }


        TextUtilsInput.textUtilsMethod(textInputEditTextEditName,textInputLayoutName);

        TextUtilsInput.textUtilsMethod(textInputEditTextEditEmail,textInputLayoutEmail);

        String profileImage = userdetails.get(SessionManagement.IMAGE);
        String userimage = BASE_URL+"uploads/"+ profileImage;
        Log.d(TAG, "onCreateimage: "+userimage);


        String defaultCoverImage = userdetails.get(SessionManagement.COVERIMAGE);
        String usercoverimage = BASE_URL+"images/"+ defaultCoverImage;
        Log.d(TAG, "onCreatedefaultimage: "+usercoverimage);


        HashMap<String,String> userupdatedetails = management.getSessionDetails();

        textInputEditTextEditName.setText(userupdatedetails.get(SessionManagement.NAME));
        textInputEditTextEditName.setSelection(Objects.requireNonNull(textInputEditTextEditName.getText()).length());


        textInputEditTextEditEmail.setText(userupdatedetails.get(SessionManagement.EMAIL));
        textInputEditTextEditEmail.setSelection(Objects.requireNonNull(textInputEditTextEditEmail.getText()).length());


        textInputEditTextEditBio.setText(userupdatedetails.get(SessionManagement.BIO));
        Log.d(TAG, "userbio: "+userupdatedetails.get(SessionManagement.BIO));
        textInputEditTextEditBio.setSelection(Objects.requireNonNull(textInputEditTextEditBio.getText()).length());


        textInputEditTextEditOccupation.setText(userupdatedetails.get(SessionManagement.OCCUPATION));
        textInputEditTextEditOccupation.setSelection(Objects.requireNonNull(textInputEditTextEditOccupation.getText()).length());


        textInputEditTextEditWebsite.setText(userupdatedetails.get(SessionManagement.WEBSITE));
        textInputEditTextEditWebsite.setSelection(Objects.requireNonNull(textInputEditTextEditWebsite.getText()).length());


        textInputEditTextEditLocation.setText( userupdatedetails.get(SessionManagement.LOCATION));
        Log.d(TAG, "userlocation: "+userupdatedetails.get(SessionManagement.LOCATION));
        textInputEditTextEditLocation.setSelection(Objects.requireNonNull(textInputEditTextEditLocation.getText()).length());


        String profileupdatefacebook = userupdatedetails.get(SessionManagement.FACEBOOK);
        textInputEditTextEditFacebook.setText(profileupdatefacebook);
        textInputEditTextEditFacebook.setSelection(Objects.requireNonNull(textInputEditTextEditFacebook.getText()).length());


        textInputEditTextEditPinterest.setText(userupdatedetails.get(SessionManagement.PINTEREST));
        Log.d(TAG, "profileupdatepinterest: "+userupdatedetails.get(SessionManagement.PINTEREST));
        textInputEditTextEditPinterest.setSelection(Objects.requireNonNull(textInputEditTextEditPinterest.getText()).length());


        textInputEditTextTwitter.setText( userupdatedetails.get(SessionManagement.TWITTER));
        Log.d(TAG, "profileupdatetwitter: "+userupdatedetails.get(SessionManagement.TWITTER));
        textInputEditTextTwitter.setSelection(Objects.requireNonNull(textInputEditTextTwitter.getText()).length());


        textInputEditTextinstagram.setText(userupdatedetails.get(SessionManagement.INSTAGRAM));
        textInputEditTextinstagram.setSelection(Objects.requireNonNull(textInputEditTextinstagram.getText()).length());


        textInputEditTextYoutube.setText( userupdatedetails.get(SessionManagement.YOUTUBE));
        textInputEditTextYoutube.setSelection(Objects.requireNonNull(textInputEditTextYoutube.getText()).length());


        textInputEditTextAddress.setText(userupdatedetails.get(SessionManagement.ADDRESS));
        Log.d(TAG, "useraddress: "+userupdatedetails.get(SessionManagement.ADDRESS));
        textInputEditTextAddress.setSelection(Objects.requireNonNull(textInputEditTextAddress.getText()).length());

        textInputEditTextPincode.setText(userupdatedetails.get(SessionManagement.PINCODE));
        textInputEditTextPincode.setSelection(Objects.requireNonNull(textInputEditTextPincode.getText()).length());


        ImagePickerActivity.clearCache(this);
        setSupportActionBar(mtoolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);



        Glide.with(ProfileUpdateActivity.this)
                .load(userimage)
                .placeholder(R.drawable.defauleimage)
                .override(600,200)
                .fitCenter()
                .into(civCircularImage);
        civCircularImage.setColorFilter(ContextCompat.getColor(ProfileUpdateActivity.this, android.R.color.transparent));

        Glide.with(ProfileUpdateActivity.this)
                .load( usercoverimage)
                .placeholder(R.drawable.backgroundimage)
                .override(600,200)
                .fitCenter()
                .into(imageViewCover);
        imageViewCover.setColorFilter(ContextCompat.getColor(ProfileUpdateActivity.this, android.R.color.transparent));




    }
    //to add coverimage


    private void loadProfile( String urlpathimage, ImageView imgProfile) {
        Log.d(TAG, "Image cache path: " + urlpathimage);

        Glide.with(this).load(urlpathimage)
                .override(600 ,200)
                .fitCenter()
                .into(imgProfile);
        imgProfile.setColorFilter(ContextCompat.getColor(this, android.R.color.transparent));
    }



    @OnClick(R.id.toolbar_backbutton)
    void loginbackbutton(){
        Intent i = new Intent(ProfileUpdateActivity.this,ProfileActivity.class);
        startActivity(i);
        finish();


    }
    @OnClick( R.id.civ_edit_profile_image)
    void userProfileImage(){
        permissionSelected(AVATAR);




    }



    @OnClick({R.id.ib_profile_click,R.id.iv_profile_cover_image})
    void onProfileCoverImage(){
        permissionSelected(COVER);


    }



    public void permissionSelected(int code) {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            showImagePickerOptions(code);
                        }

                        if (report.isAnyPermissionPermanentlyDenied()) {
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private void showImagePickerOptions(int code) {
        Log.d(TAG, "showImagePickerOptions: "+code);
        ImagePickerActivity.showImagePickerOptions(this, new ImagePickerActivity.PickerOptionListener() {
            @Override
            public void onTakeCameraSelected() {
                if (code == AVATAR) {
                    launchCameraIntent(USERAVATAR);
                }else {
                    launchCameraIntent(COVERIMAGECAMERA);

                }
            }

            @Override
            public void onChooseGallerySelected() {
                if (code == COVER){
                    launchGalleryIntent(USERAVATARGALLERY);

                }else {
                    launchGalleryIntent(COVERIMAGEGALLERY);

                }

            }
        });
    }

    private void launchCameraIntent(int code ) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, code);
    }



    private void launchGalleryIntent(int code) {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, code);

    }



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_CANCELED) {
            return;
        }


        if (resultCode == Activity.RESULT_OK) {
            Log.d(TAG, "onActivityResultcode: " + requestCode);
            if (requestCode == USERAVATAR) {
                assert data != null;

                if(data.getData()==null){
                    bitmap = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
                    civCircularImage.setImageBitmap(bitmap);
                    Uri tempUri = getImageUri(getApplicationContext(), bitmap);
                    uripath = FilePath.getPath(ProfileUpdateActivity.this,tempUri);
                    if (uripath != null) {
                        isStatus = true;
                    }

                    loadProfile(uripath, civCircularImage);


                }

            }




            if (requestCode == COVERIMAGEGALLERY) {
                assert data != null;
                Uri selectedFileURI = data.getData();

                // Uri selectedFileURI = data.getParcelableExtra("path");
                Log.d(TAG, "onActivityResulturiimage: " + selectedFileURI);
                uripath = FilePath.getPath(ProfileUpdateActivity.this, selectedFileURI);
                if (uripath != null) {
                    isStatus = true;
                }

                Log.d(TAG, "onActivityResulturi " + uripath);
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedFileURI);
                    Log.d(TAG, "onActivityResulturiimage: " + bitmap);
                    civCircularImage.setImageBitmap(bitmap);
                    loadProfile(uripath, civCircularImage);


                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            if (requestCode == COVERIMAGECAMERA) {
                assert data != null;

                if(data.getData()==null){
                    bitmap = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
                    imageViewCover.setImageBitmap(bitmap);
                }

                Uri tempUri = getImageUri(getApplicationContext(), bitmap);
                uricoverpath = FilePath.getPath(ProfileUpdateActivity.this,tempUri);
                if (uricoverpath != null) {
                    iscoverstatus = true;
                }
                // civCircularImage.setImageBitmap(bitmap1);
                loadProfile(uricoverpath, imageViewCover);



            }




            if (requestCode == USERAVATARGALLERY) {
                assert data != null;
                // Uri selectedFileURI = data.getParcelableExtra("path");
                Uri selectedFileURI = data.getData();

                Log.d(TAG, "onActivityResulturiimage: " + selectedFileURI);
                uricoverpath = FilePath.getPath(ProfileUpdateActivity.this, selectedFileURI);
                if (uricoverpath != null) {
                    iscoverstatus = true;
                }

                Log.d(TAG, "onActivityResulturi " + uricoverpath);
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedFileURI);
                    Log.d(TAG, "onActivityResulturiimage: " + bitmap);
                    imageViewCover.setImageBitmap(bitmap);
                    loadProfile(uricoverpath, imageViewCover);
                    //image_base64 = encodeToBase64(bitmap, Bitmap.CompressFormat.JPEG, 15);


                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }


    }




    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }



    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileUpdateActivity.this);
        builder.setTitle(getString(R.string.dialog_permission_title));
        builder.setMessage(getString(R.string.dialog_permission_message));
        builder.setPositiveButton(getString(R.string.go_to_settings), (dialog, which) -> {
            dialog.cancel();
            openSettings();
        });
        builder.setNegativeButton(getString(android.R.string.cancel), (dialog, which) -> dialog.cancel());
        builder.show();

    }

    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }



    @OnClick(R.id.button_save)
    void profileUpdate() {
        checkConnection();

        Log.d(TAG, "profileUpdatebutton: ");

        if (ConnectivityReceiver.isConnected(this)) {
            // for avartarimage
            if (isStatus) {
                File file = new File(uripath);
                Log.d(TAG, "profileUpdatecamera: "+file);
                RequestBody fileReqBody = RequestBody.create(parse("image/*"), file);
                image = MultipartBody.Part.createFormData("image", file.getName(), fileReqBody);
            } else {
                civCircularImage.setImageResource(R.drawable.default_profile);
            }

            // for coverimage
            if (iscoverstatus) {
                File file = new File(uricoverpath);
                RequestBody fileReqBody = create(parse("image/*"), file);
                coverImage = MultipartBody.Part.createFormData("cover_image", file.getName(), fileReqBody);
            } else {
                imageViewCover.setImageResource(R.drawable.backgroundimage);
            }




            Log.d(TAG, "profileUpdatename: " + TextUtilsInput.getString(textInputEditTextEditName) + " " + TextUtilsInput.getString(textInputEditTextEditEmail) + " " + TextUtilsInput.getString(textInputEditTextEditBio) + " " + TextUtilsInput.getString(textInputEditTextEditOccupation) + " " + TextUtilsInput.getString(textInputEditTextEditLocation) + " " + TextUtilsInput.getString(textInputEditTextEditWebsite) + " "
                    + TextUtilsInput.getString(textInputEditTextEditFacebook) + " " + TextUtilsInput.getString(textInputEditTextEditPinterest) + " " + TextUtilsInput.getString(textInputEditTextTwitter) + " " + TextUtilsInput.getString(textInputEditTextinstagram) + " " + TextUtilsInput.getString(textInputEditTextYoutube) + " "+TextUtilsInput.getString(textInputEditTextAddress) + " "+ TextUtilsInput.getString(textInputEditTextPincode));
            Log.d(TAG, "profileUpdateimage: " + image);
            Log.d(TAG, "profileUpdatecoverimage: " +coverImage);
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build();
            Api apiService = retrofit.create(Api.class);
            Call<ResponseBody> call = apiService.updateprofile("Auth Token",image,coverImage, profilenumber, TextUtilsInput.getString(textInputEditTextEditName), TextUtilsInput.getString(textInputEditTextEditEmail),TextUtilsInput.getString(textInputEditTextAddress),TextUtilsInput.getString(textInputEditTextPincode), TextUtilsInput.getString(textInputEditTextEditOccupation), TextUtilsInput.getString(textInputEditTextEditWebsite), TextUtilsInput.getString(textInputEditTextEditLocation), TextUtilsInput.getString(textInputEditTextEditFacebook), TextUtilsInput.getString(textInputEditTextEditPinterest), TextUtilsInput.getString(textInputEditTextTwitter), TextUtilsInput.getString(textInputEditTextinstagram), TextUtilsInput.getString(textInputEditTextYoutube), TextUtilsInput.getString(textInputEditTextEditBio));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                    Log.d(TAG, "onResponse: ");
                    try {
                        if (response.body() != null) {
                            String res = response.body().string();
                            Log.d(TAG, "ProfileUpdateResponse: " + res);
                            JSONObject object = new JSONObject(res);
                            JSONObject obj = object.getJSONObject("data");
                            String image = obj.getString("image");
                            Log.d(TAG, "imagepic: "+image);
                            String backgroundimage = obj.getString("cover_image");
                            Log.d(TAG, "backgroundpic: "+backgroundimage);
                            String id = obj.getString("id");
                            Log.d(TAG, "updateid: "+id);

                            if (object.getBoolean("status")) {

                                Intent i = new Intent(ProfileUpdateActivity.this, ProfileActivity.class);
                                startActivity(i);
                                management.updateProfileDetails(id, TextUtilsInput.getString(textInputEditTextEditName),image,backgroundimage, TextUtilsInput.getString(textInputEditTextEditEmail),
                                        TextUtilsInput.getString(textInputEditTextAddress), TextUtilsInput.getString(textInputEditTextPincode),TextUtilsInput.getString(textInputEditTextEditOccupation), TextUtilsInput.getString(textInputEditTextEditWebsite), TextUtilsInput.getString(textInputEditTextEditLocation), TextUtilsInput.getString(textInputEditTextEditFacebook),
                                        TextUtilsInput.getString(textInputEditTextEditPinterest), TextUtilsInput.getString(textInputEditTextTwitter), TextUtilsInput.getString(textInputEditTextinstagram), TextUtilsInput.getString(textInputEditTextYoutube), TextUtilsInput.getString(textInputEditTextEditBio));



                            }
                            TextUtilsInput.backgroundThreadShortToast(ProfileUpdateActivity.this,object.getString("message"));

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


    private void checkConnection()
    {
        boolean isConnected = ConnectivityReceiver.isConnected(this);
        showSnack(isConnected);
    }

    // Showing the status in Snackbar
    private void showSnack(boolean isConnected) {
        Log.d(TAG, "showSnack: ");

        if (!isConnected) {
            Log.d(TAG, "isconnected: ");
            noConnection(getApplicationContext());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.getInstance().setConnectivityListener(this::showSnack);

    }




    @Override
    public void onBackPressed() {
        Intent i = new Intent(ProfileUpdateActivity.this, ProfileActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();

    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);

    }
}
