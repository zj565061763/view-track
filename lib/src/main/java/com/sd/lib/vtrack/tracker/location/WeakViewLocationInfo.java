package com.sd.lib.vtrack.tracker.location;

import android.view.View;

import androidx.annotation.NonNull;

import com.sd.lib.vtrack.tracker.ViewTracker;

import java.lang.ref.WeakReference;

public class WeakViewLocationInfo implements ViewTracker.ViewLocationInfo {
    private WeakReference<View> mView;
    private final int[] mLocation = {0, 0};

    @Override
    public boolean isReady() {
        final View view = getView();
        if (view == null) return false;
        if (!view.isAttachedToWindow()) return false;
        return view.getWidth() > 0 && view.getHeight() > 0;
    }

    @Override
    public View getView() {
        return mView == null ? null : mView.get();
    }

    @Override
    public void setView(View view) {
        final View old = getView();
        if (old != view) {
            mView = view == null ? null : new WeakReference<>(view);
            onViewChanged(old, view);
        }
    }

    @Override
    public int getWidth() {
        final View view = getView();
        return view == null ? 0 : view.getWidth();
    }

    @Override
    public int getHeight() {
        final View view = getView();
        return view == null ? 0 : view.getHeight();
    }

    @Override
    public void getCoordinate(@NonNull int[] position) {
        final View view = getView();
        if (view == null) return;
        view.getLocationOnScreen(position);
    }

    protected void onViewChanged(View old, View view) {
    }
}
