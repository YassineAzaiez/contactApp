package com.example.contactapp.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.contactapp.R;

import static android.content.ContentValues.TAG;

/**
 * Created by yassine 25/01/20 .
 */
public class ChangePhotoDialog extends DialogFragment {

    public interface  OnPhotoRecievedListenner{
          void getBitMapImage(Bitmap bitmap);
          void getImagePath(Uri path);
    }

    OnPhotoRecievedListenner onPhotoRecievedListenner;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_changephoto, container, false);
        TextView mDialgoTitle = view.findViewById(R.id.dialogTitle);
        TextView mChoosePhote = view.findViewById(R.id.dialogChoosePhoto);
        TextView mTakephote = view.findViewById(R.id.dialogTakePhoto);
        TextView mCancle = view.findViewById(R.id.dialogCancel);

        mTakephote.setOnClickListener((v) -> {
            Intent camerIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(camerIntent, Permissinos.CAMERA_REQUEST_CODE);
        });

        mChoosePhote.setOnClickListener((v) -> {

            Intent choosePic = new Intent(Intent.ACTION_GET_CONTENT);
            choosePic.setType("image/*");
            startActivityForResult(choosePic, Permissinos.CHOOSE_PHOTO_REQUEST_CODE);


        });
        mCancle.setOnClickListener((v) -> {
                    getDialog().dismiss();
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == Permissinos.CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            onPhotoRecievedListenner.getBitMapImage(bitmap);
            this.dismiss();
        }


        if(requestCode == Permissinos.CHOOSE_PHOTO_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            Uri selectedPhto = data.getData();
            onPhotoRecievedListenner.getImagePath(selectedPhto);
            this.dismiss();
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
         onPhotoRecievedListenner = (OnPhotoRecievedListenner) getTargetFragment();
        }catch (ClassCastException e){
            Log.d(TAG, "onAttach: "+e.getMessage());
        }
    }
}
