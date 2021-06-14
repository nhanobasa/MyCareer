package vn.nhantd.mycareer.ui;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.nhantd.mycareer.api.ApiService;
import vn.nhantd.mycareer.firebase.auth.FirebaseAuthentication;
import vn.nhantd.mycareer.model.Notification;
import vn.nhantd.mycareer.util.Utils;

public class NotiViewModel extends ViewModel {
    private final MutableLiveData<List<Notification>> mListNotification = new MutableLiveData<>(null);

    public NotiViewModel() {
        String user_id = Utils.convertUidTo_id(FirebaseAuthentication.getCurrentUser().getUid());
        System.out.println(user_id);
        ApiService.apiService.getNotificationOfUser(user_id).enqueue(new Callback<List<Notification>>() {
            @Override
            public void onResponse(Call<List<Notification>> call, Response<List<Notification>> response) {
                if (response.body() != null) {
                    System.out.println(response.body());
                    mListNotification.setValue(response.body());
                    System.out.println("Get noti oke!");
                }
            }

            @Override
            public void onFailure(Call<List<Notification>> call, Throwable t) {

            }
        });
    }

    public MutableLiveData<List<Notification>> getmListNotification() {
        return this.mListNotification;
    }

    public void setmListNotification(List<Notification> mListNotification) {
        this.mListNotification.setValue(mListNotification);
    }

    public void addNoti(Notification notification) {
        List<Notification> noteList = mListNotification.getValue();
        noteList.add(notification);
        mListNotification.setValue(noteList);
    }
}
