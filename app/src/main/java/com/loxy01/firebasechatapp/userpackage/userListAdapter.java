package com.loxy01.firebasechatapp.userpackage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.loxy01.firebasechatapp.R;

import java.util.ArrayList;

public class userListAdapter extends RecyclerView.Adapter<userListAdapter.UserViewHolder>{

    /** This code was written by Loxy01.
     *  Anyone can use the entire functionality of the application
     */

    private ArrayList<user> users;
    public OnUserClickListener listener;
    protected interface OnUserClickListener{
        void onUserClick(int position);
    }
    public void setOnUserClickListener(OnUserClickListener listener){
        this.listener = listener;
    }

    public userListAdapter(ArrayList<user> users) {
        this.users = users;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.userlist_item, parent, false);
        return new UserViewHolder(view,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        user currentUser = users.get(position);
        holder.userNameTxt.setText(currentUser.getUserName());
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder{
        private TextView userNameTxt;

        public UserViewHolder(@NonNull View itemView, OnUserClickListener listener) {
            super(itemView);
            userNameTxt = itemView.findViewById(R.id.UserNameTxtView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null){
                        int position = getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            listener.onUserClick(position);
                        }
                    }
                }
            });
        }
    }
}
