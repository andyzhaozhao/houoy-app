package gov.smart.health.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by laoniu on 2017/10/27.
 */

public class LoginModel {
    @SerializedName("msg")
    private String msg;

    @Override
    public String toString() {
        return "LoginModel{" +
                "msg=" + msg +
                '}';
    }

}
