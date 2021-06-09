package vn.nhantd.mycareer.ui;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.nhantd.mycareer.api.ApiService;
import vn.nhantd.mycareer.firebase.auth.FirebaseAuthentication;
import vn.nhantd.mycareer.model.user.User;


public class ProfileUserViewModel extends ViewModel {
    private static final String TAG = "ProfileUserViewModel";
    private final MutableLiveData<User> muser = new MutableLiveData<>(null);

    public MutableLiveData<User> getUser() {
        return muser;
    }

    //
    public ProfileUserViewModel() {
        // get User from api
        User currentUser = FirebaseAuthentication.getProfileUser();
        ApiService.apiService.getUser(currentUser.get_id()).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Log.d(TAG, "get user from api successful");
                muser.setValue(response.body());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d(TAG, "get user from api successful");
            }
        });


    }

    public void setUser(User user) {
        muser.setValue(user);
    }

}
