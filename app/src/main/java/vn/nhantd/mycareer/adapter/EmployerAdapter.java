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
    private boolean checkLike = false;
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

//        Ki???m tra xem ng?????i d??ng ???? quan t??m nh?? cung c???p n??y hay ch??a
            checkLike = checkLike(employer);
            // N???u ???? y??u th??ch th?? ?????i m??u tr??i tim
            if (checkLike) {
                @SuppressLint("UseCompatLoadingForDrawables") Drawable img = context.getResources().getDrawable(R.drawable.heart);
                img.setBounds(0, 0, 60, 60);
                holder.btnLike.setImageDrawable(img);
            }
//        //////////////////////////////////

            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, EmployerActivity.class);

                    // get item ??? v??? tr?? hi???n t???i
                    Employer employer = data.get(position);

                    // put employer data
                    intent.putExtra("select-employer", employer);

                    context.startActivity(intent);
                }
            });

            // X??? ki???n cho n??t like
            holder.btnLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Todo Vi???t x??? l?? s??? ki???n quan t??m employer
                    // check login, n???u ch??a login -> Login Activity
                    checkLogin();

                    checkLike = checkLike(employer);
                    checkLike = !checkLike;
                    String transactionCode = checkLike ? "like" : "unlike";

                    if (checkLogin) {
                        // L???y _id c???a user
                        String _id = Utils.convertUidTo_id(FirebaseAuthentication.getCurrentUser().getUid());
                        Transaction transaction = new Transaction(transactionCode, _id);

                        ApiService.apiService.setEmployerTransaction(employer.get_id(), transaction).enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                // N???u c???p nh???t th??nh c??ng
                                if (response.body().equals("OK")) {
                                    // set icon
                                    int icon = checkLike ? R.drawable.heart : R.drawable.un_heart;
                                    @SuppressLint("UseCompatLoadingForDrawables") Drawable img = context.getResources().getDrawable(icon);
                                    img.setBounds(0, 0, 60, 60);
                                    holder.btnLike.setImageDrawable(img);

                                    employer.getTransactions().add(0, transaction);

                                    long totalLike = calculateTotalLike(employer);
                                    holder.txtTotalTransaction.setText("L?????t quan t??m: " + totalLike);

                                    Log.i(TAG, "Thay ?????i tr???ng th??i th??nh: " + transactionCode);
                                } else {
                                    Log.i(TAG, "Thay ?????i tr???ng th???t b???i!");
                                }
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                Log.i(TAG, "Thay ?????i tr???ng th???t b???i!");
                            }
                        });
                    }
                }
            });


            if (employer.getPhotoUrl() != null)
                Picasso.get().load(Uri.parse(employer.getPhotoUrl())).into(holder.imgCompany);
            holder.txtCompanyName.setText(employer.getName());

            // set t???ng s??? l?????ng c??ng vi???c ??ang tuy???n
            ApiService.apiService.getAllJobsOfEmployer(employer.get_id(), "active", 50).enqueue(new Callback<List<Job>>() {
                @Override
                public void onResponse(Call<List<Job>> call, Response<List<Job>> response) {
                    List<Job> jobs = response.body();
                    if (jobs != null) {
                        int totalJob = jobs.size();
                        holder.txtTotalJob.setText(totalJob + " c??ng vi???c ??ang c???n tuy???n");
                        Log.i(TAG, "Get Employer profile successful!");
                    }

                }

                @Override
                public void onFailure(Call<List<Job>> call, Throwable t) {
                    Log.i(TAG, "Get Employer profile failure!");
                }
            });


//            // T??NH TO??N S??? L?????T QUAN T??M ?????N NH?? TUY???N D???NG
//            //l???c l???y l?????t y??u th??ch
//            List<Transaction> likeList = employer.getTransactions().stream()
//                    .filter(transaction -> transaction.getTransaction_code().equals("like"))
//                    .distinct()
//                    .collect(Collectors.toList());
//
//            // distinct theo user_id
//            HashSet<Object> seen = new HashSet<>();
//            likeList.removeIf(e -> !seen.add(e.getUser_id()));
//
//            // t??nh s??? l?????t quan t??m
//            int totalLike = likeList.size();

            long totalLike = calculateTotalLike(employer);
            holder.txtTotalTransaction.setText("L?????t quan t??m: " + totalLike);
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

    private boolean checkLike(Employer employer) {
        // Ki???m tra xem employer n??y ????? ???????c th??ch hay hay ch??a
        // get list transaction
        boolean result = false;
        List<Transaction> transactionList = employer.getTransactions();
        if (transactionList != null) {

            // Xem c?? transation n??o c???a ng?????i d??ng c?? type l?? like hay ko
            // L???y _id c???a user
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

            for (Transaction transaction : transactionList) {
                if (transaction.getUser_id().equals(_id)) {
                    if (transaction.getTransaction_code().equals("like")) {
                        result = true;
                    } else {
                        result = false;
                    }
                    break;
                }
            }
        }
        return result;
    }

    private void checkLogin() {
        if (!checkLogin) {
            Intent i = new Intent(context, LoginActivity.class);
            context.startActivity(i);

        }
    }

    private long calculateTotalLike(Employer employer) {
        // T??NH TO??N S??? L?????T QUAN T??M ?????N NH?? TUY???N D???NG
        //l???c l???y l?????t y??u th??ch ??? l???i ch??? n??y ???
        List<Transaction> likeList = employer.getTransactions().stream()
                .distinct()
                .collect(Collectors.toList());

        //sort by dt DESC
        likeList.sort(new Comparator<Transaction>() {
            @Override
            public int compare(Transaction o1, Transaction o2) {
                return o2.getDt().compareTo(o1.getDt());
            }
        });

        // distinct theo user_id
        HashSet<Object> seen = new HashSet<>();

        // t??nh s??? l?????t quan t??m
        long totalLike = 0;
        likeList.removeIf(e -> !seen.add(e.getUser_id()));
        for (Transaction transaction : likeList) {
            if (transaction.getTransaction_code().equals("like")) {
                totalLike += 1;
            }
        }
        return totalLike;
    }
}
