package com.example.contactapp.utils;


import android.Manifest;

/**
 * Created by yassine 20/01/20 .
 */
public interface Permissinos {


    String[] PHONE_PERMISSION = {Manifest.permission.CALL_PHONE};
    String[] CAMERA_PERMISSION = {Manifest.permission.READ_EXTERNAL_STORAGE
            , Manifest.permission.WRITE_EXTERNAL_STORAGE
            , Manifest.permission.CAMERA};
    int CAMERA_REQUEST_CODE = 1;
    int CHOOSE_PHOTO_REQUEST_CODE = 2;
}
