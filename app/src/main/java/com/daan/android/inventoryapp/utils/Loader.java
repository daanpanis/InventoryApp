package com.daan.android.inventoryapp.utils;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.daan.android.inventoryapp.R;

public class Loader {

    private static final int OVERLAY_ID = 981270568;

    public static void showSpinner(Activity activity) {
        if (hasSpinner(activity)) {
            return;
        }
        ViewGroup root = getRoot(activity);

        FrameLayout overlay = buildOverlay(activity);
        ProgressBar progressBar = buildProgressBar(activity);
        overlay.addView(progressBar);

        root.addView(overlay);
    }

    private static boolean hasSpinner(Activity activity) {
        ViewGroup root = getRoot(activity);
        for (int i = 0; i < root.getChildCount(); i++) {
            if (root.getChildAt(i).getId() == OVERLAY_ID) {
                return true;
            }
        }
        return false;
    }

    public static void removeSpinner(Activity activity) {
        ViewGroup root = getRoot(activity);
        for (int i = 0; i < root.getChildCount(); i++) {
            if (root.getChildAt(i).getId() == OVERLAY_ID) {
                root.removeViewAt(i);
                break;
            }
        }
    }

    private static FrameLayout buildOverlay(Activity activity) {
        View root = getRoot(activity);
        FrameLayout overlay = new FrameLayout(activity);
        overlay.setId(OVERLAY_ID);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        View navBarBg;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP &&
                (navBarBg = root.findViewById(android.R.id.navigationBarBackground)) != null) {
            layoutParams.setMargins(0, 0, 0, (int) (root.getHeight() - navBarBg.getY()));
        }
        overlay.setLayoutParams(layoutParams);
        overlay.setClickable(true);
        overlay.setBackgroundResource(R.color.loading_overlay);
        return overlay;
    }

    public static ProgressBar buildProgressBar(Activity activity) {
        ProgressBar progressBar = new ProgressBar(activity);
        int size = activity.getResources().getDimensionPixelSize(R.dimen.loading_circle_size);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(size, size);
        layoutParams.gravity = Gravity.CENTER;
        progressBar.setLayoutParams(layoutParams);
        return progressBar;
    }

    private static ViewGroup getRoot(Activity activity) {
        return (ViewGroup) activity.getWindow().getDecorView();
    }

    private Loader() {
    }
}
