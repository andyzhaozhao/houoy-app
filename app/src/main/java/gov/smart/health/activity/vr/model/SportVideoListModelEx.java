package gov.smart.health.activity.vr.model;

import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.Serializable;

import gov.smart.health.activity.vr.downloadfile.DownloadManager;

/**
 * Created by laoniu on 11/15/17.
 */
public class SportVideoListModelEx implements Serializable {
    public SportVideoListModel videoModel;
    public ProgressBar progressBar = null;
    public TextView downloadStatus = null;
    public Boolean isDownloading = false;
    public int progress = 0;
    public DownloadManager.AsyncAppTask downlaodTask;
}
