package vn.nhantd.mycareer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.nhantd.mycareer.adapter.LanguageAdapter;
import vn.nhantd.mycareer.api.ApiService;
import vn.nhantd.mycareer.databinding.ActivityEditProfileUserBinding;
import vn.nhantd.mycareer.adapter.WorkProgressAdapter;
import vn.nhantd.mycareer.model.user.User;
import vn.nhantd.mycareer.model.user.WorkProgress;
import vn.nhantd.mycareer.ui.EditProfileUserViewModel;

public class EditProfileUserActivity extends AppCompatActivity {

    private static final String TAG = "EditProfileUserActivity";
    public static final String EXTRA_DATA = "EditProfileUserActivity-extra-data";

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
        EditProfileUserViewModel model = new EditProfileUserViewModel();
        model.setUser(user);
        Picasso.with(this).load(user.getPhotoUrl()).into(binding.imgProfile);

        // spinner giới tính
        ArrayAdapter<String> arrayAdapterSex = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item
                , getResources().getStringArray(R.array.sex));
        binding.txtProfileSex.setAdapter(arrayAdapterSex);
        arrayAdapterSex.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        if (user.getSex() != null)
//            binding.txtProfileSex.setSelection(arrayAdapterSex.getPosition(user.getSex()));
            model.setSexIdItemPosition(arrayAdapterSex.getPosition(user.getSex()));


        // spinner tình trạng hôn nhân
        ArrayAdapter<String> arrayAdapterHn = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item
                , getResources().getStringArray(R.array.marital_status));
        binding.txtProfileMaritalStatus.setAdapter(arrayAdapterHn);
        arrayAdapterHn.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        if (user.getMarital_status() != null)
//            binding.txtProfileMaritalStatus.setSelection(arrayAdapterHn.getPosition(user.getMarital_status()));
            model.setMaritalIdItemPosition(arrayAdapterHn.getPosition(user.getMarital_status()));

        // spinner Cấp bậc mong muốn
        ArrayAdapter<String> arrayAdapterPositon = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item
                , getResources().getStringArray(R.array.career_level));
        binding.txtProfileLevel.setAdapter(arrayAdapterPositon);
        arrayAdapterPositon.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        if (user.getCareer_goals() != null && user.getCareer_goals().getLevel() != null)
//            binding.txtProfileLevel.setSelection(arrayAdapterPositon.getPosition(user.getCareer_goals().getLevel()));
            model.setLevelIdItemPosition(arrayAdapterPositon.getPosition(user.getCareer_goals().getLevel()));

        // spinner Ngành nghề mong muốn
        ArrayAdapter<String> arrayAdapterCareerCategory = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item
                , getResources().getStringArray(R.array.career_category));
        binding.txtProfileCareerCategory.setAdapter(arrayAdapterCareerCategory);
        arrayAdapterCareerCategory.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        if (user.getCareer_goals() != null && user.getCareer_goals().getCategory() != null)
//            binding.txtProfileCareerCategory.setSelection(arrayAdapterCareerCategory.getPosition(user.getCareer_goals().getCategory()));
            model.setCareerCategoryIdItemPosition(arrayAdapterCareerCategory.getPosition(user.getCareer_goals().getCategory()));


        // spinner Cấp bậc hiện tại
        binding.txtProfileCurrentLevel.setAdapter(arrayAdapterPositon);
        arrayAdapterPositon.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        if (user.getExperience() != null && user.getExperience().getCurrent_level() != null)
//            binding.txtProfileCurrentLevel.setSelection(arrayAdapterPositon.getPosition(user.getExperience().getCurrent_level()));
            model.setCurrentLevelIdItemPosition(arrayAdapterPositon.getPosition(user.getExperience().getCurrent_level()));

        // recyclerviewProfileWorkProgress
        binding.recyclerviewProfileWorkProgress.setLayoutManager(new LinearLayoutManager(this));
        getAllWorkProgress(user);

        binding.recyclerviewProfileWorkProgress.setLayoutManager(new LinearLayoutManager(this));
        getAllLanguage(user);


        // set onClick for back button
        binding.btnProfileEditBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });
        // set onClick for update button
        binding.btnProfileUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data = new Intent();

                // thực hiện update user
                User user = model.getUser();
                data.putExtra(EXTRA_DATA, user);
                setResult(Activity.RESULT_OK, data);
                finish();
            }
        });
        binding.setEditProfileUserViewModel(model);

    }

    private void getAllLanguage(User user) {
        // TODO Thêm phần truyền dữ liệu vào recyclerView
        List<String> langList = new ArrayList<>();
        try {
            langList = user.getEducation().getForeign_language();
        } catch (Exception e) {
            Log.w(TAG, e);
        }
        LanguageAdapter languageAdapter = new LanguageAdapter(getApplicationContext(),
                langList);
        binding.recyclerviewProfileLanguage.setAdapter(languageAdapter);
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