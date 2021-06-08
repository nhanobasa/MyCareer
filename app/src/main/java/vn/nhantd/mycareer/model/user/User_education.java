package vn.nhantd.mycareer.model.user;

import java.util.List;

public class User_education {
    private String degree;
    private List<String> foreign_language;

    // Standard getters & setters
    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public List<String> getForeign_language() {
        return foreign_language;
    }

    public void setForeign_language(List<String> foreign_language) {
        this.foreign_language = foreign_language;
    }
}
