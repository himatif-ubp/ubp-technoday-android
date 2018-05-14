package org.gakendor.ubpdaily.clients.api;

import org.gakendor.ubpdaily.clients.model.Response;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.QueryMap;

/**
 * Created by Dizzay on 12/29/2017.
 */

public interface MobileService {

    @FormUrlEncoded
    @POST("mobile/checkNumber")
    Call<Response> checkNumber(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("mobile/login")
    Call<Response> login(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("mobile/register")
    Call<Response> register(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("mobile/update-profile")
    Call<Response> updateProfile(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("mobile/update-pwd")
    Call<Response> updatePassword(@FieldMap Map<String, String> map);

    @POST("mobile/events")
    Call<Response> events();

    @POST("mobile/events-all")
    Call<Response> eventsAll();

    @FormUrlEncoded
    @POST("mobile/my-events")
    Call<Response> myEvents(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("mobile/payment")
    Call<Response> payment(@FieldMap Map<String, String> map);

    @Multipart
    @POST("mobile/upload-bukti-tf")
    Call<Response> uploadBuktiTF(@Part MultipartBody.Part img, @PartMap Map<String, RequestBody> partMap);

    @FormUrlEncoded
    @POST("mobile/my-ticket")
    Call<Response> myTicket(@FieldMap Map<String, String> map);

    @POST("mobile/iklan")
    Call<Response> iklan();

    @POST("mobile/events-count")
    Call<Response> eventsCount();

    @FormUrlEncoded
    @POST("mobile/my-barcode")
    Call<Response> getBarcode(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("mobile/absen")
    Call<Response> absen(@FieldMap Map<String, String> map);

}   
