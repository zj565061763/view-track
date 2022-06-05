package com.sd.lib.vtrack.tracker.location;

import android.view.View;
import android.view.ViewParent;

import androidx.annotation.Nullable;

import com.sd.lib.vtrack.tracker.ViewTracker;

public class WeakSourceViewLocationInfo extends WeakViewLocationInfo implements ViewTracker.SourceLocationInfo {
    @Nullable
    @Override
    public ViewTracker.LocationInfo getParent() {
        final View view = getView();
        if (view == null) return null;

        final ViewParent parent = view.getParent();
        if (!(parent instanceof View)) return null;

        final WeakViewLocationInfo locationInfo = new WeakSourceViewLocationInfo();
        locationInfo.setView((View) parent);
        return locationInfo;
    }
}
