package com.example.shoppingapp.sessionManagement;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.shoppingapp.login.LoginActivity;

import java.util.HashMap;

public class SessionManagement {

    private SharedPreferences pref;

    private  SharedPreferences.Editor editor;
    private Context _context;

    private static final String PREF_NAME = "shopping";

    public static final String EMAIL = "email";
    private static final String ID = "id";
    private static final String COUNTRY_CODE = "countrycode";
    private static final String PASSWORD = "password";
    public static final String MOBILE = "mobile_no";
    public static final String NAME = "name";
    public static final String BIO = "bio";
    public static final String OCCUPATION = "occupation";
    public static final String WEBSITE = "website";
    public static final String LOCATION = "location";
    public static final String FACEBOOK = "facebook";
    public static final String PINTEREST = "pinterest";
    public static final String TWITTER = "twitter";
    public static final String INSTAGRAM = "instagram";
    public static final String YOUTUBE = "youtube";
    public static final String ADDRESS = "address";
    public static final String PINCODE = "pincode";
    public static final String IMAGE = "profileimage";
    public static final String COVERIMAGE = "coverimage";
    private static final String USERTYPE = "usertype";



    public SessionManagement(Context context){
        this._context = context;
        int PRIVATE_MODE = 0;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
        editor.apply();
    }






    public void createProfileUpdateSession(String id,String usertype,String name,String image,String coverimage,String countrycode, String mobile, String email, String password, String address, String pincode,String occupation,String website,String location,String facebook, String pinterest, String twitter, String instagram, String youtube, String bio){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(ID,id);
       editor.putString(USERTYPE,usertype);
        editor.putString(NAME,name);
        editor.putString(IMAGE,image);
        editor.putString(COVERIMAGE,coverimage);
        editor.putString(COUNTRY_CODE,countrycode);
        editor.putString(MOBILE,mobile);
        editor.putString(EMAIL,email);
        editor.putString(PASSWORD,password);
        editor.putString(ADDRESS,address);
        editor.putString(PINCODE,pincode);
        editor.putString(OCCUPATION,occupation);
        editor.putString(WEBSITE,website);
        editor.putString(LOCATION,location);
        editor.putString(FACEBOOK,facebook);
        editor.putString(PINTEREST,pinterest);
        editor.putString(TWITTER,twitter);
        editor.putString(INSTAGRAM,instagram);
        editor.putString(YOUTUBE,youtube);
        editor.putString(BIO,bio);




        // commit changes
        editor.commit();
    }

    public void updateProfileDetails(String id,  String name, String image, String coverimage, String email, String address, String pincode, String occupation, String website, String location, String facebook, String pinterest, String twitter, String instagram, String youtube, String bio){
        editor.putBoolean(IS_LOGIN, true);

        editor.putString(ID,id);
        editor.putString(NAME,name);
        editor.putString(IMAGE,image);
        editor.putString(COVERIMAGE,coverimage);
        editor.putString(EMAIL,email);
        editor.putString(ADDRESS,address);
        editor.putString(PINCODE,pincode);
        editor.putString(BIO,bio);
        editor.putString(OCCUPATION,occupation);
        editor.putString(WEBSITE,website);
        editor.putString(LOCATION,location);
        editor.putString(FACEBOOK,facebook);
        editor.putString(PINTEREST,pinterest);
        editor.putString(TWITTER,twitter);
        editor.putString(INSTAGRAM,instagram);
        editor.putString(YOUTUBE,youtube);
        // commit changes
        editor.commit();

    }
    public HashMap<String,String> getSessionDetails(){
        HashMap<String,String> user = new HashMap<>();

        user.put(NAME, pref.getString(NAME, ""));
        user.put(MOBILE, pref.getString(MOBILE, ""));
        user.put(ID, pref.getString(ID, ""));
        user.put(PASSWORD, pref.getString(PASSWORD, ""));
        user.put(EMAIL, pref.getString(EMAIL, ""));
        user.put(ADDRESS,pref.getString(ADDRESS,""));
        user.put(PINCODE,pref.getString(PINCODE,""));
        user.put(BIO, pref.getString(BIO, ""));
        user.put(OCCUPATION, pref.getString(OCCUPATION, ""));
        user.put(WEBSITE, pref.getString(WEBSITE, ""));
        user.put(LOCATION, pref.getString(LOCATION, ""));
        user.put(FACEBOOK, pref.getString(FACEBOOK, ""));
        user.put(PINTEREST, pref.getString(PINTEREST, ""));
        user.put(TWITTER, pref.getString(TWITTER, ""));
        user.put(INSTAGRAM,pref.getString(INSTAGRAM,""));
        user.put(YOUTUBE,pref.getString(YOUTUBE,""));
        user.put(COUNTRY_CODE,pref.getString(COUNTRY_CODE,""));
        user.put(IMAGE,pref.getString(IMAGE,""));
        user.put(COVERIMAGE,pref.getString(COVERIMAGE,""));




        return user;
    }
    public void checkLogin() {
        // Check login status
        if (!this.isLoggedIn()) {
            // user is not logged in redirect him to Login Activity

                Intent i = new Intent(_context, LoginActivity.class);

                // Closing all the Activities
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                // Add new Flag to start new Activity
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                // Staring Login Activity
                _context.startActivity(i);
            }
        }

    /**
     * Clear session details
     * */
    public void logoutUser(){

        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, LoginActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);

    }


    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN,false);
    }


    private static final String IS_LOGIN = "IsLoggedIn";

}
