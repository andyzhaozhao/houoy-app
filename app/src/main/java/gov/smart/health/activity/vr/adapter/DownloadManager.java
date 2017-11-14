package gov.smart.health.activity.vr.adapter;

import android.content.Context;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by laoniu on 11/14/17.
 */
public class DownloadManager {

    private static DownloadManager downloadManager;

    public DownloadManager shareDownloadManager(Context context){
        if(downloadManager == null){
            downloadManager = new DownloadManager(context);
        }
        return downloadManager;
    }

    private Context context;

    private DownloadManager(Context context){
        this.context = context;
    }

//    private void downlaodData (String downloadUrl){
//        URL url = null;
//        HttpURLConnection httpURLConnection = null;
//        BufferedInputStream bufferedReader;
//        FileOutputStream mOutputStream;
//        ByteArrayOutputStream mByteOutput;
//        try {
//            url = new URL(downloadUrl);
//            httpURLConnection = (HttpURLConnection)url.openConnection();
//            httpURLConnection.setUseCaches(false);  // 请求时不使用缓存
//            httpURLConnection.setConnectTimeout(5 * 1000); // 设置连接超时时间
//            httpURLConnection.setRequestMethod("GET");
//            httpURLConnection.setRequestProperty("Accept-Language", "zh-CN");
//            httpURLConnection.setRequestProperty("Charset", "UTF-8");
//            httpURLConnection.connect();
//            int bufferSize = 1024;
//            bufferedReader = new BufferedInputStream(httpURLConnection.getInputStream(),bufferSize); //为InputStream类增加缓冲区功能
//            int len = 0; //读取到的数据长度
//            byte[] buffer = new byte[bufferSize];
//            //写入中间文件
//            mOutputStream = new FileOutputStream(mTempFile,true);//true表示向打开的文件末尾追加数据
//            mByteOutput = new ByteArrayOutputStream();
//            //开始读取
//            while((len = bufferedReader.read(buffer)) != -1) {
//                mByteOutput.write(buffer,0,len);
//            }
//            mByteOutput.close();
//            bufferedReader.close();
//        }catch (Exception e){
//            e.printStackTrace();
//        }finally {
//            if(httpURLConnection!=null){
//                httpURLConnection.disconnect();
//                httpURLConnection = null;
//            }
//        }
//    }
}
