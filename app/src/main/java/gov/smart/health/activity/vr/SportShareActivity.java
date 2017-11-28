package gov.smart.health.activity.vr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;

import gov.smart.health.R;
import gov.smart.health.activity.login.model.ResetPasswordModel;
import gov.smart.health.activity.vr.model.SportVideoListModel;
import gov.smart.health.activity.vr.model.VRSaveRecordModel;
import gov.smart.health.utils.SHConstants;
import gov.smart.health.utils.SharedPreferencesHelper;

public class SportShareActivity extends AppCompatActivity implements View.OnClickListener{

    private SportVideoListModel videoModel = new SportVideoListModel();
    private String heartRate;
    private String allCal;
    private TextView textCompletion, videoCompletionComment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        View btnShare = findViewById(R.id.btn_share);
        if(getIntent() != null && getIntent().getSerializableExtra(SHConstants.Video_ModelKey)!= null){
            videoModel = (SportVideoListModel) getIntent().getSerializableExtra(SHConstants.Video_ModelKey);
        }
        btnShare.setOnClickListener(this);

        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        findViewById(R.id.btn_cancel_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        heartRate = videoModel.actor_times;
        allCal =  videoModel.actor_calorie;
        textCompletion = (TextView) findViewById(R.id.tv_video_completion);
        videoCompletionComment = (TextView) findViewById(R.id.tv_video_completion_comment);

        int percent = 0;
        try {
            if(videoModel.newHeartRate != null){
                heartRate = videoModel.newHeartRate.time;
            }
            if(videoModel.oldDailyStep != null && videoModel.newDailyStep != null ){
                 int allCalInt = (Integer.valueOf(videoModel.oldDailyStep.calories) - Integer.valueOf(videoModel.newDailyStep.calories));
                allCal = Math.abs(allCalInt) + "";
            }
            percent = (Integer.valueOf(heartRate) / Integer.valueOf(videoModel.actor_times)) * 50
                    + (Integer.valueOf(allCal) / Integer.valueOf(videoModel.actor_calorie)) * 50;

        } catch (Exception e){
            e.printStackTrace();
            Random random=new Random();
            percent = random.nextInt(100);
        }
        if(percent<=30) {
            videoCompletionComment.setText("建议您重新运动");
        } else if(percent > 30 && percent<=70){
            videoCompletionComment.setText("再接再厉");
        } else {
            videoCompletionComment.setText("不错哦");
        }
        textCompletion.setText("完成度：" + percent + "%");

        long time_length = videoModel.time_end - videoModel.time_start;
        TextView textVideoLength = (TextView)findViewById(R.id.tv_video_length);
        textVideoLength.setText(time_length/1000+"秒");
        TextView textVideoname = (TextView)findViewById(R.id.tv_video_name);
        textVideoname.setText(videoModel.video_name);

        TextView textheartRate = (TextView)findViewById(R.id.tv_actor_heart_rate);
        textheartRate.setText(heartRate + "次/秒");

        TextView textActorCal = (TextView)findViewById(R.id.tv_actor_cal);
        textActorCal.setText(allCal+"cal");

        sendData();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_share:
                Intent share = new Intent(Intent.ACTION_SEND);
                share.putExtra(Intent.EXTRA_TEXT,"我参加了"+videoModel.video_name+"运动！"+textCompletion.getText()+"，你也来运动吧！");
                share.setType("text/plain");
                startActivity(share);
                break;
        }
    }

    private void sendData(){

        String pk = SharedPreferencesHelper.gettingString(SHConstants.LoginUserPkPerson,"");
        String name = SharedPreferencesHelper.gettingString(SHConstants.LoginUserPersonName,"");

        String dateStart = DateFormat.format("yyyy-MM-dd HH:mm:ss", videoModel.time_start).toString();
        String dateEnd = DateFormat.format("yyyy-MM-dd HH:mm:ss", videoModel.time_end).toString();
        long time_length = videoModel.time_end - videoModel.time_start;

        String heartRate = videoModel.actor_times;
        if(videoModel.newHeartRate != null){
            heartRate = videoModel.newHeartRate.time;
        }
        String allCal =  videoModel.actor_calorie;
        if(videoModel.oldDailyStep != null && videoModel.newDailyStep != null ){
            allCal = (Integer.valueOf(videoModel.oldDailyStep.calories) - Integer.valueOf(videoModel.newDailyStep.calories))+"";
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(SHConstants.Record_VRSport_Save_Pk_Person, pk);
            jsonObject.put(SHConstants.Record_VRSport_Save_Person_Name, name);

            jsonObject.put(SHConstants.Record_VRSport_Save_calorie, allCal);
            jsonObject.put(SHConstants.Record_VRSport_Save_heart_rate, heartRate);
            jsonObject.put(SHConstants.Record_VRSport_Save_heart_rate_max, heartRate);

            jsonObject.put(SHConstants.Record_VRSport_Save_Pk_Place, videoModel.pk_folder);
            jsonObject.put(SHConstants.Record_VRSport_Save_Place_Name, videoModel.pk_folder);

            jsonObject.put(SHConstants.Record_VRSport_Save_Pk_Video, videoModel.pk_video);
            jsonObject.put(SHConstants.Record_VRSport_Save_Video_Name, videoModel.video_name);

            jsonObject.put(SHConstants.Record_VRSport_Save_Record_Sport_Code, System.currentTimeMillis());
            jsonObject.put(SHConstants.Record_VRSport_Save_Record_Sport_name, System.currentTimeMillis());

            jsonObject.put(SHConstants.Record_VRSport_Save_Time_Start, dateStart);
            jsonObject.put(SHConstants.Record_VRSport_Save_Time_Length, time_length/1000);
            jsonObject.put(SHConstants.Record_VRSport_Save_Time_End, dateEnd);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(SHConstants.RecordVRSportSave)
                .addJSONObjectBody(jsonObject)
                .addHeaders(SHConstants.HeaderContentType, SHConstants.HeaderContentTypeValue)
                .addHeaders(SHConstants.HeaderAccept, SHConstants.HeaderContentTypeValue)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        VRSaveRecordModel model = gson.fromJson(response,VRSaveRecordModel.class);
                        if (model.success){
                            Toast.makeText(getApplication(),"保存成功",Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplication(),"保存失败",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("","response error"+anError.getErrorDetail());
                        Toast.makeText(getApplication(),"保存失败",Toast.LENGTH_LONG).show();
                    }
                });
    }
}
