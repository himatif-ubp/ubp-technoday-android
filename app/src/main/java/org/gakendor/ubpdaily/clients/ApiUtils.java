package org.gakendor.ubpdaily.clients;

import android.content.Context;

import org.gakendor.ubpdaily.clients.api.MobileService;
import org.gakendor.ubpdaily.util.Static;


public class ApiUtils {

    public static String API = Static.BASE_URL;


//
    public static MobileService MobileService(Context context){
        return RetrofitClient.getClient(context, API).create(MobileService.class);
    }
//
//    public static RedeemService ReddemService(Context context){
//        return RetrofitClient.getClient(context, API).create(RedeemService.class);
//    }
//
//    public static OutletsService OutletsService(Context context){
//        return RetrofitClient.getClient(context, API).create(OutletsService.class);
//    }
//
//    public static RegistrationOutletService RegistrationOutletService(Context context){
//        return RetrofitClient.getClient(context, API).create(RegistrationOutletService.class);
//    }
//
//    public static SetupService SetupService(Context context){
//        return RetrofitClient.getClient(context, API).create(SetupService.class);
//    }
//
//    public static ChatService ChatService(Context context){
//        return RetrofitClient.getClient(context, API).create(ChatService.class);
//    }
//
//    public static DaerahService DaerahService(Context context){
//        return RetrofitClient.getClient(context, API).create(DaerahService.class);
//    }

}
