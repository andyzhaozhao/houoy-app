package gov.smart.health.activity.vr.model;

import android.content.Context;
import android.widget.ProgressBar;

import com.fitpolo.support.entity.DailyStep;
import com.fitpolo.support.entity.HeartRate;
import com.google.gson.annotations.SerializedName;

import java.io.File;
import java.io.Serializable;

import gov.smart.health.utils.SHConstants;

/**
 * Created by laoniu on 11/8/17.
 */
public class SportVideoListModel implements Serializable{
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
    @SerializedName("pk_video")
    public String pk_video;
    @SerializedName("video_code")
    public String video_code;
    @SerializedName("video_name")
    public String video_name;
    @SerializedName("video_desc")
    public String video_desc;
    @SerializedName("actor_times")
    public String actor_times;
    @SerializedName("video_length")
    public String video_length;
    @SerializedName("actor_calorie")
    public String actor_calorie;
    @SerializedName("path_thumbnail")
    public String path_thumbnail;
    @SerializedName("path")
    public String path;
    @SerializedName("pk_folder")
    public String pk_folder;
    @SerializedName("pkvalue")
    public String pkvalue;
    @SerializedName("tableName")
    public String tableName;
    @SerializedName("pkfield")
    public String pkfield;

    public String downlaodPath;
    public String downlaodTempPath;

    public long time_start;
    public long time_length;
    public long time_end;

    public SHHeartRate oldHeartRate;
    public SHDailyStep oldDailyStep;

    public SHHeartRate newHeartRate;
    public SHDailyStep newDailyStep;

}
