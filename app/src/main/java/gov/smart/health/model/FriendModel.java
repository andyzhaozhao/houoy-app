package gov.smart.health.model;

/**
 * Created by laoniu on 2017/07/23.
 */

public class FriendModel {
    private int imageid;
    private String title;
    private String content;

    public FriendModel(int imageid, String title, String content) {
        this.imageid = imageid;
        this.title = title;
        this.content = content;
    }

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
