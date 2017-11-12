package gov.smart.health.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.DownloadListener;
import com.androidnetworking.interfaces.DownloadProgressListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import gov.smart.health.R;
import gov.smart.health.activity.vr.VTOVRPlayerActivity;
import gov.smart.health.activity.vr.model.SportVideoListModel;
import gov.smart.health.utils.SHConstants;

import static java.security.AccessController.getContext;

/**
 * Created by laoniu on 2017/07/23.
 */

public class SportRefreshRecyclerAdapter extends RecyclerView.Adapter<SportRefreshRecyclerAdapter.ViewHolder>{
    private LayoutInflater mInflater;
    private List<SportVideoListModel> mLists;
    private Context mContext;

    public SportRefreshRecyclerAdapter(Context context , List<SportVideoListModel> lists){
        mContext = context;
        this.mInflater=LayoutInflater.from(context);
        this.mLists = lists;
    }

    public void addDataLists(List<SportVideoListModel> lists) {
        if (this.mLists == null){
            this.mLists = lists;
        } else {
            this.mLists.addAll(lists);
        }
        notifyDataSetChanged();
    }

    public void addNewDataLists(List<SportVideoListModel> lists) {
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
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final SportVideoListModel model = mLists.get(position);
        holder.image.setImageResource(R.mipmap.healthicon);
        holder.title.setText(model.video_name);
        holder.time.setText(model.video_desc);
        holder.downloadprogressBar.setVisibility(View.INVISIBLE);
        String downlaodFilePathStr = mContext.getCacheDir().getAbsolutePath() + SHConstants.Download_File_Divide+ SHConstants.Download_Download;
        File downlaodFile = new File( downlaodFilePathStr + model.video_name);
        final boolean isDownlaod = downlaodFile.exists();
        if(isDownlaod){
            holder.download.setText("已下载");
        } else {
            holder.download.setText("未下载");
        }
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isDownlaod) {
                    Intent intent = new Intent();
                    intent.putExtra(SHConstants.Video_ModelKey, model);
                    intent.setClass(mContext, VTOVRPlayerActivity.class);
                    mContext.startActivity(intent);
                }else {
                    holder.downloadprogressBar.setMax(100);
                    holder.downloadprogressBar.setVisibility(View.VISIBLE);
                    downloadVideo(model,holder.downloadprogressBar);
                }
            }
        });
    }

    private void downloadVideo(final SportVideoListModel model,final ProgressBar downloadprogressBar){
        String url = SHConstants.Download_Base_Link + model.path + SHConstants.Download_File_Divide + model.video_name;
        String path = mContext.getCacheDir().getAbsolutePath() + SHConstants.Download_File_Divide + SHConstants.Download_Temp;
        File temppath =new File(path);
        temppath.mkdirs();

        AndroidNetworking.download(url,path , model.video_name)
                .setPriority(Priority.MEDIUM)
                .build()
                .setDownloadProgressListener(new DownloadProgressListener() {
                    @Override
                    public void onProgress(long bytesDownloaded, long totalBytes) {
                        // do anything with progress
                        Log.d("onProgress","bytesDownloaded " +bytesDownloaded + " progress "+ (float)bytesDownloaded / totalBytes);
                        downloadprogressBar.setProgress((int)((bytesDownloaded * 100) / totalBytes));
                    }
                })
                .startDownload(new DownloadListener() {
                    @Override
                    public void onDownloadComplete() {
                        downloadprogressBar.setVisibility(View.INVISIBLE);
                        String tempPath = mContext.getCacheDir().getAbsolutePath() + SHConstants.Download_File_Divide+ SHConstants.Download_Temp + model.video_name;
                        String downlaodFilePathStr = mContext.getCacheDir().getAbsolutePath() + SHConstants.Download_File_Divide+ SHConstants.Download_Download;
                        try {
                            File downlaodFilePath = new File(downlaodFilePathStr);
                            downlaodFilePath.mkdirs();

                            File downlaodFile = new File( downlaodFilePathStr + model.video_name);
                            if(downlaodFile.exists()){
                                downlaodFile.delete();
                            }
                            downlaodFile.createNewFile();

                            copy(new File(tempPath),downlaodFile);
                            Toast.makeText(mContext, "下载成功", Toast.LENGTH_LONG).show();
                        } catch (Exception e){
                            e.printStackTrace();
                            Toast.makeText(mContext, "下载失败", Toast.LENGTH_LONG).show();
                        }
                        SportRefreshRecyclerAdapter.this.notifyDataSetChanged();
                        // do anything after completion
                    }
                    @Override
                    public void onError(ANError error) {
                        downloadprogressBar.setVisibility(View.INVISIBLE);
                        SportRefreshRecyclerAdapter.this.notifyDataSetChanged();
                        // handle error
                        Toast.makeText(mContext, "下载失败", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        try {
            OutputStream out = new FileOutputStream(dst);
            try {
                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            } finally {
                out.close();
            }
        } finally {
            in.close();
        }
    }

    @Override
    public int getItemCount() {
        return this.mLists == null? 0 : this.mLists.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView title;
        public TextView time;
        public TextView download;
        public ProgressBar downloadprogressBar;

        public ViewHolder(View view){
            super(view);
            image = (ImageView)view.findViewById(R.id.sport_item_img);
            title = (TextView)view.findViewById(R.id.sport_item_title);
            time = (TextView)view.findViewById(R.id.sport_item_time);
            download = (TextView)view.findViewById(R.id.sport_item_download);
            downloadprogressBar = (ProgressBar)view.findViewById(R.id.downloadprogressBar);
        }
    }
}