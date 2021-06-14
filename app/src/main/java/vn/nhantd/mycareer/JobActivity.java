package vn.nhantd.mycareer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
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
    private boolean allowedApply;
    private boolean checkLogin = FirebaseAuthentication.getCurrentUser() != null;
    private boolean checkAction = false;
    private boolean applySuccess = false;

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

        ApiService.apiService.getJobProfile(job.get_id()).enqueue(new Callback<Job>() {
            @Override
            public void onResponse(Call<Job> call, Response<Job> response) {
                if (response.body() != null) {
                    job = response.body();

                    // bắt đầu xử lý
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

                    // Kiểm tra xem job này đẫ được thích hay hay chưa
                    checkSaveJob = checkJobTransaction(job, "like");

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
            }

            @Override
            public void onFailure(Call<Job> call, Throwable t) {

            }
        });

    }

    private void clickHandler() {
        // set onClick for back button
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Activity.RESULT_CANCELED);
//                if (checkAction) {
//                    Intent intent = new Intent(JobActivity.this, MainActivity.class);
//                    intent.putExtra("check-action", checkAction);
//                    startActivity(intent);
//                }
                finish();
            }
        });

        binding.btnJobLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check login, nếu chưa login -> Login Activity
                checkLogin();
                checkSaveJob = checkJobTransaction(job, "like");
                checkSaveJob = !checkSaveJob;
                String transactionCode = checkSaveJob ? "like" : "unlike";

                if (checkLogin) {
                    // Lấy _id của user
                    String user_id = Utils.convertUidTo_id(FirebaseAuthentication.getCurrentUser().getUid());
                    Transaction transaction = new Transaction(transactionCode, user_id);

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
                                checkAction = true;

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

        binding.btnJobApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLogin();
                if (checkLogin) {
                    // Kiểm tra xem có được phép nộp hồ sơ tiếp hay ko
                    if (!applySuccess) {
                        allowedApply = checkJobTransaction(job, "apply");
                    }
                    if (allowedApply) {
                        Intent intent = new Intent(JobActivity.this, ApplyActivity.class);
                        String user_id = Utils.convertUidTo_id(FirebaseAuthentication.getCurrentUser().getUid());
                        Transaction applyTransaction = new Transaction("apply", user_id);
                        intent.putExtra("job_id", job.get_id());
                        intent.putExtra("apply_transaction", applyTransaction);
                        startActivityForResult(intent, 1006);
                    } else {
                        System.out.println("Không được phép apply");
                    }
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

    /**
     * Kiểm tra xem người dùng đã like job hay chưa, có được phép gửi hồ sơ apply tiếp hay không.
     *
     * @param job  job
     * @param type loại transaction: like - apply
     * @return result
     */
    private boolean checkJobTransaction(Job job, String type) {
        boolean result = false;
        boolean resultApply = true;

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
                    return o2.getDt().compareTo(o1.getDt());
                }
            });

            // Nếu như type check == "apply"
            if (type.equals("apply")) {
                for (Transaction transaction : transactionList) {
                    if (transaction.getUser_id().equals(_id)) {
                        if (transaction.getTransaction_code().equals(type)) {
                            // Nếu như type == apply, thì kiểm tra employer đã xem hay chưa
                            if (transaction.getViewed()) {
                                // Nếu đã xem -> được phép apply tiếp (gửi hồ sơ lên tiếp)
                                resultApply = true;
                                break;
                            } else {
                                resultApply = false;
                                break;
                            }
                        }
                    }
                }
            } else if (type.equals("like")) {
                for (Transaction transaction : transactionList) {
                    if (transaction.getUser_id().equals(_id)) {
                        if (transaction.getTransaction_code().equals(type)) {
                            result = true;
                            break;
                        } else if (transaction.getTransaction_code().equals("unlike")) {
                            result = false;
                            break;
                        }
                    }
                }
            }
        }

        // output
        if (type.equals("apply")) {
            return resultApply;
        } else {
            return result;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1006) {
            if (resultCode == Activity.RESULT_OK) {
                this.allowedApply = false;
                this.applySuccess = true;
                checkAction = true;
            }
        }
    }
}