package gov.smart.health.activity.find.model;

import com.google.gson.annotations.SerializedName;

import gov.smart.health.activity.login.model.LoginResultDataModel;

/**
 * Created by laoniu on 2017/10/27.
 */

public class FindShareModel {
    @SerializedName("msg")
    public String msg;
    @SerializedName("success")
    public Boolean success;
    @SerializedName("detailMessage")
    public String detailMessage;
    @SerializedName("statusCode")
    public String statusCode;
    @SerializedName("resultDataType")
    public String resultDataType;
    @SerializedName("resultData")
    public String resultData;
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
