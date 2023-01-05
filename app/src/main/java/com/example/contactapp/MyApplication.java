package com.example.contactapp;

import android.app.Activity;
import android.app.Application;

import com.example.contactapp.utils.UniversalImageLoader;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by yassine 30/12/19 .
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ImageLoader.getInstance().init(new UniversalImageLoader(this).getConfiguration());
    }


}
