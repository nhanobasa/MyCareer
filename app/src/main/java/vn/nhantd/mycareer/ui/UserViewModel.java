package vn.nhantd.mycareer.ui;

import androidx.lifecycle.ViewModel;

import vn.nhantd.mycareer.model.user.User;


public class UserViewModel extends ViewModel {
    private static final String TAG = "ProfileUserViewModel";
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    //
    public UserViewModel() {
//        // get User from api
//        User currentUser = FirebaseAuthentication.getProfileUser();
//        ApiService.apiService.getUser(currentUser).enqueue(new Callback<User>() {
//            @Override
//            public void onResponse(Call<User> call, Response<User> response) {
//                Log.d(TAG, "get user from api successful");
//                user = response.body();
//            }
//
//            @Override
//            public void onFailure(Call<User> call, Throwable t) {
//                Log.d(TAG, "get user from api successful");
//            }
//        });


    }

}
