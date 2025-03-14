package com.example.ecommerce.users;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.EcommerceUser;
import com.example.ecommerce.R;

import java.util.ArrayList;


public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private ArrayList<EcommerceUser> userList;
    private OnDeleteClickListener deleteClickListener;

    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }

    public UserAdapter(ArrayList<EcommerceUser> userList, OnDeleteClickListener deleteClickListener) {
        this.userList = userList;
        this.deleteClickListener = deleteClickListener;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        EcommerceUser user = userList.get(position);

        holder.nameTextView.setText(user.getName());
        holder.emailTextView.setText(user.getEmail());


        holder.deleteButton.setTag(position);

        holder.deleteButton.setOnClickListener(v -> {
            int pos = (int) v.getTag();
            deleteClickListener.onDeleteClick(pos);
        });
    }


    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;
        TextView emailTextView;
        Button deleteButton;

        public UserViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.textviewUserName);
            emailTextView = itemView.findViewById(R.id.textviewUserEmail);
            deleteButton = itemView.findViewById(R.id.buttonDeleteUser);
        }
    }
}
