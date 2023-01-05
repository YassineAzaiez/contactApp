package com.example.contactapp;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.contactapp.models.Contact;
import com.example.contactapp.utils.ChangePhotoDialog;
import com.example.contactapp.utils.DataBaseHelper;
import com.example.contactapp.utils.Permissinos;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.ContentValues.TAG;

/**
 * Created by yassine 31/12/19 .
 */
public class EditContactFragment extends Fragment implements ChangePhotoDialog.OnPhotoRecievedListenner{
    private Contact mContact;
    private EditText mPhoneNumber, mName, mEmail;
    private CircleImageView mContactImage;
    private Spinner mSelectDevice;
    private Toolbar toolbar;
    private String mSelectedImagePath;


    ImageLoader imageLoader = ImageLoader.getInstance();
    public EditContactFragment() {
        super();
        setArguments(new Bundle());

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.fragment_editcontact, container, false);
        TextView label = view.findViewById(R.id.editContactLabel);
        label.setText("Edit Contact");
        mPhoneNumber =  view.findViewById(R.id.etContactPhone);
        mName =  view.findViewById(R.id.etContactName);
        mEmail =  view.findViewById(R.id.etContactEmail);
        mContactImage =  view.findViewById(R.id.contactImage);
        mSelectDevice =  view.findViewById(R.id.selectDevice);
        toolbar =  view.findViewById(R.id.editContactToolbar);
        ImageView mCheckMark = view.findViewById(R.id.ivCheckMark);
        Log.d(TAG, "onCreateView: started.");
        ImageView mBackArrow = view.findViewById(R.id.ivEditBackArrowIcon);
        //get the contact from the bundle
        mContact = getContactArguments();

        mBackArrow.setOnClickListener(v ->
                getActivity().getSupportFragmentManager().popBackStack());

       mCheckMark.setOnClickListener((v)->{


               //get the database helper and save the contact
               DataBaseHelper databaseHelper = new DataBaseHelper(getActivity());
               Cursor cursor = databaseHelper.getContactId(mContact);

               int contactID = -1;
               while(cursor.moveToNext()){
                   contactID = cursor.getInt(0);
               }
               if(contactID > -1){
                   if(mSelectedImagePath != null){
                       mContact.setProfileImage(mSelectedImagePath);
                   }
                   mContact.setName(mName.getText().toString());
                   mContact.setPhonenumber(mPhoneNumber.getText().toString());
                   mContact.setDevice(mSelectDevice.getSelectedItem().toString());
                   mContact.setEmail(mEmail.getText().toString());

                   databaseHelper.updateContact(mContact, contactID);
                   Toast.makeText(getActivity(), "Contact Updated", Toast.LENGTH_SHORT).show();
               }
               else{
                   Toast.makeText(getActivity(), "Database Error", Toast.LENGTH_SHORT).show();
               }

       });

          ImageView changePhoto = view.findViewById(R.id.ivCamera);
          changePhoto.setOnClickListener((v)->{


              for(int i = 0; i< Permissinos.CAMERA_PERMISSION.length; i++){

                  if(((MainActivity)getActivity()).checkPermission(Permissinos.CAMERA_PERMISSION[i])){
                             if(i == Permissinos.CAMERA_PERMISSION.length-1){
                                 ChangePhotoDialog photoDialog = new ChangePhotoDialog();
                                 photoDialog.show(getFragmentManager().beginTransaction(),"Change_Photo_Dialog");
                                   photoDialog.setTargetFragment(this,0);
                             }
                  }else{
                      ((MainActivity)getActivity()).verifyPermissions(Permissinos.CAMERA_PERMISSION);
                  }
              }



                  }

          );
        init();
        return  view;
    }
    private void init(){
        mPhoneNumber.setText(mContact.getPhonenumber());
        mName.setText(mContact.getName());
        mEmail.setText(mContact.getEmail());
        imageLoader.displayImage("http://"+ mContact.getProfileImage(), mContactImage);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.phone_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSelectDevice.setAdapter(adapter);
        int position = adapter.getPosition(mContact.getDevice());
        mSelectDevice.setSelection(position);}

    private Contact getContactArguments(){
        Log.d("ContactInfo", "getContactArguments: "+ getArguments());
        Bundle bundle = this.getArguments();
       if(bundle != null) {

           return (Contact) bundle.getSerializable("ContactInfo");
       }
       return null;
    }

    private  Bitmap compressBitMap(Bitmap bitmap,int quality){
        ByteArrayOutputStream bitMapStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,quality,bitMapStream);
        return bitmap;
    }

    @Override
    public void getBitMapImage(Bitmap bitmap) {
compressBitMap(bitmap,60);
        mContactImage.setImageBitmap(bitmap);
    }

    @Override
    public void getImagePath(Uri path) {
        if(!path.toString().equals("")){

           mContactImage.setImageURI(path);
        }
    }
}
