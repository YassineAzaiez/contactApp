package com.example.contactapp;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactapp.models.Contact;
import com.example.contactapp.utils.ContactPropretiesAdpater;
import com.example.contactapp.utils.DataBaseHelper;
import com.example.contactapp.utils.UniversalImageLoader;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by yassine 31/12/19 .
 */
public class ContactFragment extends Fragment {

    public  interface OnEditContactListener{
        void oneEditContactSelected(Contact contact);
    }

    OnEditContactListener mEditContactLstener;
    private ImageView mEditContact ;
    Contact mcontct;
   private CircleImageView mContactImage;
   private ContactPropretiesAdpater contactPropretiesAdpater;
   private RecyclerView mContactCard;
   private TextView mContactName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact,container,false);
        Toolbar mToolbar = view.findViewById(R.id.contactToolbar);
        //TODO
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        setHasOptionsMenu(true);
        ImageView mBackArrow = view.findViewById(R.id.ivBackArrowIcon);
        mEditContact = view.findViewById(R.id.ivEditIcon);
        mContactImage = view.findViewById(R.id.contactImage);
        mContactName = view.findViewById(R.id.contactName);
        mContactCard = view.findViewById(R.id.contactProperties);

        mcontct = getContactFromBundle();
          if(mcontct !=null){
              Log.d("contact", "onCreateView: "+mcontct);
              initViews();
              contactPropretiesAdpater = new ContactPropretiesAdpater(getActivity(),mcontct);
              RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
              mContactCard.setLayoutManager(layoutManager);
              mContactCard.setHasFixedSize(true);
              mContactCard.setAdapter(contactPropretiesAdpater);

          }

        mBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    getActivity().getSupportFragmentManager().popBackStack();
            }
        });


        mEditContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                EditContactFragment fragment = new EditContactFragment();
//                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//                transaction.replace(R.id.fragment_container, fragment);
//                transaction.addToBackStack(getString(R.string.edit_contact_fragment));
//                transaction.commit();
                mEditContactLstener.oneEditContactSelected(mcontct);

            }
        });

        return view;




    }


    private void initViews(){
        mContactName.setText(mcontct.getName());
        UniversalImageLoader.loadCOntactImage(mcontct.getProfileImage(),mContactImage,"http://");

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.contact_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.menuItem_delete){
            DataBaseHelper databaseHelper = new DataBaseHelper(getActivity());
            Cursor cursor = databaseHelper.getContactId(mcontct);

            int contactID = -1;
            while(cursor.moveToNext()){
                contactID = cursor.getInt(0);
            }
            if(contactID > -1){
                if(databaseHelper.deleteContact(contactID) > 0){
                    Toast.makeText(getActivity(), "Contact Deleted", Toast.LENGTH_SHORT).show();

                    //clear the arguments ont he current bundle since the contact is deleted
                    this.getArguments().clear();

                    //remove previous fragemnt from the backstack (therefore navigating back)
                    getActivity().getSupportFragmentManager().popBackStack();
                }
                else{
                    Toast.makeText(getActivity(), "Database Error", Toast.LENGTH_SHORT).show();
                }
            }
        }
        return super.onOptionsItemSelected(item);

    }

      private Contact getContactFromBundle(){
        Bundle contactBundle = this.getArguments();
        if(contactBundle != null){
            return (Contact) contactBundle.getSerializable("contact");

        }
        return null;
      }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
       mEditContactLstener = (OnEditContactListener)getActivity();
        }catch (ClassCastException e){
            Log.d("ClassCastException", "onAttach: "+e.getMessage());
        }
    }
}
