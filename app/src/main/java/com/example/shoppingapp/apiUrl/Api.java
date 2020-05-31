package com.example.shoppingapp.apiUrl;


import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface Api {

@POST("sliders.json")
Call<ResponseBody> sliderImage();

@GET("bannerimage.json")
Call<ResponseBody> bannerImage();

@POST("productDetails.json")
@FormUrlEncoded
Call<ResponseBody> productDetails(@Field("product_items_id") String productItemsId);

@POST("List.json")
@FormUrlEncoded
Call<ResponseBody> listItems(@Field("product_id") String productId);

@POST("Itemslist.json")
Call<ResponseBody> horizontalListItems();

  @POST("sign_in.php")
  @FormUrlEncoded
  Call<ResponseBody> userAuth(@Field("mobile_number") String mobileNumber, @Field("password") String password);

  @POST("register_verify.php")
  @FormUrlEncoded
  Call<ResponseBody> registerstation( @Field("name") String name,@Field("country_code") String country_code,@Field("mobile_number") String mobile_number ,
                                      @Field("email") String email,  @Field("password") String password  );
  @POST("country_code.php")
  @FormUrlEncoded
  Call<ResponseBody> countrycode(@Field("country_id") String countryId,@Field("country_code") String countryCode,@Field("country_name") String countryName);

  @POST("change_password.php")
  @FormUrlEncoded
  Call<ResponseBody> resetPassword(@Field("mobile_number") String mobileNumber, @Field("new_password") String password);

  @POST("otp_verification.php")
  @FormUrlEncoded
  Call<ResponseBody> registerVerify(@Field("mobile_number") String mobileNumber, @Field("otp") String otp);

  @POST("resend_otp.php")
  @FormUrlEncoded
  Call<ResponseBody> resendotp (@Field("mobile_number") String mobileNumber);

  @FormUrlEncoded
  @POST("view_profile.php")
  Call<ResponseBody> profile( @Field("mobile_number") String mobileNumber);

  @POST("forgot_password.php")
  @FormUrlEncoded
  Call<ResponseBody> forgotPassword(@Field("mobile_number") String mobileNumber);

  @POST("otp.php")
  @FormUrlEncoded
  Call<ResponseBody> otpverify(@Field("mobile_number") String mobileNumber, @Field("otp") String otp);


  @Multipart
  @POST("profile_update.php")
  Call<ResponseBody> updateprofile(@Header("Authorization") String authHeader, @Part MultipartBody.Part image, @Part MultipartBody.Part cover_image, @Part("mobile_number") String mobileNumber, @Part("name") String name, @Part("email") String email,
                                   @Part("address") String address, @Part("pincode") String pincode,  @Part("occupation") String occupation, @Part("website") String website ,
                                   @Part("location") String location, @Part("facebook") String facebook, @Part("pinterest") String pinterest,
                                   @Part("twitter") String twitter, @Part("instagram") String instagram, @Part("youtube") String youtube, @Part("bio") String bio);








}
