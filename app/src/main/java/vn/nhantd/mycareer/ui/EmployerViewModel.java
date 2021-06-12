package vn.nhantd.mycareer.ui;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.nhantd.mycareer.api.ApiService;
import vn.nhantd.mycareer.model.employeer.Employer;

public class EmployerViewModel extends ViewModel {
    private static final String TAG = "JobViewModel";
    private MutableLiveData<List<Employer>> mListEmployer = new MutableLiveData<>(null);

    public EmployerViewModel() {
        //get employer profile
        int limit = 20;
        ApiService.apiService.getAllEmployer(limit).enqueue(new Callback<List<Employer>>() {
            @Override
            public void onResponse(Call<List<Employer>> call, Response<List<Employer>> response) {
                if (response.body() != null) {
                    mListEmployer.setValue(response.body());
                    Log.i(TAG, "Get Employer profile successful!");
                }
            }

            @Override
            public void onFailure(Call<List<Employer>> call, Throwable t) {
                Log.i(TAG, "Get Employer profile failure!");
            }
        });
    }

    public MutableLiveData<List<Employer>> getmEmployer() {
        return mListEmployer;
    }

    public void setmEmployer(MutableLiveData<List<Employer>> mEmployer) {
        this.mListEmployer = mEmployer;
    }
}
