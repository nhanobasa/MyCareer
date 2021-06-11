package vn.nhantd.mycareer.ui;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.nhantd.mycareer.api.ApiService;
import vn.nhantd.mycareer.firebase.auth.FirebaseAuthentication;
import vn.nhantd.mycareer.model.job.Job;
import vn.nhantd.mycareer.model.user.User;


public class HomeViewModel extends ViewModel {
    private static final String TAG = "HomeViewModel";
    private final MutableLiveData<List<Job>> mTopJob = new MutableLiveData<>(null);
    private final MutableLiveData<List<Job>> mJob4User = new MutableLiveData<>(null);

    public MutableLiveData<List<Job>> getTopJob() {
        return mTopJob;
    }

    public HomeViewModel() {
        // get Top Job from api
        ApiService.apiService.getAllJobs(20).enqueue(new Callback<List<Job>>() {
            @Override
            public void onResponse(Call<List<Job>> call, Response<List<Job>> response) {
                Log.d(TAG, "get user from api successful");
                mTopJob.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Job>> call, Throwable t) {
                Log.d(TAG, "get user from api failure");
            }
        });

        // get Top job for user from api
        // kiểm tra xem đã đăng nhập hay chưa
        User user = FirebaseAuthentication.getProfileUser();
        // Giới hạn hiển thị 10 jobs
        int limit = 10;

        if (user == null) {
            ApiService.apiService.getAllJobs(limit).enqueue(new Callback<List<Job>>() {
                @Override
                public void onResponse(Call<List<Job>> call, Response<List<Job>> response) {
                    mJob4User.setValue(response.body());
                    Log.d(TAG, "get job for user from api successful");
                }

                @Override
                public void onFailure(Call<List<Job>> call, Throwable t) {
                    Log.d(TAG, "get job for user from api failure");
                }
            });
        } else {
            ApiService.apiService.getJobForUser(user.get_id(), limit).enqueue(new Callback<List<Job>>() {
                @Override
                public void onResponse(Call<List<Job>> call, Response<List<Job>> response) {
                    mJob4User.setValue(response.body());
                    Log.d(TAG, "get job for user from api successful");
                }

                @Override
                public void onFailure(Call<List<Job>> call, Throwable t) {
                    Log.d(TAG, "get job for user from api failure");
                }
            });
        }

    }

    public void setTopJob(List<Job> category) {
        mTopJob.setValue(category);
    }

    public void setJob4User(List<Job> job) {
        mJob4User.setValue(job);
    }

    public MutableLiveData<List<Job>> getJob4User() {
        return mJob4User;
    }
}
