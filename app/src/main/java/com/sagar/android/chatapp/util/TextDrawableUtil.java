package com.sagar.android.chatapp.util;

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
                                ),
                                ColorGenerator.MATERIAL.getColor(
                                        stringToShow
                                )
                        );
            case RECTANGLE:
                return TextDrawable.builder()
                        .buildRect(
                                String.valueOf(
                                        stringToShow.toCharArray()[0]
                                ),
                                ColorGenerator.MATERIAL.getColor(
                                        stringToShow
                                )
                        );
        }
        return null;
    }
}
