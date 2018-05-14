package org.gakendor.ubpdaily.clients.daerah;

import org.gakendor.ubpdaily.clients.model.Response;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

/**
 * Created by Dizzay on 12/29/2017.
 */

public interface DaerahService {

    @GET("daerah/propinsi")
    Call<Response> propinsi();

    @GET("daerah/kabupaten")
    Call<Response> kabupaten(@QueryMap Map<String, String> map);

    @GET("daerah/kecamatan")
    Call<Response> kecamatan(@QueryMap Map<String, String> map);

    @GET("daerah/kelurahan")
    Call<Response> kelurahan(@QueryMap Map<String, String> map);
}   
