package vn.nhantd.mycareer.model;

import java.util.List;

public class Transaction {
    private Long dt;
    private String transaction_code; // view - like- unlike
    private String user_id;
    private List<String> cv_path;

    public Long getDt() {
        return dt;
    }

    public void setDt(Long dt) {
        this.dt = dt;
    }

    public String getTransaction_code() {
        return transaction_code;
    }

    public void setTransaction_code(String transaction_code) {
        this.transaction_code = transaction_code;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public List<String> getCv_path() {
        return cv_path;
    }

    public void setCv_path(List<String> cv_path) {
        this.cv_path = cv_path;
    }
}
