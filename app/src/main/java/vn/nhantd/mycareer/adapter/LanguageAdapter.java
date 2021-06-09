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

public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.LanguageViewHolder> {

    private static final String TAG = "REALM_RECYCLER_ADAPTER";
    private Context context;
    List<String> data;

    public LanguageAdapter(Context context, List<String> data) {
        this.context = context;
        this.data = data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    public List<String> getData() {
        return this.data;
    }

    @NonNull
    @NotNull
    @Override
    public LanguageViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        Log.i(TAG, "Creating view holder");
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.language_cart_item, parent, false);
        return new LanguageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull LanguageViewHolder holder, int position) {
        String obj = data.get(position);
        Log.i(TAG, "Binding view holder: " + obj);
        holder.txtLanguageName.setText(obj);
    }


    @Override
    public int getItemCount() {
        if (data == null) {
            return 0;
        }
        return data.size();
    }

    public static class LanguageViewHolder extends RecyclerView.ViewHolder {
        TextView txtLanguageName;

        public LanguageViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            txtLanguageName = itemView.findViewById(R.id.txt_language_name);
        }
    }


}
