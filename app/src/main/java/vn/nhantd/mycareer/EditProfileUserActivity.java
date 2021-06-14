package vn.nhantd.mycareer;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.nhantd.mycareer.adapter.LanguageAdapter;
import vn.nhantd.mycareer.adapter.WorkProgressAdapter;
import vn.nhantd.mycareer.api.ApiService;
import vn.nhantd.mycareer.databinding.ActivityEditProfileUserBinding;
import vn.nhantd.mycareer.model.user.User;
import vn.nhantd.mycareer.model.user.User_career_goals;
import vn.nhantd.mycareer.model.user.Salary;
import vn.nhantd.mycareer.model.user.User_experience;
import vn.nhantd.mycareer.model.user.WorkProgress;
import vn.nhantd.mycareer.ui.EditProfileUserViewModel;

public class EditProfileUserActivity extends AppCompatActivity {

    private static final String TAG = "EditProfileUserActivity";
    public static final String EXTRA_DATA = "EditProfileUserActivity-extra-data";

    private List<WorkProgress> workProgressList;
    private WorkProgressAdapter adapter;
    private ActivityEditProfileUserBinding binding;
    private User user;
    private Uri cvUri;
    private FirebaseStorage storage;
    private EditProfileUserViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("profile-user");

        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_profile_user);
        setContentView(binding.getRoot());

        storage = FirebaseStorage.getInstance();

        model = new EditProfileUserViewModel();
        model.setUser(user);
        Picasso.get().load(user.getPhotoUrl()).into(binding.imgProfile);

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

        binding.setEditProfileUserViewModel(model);

        clickHandler();

        spinnerHandler();


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

    private void spinnerHandler() {
        binding.txtProfileSex.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) return;
                user = model.getUser();
                String item = parent.getSelectedItem().toString();
                user.setSex(item);
                model.setUser(user);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.txtProfileMaritalStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) return;
                user = model.getUser();
                String item = parent.getSelectedItem().toString();
                user.setMarital_status(item);
                model.setUser(user);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.txtProfileLevel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) return;
                user = model.getUser();
                String item = parent.getSelectedItem().toString();
                User_career_goals careerGoals = user.getCareer_goals();
                if (careerGoals == null) {
                    careerGoals = new User_career_goals();
                }
                careerGoals.setLevel(item);
                user.setCareer_goals(careerGoals);
                model.setUser(user);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.txtProfileCareerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) return;
                user = model.getUser();
                String item = parent.getSelectedItem().toString();
                User_career_goals careerGoals = user.getCareer_goals();
                if (careerGoals == null) {
                    careerGoals = new User_career_goals();
                }
                careerGoals.setCategory(item);
                user.setCareer_goals(careerGoals);
                model.setUser(user);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.txtProfileCurrentLevel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) return;
                user = model.getUser();
                String item = parent.getSelectedItem().toString();
                User_experience user_experience = user.getExperience();
                if (user_experience == null) {
                    user_experience = new User_experience();
                }
                user_experience.setCurrent_level(item);
                user.setExperience(user_experience);
                model.setUser(user);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void clickHandler() {
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

        // set onClick for txt salary
        binding.txtProfileSalary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditProfileUserActivity.this, UpdateSalaryActivity.class);
                intent.putExtra("profile-salary", user.getCareer_goals().getSalary());
                startActivityForResult(intent, 1);
            }
        });

        binding.imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 2000);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                Salary salary = (Salary) data.getSerializableExtra(UpdateSalaryActivity.EXTRA_DATA);
                User user = model.getUser();
                User_career_goals careerGoals = user.getCareer_goals();
                careerGoals.setSalary(salary);

                user.setCareer_goals(careerGoals);
                model.setUser(user);
                binding.txtProfileSalary.setText(model.getUser().getCareer_goals().getSalary().toString());
            }
        }

        if (requestCode == 2000) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    cvUri = data.getData();
                    String fileName = cvUri.getLastPathSegment();
                    fileName = fileName.substring(fileName.lastIndexOf("/") + 1); // ví dụ: TranDucNhan-CV.pdf

                    // [START upload_create_reference]
                    // Create a storage reference from our app
                    StorageReference storageRef = storage.getReference();

                    // Nếu upload CV cá nhân lên
                    if (cvUri != null) {
                        // Upload file and metadata to the path 'images/mountains.jpg'
                        UploadTask uploadTask = storageRef.child("image/" + user.get_id() + "/" + fileName).putFile(cvUri);

                        // thực hiện quá trình upload và đẩy transaction lên service
                        uploadFileProcess(uploadTask);
                    }
                }
            }
        }
    }

    private void uploadFileProcess(UploadTask uploadTask) {
        // Listen for state changes, errors, and completion of the upload.
        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                Log.d(TAG, "Upload is " + progress + "% done");
            }
        }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d(TAG, "Upload is paused");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // Handle successful uploads on complete
                // ...
                Log.d(TAG, "Upload is done!");
            }
        });

        // Sau khi tải file lên thành công, nhận url download file để add vào transaction
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return task.getResult().getStorage().getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                } else {
                    // Handle failures
                    // ...
                }
            }
        });
        urlTask.addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Uri> task) {
                Uri uriDowload = task.getResult();
                Log.d(TAG, "URi download: " + uriDowload);
                if (user.get_id() != null) {
                    User user = model.getUser();
                    user.setPhotoUrl(uriDowload.toString());
                    model.setUser(user);
                }
            }
        });
    }
}