package gov.smart.health.activity.vr.downloadfile.listener;

import gov.smart.health.activity.vr.downloadfile.database.model.DownloadFileInfo;

/**
 *  文件下载监听
 */
public interface FileDownloadListener{

    /**
     *
     * @param downloadFileInfo
     */
    public void onFileDownloading(DownloadFileInfo downloadFileInfo);

    /**
     *
     * @param downloadFileInfo
     */
    public void onFileDownloadFail(DownloadFileInfo downloadFileInfo);
    /**
     *
     * @param downloadFileInfo
     */
    public void onFileDownloadCompleted(DownloadFileInfo downloadFileInfo);
    /**
     *
     * @param downloadFileInfo
     */
    public void onFileDownloadPaused(DownloadFileInfo downloadFileInfo);
}