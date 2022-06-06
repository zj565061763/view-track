package com.sd.lib.vtrack.updater;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.ref.WeakReference;

public abstract class BaseViewUpdater implements ViewUpdater {
    private boolean mIsStarted;
    private Updatable mUpdatable;
    private WeakReference<View> mView;

    @Override
    public final void setUpdatable(@Nullable Updatable updatable) {
        mUpdatable = updatable;
    }

    @Override
    public final void notifyUpdatable() {
        if (isStarted()) {
            if (mUpdatable != null) {
                mUpdatable.update();
            }
        }
    }

    @Nullable
    @Override
    public final View getView() {
        return mView == null ? null : mView.get();
    }

    @Override
    public final void setView(@Nullable View view) {
        final View old = getView();
        if (old != view) {
            stop();
            mView = view == null ? null : new WeakReference<>(view);
            onViewChanged(old, view);
        }
    }

    @Override
    public final boolean isStarted() {
        if (getView() == null) {
            setStarted(false);
        }
        return mIsStarted;
    }

    @Override
    public final boolean start() {
        if (isStarted()) {
            return true;
        }

        final View view = getView();
        if (view == null) {
            return false;
        }

        final boolean startImpl = startImpl(view);
        setStarted(startImpl);

        return mIsStarted;
    }

    @Override
    public final void stop() {
        if (mIsStarted) {
            final View view = getView();
            if (view != null) {
                stopImpl(view);
            }

            setStarted(false);
        }
    }

    private void setStarted(boolean started) {
        if (mIsStarted != started) {
            mIsStarted = started;
            onStateChanged(started);
        }
    }

    protected void onViewChanged(@Nullable View oldView, @Nullable View newView) {
    }

    protected void onStateChanged(boolean started) {
    }

    /**
     * 开始监听
     *
     * @return true-成功开始
     */
    protected abstract boolean startImpl(@NonNull View view);

    /**
     * 停止监听
     */
    protected abstract void stopImpl(@NonNull View view);
}