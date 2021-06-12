package vn.nhantd.mycareer.model.employeer;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import vn.nhantd.mycareer.model.Transaction;


public class Employer implements Serializable {
    @SerializedName("id")
    private String _id = null;
    private String _partition = "sync";
    private Long dt;
    private String name = null;
    private String address = null;
    private String contact = null;
    private String description = null;
    private String status = null;
    private String photoUrl = null;
    private List<Transaction> transactions = null;
    private List<String> jobs = null;

    public Employer() {
    }

    public Employer(String _id, String _partition, Long dt, String name, String address, String contact, String description, String status, List<Transaction> transactions, List<String> jobs) {
        this._id = _id;
        this._partition = _partition;
        this.dt = dt;
        this.name = name;
        this.address = address;
        this.contact = contact;
        this.description = description;
        this.status = status;
        this.transactions = transactions;
        this.jobs = jobs;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String get_partition() {
        return _partition;
    }

    public void set_partition(String _partition) {
        this._partition = _partition;
    }

    public Long getDt() {
        return dt;
    }

    public void setDt(Long dt) {
        this.dt = dt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public List<String> getJobs() {
        return jobs;
    }

    public void setJobs(List<String> jobs) {
        this.jobs = jobs;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
