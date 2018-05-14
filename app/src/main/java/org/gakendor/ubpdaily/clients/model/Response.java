package org.gakendor.ubpdaily.clients.model;

/**
 * Created by Dizzay on 11/10/2017.
 */

public class Response {

    String message;
    Object data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
