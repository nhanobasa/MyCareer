package vn.nhantd.mycareer.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import vn.nhantd.mycareer.R;
import vn.nhantd.mycareer.model.user.WorkProgress;

public class WorkProgressAdapter extends RecyclerView.Adapter<WorkProgressAdapter.WorkProgressViewHolder> {

    private static final String TAG = "REALM_RECYCLER_ADAPTER";
    private Context context;
    List<WorkProgress> data;

    public WorkProgressAdapter(Context context, List<WorkProgress> data) {
        this.context = context;
        this.data = data;
    }

    public void setData(List<WorkProgress> data) {
        this.data = data;
    }

    public List<WorkProgress> getData() {
        return this.data;
    }

    @NonNull
    @NotNull
    @Override
    public WorkProgressViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        Log.i(TAG, "Creating view holder");
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.work_progress_cart_item, parent, false);
        return new WorkProgressViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull WorkProgressViewHolder holder, int position) {
        WorkProgress obj = data.get(position);
        Log.i(TAG, "Binding view holder: " + obj.getCompany_name());
        holder.txtCompanyName.setText("Công ty: " + obj.getCompany_name());
        holder.txtPosition.setText("Vị trí: " + obj.getPosition());
        holder.txtFromDate.setText("Từ: " + obj.getFrom_date());
        holder.txtToDate.setText("đến: " + obj.getTo_date());
        holder.txtDes.setText("Công ty: " + obj.getDescription());
    }


    @Override
    public int getItemCount() {
        if (data == null) {
            return 0;
        }
        return data.size();
    }

    public class WorkProgressViewHolder extends RecyclerView.ViewHolder {
        TextView txtCompanyName;
        TextView txtPosition;
        TextView txtFromDate;
        TextView txtToDate;
        TextView txtDes;

        public WorkProgressViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            txtCompanyName = itemView.findViewById(R.id.txt_company_name);
            txtPosition = itemView.findViewById(R.id.txt_position);
            txtFromDate = itemView.findViewById(R.id.txt_from_date);
            txtToDate = itemView.findViewById(R.id.txt_to_date);
            txtDes = itemView.findViewById(R.id.txt_description);

        }
    }


}
