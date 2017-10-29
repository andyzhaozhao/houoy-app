package gov.smart.health.model;

/**
 * Created by laoniu on 2017/07/23.
 */

public class SportModel {
    private int imageid;
    private String title;
    private String time;

    public SportModel(int imageid, String title, String time, Boolean isDownload) {
        this.imageid = imageid;
        this.title = title;
        this.time = time;
        this.isDownload = isDownload;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Boolean getDownload() {
        return isDownload;
    }

    public void setDownload(Boolean download) {
        isDownload = download;
    }

    private Boolean isDownload;


    public int getImageid() {
        return imageid;
    }

    public void setImageid(int imageid) {
        this.imageid = imageid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
