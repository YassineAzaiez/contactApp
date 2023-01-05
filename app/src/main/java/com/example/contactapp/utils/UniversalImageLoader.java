package com.example.contactapp.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.contactapp.R;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * Created by yassine 30/12/19 .
 */
public class UniversalImageLoader {
    public static final int DEFAULT = R.drawable.ic_default;
    private Context mContext;

    public UniversalImageLoader(Context context) {
        this.mContext = context;
    }

    public ImageLoaderConfiguration getConfiguration(){
        DisplayImageOptions defaultOption = new DisplayImageOptions.Builder()
                .showImageOnLoading(DEFAULT)
                .showImageForEmptyUri(DEFAULT)
                .showImageOnFail(DEFAULT)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .resetViewBeforeLoading(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300))
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(mContext)
                .defaultDisplayImageOptions(defaultOption)
                .memoryCache(new WeakMemoryCache())
                .diskCacheSize(2*1024*1024)
                .build();
        return config;
    }

    public static void loadCOntactImage(String ulr , ImageView imageView, String append){
       ImageLoader.getInstance().displayImage(append +ulr, imageView, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {


            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });
    }

}
