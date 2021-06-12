package vn.nhantd.mycareer.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.nhantd.mycareer.EmployerActivity;
import vn.nhantd.mycareer.LoginActivity;
import vn.nhantd.mycareer.R;
import vn.nhantd.mycareer.api.ApiService;
import vn.nhantd.mycareer.firebase.auth.FirebaseAuthentication;
import vn.nhantd.mycareer.model.Transaction;
import vn.nhantd.mycareer.model.employeer.Employer;
import vn.nhantd.mycareer.model.job.Job;
import vn.nhantd.mycareer.util.Utils;

public class EmployerAdapter extends RecyclerView.Adapter<EmployerAdapter.EmployerViewHolder> {
    private static final String TAG = "REALM_RECYCLER_ADAPTER";
    private Context context;
    private Employer employer;
    List<Employer> data;
    private boolean checkSaveJob = false;
    private boolean checkLogin = FirebaseAuthentication.getCurrentUser() != null;

    public EmployerAdapter(Context context, List<Employer> data) {
        this.context = context;
        this.data = data;
    }

    public void setData(List<Employer> data) {
        this.data = data;
    }

    public List<Employer> getData() {
        return this.data;
    }

    @NonNull
    @NotNull
    @Override
    public EmployerViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        Log.i(TAG, "Creating view holder");
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.top_employer_item, parent, false);
        return new EmployerAdapter.EmployerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull EmployerViewHolder holder, int position) {
        // get data;
        Employer employer = data.get(position);
        if (employer != null) {
            Log.i(TAG, "Binding view holder: " + employer.getName());

//        Kiểm tra xem người dùng đã quan tâm nhà cung cấp này hay chưa
            checkLike(employer);
            // Nếu đã yêu thích thì đổi màu trái tim
            if (checkSaveJob) {
                @SuppressLint("UseCompatLoadingForDrawables") Drawable img = context.getResources().getDrawable(R.drawable.heart);
                img.setBounds(0, 0, 60, 60);
                holder.btnLike.setImageDrawable(img);
            }
//        //////////////////////////////////

            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, EmployerActivity.class);

                    // get item ở vị trí hiện tại
                    Employer employer = data.get(position);

                    // put employer data
                    intent.putExtra("select-employer", employer);

                    context.startActivity(intent);
                }
            });

            // Xự kiện cho nút like
            holder.btnLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Todo Viết xử lý sự kiện quan tâm employer
                    // check login, nếu chưa login -> Login Activity
                    checkLogin();

                    checkSaveJob = !checkSaveJob;
                    String transactionCode = checkSaveJob ? "like" : "unlike";

                    if (checkLogin) {
                        // Lấy _id của user
                        String _id = Utils.convertUidTo_id(FirebaseAuthentication.getCurrentUser().getUid());
                        Transaction transaction = new Transaction(transactionCode, _id);

                        ApiService.apiService.setEmployerTransaction(employer.get_id(), transaction).enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                // Nếu cập nhật thành công
                                if (response.body().equals("OK")) {
                                    // set icon
                                    int icon = checkSaveJob ? R.drawable.heart : R.drawable.un_heart;
                                    @SuppressLint("UseCompatLoadingForDrawables") Drawable img = context.getResources().getDrawable(icon);
                                    img.setBounds(0, 0, 60, 60);
                                    holder.btnLike.setImageDrawable(img);

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


            if (employer.getPhotoUrl() != null)
                Picasso.get().load(Uri.parse(employer.getPhotoUrl())).into(holder.imgCompany);
            holder.txtCompanyName.setText(employer.getName());

            // set tổng số lượng công việc đang tuyển
            ApiService.apiService.getAllJobsOfEmployer(employer.get_id(), "active", 50).enqueue(new Callback<List<Job>>() {
                @Override
                public void onResponse(Call<List<Job>> call, Response<List<Job>> response) {
                    List<Job> jobs = response.body();
                    if (jobs != null) {
                        int totalJob = jobs.size();
                        holder.txtTotalJob.setText(totalJob + " công việc đang cần tuyển");
                        Log.i(TAG, "Get Employer profile successful!");
                    }

                }

                @Override
                public void onFailure(Call<List<Job>> call, Throwable t) {
                    Log.i(TAG, "Get Employer profile failure!");
                }
            });


            // TÍNH TOÁN SỐ LƯỢT QUAN TÂM ĐẾN NHÀ TUYỂN DỤNG
            //lọc lấy lượt yêu thích
            List<Transaction> likeList = employer.getTransactions().stream()
                    .filter(transaction -> transaction.getTransaction_code().equals("like"))
                    .distinct()
                    .collect(Collectors.toList());

            // distinct theo user_id
            HashSet<Object> seen = new HashSet<>();
            likeList.removeIf(e -> !seen.add(e.getUser_id()));

            // tính số lượt quan tâm
            int totalLike = likeList.size();
            holder.txtTotalTransaction.setText("Lượt quan tâm: " + totalLike);
        }

    }

    @Override
    public int getItemCount() {
        if (data == null) {
            return 0;
        }
        return data.size();
    }

    public static class EmployerViewHolder extends RecyclerView.ViewHolder {
        ImageView imgCompany;
        TextView txtTotalJob;
        TextView txtCompanyName;
        TextView txtTotalTransaction;
        CardView cardView;
        ImageButton btnLike;

        public EmployerViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            imgCompany = itemView.findViewById(R.id.img_company);
            txtCompanyName = itemView.findViewById(R.id.txt_company_name);
            txtTotalJob = itemView.findViewById(R.id.txt_total_job);
            txtTotalTransaction = itemView.findViewById(R.id.txt_total_transaction);
            cardView = itemView.findViewById(R.id.card_view_top_employer);
            btnLike = itemView.findViewById(R.id.btn_like_employer);
        }
    }

    private void checkLike(Employer employer) {
        // Kiểm tra xem employer này đẫ được thích hay hay chưa
        // get list transaction
        List<Transaction> transactionList = employer.getTransactions();
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
    }

    private void checkLogin() {
        if (!checkLogin) {
            Intent i = new Intent(context, LoginActivity.class);
            context.startActivity(i);

        }
    }
}
