package com.sd.lib.vtrack.updater.impl;

import android.view.ViewTreeObserver;

import androidx.annotation.NonNull;

import com.sd.lib.vtrack.updater.ViewTreeObserverUpdater;

/**
 * 通过{@link ViewTreeObserver.OnPreDrawListener}来实现更新
 */
public class OnPreDrawUpdater extends ViewTreeObserverUpdater {
    @Override
    protected final void register(@NonNull ViewTreeObserver observer) {
        observer.addOnPreDrawListener(mListener);
    }

    @Override
    protected final void unregister(@NonNull ViewTreeObserver observer) {
        observer.removeOnPreDrawListener(mListener);
    }

    private final ViewTreeObserver.OnPreDrawListener mListener = new ViewTreeObserver.OnPreDrawListener() {
        @Override
        public boolean onPreDraw() {
            notifyUpdatable();
            return true;
        }
    };
}