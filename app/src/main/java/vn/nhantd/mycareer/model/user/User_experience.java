package vn.nhantd.mycareer.model.user;

import io.realm.RealmObject;
import io.realm.RealmSet;
import io.realm.annotations.RealmClass;
import io.realm.annotations.Required;

import org.bson.types.ObjectId;

@RealmClass(embedded = true)
public class User_experience extends RealmObject {

    private String current_level;
    private Integer total_years;
    @Required
    private RealmSet<ObjectId> work_progress;

    // Standard getters & setters
    public String getCurrent_level() { return current_level; }
    public void setCurrent_level(String current_level) { this.current_level = current_level; }
    public Integer getTotal_years() { return total_years; }
    public void setTotal_years(Integer total_years) { this.total_years = total_years; }
    public RealmSet<ObjectId> getWork_progress() { return work_progress; }
    public void setWork_progress(RealmSet<ObjectId> work_progress) { this.work_progress = work_progress; }
}

