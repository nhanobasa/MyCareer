package vn.nhantd.mycareer.model.user;

import org.bson.types.ObjectId;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class User implements Serializable {

    private ObjectId _id;
    private String _partition;
    private String address;
    private User_career_goals career_goals;
    private String dob;
    private User_education education;
    private String email;
    private User_experience experience;
    private String marital_status;
    private String name;
    private String phone;
    private String photoUrl;
    private User_references references;
    private String sex;
    private String uid;
    private String username;

    // Standard getters & setters
    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public String get_partition() {
        return _partition;
    }

    public void set_partition(String _partition) {
        this._partition = _partition;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public User_career_goals getCareer_goals() {
        return career_goals;
    }

    public void setCareer_goals(User_career_goals career_goals) {
        this.career_goals = career_goals;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public User_education getEducation() {
        return education;
    }

    public void setEducation(User_education education) {
        this.education = education;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User_experience getExperience() {
        return experience;
    }

    public void setExperience(User_experience experience) {
        this.experience = experience;
    }

    public String getMarital_status() {
        return marital_status;
    }

    public void setMarital_status(String marital_status) {
        this.marital_status = marital_status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public User_references getReferences() {
        return references;
    }

    public void setReferences(User_references references) {
        this.references = references;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
