package com.project.kalpawijesooriya.patient;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.graphics.Color;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Kalpa Wijesooriya on 3/26/2018.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private List<Messages>mMessagesList;
    private FirebaseAuth mAuth;

    public MessageAdapter(List<Messages>mMessagesList){
        this.mMessagesList=mMessagesList;
    }
    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup patient, int viewType)
    {
          View v= LayoutInflater.from(patient.getContext())
                  .inflate(R.layout.message_single_layout,patient,false);
          return new MessageViewHolder(v);


    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
       public TextView messageText;
       public CircleImageView profileImage;
        View mView;

        public MessageViewHolder(View View) {

            super(View);
            mView=View;
            messageText = (TextView) View.findViewById(R.id.message_text);
            profileImage = (CircleImageView) View.findViewById(R.id.message_profile);
        }
            public void setImage(String image)
            {
                Picasso.with(mView.getContext())
                        .load(image)
                        .into(profileImage);
            }




    }
    @Override
    public void onBindViewHolder(MessageViewHolder viewHolder,int i)
    {    mAuth = FirebaseAuth.getInstance();
        String currentuserid=mAuth.getCurrentUser().getUid();
        Messages c=mMessagesList.get(i);
        String from_usr=c.getFrom();

        if (from_usr.equals(currentuserid)){
            viewHolder.messageText.setBackgroundResource(R.drawable.message_backgroundwhite);
            viewHolder.messageText.setTextColor(Color.BLACK);
        }
        else
        {
            viewHolder.messageText.setBackgroundResource(R.drawable.message_background);
            viewHolder.messageText.setTextColor(Color.WHITE);
        }
        viewHolder.messageText.setText(c.getMessage());
        viewHolder.setImage(c.getImage());



    }

    @Override
    public int getItemCount()
    {
        return mMessagesList.size();
    }
}
