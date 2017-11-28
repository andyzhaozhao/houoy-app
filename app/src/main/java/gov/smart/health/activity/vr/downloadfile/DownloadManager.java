package gov.smart.health.activity.vr.downloadfile;

import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import gov.smart.health.activity.vr.downloadfile.Utils.DownloadUtils;
import gov.smart.health.activity.vr.downloadfile.Utils.FileUtils;
import gov.smart.health.activity.vr.downloadfile.listener.FileDownloadListener;
import gov.smart.health.activity.vr.model.SportVideoListModel;
import gov.smart.health.activity.vr.model.SportVideoListModelEx;
import gov.smart.health.utils.SHConstants;
import gov.smart.health.utils.SharedPreferencesHelper;

/**
 * Created by laoniu on 11/14/17.
 */
public class DownloadManager {

    private static DownloadManager downloadManager;

    public HashMap<String,SportVideoListModelEx> downloadMap = new HashMap<>();

    public static DownloadManager shareDownloadManager(){
        if(downloadManager == null){
            downloadManager = new DownloadManager();
        }
        return downloadManager;
    }

    private static int BufferSize = 1024;

    private DownloadManager(){
    }

    public void downloadData(SportVideoListModelEx model,FileDownloadListener fileDownloadListener) {
        model.downlaodTask =  new AsyncAppTask(fileDownloadListener,model);
        model.downlaodTask.execute();
    }

    public class AsyncAppTask extends AsyncTask<String, Void, String> {

        private FileDownloadListener mFileDownloadListener;
        private SportVideoListModelEx model;

        public void setFileDownloadListener(FileDownloadListener fileDownloadListener, SportVideoListModelEx model) {
            this.mFileDownloadListener = fileDownloadListener;
            this.model = model;
        }

        public AsyncAppTask(FileDownloadListener fileDownloadListener, SportVideoListModelEx model) {
            this.mFileDownloadListener = fileDownloadListener;
            this.model = model;
        }

        @Override
        protected String doInBackground(String... arg0) {
            downloadModel();
            return "";
        }

        private void downloadModel() {
            File tmpFile = null;
            try {
                //init parameter
                String downlaodFileTempPathStr = model.videoModel.downlaodTempPath;
                String fileName = model.videoModel.video_name;

                //get connection
                String downloadUrl = SHConstants.Download_Base_Link + model.videoModel.path + File.separator + fileName;
                URL url = new URL(downloadUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setUseCaches(false);  // 请求时不使用缓存
                httpURLConnection.setConnectTimeout(5 * 1000); // 设置连接超时时间
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setRequestProperty("Accept-Language", "zh-CN");
                httpURLConnection.setRequestProperty("Charset", "UTF-8");

                //read old size.
                tmpFile = new File(downlaodFileTempPathStr + File.separator + fileName);
                long downloadSize = 0;
                if (tmpFile.exists()) {
                    downloadSize = tmpFile.length();
                    if (downloadSize > 0) {
                        httpURLConnection.setRequestProperty("Range", "bytes=" + downloadSize + "-");
                    }
                } else {
                    tmpFile = FileUtils.createTempFile(downlaodFileTempPathStr, fileName);
                }
                long fileLength = httpURLConnection.getContentLength() + downloadSize; // 获取文件的大小
                if(isCancelled()){
                    return;
                }
                SharedPreferencesHelper.settingLong(SHConstants.VideoLength + model.videoModel.video_code, fileLength);
                if (mFileDownloadListener != null) {
                    model.progress = DownloadUtils.getProgress(downloadSize, fileLength);
                    mFileDownloadListener.onFileDownloadStart(model);
                }

                //connect
                httpURLConnection.connect();
                if(isCancelled()){
                    return;
                }
                if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK || httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_PARTIAL) {
                    long currentTime = System.currentTimeMillis();
                    int len; //读取到的数据长度
                    byte[] buffer = new byte[BufferSize];

                    //写入中间文件
                    BufferedInputStream bufferedInputStream = new BufferedInputStream(httpURLConnection.getInputStream(), BufferSize);
                    OutputStream mOutputStream = new FileOutputStream(tmpFile, true);//true表示向打开的文件末尾追加数据
                    ByteArrayOutputStream mByteOutput = new ByteArrayOutputStream();
                    // 开始读取
                    while ((len = bufferedInputStream.read(buffer)) != -1) {
                        mByteOutput.write(buffer, 0, len);
                        if (mByteOutput.size() > 0) {
                            downloadSize += mByteOutput.size();
                            model.progress = DownloadUtils.getProgress(downloadSize, fileLength);
                            mByteOutput.writeTo(mOutputStream);
                            mByteOutput.reset();
                        }
                        if (downloadSize >= fileLength) {  // 下载完成后重命名
                            File newDownloadFile = FileUtils.createTempFile(model.videoModel.downlaodPath, fileName);
                            copy(tmpFile, newDownloadFile);
                            tmpFile.delete();
                            model.downlaodTask = null;
                            if (downloadMap.containsKey(model.videoModel.video_code)) {
                                downloadMap.remove(model.videoModel.video_code);
                            }
                            if(isCancelled()){
                                return;
                            }
                            if (mFileDownloadListener != null) {
                                mFileDownloadListener.onFileDownloadCompleted(model);
                            }
                        } else { //show progress
                            if(isCancelled()){
                                return;
                            }
                            if (mFileDownloadListener != null) {
                                long nowTime = System.currentTimeMillis();
                                if (currentTime < nowTime - 500) {
                                    currentTime = nowTime;
                                    mFileDownloadListener.onFileDownloading(model);
                                }
                            }
                        }
                    }
                } else {
                    model.downlaodTask = null;
                    if (downloadMap.containsKey(model.videoModel.video_code)) {
                        downloadMap.remove(model.videoModel.video_code);
                    }
                    if (mFileDownloadListener != null) {
                        mFileDownloadListener.onFileDownloadFail(model);
                    }
                }
            } catch (InterruptedIOException e){
                e.printStackTrace();
                model.downlaodTask = null;
                if (downloadMap.containsKey(model.videoModel.video_code)) {
                    downloadMap.remove(model.videoModel.video_code);
                }
            } catch (IOException e) {
                e.printStackTrace();
                model.downlaodTask = null;
                if (downloadMap.containsKey(model.videoModel.video_code)) {
                    downloadMap.remove(model.videoModel.video_code);
                }
                if (mFileDownloadListener != null) {
                    mFileDownloadListener.onFileDownloadFail(model);
                }
            }
        }
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
}
