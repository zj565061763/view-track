package com.sd.lib.vtrack.tracker.location

import android.view.View
import com.sd.lib.vtrack.tracker.ViewTracker.ViewLocationInfo
import java.lang.ref.WeakReference

open class WeakViewLocationInfo : ViewLocationInfo {
    private var _viewRef: WeakReference<View>? = null

    override val isReady: Boolean
        get() {
            val view = _viewRef?.get() ?: return false
            if (!view.isAttachedToWindow) return false
            return view.width > 0 && view.height > 0
        }

    override var view: View?
        get() = _viewRef?.get()
        set(value) {
            val old = _viewRef?.get()
            if (old != value) {
                _viewRef = if (value == null) null else WeakReference(value)
                onViewChanged(old, value)
            }
        }

    override val width: Int
        get() = _viewRef?.get()?.width ?: 0

    override val height: Int
        get() = _viewRef?.get()?.height ?: 0

    override fun getCoordinate(position: IntArray) {
        _viewRef?.get()?.getLocationOnScreen(position)
    }

    protected open fun onViewChanged(old: View?, view: View?) {}
}