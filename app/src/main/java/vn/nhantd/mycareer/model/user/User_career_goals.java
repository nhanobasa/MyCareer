package vn.nhantd.mycareer.model.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

public class User_career_goals {
    private String address;
    private String category;
    private String level;
    private String position;
    private User_career_goals_salary salary;

    // Standard getters & setters
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public User_career_goals_salary getSalary() {
        return salary;
    }

    public void setSalary(User_career_goals_salary salary) {
        this.salary = salary;
    }
}
