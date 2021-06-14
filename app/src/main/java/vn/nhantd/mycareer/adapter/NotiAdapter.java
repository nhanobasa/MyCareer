package vn.nhantd.mycareer.adapter;

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
import vn.nhantd.mycareer.model.Notification;

public class NotiAdapter extends RecyclerView.Adapter<NotiAdapter.NotiViewHolder> {
    private static final String TAG = "NotiAdapter";
    List<Notification> data;

    public List<Notification> getData() {
        return data;
    }

    public NotiAdapter(List<Notification> data) {
        this.data = data;
    }

    public void setData(List<Notification> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @NotNull
    @Override
    public NotiViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        Log.i(TAG, "Creating view holder");
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.noti_item, parent, false);
        return new NotiAdapter.NotiViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull NotiViewHolder holder, int position) {
        Notification notification = data.get(position);
        String sub = notification.getSubject();
        String content = notification.getContent();
        holder.txtSub.setText(sub);
        holder.txtContent.setText(content);
    }

    @Override
    public int getItemCount() {
        if (data == null) {
            return 0;
        }
        return data.size();
    }

    public class NotiViewHolder extends RecyclerView.ViewHolder {
        TextView txtSub;
        TextView txtContent;

        public NotiViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            txtSub = itemView.findViewById(R.id.txt_sub);
            txtContent = itemView.findViewById(R.id.txt_content);
        }
    }
}
