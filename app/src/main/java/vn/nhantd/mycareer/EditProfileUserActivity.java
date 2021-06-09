package vn.nhantd.mycareer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.nhantd.mycareer.api.ApiService;
import vn.nhantd.mycareer.databinding.ActivityEditProfileUserBinding;
import vn.nhantd.mycareer.firebase.auth.FirebaseAuthentication;
import vn.nhantd.mycareer.fragment.adapter.WorkProgressAdapter;
import vn.nhantd.mycareer.model.user.User;
import vn.nhantd.mycareer.model.user.WorkProgress;
import vn.nhantd.mycareer.ui.UserViewModel;

public class EditProfileUserActivity extends AppCompatActivity {

    private static final String TAG = "EditProfileUserActivity";
    private List<WorkProgress> workProgressList;
    private WorkProgressAdapter adapter;
    private ActivityEditProfileUserBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_user);

        Intent intent = getIntent();
        User user = (User) intent.getSerializableExtra("profile-user");

        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_profile_user);
        UserViewModel model = new UserViewModel();
        model.setUser(user);
        binding.setUserViewModel(model);
        Picasso.with(this).load(user.getPhotoUrl()).into(binding.imgProfile);

        // spinner tình trạng hôn nhân
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item
                , getResources().getStringArray(R.array.marital_status));

        binding.txtProfileMaritalStatus.setAdapter(arrayAdapter);
        arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        binding.txtProfileMaritalStatus.setSelection(arrayAdapter.getPosition(user.getMarital_status()));

        // recyclerviewProfileWorkProgress
        binding.recyclerviewProfileWorkProgress.setLayoutManager(new LinearLayoutManager(this));
        getAllWorkProgress(FirebaseAuthentication.getProfileUser());


    }

    private void getAllWorkProgress(User user) {
        ApiService.apiService.getAllWorkProgressOfUser(user.getUid()).enqueue(new Callback<List<WorkProgress>>() {
            @Override
            public void onResponse(Call<List<WorkProgress>> call, Response<List<WorkProgress>> response) {

                if (response.body() != null) {
                    workProgressList = response.body();
                    adapter = new WorkProgressAdapter(getApplicationContext(), workProgressList);
                    binding.recyclerviewProfileWorkProgress.setAdapter(adapter);
                    Log.d(TAG, "get all work progress of user successful!");
                    Log.d(TAG, adapter.getData().toString());
                } else
                    Log.d(TAG, "get all work progress of user fail!");
            }

            @Override
            public void onFailure(Call<List<WorkProgress>> call, Throwable t) {
                Log.d(TAG, "get all work progress of user fail!");
            }
        });
    }

}