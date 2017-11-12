package gov.smart.health.activity.vr.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by laoniu on 11/8/17.
 */
public class VideoFolderListModel implements Serializable{
    @SerializedName("memo")
    public String memo;
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
    @SerializedName("be_std")
    public String be_std;
    @SerializedName("ts")
    public String ts;
    @SerializedName("dr")
    public String dr;
    @SerializedName("start")
    public String start;
    @SerializedName("length")
    public String length;
    @SerializedName("orderColumnName")
    public String orderColumnName;
    @SerializedName("orderDir")
    public String orderDir;
    @SerializedName("folder_code")
    public String folder_code;
    @SerializedName("pk_folder")
    public String pk_folder;
    @SerializedName("folder_name")
    public String folder_name;
    @SerializedName("folder_desc")
    public String folder_desc;
    @SerializedName("pk_parent")
    public String pk_parent;
    @SerializedName("text")
    public String text;
    @SerializedName("pkvalue")
    public String pkvalue;
    @SerializedName("tableName")
    public String tableName;
    @SerializedName("parentPKField")
    public String parentPKField;
    @SerializedName("pkfield")
    public String pkfield;
}
