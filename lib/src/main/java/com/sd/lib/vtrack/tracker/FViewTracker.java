package com.sd.lib.vtrack.tracker;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.sd.lib.vtrack.tracker.location.WeakSourceViewLocationInfo;
import com.sd.lib.vtrack.tracker.location.WeakViewLocationInfo;

/**
 * view的位置追踪
 */
public class FViewTracker implements ViewTracker {
    private SourceLocationInfo mSourceLocationInfo;
    private LocationInfo mTargetLocationInfo;

    private final int[] mLocationSourceParent = {0, 0};
    private final int[] mLocationTarget = {0, 0};

    private Integer mX = 0;
    private Integer mY = 0;
    private Position mPosition = Position.TopRight;

    private Callback mCallback;

    @Override
    public void setCallback(@Nullable Callback callback) {
        mCallback = callback;
    }

    @Override
    public void setSource(@Nullable View source) {
        if (!(mSourceLocationInfo instanceof ViewLocationInfo)) {
            mSourceLocationInfo = new WeakSourceViewLocationInfo() {
                @Override
                protected void onViewChanged(View old, View view) {
                    super.onViewChanged(old, view);
                    if (mCallback != null) {
                        mCallback.onSourceChanged(old, source);
                    }
                }
            };
        }
        ((ViewLocationInfo) mSourceLocationInfo).setView(source);
    }

    @Override
    public void setTarget(@Nullable View target) {
        if (!(mTargetLocationInfo instanceof ViewLocationInfo)) {
            mTargetLocationInfo = new WeakViewLocationInfo() {
                @Override
                protected void onViewChanged(View old, View view) {
                    super.onViewChanged(old, view);
                    if (mCallback != null) {
                        mCallback.onTargetChanged(old, view);
                    }
                }
            };
        }
        ((ViewLocationInfo) mTargetLocationInfo).setView(target);
    }

    @Nullable
    @Override
    public View getSource() {
        if (mSourceLocationInfo instanceof ViewLocationInfo) {
            return ((ViewLocationInfo) mSourceLocationInfo).getView();
        }
        return null;
    }

    @Nullable
    @Override
    public View getTarget() {
        if (mTargetLocationInfo instanceof ViewLocationInfo) {
            return ((ViewLocationInfo) mTargetLocationInfo).getView();
        }
        return null;
    }

    @Override
    public void setSourceLocationInfo(SourceLocationInfo locationInfo) {
        mSourceLocationInfo = locationInfo;
    }

    @Override
    public void setTargetLocationInfo(@Nullable LocationInfo locationInfo) {
        mTargetLocationInfo = locationInfo;
    }

    @Override
    public void setPosition(@NonNull Position position) {
        mPosition = position;
    }

    @Override
    public final boolean update() {
        final Callback callback = mCallback;
        if (callback == null) return false;

        // check null
        final SourceLocationInfo source = mSourceLocationInfo;
        final LocationInfo target = mTargetLocationInfo;
        if (source == null) return false;
        if (target == null) return false;

        // check canUpdate
        if (!callback.canUpdate(source, target)) {
            return false;
        }

        // check isReady
        if (!source.isReady()) return false;
        if (!target.isReady()) return false;

        // check parent
        final LocationInfo sourceParent = source.getParentLocationInfo();
        if (sourceParent == null) return false;
        if (!sourceParent.isReady()) return false;

        sourceParent.getCoordinate(mLocationSourceParent);
        target.getCoordinate(mLocationTarget);

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

    private int getX_alignRight(LocationInfo source, LocationInfo target) {
        return getX_alignLeft() + (target.getWidth() - source.getWidth());
    }

    private int getX_alignCenter(LocationInfo source, LocationInfo target) {
        return getX_alignLeft() + (target.getWidth() - source.getWidth()) / 2;
    }

    private int getY_alignTop() {
        return mLocationTarget[1] - mLocationSourceParent[1];
    }

    private int getY_alignBottom(LocationInfo source, LocationInfo target) {
        return getY_alignTop() + (target.getHeight() - source.getHeight());
    }

    private int getY_alignCenter(LocationInfo source, LocationInfo target) {
        return getY_alignTop() + (target.getHeight() - source.getHeight()) / 2;
    }

    //---------- position start----------

    private void layoutTopLeft(LocationInfo source, LocationInfo target) {
        mX = getX_alignLeft();
        mY = getY_alignTop();
    }

    private void layoutTopCenter(LocationInfo source, LocationInfo target) {
        mX = getX_alignCenter(source, target);
        mY = getY_alignTop();
    }

    private void layoutTopRight(LocationInfo source, LocationInfo target) {
        mX = getX_alignRight(source, target);
        mY = getY_alignTop();
    }


    private void layoutLeftCenter(LocationInfo source, LocationInfo target) {
        mX = getX_alignLeft();
        mY = getY_alignCenter(source, target);
    }

    private void layoutCenter(LocationInfo source, LocationInfo target) {
        mX = getX_alignCenter(source, target);
        mY = getY_alignCenter(source, target);
    }

    private void layoutRightCenter(LocationInfo source, LocationInfo target) {
        mX = getX_alignRight(source, target);
        mY = getY_alignCenter(source, target);
    }


    private void layoutBottomLeft(LocationInfo source, LocationInfo target) {
        mX = getX_alignLeft();
        mY = getY_alignBottom(source, target);
    }

    private void layoutBottomCenter(LocationInfo source, LocationInfo target) {
        mX = getX_alignCenter(source, target);
        mY = getY_alignBottom(source, target);
    }

    private void layoutBottomRight(LocationInfo source, LocationInfo target) {
        mX = getX_alignRight(source, target);
        mY = getY_alignBottom(source, target);
    }


    private void layoutLeft(LocationInfo source, LocationInfo target) {
        mX = getX_alignLeft();
        mY = null;
    }

    private void layoutTop(LocationInfo source, LocationInfo target) {
        mX = null;
        mY = getY_alignTop();
    }

    private void layoutRight(LocationInfo source, LocationInfo target) {
        mX = getX_alignRight(source, target);
        mY = null;
    }

    private void layoutBottom(LocationInfo source, LocationInfo target) {
        mX = null;
        mY = getY_alignBottom(source, target);
    }

    //---------- position end----------
}