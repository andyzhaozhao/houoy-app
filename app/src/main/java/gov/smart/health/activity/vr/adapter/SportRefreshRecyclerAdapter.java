package gov.smart.health.activity.vr.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.widget.ANImageView;

import java.io.File;
import java.util.List;

import gov.smart.health.R;
import gov.smart.health.activity.vr.VTOVRPlayerActivity;
import gov.smart.health.activity.vr.downloadfile.DownloadManager;
import gov.smart.health.activity.vr.downloadfile.Utils.DownloadUtils;
import gov.smart.health.activity.vr.downloadfile.listener.FileDownloadListener;
import gov.smart.health.activity.vr.model.SportVideoListModel;
import gov.smart.health.activity.vr.model.SportVideoListModelEx;
import gov.smart.health.utils.SHConstants;
import gov.smart.health.utils.SharedPreferencesHelper;

/**
 * Created by laoniu on 2017/07/23.
 */

public class SportRefreshRecyclerAdapter extends RecyclerView.Adapter<SportRefreshRecyclerAdapter.ViewHolder>{
    private LayoutInflater mInflater;
    private List<SportVideoListModelEx> mLists;
    private Activity mActivity;

    public SportRefreshRecyclerAdapter(Activity activity , List<SportVideoListModelEx> lists){
        mActivity = activity;
        this.mInflater=LayoutInflater.from(activity);
        this.mLists = lists;
    }

    public void addDataLists(List<SportVideoListModelEx> lists) {
        if (this.mLists == null){
            this.mLists = lists;
        } else {
            this.mLists.addAll(lists);
        }
        notifyDataSetChanged();
    }

    public void addNewDataLists(List<SportVideoListModelEx> lists) {
        if (this.mLists == null){
            this.mLists = lists;
        } else {
            this.mLists.addAll(0,lists);
        }
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = mInflater.inflate(R.layout.sport_list_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    /**
     * 数据的绑定显示
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(mLists.size() <position){
            return;
        }
        final SportVideoListModelEx model = mLists.get(position);
        model.videoModel.downlaodTempPath = mActivity.getCacheDir().getAbsolutePath() + File.separator+ SHConstants.Download_Temp+ File.separator + model.videoModel.video_code;
        model.videoModel.downlaodPath = mActivity.getCacheDir().getAbsolutePath() + File.separator+ SHConstants.Download_Download+ File.separator + model.videoModel.video_code;
        String fileName = model.videoModel.video_name;

        model.progressBar = holder.downloadprogressBar;
        model.tvProgress = holder.tvProgress;
        model.downloadStatus = holder.downloadStatus;
        holder.image.setDefaultImageResId(R.mipmap.healthicon);
        holder.image.setErrorImageResId(R.mipmap.healthicon);
        holder.image.setImageUrl(model.videoModel.path_thumbnail);

        holder.title.setText(fileName);
        holder.time.setText(model.videoModel.time_length+"秒");
        holder.cal.setText(model.videoModel.actor_calorie+"cal");
        holder.actorTime.setText(model.videoModel.actor_times+"次/分");
        holder.downloadprogressBar.setMax(10000);
        holder.downloadprogressBar.setVisibility(View.GONE);
        holder.tvProgress.setVisibility(View.GONE);
        DownloadManager downloadManager = DownloadManager.shareDownloadManager();
        SportVideoListModelEx oldModel = downloadManager.downloadMap.get(model.videoModel.video_code);
        if(oldModel != null){
            downloadManager.downloadMap.remove(oldModel.videoModel.video_code);
            model.isDownloading = oldModel.isDownloading;
            model.downlaodTask = oldModel.downlaodTask;
            model.downlaodTask.setFileDownloadListener(fileDownloadListener,model);
        }

        File downloadFile = new File(model.videoModel.downlaodPath + File.separator + fileName);
        final boolean isDownloaded = downloadFile.exists();
        if(model.isDownloading){
            holder.downloadStatus.setText("准备下载");
            holder.downloadprogressBar.setVisibility(View.VISIBLE);
            holder.tvProgress.setVisibility(View.VISIBLE);
            holder.tvProgress.setText("0%");
        } else if(isDownloaded){
            holder.downloadStatus.setText("已下载");
        } else {
            File downlaodTempFile = new File(model.videoModel.downlaodTempPath + File.separator + fileName);
            if(downlaodTempFile.exists() && downlaodTempFile.length() > 0){
                holder.downloadStatus.setText("继续下载");
                holder.downloadprogressBar.setVisibility(View.VISIBLE);
                holder.tvProgress.setVisibility(View.VISIBLE);
                long fileLength = SharedPreferencesHelper.gettingLong(SHConstants.VideoLength + model.videoModel.video_code,1);
                int progress = DownloadUtils.getProgress(downlaodTempFile.length(),fileLength);
                holder.downloadprogressBar.setProgress(progress);
                holder.tvProgress.setText((progress/100)+"%");
            } else {
                holder.downloadStatus.setText("未下载");
            }
        }
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isDownloaded) {
                    model.progressBar.setVisibility(View.GONE);
                    model.tvProgress.setVisibility(View.GONE);
                    Intent intent = new Intent();
                    intent.putExtra(SHConstants.Video_ModelKey, model.videoModel);
                    intent.setClass(mActivity, VTOVRPlayerActivity.class);
                    mActivity.startActivity(intent);
                }else {
                    if(!model.isDownloading) {
                        model.isDownloading = true;
                        model.progressBar.setVisibility(View.VISIBLE);
                        model.tvProgress.setVisibility(View.VISIBLE);
                        model.downloadStatus.setText("准备下载");
                        downloadVideo(model);
                    } else {
                        Toast.makeText(mActivity, model.videoModel.video_name + "下载中！", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    private void downloadVideo(SportVideoListModelEx model){
        DownloadManager downloadManager = DownloadManager.shareDownloadManager();
        downloadManager.downloadData(model,fileDownloadListener);
    }

    private FileDownloadListener fileDownloadListener = new FileDownloadListener() {
        @Override
        public void onFileDownloadStart(final SportVideoListModelEx model) {
            Log.d("onProgress","onFileDownloading " +model.progress);
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    model.downloadStatus.setText("下载中");
                    model.progressBar.setProgress(model.progress);
                    model.tvProgress.setText((model.progress/100)+"%");
                }
            });
        }

        @Override
        public void onFileDownloading(final SportVideoListModelEx model) {
            Log.d("onProgress","onFileDownloading " +model.progress);
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    model.downloadStatus.setText("下载中");
                    model.progressBar.setProgress(model.progress);
                    model.tvProgress.setText((model.progress/100)+"%");
                }
            });
        }

        @Override
        public void onFileDownloadFail(final SportVideoListModelEx model) {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    model.isDownloading = false;
                    SportRefreshRecyclerAdapter.this.notifyDataSetChanged();
                    Toast.makeText(mActivity, model.videoModel.video_name + "下载失败", Toast.LENGTH_LONG).show();
                }
            });
            Log.d("onProgress","onFileDownloadFail " +model.progress);
        }

        @Override
        public void onFileDownloadCompleted(final SportVideoListModelEx model) {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    model.isDownloading = false;
                    Toast.makeText(mActivity, model.videoModel.video_name + "下载成功", Toast.LENGTH_LONG).show();
                    SportRefreshRecyclerAdapter.this.notifyDataSetChanged();
                }
            });
            Log.d("onProgress","onFileDownloadCompleted " +model.progress);
        }
    };

    @Override
    public int getItemCount() {
        return this.mLists == null? 0 : this.mLists.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    class ViewHolder extends RecyclerView.ViewHolder {
        public ANImageView image;
        public TextView title;
        public TextView time;
        public TextView tvProgress;
        public TextView cal;
        public TextView actorTime;
        public TextView downloadStatus;
        public ProgressBar downloadprogressBar;

        public ViewHolder(View view){
            super(view);
            image = (ANImageView)view.findViewById(R.id.sport_item_img);
            title = (TextView)view.findViewById(R.id.sport_item_title);
            time = (TextView)view.findViewById(R.id.sport_item_time);
            tvProgress = (TextView)view.findViewById(R.id.tv_progress);
            cal = (TextView)view.findViewById(R.id.sport_item_cal);
            actorTime = (TextView)view.findViewById(R.id.sport_item_actor_time);
            downloadStatus = (TextView)view.findViewById(R.id.sport_item_download);
            downloadprogressBar = (ProgressBar)view.findViewById(R.id.downloadprogressBar);
        }
    }
}