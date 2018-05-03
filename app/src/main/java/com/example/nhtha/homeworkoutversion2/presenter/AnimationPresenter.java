package com.example.nhtha.homeworkoutversion2.presenter;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by nhtha on 16-Jan-18.
 */

public class AnimationPresenter {

    private Context context;
    AssetManager assetManager;

    public AnimationPresenter(Context context) {
        this.context = context;
        assetManager = context.getAssets();
    }

    public AnimationDrawable getAnimationDrawableForMovement(String movementName) {
        AnimationDrawable animationDrawable = new AnimationDrawable();

        Drawable drawable;
        InputStream inputStream;

        try {
            String[] imagePath = assetManager.list(movementName);

            for (int i = 0; i < imagePath.length; i++) {
                inputStream = assetManager.open(movementName + "/" + imagePath[i]);

                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                drawable = new BitmapDrawable(context.getResources(), bitmap);

                animationDrawable.addFrame(drawable, 800);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return animationDrawable;
    }

    public String[] getAllMovementsNameInExercise(String exerciseName) {
        String[] list = null;

        try {
            list = assetManager.list(exerciseName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }

}
