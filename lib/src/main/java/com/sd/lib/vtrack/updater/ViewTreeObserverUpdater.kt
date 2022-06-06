package com.sd.lib.vtrack.updater;

import android.view.View;
import android.view.ViewTreeObserver;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;

public abstract class ViewTreeObserverUpdater extends BaseViewUpdater {
    @CallSuper
    @Override
    protected void onStateChanged(boolean started) {
        super.onStateChanged(started);
        final View view = getView();
        if (view != null) {
            view.removeOnAttachStateChangeListener(mOnAttachStateChangeListener);
            if (started) {
                view.addOnAttachStateChangeListener(mOnAttachStateChangeListener);
            }
        }
    }

    private final View.OnAttachStateChangeListener mOnAttachStateChangeListener = new View.OnAttachStateChangeListener() {
        @Override
        public void onViewAttachedToWindow(View v) {
            if (v == getView()) {
                startImpl(v);
            }
        }

        @Override
        public void onViewDetachedFromWindow(View v) {
            if (v == getView()) {
                stopImpl(v);
            }
        }
    };

    @Override
    protected final boolean startImpl(@NonNull View view) {
        final ViewTreeObserver observer = view.getViewTreeObserver();
        if (observer.isAlive()) {
            unregister(observer);
            register(observer);
            return true;
        }
        return false;
    }

    @Override
    protected final void stopImpl(@NonNull View view) {
        final ViewTreeObserver observer = view.getViewTreeObserver();
        if (observer.isAlive()) {
            unregister(observer);
        }
    }

    /**
     * 注册监听
     */
    protected abstract void register(@NonNull ViewTreeObserver observer);

    /**
     * 取消注册监听
     */
    protected abstract void unregister(@NonNull ViewTreeObserver observer);
}