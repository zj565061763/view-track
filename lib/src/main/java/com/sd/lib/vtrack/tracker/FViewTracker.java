package com.sd.lib.vtrack.tracker;

import android.view.View;
import android.view.ViewParent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.ref.WeakReference;

/**
 * view的位置追踪
 */
public class FViewTracker implements ViewTracker {
    private WeakReference<View> mSource;
    private WeakReference<View> mTarget;

    private Location mSourceLocation;
    private Location mTargetLocation;

    private final int[] mLocationSourceParent = {0, 0};
    private final int[] mLocationTarget = {0, 0};

    private int mX;
    private int mY;
    private Position mPosition = Position.TopRight;

    private Callback mCallback;

    @Override
    public void setCallback(@Nullable Callback callback) {
        mCallback = callback;
    }

    @Override
    public void setSource(@Nullable View source) {
        final View old = getSource();
        if (old != source) {
            mSource = source == null ? null : new WeakReference<>(source);
            if (mCallback != null) {
                mCallback.onSourceChanged(old, source);
            }
        }
    }

    @Override
    public void setSourceLocation(@Nullable Location location) {
        mSourceLocation = location;
    }

    @Override
    public void setTarget(@Nullable View target) {
        final View old = getTarget();
        if (old != target) {
            mTarget = target == null ? null : new WeakReference<>(target);
            if (mCallback != null) {
                mCallback.onTargetChanged(old, target);
            }
        }
    }

    @Override
    public void setTargetLocation(@Nullable Location location) {
        mTargetLocation = location;
    }

    @Override
    public void setPosition(@NonNull Position position) {
        mPosition = position;
    }

    @Nullable
    @Override
    public View getSource() {
        return mSource == null ? null : mSource.get();
    }

    @Nullable
    @Override
    public View getTarget() {
        return mTarget == null ? null : mTarget.get();
    }

    @Override
    public final boolean update() {
        final Callback callback = mCallback;
        if (callback == null) {
            return false;
        }

        final View source = getSource();
        final View target = getTarget();
        if (source == null || target == null) {
            return false;
        }

        if (!callback.canUpdate(source, target)) {
            return false;
        }

        final ViewParent parent = source.getParent();
        if (!(parent instanceof View)) {
            return false;
        }

        if (!source.isAttachedToWindow() || !target.isAttachedToWindow()) {
            return false;
        }

        if (source.getWidth() <= 0 || source.getHeight() <= 0) {
            return false;
        }

        ((View) parent).getLocationOnScreen(mLocationSourceParent);
        target.getLocationOnScreen(mLocationTarget);

        switch (mPosition) {
            case TopLeft:
                layoutTopLeft(source, target);
                break;
            case TopCenter:
                layoutTopCenter(source, target);
                break;
            case TopRight:
                layoutTopRight(source, target);
                break;

            case LeftCenter:
                layoutLeftCenter(source, target);
                break;
            case Center:
                layoutCenter(source, target);
                break;
            case RightCenter:
                layoutRightCenter(source, target);
                break;

            case BottomLeft:
                layoutBottomLeft(source, target);
                break;
            case BottomCenter:
                layoutBottomCenter(source, target);
                break;
            case BottomRight:
                layoutBottomRight(source, target);
                break;

            case Left:
                layoutLeft(source, target);
                break;
            case Top:
                layoutTop(source, target);
                break;
            case Right:
                layoutRight(source, target);
                break;
            case Bottom:
                layoutBottom(source, target);
                break;
        }

        callback.onUpdate(mX, mY, source, target);
        return true;
    }

    private int getX_alignLeft() {
        return mLocationTarget[0] - mLocationSourceParent[0];
    }

    private int getX_alignRight(View source, View target) {
        return getX_alignLeft() + (target.getWidth() - source.getWidth());
    }

    private int getX_alignCenter(View source, View target) {
        return getX_alignLeft() + (target.getWidth() - source.getWidth()) / 2;
    }

    private int getY_alignTop() {
        return mLocationTarget[1] - mLocationSourceParent[1];
    }

    private int getY_alignBottom(View source, View target) {
        return getY_alignTop() + (target.getHeight() - source.getHeight());
    }

    private int getY_alignCenter(View source, View target) {
        return getY_alignTop() + (target.getHeight() - source.getHeight()) / 2;
    }

    //---------- position start----------

    private void layoutTopLeft(View source, View target) {
        mX = getX_alignLeft();
        mY = getY_alignTop();
    }

    private void layoutTopCenter(View source, View target) {
        mX = getX_alignCenter(source, target);
        mY = getY_alignTop();
    }

    private void layoutTopRight(View source, View target) {
        mX = getX_alignRight(source, target);
        mY = getY_alignTop();
    }


    private void layoutLeftCenter(View source, View target) {
        mX = getX_alignLeft();
        mY = getY_alignCenter(source, target);
    }

    private void layoutCenter(View source, View target) {
        mX = getX_alignCenter(source, target);
        mY = getY_alignCenter(source, target);
    }

    private void layoutRightCenter(View source, View target) {
        mX = getX_alignRight(source, target);
        mY = getY_alignCenter(source, target);
    }


    private void layoutBottomLeft(View source, View target) {
        mX = getX_alignLeft();
        mY = getY_alignBottom(source, target);
    }

    private void layoutBottomCenter(View source, View target) {
        mX = getX_alignCenter(source, target);
        mY = getY_alignBottom(source, target);
    }

    private void layoutBottomRight(View source, View target) {
        mX = getX_alignRight(source, target);
        mY = getY_alignBottom(source, target);
    }


    private void layoutLeft(View source, View target) {
        mX = getX_alignLeft();
        mY = source.getTop();
    }

    private void layoutTop(View source, View target) {
        mX = source.getLeft();
        mY = getY_alignTop();
    }

    private void layoutRight(View source, View target) {
        mX = getX_alignRight(source, target);
        mY = source.getTop();
    }

    private void layoutBottom(View source, View target) {
        mX = source.getLeft();
        mY = getY_alignBottom(source, target);
    }

    //---------- position end----------
}