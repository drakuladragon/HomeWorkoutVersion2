package com.example.nhtha.homeworkoutversion2.callback;

import android.graphics.Bitmap;

public interface ImageLoaderCallBack {
    void onLoadSucces(Bitmap bitmap);
    void onFail();
}
