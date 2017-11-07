package gov.smart.health.activity.vr.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by laoniu on 11/8/17.
 */
public class SportVideoModel {
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
    public SportVideoListModel resultData;
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
    @SerializedName("start")
    public String start;
    @SerializedName("length")
    public String length;
    @SerializedName("orderColumnName")
    public String orderColumnName;
    @SerializedName("orderDir")
    public String orderDir;
    @SerializedName("total")
    public String total;
}
