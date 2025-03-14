package com.example.ecommerce.AdminFiles.AdminNavbarFiles.HomeNavbar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.R;

import java.util.List;

public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.SettingsViewHolder> {

    private List<String> settingsList;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public SettingsAdapter(List<String> settingsList, OnItemClickListener onItemClickListener) {
        this.settingsList = settingsList;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public SettingsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_setting, parent, false);
        return new SettingsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SettingsViewHolder holder, int position) {
        String setting = settingsList.get(position);
        holder.settingName.setText(setting);

        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(position));
    }

    @Override
    public int getItemCount() {
        return settingsList.size();
    }

    public static class SettingsViewHolder extends RecyclerView.ViewHolder {
        TextView settingName;

        public SettingsViewHolder(View itemView) {
            super(itemView);
            settingName = itemView.findViewById(R.id.textSettingName);
        }
    }
}
