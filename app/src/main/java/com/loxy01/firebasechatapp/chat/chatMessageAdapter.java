package com.loxy01.firebasechatapp.chat;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.loxy01.firebasechatapp.R;

import java.util.List;

public class chatMessageAdapter extends ArrayAdapter<chatMessage> {

    /** This code was written by Loxy01.
     *  Anyone can use the entire functionality of the application
     */

    Activity activity;
    ViewHolder viewHolder = null;

    public chatMessageAdapter(Activity activityContext, int resource, List<chatMessage> messagesList) {
        super(activityContext, resource, messagesList);

        this.activity = activityContext;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        int mine_flag = getItemViewType(position);
        int layoutResourse;

        chatMessage chatMessage = getItem(position);
        boolean isText = chatMessage.getImageUrl() == null;

        if(mine_flag == 0){
            layoutResourse = R.layout.my_message_item;
        } else{
            layoutResourse = R.layout.your_message_item;
        }

        if(convertView !=null){
            viewHolder = (ViewHolder) convertView.getTag();
        }else{
            convertView = inflater.inflate(layoutResourse, parent, false);

            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }

        if(isText){
            viewHolder.setVisibility(true);
            viewHolder.viewMess.setText(chatMessage.getText());
        }else if(!isText){
            viewHolder.setVisibility(false);
            Glide.with(viewHolder.imgView.getContext())
                    .load(chatMessage.getImageUrl())
                    .into(viewHolder.imgView);
        }
        if(isText){
            viewHolder.setVisibility(true);
            viewHolder.viewMess.setText(chatMessage.getText());
        }else if(!isText){
            viewHolder.setVisibility(false);
            Glide.with(viewHolder.imgView.getContext())
                    .load(chatMessage.getImageUrl())
                    .into(viewHolder.imgView);
        }

        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        int flag;

        chatMessage mess = getItem(position);

        if(mess.getIsMine()){
            flag = 0;
        } else {
            flag = 1;
        }

        return flag;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    private class ViewHolder {
        private TextView viewMess;
        private ImageView imgView;

        private ViewHolder(View view){
            viewMess = view.findViewById(R.id.TextViewMessage);
            imgView = view.findViewById(R.id.PhotoImageView);
        }
        private void setVisibility(boolean text){
            if(text){
                viewMess.setVisibility(View.VISIBLE);
                imgView.setVisibility(View.GONE);
            } else{
                viewMess.setVisibility(View.GONE);
                imgView.setVisibility(View.VISIBLE);
            }
        }
    }
}
