package gov.smart.health.activity.find.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by laoniu on 11/5/17.
 */

public class FindEssayListDataModel implements Serializable {
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
    @SerializedName("pk_essay")
    public String pk_essay;
    @SerializedName("essay_code")
    public String essay_code;
    @SerializedName("essay_name")
    public String essay_name;
    @SerializedName("essay_subname")
    public String essay_subname;
    @SerializedName("essay_content")
    public String essay_content;
    @SerializedName("is_publish")
    public String is_publish;
    @SerializedName("ts_start")
    public String ts_start;
    @SerializedName("ts_end")
    public String ts_end;
    @SerializedName("pk_person")
    public String pk_person;
    @SerializedName("person_name")
    public String person_name;
    @SerializedName("path_thumbnail")
    public String path_thumbnail;
    @SerializedName("pkvalue")
    public String pkvalue;
    @SerializedName("tableName")
    public String tableName;
    @SerializedName("pkfield")
    public String pkfield;
}
