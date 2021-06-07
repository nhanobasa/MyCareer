package vn.nhantd.mycareer.model.user;

import io.realm.RealmObject;
import io.realm.RealmSet;
import io.realm.annotations.RealmClass;
import io.realm.annotations.Required;

@RealmClass(embedded = true)
public class User_education extends RealmObject {

    private String degree;
    @Required
    private RealmSet<String> foreign_language;

    // Standard getters & setters
    public String getDegree() { return degree; }
    public void setDegree(String degree) { this.degree = degree; }
    public RealmSet<String> getForeign_language() { return foreign_language; }
    public void setForeign_language(RealmSet<String> foreign_language) { this.foreign_language = foreign_language; }
}
