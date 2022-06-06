package com.sd.lib.vtrack.ext

import android.view.View
import com.sd.lib.vtrack.tracker.FViewTracker
import com.sd.lib.vtrack.tracker.ViewTracker
import com.sd.lib.vtrack.updater.ViewUpdater
import com.sd.lib.vtrack.updater.ViewUpdater.Updatable
import com.sd.lib.vtrack.updater.impl.OnLayoutChangeUpdater

/**
 * 位置跟踪
 */
open class FPositionTracker {
    private val _tracker: ViewTracker = FViewTracker()

    private val _sourceUpdater: ViewUpdater by lazy {
        createSourceUpdater().apply {
            setUpdatable(_updatable)
        }
    }

    private val _targetUpdater: ViewUpdater by lazy {
        createTargetUpdater().apply {
            setUpdatable(_updatable)
        }
    }

    private val _updatable = Updatable {
        _tracker.update()
    }

    /**
     * 设置回调对象
     */
    fun setCallback(callback: ViewTracker.Callback?) {
        _tracker.setCallback(callback)
    }

    /**
     * 设置源View
     */
    fun setSource(view: View?) {
        _tracker.source = view
        _sourceUpdater.view = view
    }

    /**
     * 设置目标View
     */
    fun setTarget(view: View?) {
        _tracker.target = view
        _targetUpdater.view = view
    }

    /**
     * 设置追踪位置
     */
    fun setPosition(position: ViewTracker.Position) {
        _tracker.position = position
    }

    /**
     * 开始追踪
     */
    fun start() {
        _sourceUpdater.start()
        _targetUpdater.start()
        _tracker.update()
    }

    /**
     * 停止追踪
     */
    fun stop() {
        _sourceUpdater.stop()
        _targetUpdater.stop()
    }

    /**
     * 创建源View更新对象
     */
    protected open fun createSourceUpdater(): ViewUpdater {
        return OnLayoutChangeUpdater()
    }

    /**
     * 创建目标View更新对象
     */
    protected open fun createTargetUpdater(): ViewUpdater {
        return OnLayoutChangeUpdater()
    }
}