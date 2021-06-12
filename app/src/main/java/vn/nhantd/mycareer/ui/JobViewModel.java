package vn.nhantd.mycareer.ui;

import androidx.lifecycle.ViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;

import vn.nhantd.mycareer.model.employeer.Employer;
import vn.nhantd.mycareer.model.job.Job;
import vn.nhantd.mycareer.model.job.JobDescription;

public class JobViewModel extends ViewModel {
    private static final String TAG = "JobViewModel";
    private Job job;
    private Employer employer;

    public JobViewModel(Job job, Employer employer) {
        this.job = job;
        this.employer = employer;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public Employer getEmployer() {
        return employer;
    }

    public void setEmployer(Employer employer) {
        this.employer = employer;
    }

    public String timeToDate() {
        Long timemilis = getJob().getDt();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        date.setTime(timemilis);
        return dateFormat.format(date);
    }

    public String getMoreDetail() {
        Job job = getJob();
        JobDescription description = job.getDescription();
        String jobType = "- Loại công việc: " + description.getType() + "\n";
        String sex = "- Giới tính: " + description.getSex() + "\n";
        String age = "- Tuổi: " + description.getAge() + "\n";
        String degree = "- Bằng cấp: " + description.getDegree() + "\n";

        String result = jobType + sex + age + degree;

        return result;
    }
}
