package com.example.contactapp.utils;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactapp.R;
import com.example.contactapp.models.Contact;
import com.nostra13.universalimageloader.core.ImageLoader;


import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by yassine 30/12/19 .
 */
public class ContactsListAdapter extends RecyclerView.Adapter<ContactsListAdapter.ContactsViewHolder> {

    private Context mContext;
    private ArrayList<Contact> mContacts ;
    private ArrayList<Contact> searchContacts ;
    private String mAppend;
    ContactClickListener mContactClickListener;



    public ContactsListAdapter(Context context , ArrayList<Contact> contacts, ContactClickListener mContactClickListener) {
        this.mContext = context;
        this.mContacts = contacts;

        this.searchContacts = new ArrayList<>();
        searchContacts.addAll(contacts);
        this.mContactClickListener = mContactClickListener;
    }
     public interface  ContactClickListener{
       void   contactClicked(int position);

    }

    @NonNull
    @Override
    public ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.layout_contactlistitem;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, parent, false);
       ContactsViewHolder viewHolder = new ContactsViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ContactsViewHolder holder, int position) {
                  holder.mContactName.setText(mContacts.get(position).getName());
        ImageLoader imageLoader = ImageLoader.getInstance();

        imageLoader.displayImage( mContacts.get(position).getProfileImage(), holder.mContactImage);
    }

    @Override
    public int getItemCount() {
        return mContacts.size();
    }


    public  class ContactsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
         ImageView  mContactImage;
         TextView mContactName;


        public ContactsViewHolder(@NonNull View itemView) {
            super(itemView);
            mContactImage = itemView.findViewById(R.id.contactImage);
            mContactName = itemView.findViewById(R.id.contactName);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int clickpos = getAdapterPosition();
            mContactClickListener.contactClicked(clickpos);
        }
    }
    public void filter(String contactText){
               contactText =contactText.toLowerCase(Locale.getDefault());
               mContacts.clear();

               if(contactText.length() == 0){
                   mContacts.addAll(searchContacts);
               }else{
                   for(Contact contact : searchContacts){
                       if (contact.getName().toLowerCase(Locale.getDefault()).contains(contactText)){
                           mContacts.add(contact);
                       }
                   }
               }
               notifyDataSetChanged();
    }
}
