package com.sd.lib.vtrack.tracker

import android.view.View
import com.sd.lib.vtrack.tracker.ViewTracker.*
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

    override var position = Position.TopRight
    override var sourceLocationInfo: SourceLocationInfo? = null
    override var targetLocationInfo: LocationInfo? = null
    override var callback: Callback? = null

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
                        callback?.onSourceChanged(old, view)
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
                info.view = value
            } else {
                targetLocationInfo = object : WeakViewLocationInfo() {
                    override fun onViewChanged(old: View?, view: View?) {
                        super.onViewChanged(old, view)
                        callback?.onTargetChanged(old, view)
                    }
                }.also { it.view = value }
            }
        }

    override fun update(): Boolean {
        // check null
        val callback = callback ?: return false
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
            Position.TopLeft -> layoutTopLeft(source, target)
            Position.TopCenter -> layoutTopCenter(source, target)
            Position.TopRight -> layoutTopRight(source, target)

            Position.LeftCenter -> layoutLeftCenter(source, target)
            Position.Center -> layoutCenter(source, target)
            Position.RightCenter -> layoutRightCenter(source, target)

            Position.BottomLeft -> layoutBottomLeft(source, target)
            Position.BottomCenter -> layoutBottomCenter(source, target)
            Position.BottomRight -> layoutBottomRight(source, target)

            Position.Left -> layoutLeft(source, target)
            Position.Top -> layoutTop(source, target)
            Position.Right -> layoutRight(source, target)
            Position.Bottom -> layoutBottom(source, target)
        }

        callback.onUpdate(_x, _y, source, target)
        return true
    }

    private fun getXAlignLeft(): Int {
        return _locationTarget[0] - _locationSourceParent[0]
    }

    private fun getXAlignRight(source: LocationInfo, target: LocationInfo): Int {
        return getXAlignLeft() + (target.width - source.width)
    }

    private fun getXAlignCenter(source: LocationInfo, target: LocationInfo): Int {
        return getXAlignLeft() + (target.width - source.width) / 2
    }

    private fun getYAlignTop(): Int {
        return _locationTarget[1] - _locationSourceParent[1]
    }

    private fun getYAlignBottom(source: LocationInfo, target: LocationInfo): Int {
        return getYAlignTop() + (target.height - source.height)
    }

    private fun getYAlignCenter(source: LocationInfo, target: LocationInfo): Int {
        return getYAlignTop() + (target.height - source.height) / 2
    }

    //---------- position start----------

    private fun layoutTopLeft(source: LocationInfo, target: LocationInfo) {
        _x = getXAlignLeft()
        _y = getYAlignTop()
    }

    private fun layoutTopCenter(source: LocationInfo, target: LocationInfo) {
        _x = getXAlignCenter(source, target)
        _y = getYAlignTop()
    }

    private fun layoutTopRight(source: LocationInfo, target: LocationInfo) {
        _x = getXAlignRight(source, target)
        _y = getYAlignTop()
    }

    private fun layoutLeftCenter(source: LocationInfo, target: LocationInfo) {
        _x = getXAlignLeft()
        _y = getYAlignCenter(source, target)
    }

    private fun layoutCenter(source: LocationInfo, target: LocationInfo) {
        _x = getXAlignCenter(source, target)
        _y = getYAlignCenter(source, target)
    }

    private fun layoutRightCenter(source: LocationInfo, target: LocationInfo) {
        _x = getXAlignRight(source, target)
        _y = getYAlignCenter(source, target)
    }

    private fun layoutBottomLeft(source: LocationInfo, target: LocationInfo) {
        _x = getXAlignLeft()
        _y = getYAlignBottom(source, target)
    }

    private fun layoutBottomCenter(source: LocationInfo, target: LocationInfo) {
        _x = getXAlignCenter(source, target)
        _y = getYAlignBottom(source, target)
    }

    private fun layoutBottomRight(source: LocationInfo, target: LocationInfo) {
        _x = getXAlignRight(source, target)
        _y = getYAlignBottom(source, target)
    }

    private fun layoutLeft(source: LocationInfo, target: LocationInfo) {
        _x = getXAlignLeft()
        _y = null
    }

    private fun layoutTop(source: LocationInfo, target: LocationInfo) {
        _x = null
        _y = getYAlignTop()
    }

    private fun layoutRight(source: LocationInfo, target: LocationInfo) {
        _x = getXAlignRight(source, target)
        _y = null
    }

    private fun layoutBottom(source: LocationInfo, target: LocationInfo) {
        _x = null
        _y = getYAlignBottom(source, target)
    }
}