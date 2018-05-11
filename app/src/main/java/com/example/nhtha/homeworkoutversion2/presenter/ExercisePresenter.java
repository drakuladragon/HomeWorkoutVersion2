package com.example.nhtha.homeworkoutversion2.presenter;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.example.nhtha.homeworkoutversion2.model.Movement;
import com.example.nhtha.homeworkoutversion2.presenter.db.DatabasePresenter;

import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * Created by nhtha on 16-Jan-18.
 */

public class ExercisePresenter extends DatabasePresenter {
    public static final String RESOURCE_FILE_NAME = "action_image";

    public static final String CHEST_BEGINER = "chest_beginer";
    public static final String CHEST_INTERMEDIATE = "chest_intermediate";
    public static final String CHEST_ADVANCE = "chest_advance";

    public static final String ABS_BEGINNER = "abs_beginner";
    public static final String ABS_INTERMEDIATE = "abs_intermediate";
    public static final String ABS_ADVANCE = "abs_advanced";

    public static final String SB_BEGINNER = "shoulder_back_beginner";
    public static final String SB_INTERMEDIATE = "shoulder_back_intermediate";
    public static final String SB_ADVANCE = "shoulder_back_advanced";

    public static final String LEG_BEGINNER = "leg_beginner";
    public static final String LEG_INTERMEDIATE = "leg_intermediate";
    public static final String LEG_ADVANCE = "leg_advanced";

    private AssetManager assetManager;
    private Drawable drawable;
    private ImageView imgNone;

    public ExercisePresenter(Context context) {
        super(context);
        assetManager = context.getAssets();
        imgNone = new ImageView(context);
    }

    public List<Movement> getExerciseList(String exName) {
        List<Movement> movements = getExercise(exName);

        for (int i = 0; i < movements.size(); i++) {
            String exCode = movements.get(i).getExCode();
            AnimationDrawable drawable = getAnimationDrawableForMovement(RESOURCE_FILE_NAME + File.separator + exCode);
            movements.get(i).setMovementAnimation(drawable);
        }

        return movements;
    }

    public AnimationDrawable getAnimationDrawableForMovement(String movementName) {
        AnimationDrawable animationDrawable = new AnimationDrawable();

        InputStream inputStream;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;

        try {
            String[] imagePath = assetManager.list(movementName);

            for (int i = 0; i < imagePath.length; i++) {
                String path = movementName + "/" + imagePath[i];
                inputStream = assetManager.open(path);

                Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);
                drawable = new BitmapDrawable(context.getResources(), bitmap);

               // drawable.createFromStream(inputStream, null);

                animationDrawable.addFrame(drawable, 800);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return animationDrawable;
    }
}
