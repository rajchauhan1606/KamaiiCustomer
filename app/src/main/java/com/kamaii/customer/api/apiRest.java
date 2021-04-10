package com.kamaii.customer.api;


import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface apiRest {
    @FormUrlEncoded
    @POST("getMyCurrentBookingUser")
    Call<ResponseBody> getbooking(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("getMyCurrentBookingUserHome")
    Call<ResponseBody> getbookingHome(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("get_booking_route")
    Call<ResponseBody> getBookingRoute(@Field("booking_id") String booking_id);

    @FormUrlEncoded
    @POST("get_user_address_history")
    Call<ResponseBody> getUserAddressHistory(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("RemoveAddressOperation")
    Call<ResponseBody> RemoveAddressOperation(@Field("id") String id, @Field("user_id") String user_id, @Field("address_type") String address_type);

    @FormUrlEncoded
    @POST("RouteOperation")
    Call<ResponseBody> RouteOperation(@Field("booking_id") String booking_id, @Field("route") String route, @Field("source_latitude") String source_latitude, @Field("source_longitude") String source_longitude, @Field("dest_latitude") String dest_latitude, @Field("dest_longitude") String dest_longitude);

    @FormUrlEncoded
    @POST("AddressOperation")
    Call<ResponseBody> AddressOperation(@Field("user_id") String user_id, @Field("address") String address, @Field("lat") String lat, @Field("lang") String lang, @Field("address_type") String address_type);

    @FormUrlEncoded
    @POST("getMyTestCurrentBookingUser")
    Call<ResponseBody> getTestbooking(@Field("user_id") String user_id, @Field("booking_id") String booking_id);

    @FormUrlEncoded
    @POST("getMyBookingDetails")
    Call<ResponseBody> getMyBookingDetails(@Field("booking_id") String booking_id);

    @FormUrlEncoded
    @POST("get_sublevel_cat")
    Call<ResponseBody> getthirdcategory(@Field("category_id") String category_id, @Field("sub_category_id") String sub_category_id);

    @FormUrlEncoded
    @POST("get_sublevel_cat2")
    Call<ResponseBody> getthirdcategory2(@Field("category_id") String category_id, @Field("sub_category_id") String sub_category_id, @Field("lat") String lat, @Field("lag") String lag, @Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("getMapCategory")
    Call<ResponseBody> getMapCategory(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("getAllCatalogCategory")
    Call<ResponseBody> getAllCatalogCategory(@Field("user_id") String user_id, @Field("device_token") String device_token);

    @FormUrlEncoded
    @POST("get_sub_cat")
    Call<ResponseBody> getsubcategory(@Field("category_id") String category_id, @Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("deleteNotification")
    Call<ResponseBody> deleteNotification(@Field("id") String id);

    @FormUrlEncoded
    @POST("checksignin")
    Call<ResponseBody> checksignin(@Field("mobile") String mobile, @Field("role") String role);

    @FormUrlEncoded
    @POST("send_otp")
    Call<ResponseBody> send_otp(@Field("mobile") String mobile, @Field("role") String role);

    @FormUrlEncoded
    @POST("getAllArtists")
    Call<ResponseBody> getArtist(@Field("category_id") String category_id, @Field("sub_category_id") String sub_category_id, @Field("thirdcategory") String thirdcategory, @Field("user_id") String user_id, @Field("latitude") String latitude, @Field("longitude") String longitude);

    @FormUrlEncoded
    @POST("getallservice_forsearch")
    Call<ResponseBody> getSearch(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("getallprovider_forsearch_temp")
    Call<ResponseBody> getSearchProvider(@Field("stext") String stext, @Field("user_id") String user_id, @Field("longitude") String longitude, @Field("latitude") String latitude);

    @FormUrlEncoded
    @POST("getproviderprofilesubcategory")
    Call<ResponseBody> getproviderprofilesubcategory(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("getArtistByid_api")
    Call<ResponseBody> getArtistbyid(@Field("sub_category_id") String sub_category_id, @Field("artist_id") String artist_id, @Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("getArtistByid_apiProductList")
    Call<ResponseBody> getArtistbyidProductList(@Field("sub_category_id") String sub_category_id, @Field("artist_id") String artist_id, @Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("getcartdata2")
    Call<ResponseBody> getcartdata(@Field("user_id") String user_id,@Field("device_token") String device_token);

    @FormUrlEncoded
    @POST("checkPaymentStatus")
    Call<ResponseBody> getpaymenttype(@Field("cat_id") String category_id, @Field("user_id") String user_id);


    @FormUrlEncoded
    @POST("getArtistByid_api_third")
    Call<ResponseBody> getArtistbyid_third(@Field("sub_category_id") String sub_category_id, @Field("artist_id") String artist_id, @Field("user_id") String user_id, @Field("thirdcategory") String thirdcategory);


    @POST("Update_Step_One")
    Call<ResponseBody> Update_Step_One(@Part("about_us") RequestBody about_us, @Part("description") RequestBody description, @Part("available_seat") RequestBody available_seat, @Part("saloon_id") RequestBody saloon_id, @Part MultipartBody.Part file);


    @Multipart
    @POST("get_shipping_data")
    Call<ResponseBody> getCartAmount(@Field("id") ArrayList<String> product_id, @Field("vendor_id") String vendor_id, ArrayList<String> qty);

}
