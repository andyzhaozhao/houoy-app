package gov.smart.health.activity.vr;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;

import android.preference.DialogPreference;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.fitpolo.support.bluetooth.BluetoothModule;
import com.fitpolo.support.entity.DailyStep;
import com.fitpolo.support.entity.HeartRate;
import com.google.gson.Gson;
import com.utovr.player.UVEventListener;
import com.utovr.player.UVInfoListener;
import com.utovr.player.UVMediaPlayer;
import com.utovr.player.UVMediaType;
import com.utovr.player.UVPlayerCallBack;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import gov.smart.health.R;
import gov.smart.health.activity.vr.model.SportVideoListModel;
import gov.smart.health.activity.vr.model.SportVideoListModelEx;
import gov.smart.health.activity.vr.model.VRSaveRecordModel;
import gov.smart.health.utils.SHConstants;
import gov.smart.health.utils.SharedPreferencesHelper;

public class VTOVRPlayerActivity extends AppCompatActivity implements UVPlayerCallBack, VideoController.PlayerControl{

    private UVMediaPlayer mMediaplayer = null;  // 媒体视频播放器
    private VideoController mCtrl = null;
    private boolean bufferResume = true;
    private boolean needBufferAnim = true;
    private ImageView imgBuffer;                // 缓冲动画
    private ImageView imgBack;
    private RelativeLayout rlParent = null;
    private AlertDialog.Builder mAlertDialogBuilder;
    protected int CurOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
    private int SmallPlayH = 0;
    private boolean colseDualScreen = false;
    private boolean isSecond = false;
    private SportVideoListModel model = new SportVideoListModel();

    private List<String> dateKeylist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vto_vrplayer);

        if(getIntent() != null && getIntent().getSerializableExtra(SHConstants.Video_ModelKey)!= null){
            model = (SportVideoListModel) getIntent().getSerializableExtra(SHConstants.Video_ModelKey);
        }

        initView();


        TextView textVideoLength = (TextView)findViewById(R.id.tv_video_length);
        textVideoLength.setText(model.video_length+"秒");
        TextView textVideoname = (TextView)findViewById(R.id.tv_video_name);
        textVideoname.setText(model.video_name);
        TextView textheartRate = (TextView)findViewById(R.id.tv_actor_heart_rate);
        textheartRate.setText(model.actor_times +"分/秒");
        TextView textActorCal = (TextView)findViewById(R.id.tv_actor_cal);
        textActorCal.setText(model.actor_calorie+"cal");
        TextView textView = (TextView)findViewById(R.id.tv_detail);
        textView.setText(model.video_desc);

        //初始化播放器
        RelativeLayout rlPlayView = (RelativeLayout) findViewById(R.id.activity_rlPlayView);
        mMediaplayer = new UVMediaPlayer(this, rlPlayView);
        //将工具条的显示或隐藏交个SDK管理，也可自己管理
        RelativeLayout rlToolbar = (RelativeLayout) findViewById(R.id.activity_rlToolbar);
        mMediaplayer.setToolbar(rlToolbar, null, imgBack);
        mCtrl = new VideoController(rlToolbar, this, true,false);
        changeOrientation(false);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (mMediaplayer != null)
        {
            mMediaplayer.onResume(this);
        }
    }

    @Override
    public void onPause()
    {
        super.onPause();
        if (mMediaplayer != null)
        {
            mMediaplayer.onPause();
        }
    }

    @Override
    public void onDestroy()
    {
        releasePlayer();
        super.onDestroy();
    }
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            back();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        changeOrientation(newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE);
    }

    private void changeOrientation(boolean isLandscape)
    {
        if (rlParent == null)
        {
            return;
        }
        if (isLandscape)
        {
            CurOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                    | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT);
            rlParent.setLayoutParams(lp);
            if (colseDualScreen && mMediaplayer != null)
            {
                mCtrl.setDualScreenEnabled(true);
            }
            colseDualScreen = false;
            mCtrl.changeOrientation(true, 0);
        }
        else
        {
            CurOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            getSmallPlayHeight();
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, SmallPlayH);
            rlParent.setLayoutParams(lp);
            if (mMediaplayer != null && mMediaplayer.isDualScreenEnabled())
            {
                mCtrl.setDualScreenEnabled(false);
                colseDualScreen = true;
            }
            mCtrl.changeOrientation(false, 0);
        }
    }

    private void initView()
    {
        rlParent = (RelativeLayout) findViewById(R.id.activity_rlParent);
        imgBuffer = (ImageView) findViewById(R.id.activity_imgBuffer);
        imgBack = (ImageView) findViewById(R.id.activity_imgBack);
        imgBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                back();
            }
        });
    }

    public void releasePlayer()
    {
        if (mMediaplayer != null)
        {
            mMediaplayer.release();
            mMediaplayer = null;
        }
    }

    @Override
    public void createEnv()
    {
        try
        {
            // 创建媒体视频播放器
            mMediaplayer.initPlayer();
            mMediaplayer.setListener(mListener);
            mMediaplayer.setInfoListener(mInfoListener);
            //如果是网络MP4，可调用 mCtrl.startCachePro();mCtrl.stopCachePro();
            String path = "file:///android_asset/videos/wu.mp4";
            mMediaplayer.setSource(UVMediaType.UVMEDIA_TYPE_MP4, path);
            mMediaplayer.pause();
        }
        catch (Exception e)
        {
            Log.e("utovr", e.getMessage(), e);
        }
    }

    @Override
    public void updateProgress(long position)
    {
        if (mCtrl != null) {
            mCtrl.updateCurrentPosition();
        }
        getData();
    }

    private UVEventListener mListener = new UVEventListener()
    {
        @Override
        public void onStateChanged(int playbackState)
        {
            Log.i("utovr", "+++++++ playbackState:" + playbackState);
            switch (playbackState)
            {
                case UVMediaPlayer.STATE_PREPARING:
                    break;
                case UVMediaPlayer.STATE_BUFFERING:
                    if (needBufferAnim && mMediaplayer != null && mMediaplayer.isPlaying()) {
                        bufferResume = true;
                        VTOUtils.setBufferVisibility(imgBuffer, true);
                    }
                    break;
                case UVMediaPlayer.STATE_READY:
                    // 设置时间和进度条
                    mCtrl.setInfo();
                    if (bufferResume)
                    {
                        bufferResume = false;
                        VTOUtils.setBufferVisibility(imgBuffer, false);
                    }
                    break;
                case UVMediaPlayer.STATE_ENDED:
                    if(!isSecond) {
                        isSecond= true;
                        String downlaodFile =  model.downlaodPath + File.separator + model.video_name;
                        mMediaplayer.setSource(UVMediaType.UVMEDIA_TYPE_MP4, downlaodFile);
                        model.time_start = System.currentTimeMillis();
                    } else {
                        seekTo(0);
                        mMediaplayer.pause();
                        mCtrl.settbtnPlayPauseStatus(false);
                        if(mAlertDialogBuilder == null) {
                            mAlertDialogBuilder = new AlertDialog.Builder(VTOVRPlayerActivity.this);
                            mAlertDialogBuilder.setMessage("是否分享本次运动？");
                            mAlertDialogBuilder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    mAlertDialogBuilder = null;
                                }
                            });
                            mAlertDialogBuilder.setNeutralButton("好的",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            mAlertDialogBuilder = null;
                                            model.time_end = System.currentTimeMillis();
                                            Intent intent = new Intent();
                                            intent.putExtra(SHConstants.Video_ModelKey, model);
                                            intent.setClass(VTOVRPlayerActivity.this, SportShareActivity.class);
                                            startActivity(intent);
                                        }
                                    });
                            mAlertDialogBuilder.setCancelable(true);
                            AlertDialog alertDialog = mAlertDialogBuilder.create();
                            alertDialog.show();
                        }
                    }
                    break;
                case UVMediaPlayer.TRACK_DISABLED:
                case UVMediaPlayer.TRACK_DEFAULT:
                    break;
            }
        }

        @Override
        public void onError(Exception e, int ErrType)
        {
            Toast.makeText(VTOVRPlayerActivity.this, VTOUtils.getErrMsg(ErrType), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onVideoSizeChanged(int width, int height)
        {
        }

    };

    private UVInfoListener mInfoListener = new UVInfoListener()
    {
        @Override
        public void onBandwidthSample(int elapsedMs, long bytes, long bitrateEstimate)
        {
        }

        @Override
        public void onLoadStarted()
        {
        }

        @Override
        public void onLoadCompleted()
        {
            if (bufferResume)
            {
                bufferResume = false;
                VTOUtils.setBufferVisibility(imgBuffer, false);
            }
            if (mCtrl != null) {
                mCtrl.updateBufferProgress();
            }

        }
    };

    @Override
    public long getDuration()
    {
        return mMediaplayer != null ? mMediaplayer.getDuration() : 0;
    }

    @Override
    public long getBufferedPosition()
    {
        return mMediaplayer != null ? mMediaplayer.getBufferedPosition() : 0;
    }

    @Override
    public long getCurrentPosition()
    {
        return mMediaplayer != null ? mMediaplayer.getCurrentPosition() : 0;
    }

    @Override
    public void setGyroEnabled(boolean val)
    {
        if (mMediaplayer != null)
            mMediaplayer.setGyroEnabled(val);
    }

    @Override
    public boolean isGyroEnabled()
    {
        return mMediaplayer != null && mMediaplayer.isGyroEnabled();
    }

    @Override
    public boolean isDualScreenEnabled()
    {
        return mMediaplayer != null && mMediaplayer.isDualScreenEnabled();
    }

    @Override
    public void toolbarTouch(boolean start)
    {
        if (mMediaplayer != null)
        {
            if (true)
            {
                mMediaplayer.cancelHideToolbar();
            }
            else
            {
                mMediaplayer.hideToolbarLater();
            }
        }
    }

    @Override
    public void pause()
    {
        if (mMediaplayer != null && mMediaplayer.isPlaying())
        {
            mMediaplayer.pause();
        }
    }

    @Override
    public void seekTo(long positionMs)
    {
        if (mMediaplayer != null)
            mMediaplayer.seekTo(positionMs);
    }

    @Override
    public void play()
    {
        if (mMediaplayer != null && !mMediaplayer.isPlaying())
        {
            mMediaplayer.play();
        }
    }

    @Override
    public void setDualScreenEnabled(boolean val)
    {
        if (mMediaplayer != null)
            mMediaplayer.setDualScreenEnabled(val);
    }

    @Override
    public void toFullScreen()
    {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }
    /* 大小屏切 可以再加上 ActivityInfo.SCREEN_ORIENTATION_SENSOR 效果更佳**/

    private void back()
    {
        if (CurOrientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        {
            releasePlayer();
            finish();
        }
        else
        {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    private void getSmallPlayHeight()
    {
        if (this.SmallPlayH != 0) {
            return;
        }
        int ScreenW = getWindowManager().getDefaultDisplay().getWidth();
        int ScreenH = getWindowManager().getDefaultDisplay().getHeight();
        if (ScreenW > ScreenH)
        {
            int temp = ScreenW;
            ScreenW = ScreenH;
            ScreenH = temp;
        }
        SmallPlayH = ScreenW * ScreenW / ScreenH;
    }

    private void getData(){
        String dateKey = DateFormat.format("yyyy-MM-dd-HH-mm", Calendar.getInstance()).toString();
        Log.d("",dateKey);
        //TODO get data.
        BluetoothModule bluetoothModule = BluetoothModule.getInstance();
        if(!dateKeylist.contains(dateKey) && (bluetoothModule.isSupportHeartRate() || bluetoothModule.isSupportTodayData())) {
            HeartRate heartRate = new HeartRate();
            DailyStep dailyStep = new DailyStep();
            if (bluetoothModule.isSupportHeartRate()) {
                List<HeartRate> heartRates = bluetoothModule.getHeartRates();
                for (HeartRate rate:heartRates) {
                    String heartKey = rate.time;
                    if(dateKey.equals(heartKey)) {
                        heartRate = rate;
                        dateKeylist.add(dateKey);
                        break;
                    }
                }
                Log.d("", heartRates.toString());
            }
            if (bluetoothModule.isSupportTodayData()) {
                ArrayList<DailyStep> dailySteps = bluetoothModule.getDailySteps();
                for (DailyStep step :dailySteps) {
                    String heartKey = step.date;
                    if(dateKey.equals(heartKey)) {
                        dailyStep = step;
                        dateKeylist.add(dateKey);
                        break;
                    }
                }
                Log.d("", dailySteps.toString());
            }
            sendData(dateKey, heartRate,dailyStep);
        }
    }

    private void sendData(String dateKey, HeartRate heartRate,DailyStep dailyStep){

        String pk = SharedPreferencesHelper.gettingString(SHConstants.LoginUserPkPerson,"");
        String name = SharedPreferencesHelper.gettingString(SHConstants.LoginUserPersonName,"");

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(SHConstants.Record_VRSportDetailSave_calorie, dailyStep.calories);
            jsonObject.put(SHConstants.Record_VRSportDetailSave_heart, heartRate.value);
            jsonObject.put(SHConstants.Record_VRSportDetailSave_length, "0");

            jsonObject.put(SHConstants.Record_VRSportDetailSave_Pk_Person, pk);
            jsonObject.put(SHConstants.Record_VRSportDetailSave_Person_Name, name);

            jsonObject.put(SHConstants.Record_VRSportDetailSave_Pk_Place, model.pk_folder);
            jsonObject.put(SHConstants.Record_VRSportDetailSave_Place_Name, "Place");

            jsonObject.put(SHConstants.Record_VRSportDetailSave_Pk_Video, model.pk_video);
            jsonObject.put(SHConstants.Record_VRSportDetailSave_Video_Name, model.video_name);

            jsonObject.put(SHConstants.Record_VRSportDetailSave_Timestamp, dateKey);
            long millis = System.currentTimeMillis();
            jsonObject.put(SHConstants.Record_VRSportDetailSave_Record_SportDetailCode, millis);
            jsonObject.put(SHConstants.Record_VRSportDetailSave_Record_SportDetailname, millis);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        AndroidNetworking.post(SHConstants.RecordVRSportDetailsave)
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

