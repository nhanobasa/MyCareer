package vn.nhantd.mycareer.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.nhantd.mycareer.JobActivity;
import vn.nhantd.mycareer.R;
import vn.nhantd.mycareer.api.ApiService;
import vn.nhantd.mycareer.model.employeer.Employer;
import vn.nhantd.mycareer.model.job.Job;

public class TopJobAdapter extends RecyclerView.Adapter<TopJobAdapter.TopCategoryViewHolder> {
    private static final String TAG = "REALM_RECYCLER_ADAPTER";
    private final Context context;
    List<Job> data;

    public TopJobAdapter(Context context, List<Job> data) {
        this.context = context;
        this.data = data;
    }

    public void setData(List<Job> data) {
        this.data = data;
    }

    public List<Job> getData() {
        return this.data;
    }

    @NonNull
    @NotNull
    @Override
    public TopCategoryViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        Log.i(TAG, "Creating view holder");
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.top_job_item, parent, false);
        return new TopJobAdapter.TopCategoryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull TopCategoryViewHolder holder, int position) {
        switch (position) {
            case 0:
                holder.cardView.setBackgroundResource(R.drawable.border_set_bottom_top_1);
                break;

            case 1:
                holder.cardView.setBackgroundResource(R.drawable.border_set_bottom_top_2);
                break;

            case 2:
                holder.cardView.setBackgroundResource(R.drawable.border_set_bottom_top_3);
                break;
            default:
                holder.cardView.setBackgroundResource(R.drawable.border_set_bottom_top_0);
                break;
        }

        Job job = data.get(position);
        Log.i(TAG, "Binding view holder: " + job.getName());

        //get employer profile
        ApiService.apiService.getEmployer(job.getEmployer_id()).enqueue(new Callback<Employer>() {
            @Override
            public void onResponse(Call<Employer> call, Response<Employer> response) {
                Employer employer = response.body();
                Log.i(TAG, "Get Employer profile successful!");
                if (employer.getPhotoUrl() != null)
                    Picasso.get().load(Uri.parse(employer.getPhotoUrl())).into(holder.imgCompany);
                holder.txtJobName.setText(job.getName());
                holder.txtCompanyName.setText(employer.getName());
                holder.txtJobSalary.setText(job.getSalary().toString());

                holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, JobActivity.class);

                        // get item ở vị trí hiện tại
//                        Job job = data.get(position);

                        // put job data
                        intent.putExtra("select-job", job);
                        // put employer data
                        intent.putExtra("select-job-employer", employer);

                        context.startActivity(intent);
                    }
                });
            }

            @Override
            public void onFailure(Call<Employer> call, Throwable t) {
                Log.i(TAG, "Get Employer profile failure!");
            }
        });

    }

    @Override
    public int getItemCount() {
        if (data == null) {
            return 0;
        }
        return data.size();
    }

    public class TopCategoryViewHolder extends RecyclerView.ViewHolder {
        ImageView imgCompany;
        TextView txtJobName;
        TextView txtCompanyName;
        TextView txtJobSalary;
        CardView cardView;

        public TopCategoryViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            imgCompany = itemView.findViewById(R.id.img_company);
            txtJobName = itemView.findViewById(R.id.txt_job_name);
            txtCompanyName = itemView.findViewById(R.id.txt_company_name);
            txtJobSalary = itemView.findViewById(R.id.txt_job_salary);
            cardView = itemView.findViewById(R.id.top_job_item);
        }
    }
}
