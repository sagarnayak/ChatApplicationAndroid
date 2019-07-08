package com.sagar.android.chatapp.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

/*
created by SAGAR KUMAR NAYAK
class to create place holder for image views.

dependencies -

project level -
maven { url 'http://dl.bintray.com/amulyakhare/maven' }

app level gradle -
implementation 'com.amulyakhare:com.amulyakhare.textdrawable:1.0.1'

how to use -
to ge the place holder call the getPlaceHolder method with the string to create
the image and the shape you want to create.
 */
public class TextDrawableUtil {
    public enum Shape {
        ROUND,
        RECTANGLE
    }

    public static Drawable getPlaceHolder(String stringToShow, Shape shape) {
        switch (shape) {
            case ROUND:
                return TextDrawable.builder()
                        .buildRound(
                                String.valueOf(
                                        stringToShow.toCharArray()[0]
                                ).toUpperCase(),
                                ColorGenerator.MATERIAL.getColor(
                                        stringToShow
                                )
                        );
            case RECTANGLE:
                return TextDrawable.builder()
                        .buildRect(
                                String.valueOf(
                                        stringToShow.toCharArray()[0]
                                ).toUpperCase(),
                                ColorGenerator.MATERIAL.getColor(
                                        stringToShow
                                )
                        );
        }
        return null;
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }


        int width = drawable.getIntrinsicWidth();
        width = width > 0 ? width : 50; // Replaced the 1 by a 96
        int height = drawable.getIntrinsicHeight();
        height = height > 0 ? height : 50; // Replaced the 1 by a 96

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }
}
