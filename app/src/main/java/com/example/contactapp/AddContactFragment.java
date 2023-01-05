package com.example.contactapp;


import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.contactapp.models.Contact;
import com.example.contactapp.utils.ChangePhotoDialog;
import com.example.contactapp.utils.DataBaseHelper;
import com.example.contactapp.utils.Permissinos;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class AddContactFragment extends Fragment implements ChangePhotoDialog.OnPhotoRecievedListenner {

    private Contact mContact;
    private EditText mPhoneNumber, mName, mEmail;
    private CircleImageView mContactImage;
    private Spinner mSelectDevice;
    private Toolbar toolbar;
    private int mPreviousKeyStroke;
    String mSelectedImagePath;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_addcontact, container, false);


        TextView label = view.findViewById(R.id.editContactLabel);
        label.setText("Add Contact");
        mPhoneNumber = view.findViewById(R.id.etContactPhone);
        mName = view.findViewById(R.id.etContactName);
        mEmail = view.findViewById(R.id.etContactEmail);
        mContactImage = view.findViewById(R.id.contactImage);
        mSelectDevice = view.findViewById(R.id.selectDevice);
        toolbar = view.findViewById(R.id.editContactToolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);
        ImageView mBackArrow = view.findViewById(R.id.ivEditBackArrowIcon);
        mBackArrow.setOnClickListener(v ->
                getActivity().getSupportFragmentManager().popBackStack());

        ImageView checkmark = view.findViewById(R.id.ivCheckMark);
         checkmark.setOnClickListener(v->{
            DataBaseHelper databaseHelper = new DataBaseHelper(getActivity());
             Contact contact = new Contact(mName.getText().toString(),
                     mPhoneNumber.getText().toString(),
                     mSelectDevice.getSelectedItem().toString(),
                     mEmail.getText().toString(),
                     mSelectedImagePath);
            if(databaseHelper.addContact(contact)>0){
                Toast.makeText(getActivity(),"contact added with success",Toast.LENGTH_SHORT).show();

                getActivity().getSupportFragmentManager().popBackStack();
            }else{
                Toast.makeText(getActivity(),"failed to add contact",Toast.LENGTH_SHORT).show();
            }
         });

        ImageView changePhoto = view.findViewById(R.id.ivCamera);
        changePhoto.setOnClickListener((v) -> {


                    for (int i = 0; i < Permissinos.CAMERA_PERMISSION.length; i++) {

                        if (((MainActivity) getActivity()).checkPermission(Permissinos.CAMERA_PERMISSION[i])) {
                            if (i == Permissinos.CAMERA_PERMISSION.length - 1) {
                                ChangePhotoDialog photoDialog = new ChangePhotoDialog();
                                photoDialog.show(getFragmentManager().beginTransaction(), "Change_Photo_Dialog");
                                photoDialog.setTargetFragment(this, 0);
                            }
                        } else {
                            ((MainActivity) getActivity()).verifyPermissions(Permissinos.CAMERA_PERMISSION);
                        }
                    }


                }

        );


        initOntextChangedListener();


        return view;
    }


    private Bitmap compressBitMap(Bitmap bitmap, int quality) {
        ByteArrayOutputStream bitMapStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, bitMapStream);
        return bitmap;
    }

    @Override
    public void getBitMapImage(Bitmap bitmap) {
        compressBitMap(bitmap, 60);
        mContactImage.setImageBitmap(bitmap);
    }

    @Override
    public void getImagePath(Uri path) {
        if (!path.toString().equals("")) {
            mSelectedImagePath = path.toString();
            mContactImage.setImageURI(path);
        }
    }

    private void initOntextChangedListener() {

        mPhoneNumber.setOnKeyListener((v, keyCode, event) -> {
                    mPreviousKeyStroke = keyCode;
                    return false;

                }
        );
        mPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d(TAG, "onTextChanged: " + s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                String number = s.toString();
                String phonenumber = "";
                if(number.length() == 3 && mPreviousKeyStroke != KeyEvent.KEYCODE_DEL
                        && !number.contains("(")){
                    phonenumber = String.format("(%s", s.toString().substring(0,3));
                    mPhoneNumber.setText(phonenumber);
                    mPhoneNumber.setSelection(phonenumber.length());
                }
                else if(number.length() == 5 && mPreviousKeyStroke != KeyEvent.KEYCODE_DEL
                        && !number.contains(")")){
                    number = String.format("(%s) %s",
                            s.toString().substring(1,4),
                            s.toString().substring(4,5));
                    mPhoneNumber.setText(number);
                    mPhoneNumber.setSelection(number.length());
                }
                else if(number.length() ==10 && mPreviousKeyStroke != KeyEvent.KEYCODE_DEL
                        && !number.contains("-")){
                    number = String.format("(%s) %s-%s",
                            s.toString().substring(1,4),
                            s.toString().substring(6,9),
                            s.toString().substring(9,10));
                    mPhoneNumber.setText(number);
                    mPhoneNumber.setSelection(number.length());


                }
            }

        });

    }

}
