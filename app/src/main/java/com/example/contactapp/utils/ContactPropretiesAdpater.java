package com.example.contactapp.utils;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactapp.MainActivity;
import com.example.contactapp.R;
import com.example.contactapp.models.Contact;

import java.util.ArrayList;


/**
 * Created by yassine 30/12/19 .
 */
public class ContactPropretiesAdpater extends RecyclerView.Adapter<ContactPropretiesAdpater.ContactPropretiesViewHolder> {

    private Context mContext;
    private ArrayList<Contact> mContacts = new ArrayList<>();





    public ContactPropretiesAdpater(Context context , Contact contacts) {
        this.mContext = context;
        this.mContacts.add(contacts);


    }


    @NonNull
    @Override
    public ContactPropretiesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.layout_contact_cardview;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, parent, false);
        ContactPropretiesViewHolder viewHolder =new ContactPropretiesViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ContactPropretiesViewHolder holder, final int position) {
        if(position == 0){
            final String name = mContacts.get(0).getPhonenumber();
            holder.mContactName.setText(mContacts.get(0).getPhonenumber());
            holder.mRightIcon.setImageResource(R.drawable.ic_phone);
            holder.mRightIcon.setOnClickListener(new OnClickListener(){

                @Override
                public void onClick(View v) {
                    if (((MainActivity) mContext).checkPermission(Manifest.permission.CALL_PHONE)) {
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.fromParts("tel",name,null));
                        mContext.startActivity(intent);

                    }else {
                        ((MainActivity)mContext).verifyPermissions(Permissinos.PHONE_PERMISSION);
                    }
                }
            });
            holder.mLeftIcon.setImageResource(R.drawable.ic_message);
            holder.mLeftIcon.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent smsIntent = new Intent(Intent.ACTION_VIEW,Uri.fromParts("sms",name,null));
                    mContext.startActivity(smsIntent);
                }
            });
        }else{
        holder.mContactName.setText(mContacts.get(0).getEmail());
        holder.mRightIcon.setImageResource(R.drawable.ic_email);
        holder.mRightIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType("plain/text");
                emailIntent.putExtra(Intent.EXTRA_EMAIL,new String[]{mContacts.get(0).getEmail()});
                mContext.startActivity(emailIntent);
            }
        });}
    }


    @Override
    public int getItemCount() {
        return 2;
    }


    public  class ContactPropretiesViewHolder extends RecyclerView.ViewHolder  {
         ImageView  mRightIcon;
         ImageView  mLeftIcon;
         TextView mContactName;


        public ContactPropretiesViewHolder(@NonNull View itemView) {
            super(itemView);
            mRightIcon = itemView.findViewById(R.id.ivRight);
            mLeftIcon = itemView.findViewById(R.id.ivLeft);
            mContactName = itemView.findViewById(R.id.text);



        }


    }
}
