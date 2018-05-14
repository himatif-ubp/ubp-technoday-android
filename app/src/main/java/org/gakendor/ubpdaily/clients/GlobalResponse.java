package org.gakendor.ubpdaily.clients;

import com.google.gson.annotations.SerializedName;

/**
 * Created by user on 4/15/17.
 */

public class GlobalResponse {

    @SerializedName("status")
    String status;
//    @SerializedName("data")
//    UserModel data;
    @SerializedName("message")
    String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

//    public UserModel getData() {
//        return data;
//    }
//
//    public void setData(UserModel data) {
//        this.data = data;
//    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
