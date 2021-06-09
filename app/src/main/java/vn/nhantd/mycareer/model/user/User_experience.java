package vn.nhantd.mycareer.model.user;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User_experience implements Serializable {
    private String current_level = "";
    private Integer total_years = 0;

    private List<String> work_progress = new ArrayList<>();

    // Standard getters & setters
    public String getCurrent_level() {
        return current_level;
    }

    public void setCurrent_level(String current_level) {
        this.current_level = current_level;
    }

    public Integer getTotal_years() {
        return total_years;
    }

    public void setTotal_years(Integer total_years) {
        this.total_years = total_years;
    }

    public List<String> getWork_progress() {
        return work_progress;
    }

    public void setWork_progress(List<String> work_progress) {
        this.work_progress = work_progress;
    }
}
