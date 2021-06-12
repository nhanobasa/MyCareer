package vn.nhantd.mycareer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.squareup.picasso.Picasso;

import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.nhantd.mycareer.api.ApiService;
import vn.nhantd.mycareer.databinding.ActivityJobBinding;
import vn.nhantd.mycareer.firebase.auth.FirebaseAuthentication;
import vn.nhantd.mycareer.model.Transaction;
import vn.nhantd.mycareer.model.employeer.Employer;
import vn.nhantd.mycareer.model.job.Job;
import vn.nhantd.mycareer.ui.JobViewModel;
import vn.nhantd.mycareer.util.Utils;

public class JobActivity extends AppCompatActivity {
    private static final String TAG = "EditProfileUserActivity";
    public static final String EXTRA_DATA = "JobActivity-extra-data";

    private ActivityJobBinding binding;
    private JobViewModel model;
    private Job job;
    private Employer employer;
    private boolean checkSaveJob = false;
    private boolean checkLogin = FirebaseAuthentication.getCurrentUser() != null;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(JobActivity.this, R.layout.activity_job);
        setContentView(binding.getRoot());

        // get intent từ job được chọn
        Intent intent = getIntent();
        // lấy thông tin job
        job = (Job) intent.getSerializableExtra("select-job");
        // lấy thông tin công ty
        employer = (Employer) intent.getSerializableExtra("select-job-employer");

        // tạo model + set datal
        model = new JobViewModel(job, employer);

        //set image company
        if (employer.getPhotoUrl() != null && !employer.getPhotoUrl().isEmpty()) {
            Picasso.get().load(Uri.parse(employer.getPhotoUrl())).into(binding.imgCompany);
        }
        //set ảnh bìa cho job nếu có
        if (job.getPhotoUrl() != null && !job.getPhotoUrl().isEmpty()) {
            Picasso.get().load(Uri.parse(employer.getPhotoUrl())).into(binding.imgCompany);
            binding.txtActivityTitle.setTextColor(R.color.black);
            binding.btnJobApply.setTextColor(R.color.black);
        }

        // Kiểm tra xem job này đẫ được lưu hay chưa
        // get list transaction
        List<Transaction> transactionList = job.getTransactions();
        if (transactionList != null) {

            // Xem có transation nào của người dùng có type là like hay ko
            // Lấy _id của user
            String _id = "";
            if (checkLogin) {
                _id = Utils.convertUidTo_id(FirebaseAuthentication.getCurrentUser().getUid());
            }


            //sort by dt DESC
            transactionList.sort(new Comparator<Transaction>() {
                @Override
                public int compare(Transaction o1, Transaction o2) {
                    return o1.getDt() < o2.getDt() ? 1 : -1;
                }
            });

            for (Transaction transaction : transactionList) {
                if (transaction.getUser_id().equals(_id)) {
                    if (transaction.getTransaction_code().equals("like")) {
                        checkSaveJob = true;
                    } else {
                        checkSaveJob = false;
                    }
                    break;
                }
            }

        }
        // Nếu đã yêu thích thì đổi màu trái tim
        if (checkSaveJob) {
            @SuppressLint("UseCompatLoadingForDrawables") Drawable img = getResources().getDrawable(R.drawable.heart);
            img.setBounds(0, 0, 60, 60);
            binding.btnJobLike.setCompoundDrawables(img, null, null, null);
        }

        // set model
        binding.setJobViewModel(model);

        // xử lý sự kiến
        clickHandler();

    }

    private void clickHandler() {
        // set onClick for back button
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });

        binding.btnJobLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check login, nếu chưa login -> Login Activity
                checkLogin();

                checkSaveJob = !checkSaveJob;
                String transactionCode = checkSaveJob ? "like" : "unlike";

                if (checkLogin) {
                    // Lấy _id của user
                    String _id = Utils.convertUidTo_id(FirebaseAuthentication.getCurrentUser().getUid());
                    Transaction transaction = new Transaction(transactionCode, _id);

                    ApiService.apiService.setJobTransaction(job.get_id(), transaction).enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            // Nếu cập nhật thành công
                            if (response.body().equals("OK")) {
                                // set icon
                                int icon = checkSaveJob ? R.drawable.heart : R.drawable.un_heart;
                                @SuppressLint("UseCompatLoadingForDrawables") Drawable img = getResources().getDrawable(icon);
                                img.setBounds(0, 0, 60, 60);
                                binding.btnJobLike.setCompoundDrawables(img, null, null, null);

                                Log.i(TAG, "Thay đổi trạng thái thành: " + transactionCode);
                            } else {
                                Log.i(TAG, "Thay đổi trạng thật bại!");
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Log.i(TAG, "Thay đổi trạng thật bại!");
                        }
                    });
                }
            }
        });


    }

    private void checkLogin() {
        if (!checkLogin) {
            Intent i = new Intent(JobActivity.this, LoginActivity.class);
            startActivity(i);

        }
    }

}