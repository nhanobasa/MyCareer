package vn.nhantd.mycareer.model;

import com.google.gson.annotations.SerializedName;

public class Notification {

    @SerializedName("_id")
    private String _id;
    private Long dt;
    private String subject;
    private String content;
    private String user_id;
    private String token;

    public Notification(String subject, String content, String user_id, String token) {
        this.dt = System.currentTimeMillis();
        this.subject = subject;
        this.content = content;
        this.user_id = user_id;
        this.token = token;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
