package com.sd.lib.vtrack.tracker.location

import android.view.View
import com.sd.lib.vtrack.tracker.ViewTracker
import com.sd.lib.vtrack.tracker.ViewTracker.SourceLocationInfo

open class WeakSourceViewLocationInfo : WeakViewLocationInfo(), SourceLocationInfo {
    private val _parentInfo = WeakViewLocationInfo()

    override val parentLocationInfo: ViewTracker.LocationInfo?
        get() {
            val view = view ?: return null
            val parent = view.parent ?: return null
            if (parent !is View) return null
            _parentInfo.view = parent
            return _parentInfo
        }
}