package com.example.nhtha.homeworkoutversion2.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.example.nhtha.homeworkoutversion2.callback.ImageLoaderCallBack;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class ImageLoader {

    private ImageLoaderCallBack callBack;
    private Context context;
    private String imageUrl;

    public ImageLoader(Context context,String imageUrl,ImageLoaderCallBack callBack){
        this.context = context;
        this.imageUrl = imageUrl;
        this.callBack = callBack;
        loadImage();
    }

    public void loadImage(){
        Picasso.with(context).load(imageUrl).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                if (bitmap == null){
                    callBack.onFail();
                }
                callBack.onLoadSucces(bitmap);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                callBack.onFail();
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });
    }
}
