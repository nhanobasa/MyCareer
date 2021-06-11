package vn.nhantd.mycareer.model.job;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import vn.nhantd.mycareer.model.Transaction;
import vn.nhantd.mycareer.model.user.Salary;

public class Job {

    @SerializedName("id")
    private String _id = null;
    private String _partition = "sync";
    private Long dt = System.currentTimeMillis();
    private String name = null;
    private String address = null;
    private String position = null;
    private String category = null;
    private Salary salary = null;
    private JobDescription description = null;
    private String status = null;
    private List<Transaction> transactions = null;

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

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Salary getSalary() {
        return salary;
    }

    public void setSalary(Salary salary) {
        this.salary = salary;
    }

    public JobDescription getDescription() {
        return description;
    }

    public void setDescription(JobDescription description) {
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

}
