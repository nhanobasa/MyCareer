package vn.nhantd.mycareer.firebase.messaging;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.nhantd.mycareer.api.ApiService;
import vn.nhantd.mycareer.firebase.auth.FirebaseAuthentication;
import vn.nhantd.mycareer.model.user.User;

import static vn.nhantd.mycareer.firebase.messaging.CloudMessaging.FGM_TOKEN;
import static vn.nhantd.mycareer.firebase.messaging.CloudMessaging.PRE_FGM_TOKEN;

public class CloudMessagingUtils {

    private static final String TAG = CloudMessagingUtils.class.getSimpleName();

    public static void setTokenFCM(Context context) {
        User user = FirebaseAuthentication.getProfileUser();

        // Nếu như đã đăng nhập thành công
        if (user != null) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(PRE_FGM_TOKEN, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            String fgm_token = sharedPreferences.getString(FGM_TOKEN, null);
            if (fgm_token != null) {
                ApiService.apiService.getUser(user.get_id()).enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        User u = response.body();
                        if (u != null) {
                            u.setFgm_token(fgm_token);
                            ApiService.apiService.updateUser(user.get_id(), u).enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    Log.d(TAG, "Update fgm_token Successful!");
                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {
                                    Log.e(TAG, "Update fgm_token Failure!");
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {

                    }
                });

            }


        }

    }
}
