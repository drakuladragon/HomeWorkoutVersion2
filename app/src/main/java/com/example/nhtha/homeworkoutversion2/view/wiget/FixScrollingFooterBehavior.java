package com.example.nhtha.homeworkoutversion2.view.wiget;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.example.nhtha.homeworkoutversion2.R;

/**
 * Created by nhtha on 06-Mar-18.
 */

public class FixScrollingFooterBehavior extends AppBarLayout.ScrollingViewBehavior {
    private AppBarLayout appBarLayout;
    private Context context;

    public FixScrollingFooterBehavior() {
        super();
    }

    public FixScrollingFooterBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {

        if (appBarLayout == null) {
            appBarLayout = (AppBarLayout) dependency;
        }

        final boolean result = super.onDependentViewChanged(parent, child, dependency);
        final int bottomPadding = calculateBottomPadding(appBarLayout);
        final boolean paddingChanged = bottomPadding != child.getPaddingBottom();
        if (paddingChanged) {
            child.setPadding(
                    child.getPaddingLeft(),
                    child.getPaddingTop(),
                    child.getPaddingRight(),
                    bottomPadding);
            child.requestLayout();
        }
        return paddingChanged || result;
    }


    // Calculate the padding needed to keep the bottom of the view pager's content at the same location on the screen.
    private int calculateBottomPadding(AppBarLayout dependency) {
        final int totalScrollRange = dependency.getTotalScrollRange();
        return totalScrollRange + dependency.getTop() + getActionBarHeight();
    }

    private int getActionBarHeight() {
        int actionBarHeight = 0;
        TypedValue tv = new TypedValue();
        if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, context.getResources().getDisplayMetrics());

        if (actionBarHeight == 0 && context.getTheme().resolveAttribute(R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, context.getResources().getDisplayMetrics());
        }
        return actionBarHeight;
    }
}
