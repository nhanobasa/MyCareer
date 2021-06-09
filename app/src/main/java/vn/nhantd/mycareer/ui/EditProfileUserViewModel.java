package vn.nhantd.mycareer.ui;

import androidx.lifecycle.ViewModel;

import vn.nhantd.mycareer.model.user.User;


public class EditProfileUserViewModel<Int> extends ViewModel {
    private static final String TAG = "ProfileUserViewModel";
    private User user;
    private int sexIdItemPosition;
    private int maritalIdItemPosition;
    private int levelIdItemPosition;
    private int careerCategoryIdItemPosition;
    private int currentLevelIdItemPosition;

    public int getMaritalIdItemPosition() {
        return maritalIdItemPosition;
    }

    public void setMaritalIdItemPosition(int maritalIdItemPosition) {
        this.maritalIdItemPosition = maritalIdItemPosition;
    }

    public int getLevelIdItemPosition() {
        return levelIdItemPosition;
    }

    public void setLevelIdItemPosition(int levelIdItemPosition) {
        this.levelIdItemPosition = levelIdItemPosition;
    }

    public int getCareerCategoryIdItemPosition() {
        return careerCategoryIdItemPosition;
    }

    public void setCareerCategoryIdItemPosition(int careerCategoryIdItemPosition) {
        this.careerCategoryIdItemPosition = careerCategoryIdItemPosition;
    }

    public int getCurrentLevelIdItemPosition() {
        return currentLevelIdItemPosition;
    }

    public void setCurrentLevelIdItemPosition(int currentLevelIdItemPosition) {
        this.currentLevelIdItemPosition = currentLevelIdItemPosition;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    //
    public EditProfileUserViewModel() {
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

    public int getSexIdItemPosition() {
        return sexIdItemPosition;
    }

    public void setSexIdItemPosition(int sexIdItemPosition) {
        this.sexIdItemPosition = sexIdItemPosition;
    }
}
