package com.sagar.android.chatapp.ui.dashboard.behaviour;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class FabBehaviour extends FloatingActionButton.Behavior {

    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private float posX, posY = 0;

    public FabBehaviour(@SuppressWarnings("unused") Context context, @SuppressWarnings("unused") AttributeSet attrs) {
        super();
    }

    @Override
    public boolean layoutDependsOn(@NonNull CoordinatorLayout parent, @NonNull FloatingActionButton child, @NonNull View dependency) {
        if (posY == 0) {
            posX = child.getX();
            posY = child.getY();
        }
        return super.layoutDependsOn(parent, child, dependency);
    }

    @Override
    public boolean onStartNestedScroll(
            @NonNull CoordinatorLayout coordinatorLayout,
            @NonNull FloatingActionButton child,
            @NonNull View directTargetChild,
            @NonNull View target,
            int axes,
            int type
    ) {
        if (axes == ViewCompat.SCROLL_AXIS_VERTICAL)
            return true;
        return super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, axes, type);
    }

    @Override
    public void onNestedScroll(
            @NonNull CoordinatorLayout coordinatorLayout,
            @NonNull FloatingActionButton child,
            @NonNull View target,
            int dxConsumed,
            int dyConsumed,
            int dxUnconsumed,
            int dyUnconsumed,
            int type
    ) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type);
        if (dyConsumed > 0) {
            child.animate().y(posY + ((float) (target.getHeight() + child.getHeight())));
        } else if (dyConsumed < 0) {
            child.animate().y(posY);
        }
    }
}