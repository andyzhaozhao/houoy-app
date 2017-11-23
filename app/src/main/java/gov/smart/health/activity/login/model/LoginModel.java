package gov.smart.health.activity.login.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by laoniu on 2017/10/27.
 */

public class LoginModel {
    @SerializedName("msg")
    public String msg;
    @SerializedName("success")
    public boolean success;
    @SerializedName("detailMessage")
    public String detailMessage;
    @SerializedName("statusCode")
    public String statusCode;
    @SerializedName("resultDataType")
    public String resultDataType;
    @SerializedName("resultData")
    public LoginResultDataModel resultData;
    @SerializedName("uploadId")
    public String uploadId;
    @SerializedName("def1")
    public String def1;
    @SerializedName("def2")
    public String def2;
    @SerializedName("def3")
    public String def3;
    @SerializedName("def4")
    public String def4;
    @SerializedName("def5")
    public String def5;
}
