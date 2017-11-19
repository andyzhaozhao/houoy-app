package gov.smart.health.activity.vr.downloadfile.listener;

import gov.smart.health.activity.vr.model.SportVideoListModel;
import gov.smart.health.activity.vr.model.SportVideoListModelEx;

/**
 *  文件下载监听
 */
public interface FileDownloadListener{

    /**
     *
     * @param model
     */
    public void onFileDownloadStart(SportVideoListModelEx model);

    /**
     *
     * @param model
     */
    public void onFileDownloading(SportVideoListModelEx model);

    /**
     *
     * @param model
     */
    public void onFileDownloadFail(SportVideoListModelEx model);
    /**
     *
     * @param model
     */
    public void onFileDownloadCompleted(SportVideoListModelEx model);
//    /**
//     *
//     * @param model
//     */
//    public void onFileDownloadPaused(SportVideoListModelEx model);
}
