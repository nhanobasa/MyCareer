package vn.nhantd.mycareer.model.user;

import java.io.Serializable;

public class User_references implements Serializable {
    private String email = "";
    private String name = "";
    private String phone = "";
    // Standard getters & setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    @Override
    public String toString() {
        return "Người tham khảo: " + name + " - " + phone + " - " + email;
    }
}
