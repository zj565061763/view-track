package com.sd.lib.vtrack.updater.impl;

import android.view.ViewTreeObserver;

import androidx.annotation.NonNull;

import com.sd.lib.vtrack.updater.ViewTreeObserverUpdater;

/**
 * 通过{@link ViewTreeObserver.OnGlobalLayoutListener}来实现更新
 */
public class OnGlobalLayoutChangeUpdater extends ViewTreeObserverUpdater {
    @Override
    protected final void register(@NonNull ViewTreeObserver observer) {
        observer.addOnGlobalLayoutListener(mListener);
    }

    @Override
    protected final void unregister(@NonNull ViewTreeObserver observer) {
        observer.removeOnGlobalLayoutListener(mListener);
    }

    private final ViewTreeObserver.OnGlobalLayoutListener mListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            notifyUpdatable();
        }
    };
}