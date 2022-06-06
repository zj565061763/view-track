package com.sd.lib.vtrack.tracker

import android.view.View
import com.sd.lib.vtrack.tracker.ViewTracker.SourceLocationInfo
import com.sd.lib.vtrack.tracker.ViewTracker.ViewLocationInfo
import com.sd.lib.vtrack.tracker.location.WeakSourceViewLocationInfo
import com.sd.lib.vtrack.tracker.location.WeakViewLocationInfo

/**
 * 位置追踪
 */
class FViewTracker : ViewTracker {
    private val _locationSourceParent = intArrayOf(0, 0)
    private val _locationTarget = intArrayOf(0, 0)

    private var _x: Int? = 0
    private var _y: Int? = 0

    override var sourceLocationInfo: SourceLocationInfo? = null
    override var targetLocationInfo: ViewTracker.LocationInfo? = null
    override var position = ViewTracker.Position.TopRight

    private var _callback: ViewTracker.Callback? = null

    override fun setCallback(callback: ViewTracker.Callback?) {
        _callback = callback
    }

    override var source: View?
        get() {
            val info = sourceLocationInfo ?: return null
            return if (info is ViewLocationInfo) info.view else null
        }
        set(value) {
            val info = sourceLocationInfo
            if (info is ViewLocationInfo) {
                info.view = value
            } else {
                sourceLocationInfo = object : WeakSourceViewLocationInfo() {
                    override fun onViewChanged(old: View?, view: View?) {
                        super.onViewChanged(old, view)
                        _callback?.onSourceChanged(old, value)
                    }
                }.also { it.view = value }
            }
        }

    override var target: View?
        get() {
            val info = targetLocationInfo ?: return null
            return if (info is ViewLocationInfo) info.view else null
        }
        set(value) {
            val info = targetLocationInfo
            if (info is ViewLocationInfo) {
                info.view = source
            } else {
                targetLocationInfo = object : WeakViewLocationInfo() {
                    override fun onViewChanged(old: View?, view: View?) {
                        super.onViewChanged(old, view)
                        _callback?.onTargetChanged(old, view)
                    }
                }.also { it.view = value }
            }
        }

    override fun update(): Boolean {
        val callback = _callback ?: return false

        // check null
        val source = sourceLocationInfo ?: return false
        val target = targetLocationInfo ?: return false

        // check canUpdate
        if (!callback.canUpdate(source, target)) {
            return false
        }

        // check isReady
        if (!source.isReady) return false
        if (!target.isReady) return false

        // check parent
        val sourceParent = source.parentLocationInfo ?: return false
        if (!sourceParent.isReady) return false

        sourceParent.getCoordinate(_locationSourceParent)
        target.getCoordinate(_locationTarget)

        when (position) {
            ViewTracker.Position.TopLeft -> layoutTopLeft(source, target)
            ViewTracker.Position.TopCenter -> layoutTopCenter(source, target)
            ViewTracker.Position.TopRight -> layoutTopRight(source, target)

            ViewTracker.Position.LeftCenter -> layoutLeftCenter(source, target)
            ViewTracker.Position.Center -> layoutCenter(source, target)
            ViewTracker.Position.RightCenter -> layoutRightCenter(source, target)

            ViewTracker.Position.BottomLeft -> layoutBottomLeft(source, target)
            ViewTracker.Position.BottomCenter -> layoutBottomCenter(source, target)
            ViewTracker.Position.BottomRight -> layoutBottomRight(source, target)

            ViewTracker.Position.Left -> layoutLeft(source, target)
            ViewTracker.Position.Top -> layoutTop(source, target)
            ViewTracker.Position.Right -> layoutRight(source, target)
            ViewTracker.Position.Bottom -> layoutBottom(source, target)
        }

        callback.onUpdate(_x, _y, source, target)
        return true
    }

    private fun getXAlignLeft(): Int {
        return _locationTarget[0] - _locationSourceParent[0]
    }

    private fun getXAlignRight(source: ViewTracker.LocationInfo, target: ViewTracker.LocationInfo): Int {
        return getXAlignLeft() + (target.width - source.width)
    }

    private fun getXAlignCenter(source: ViewTracker.LocationInfo, target: ViewTracker.LocationInfo): Int {
        return getXAlignLeft() + (target.width - source.width) / 2
    }

    private fun getYAlignTop(): Int {
        return _locationTarget[1] - _locationSourceParent[1]
    }

    private fun getYAlignBottom(source: ViewTracker.LocationInfo, target: ViewTracker.LocationInfo): Int {
        return getYAlignTop() + (target.height - source.height)
    }

    private fun getYAlignCenter(source: ViewTracker.LocationInfo, target: ViewTracker.LocationInfo): Int {
        return getYAlignTop() + (target.height - source.height) / 2
    }

    //---------- position start----------

    private fun layoutTopLeft(source: ViewTracker.LocationInfo, target: ViewTracker.LocationInfo) {
        _x = getXAlignLeft()
        _y = getYAlignTop()
    }

    private fun layoutTopCenter(source: ViewTracker.LocationInfo, target: ViewTracker.LocationInfo) {
        _x = getXAlignCenter(source, target)
        _y = getYAlignTop()
    }

    private fun layoutTopRight(source: ViewTracker.LocationInfo, target: ViewTracker.LocationInfo) {
        _x = getXAlignRight(source, target)
        _y = getYAlignTop()
    }

    private fun layoutLeftCenter(source: ViewTracker.LocationInfo, target: ViewTracker.LocationInfo) {
        _x = getXAlignLeft()
        _y = getYAlignCenter(source, target)
    }

    private fun layoutCenter(source: ViewTracker.LocationInfo, target: ViewTracker.LocationInfo) {
        _x = getXAlignCenter(source, target)
        _y = getYAlignCenter(source, target)
    }

    private fun layoutRightCenter(source: ViewTracker.LocationInfo, target: ViewTracker.LocationInfo) {
        _x = getXAlignRight(source, target)
        _y = getYAlignCenter(source, target)
    }

    private fun layoutBottomLeft(source: ViewTracker.LocationInfo, target: ViewTracker.LocationInfo) {
        _x = getXAlignLeft()
        _y = getYAlignBottom(source, target)
    }

    private fun layoutBottomCenter(source: ViewTracker.LocationInfo, target: ViewTracker.LocationInfo) {
        _x = getXAlignCenter(source, target)
        _y = getYAlignBottom(source, target)
    }

    private fun layoutBottomRight(source: ViewTracker.LocationInfo, target: ViewTracker.LocationInfo) {
        _x = getXAlignRight(source, target)
        _y = getYAlignBottom(source, target)
    }

    private fun layoutLeft(source: ViewTracker.LocationInfo, target: ViewTracker.LocationInfo) {
        _x = getXAlignLeft()
        _y = null
    }

    private fun layoutTop(source: ViewTracker.LocationInfo, target: ViewTracker.LocationInfo) {
        _x = null
        _y = getYAlignTop()
    }

    private fun layoutRight(source: ViewTracker.LocationInfo, target: ViewTracker.LocationInfo) {
        _x = getXAlignRight(source, target)
        _y = null
    }

    private fun layoutBottom(source: ViewTracker.LocationInfo, target: ViewTracker.LocationInfo) {
        _x = null
        _y = getYAlignBottom(source, target)
    }
}