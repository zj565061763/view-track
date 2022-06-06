package com.sd.lib.vtrack.updater

import android.view.View
import com.sd.lib.vtrack.updater.ViewUpdater.Updatable
import java.lang.ref.WeakReference

abstract class BaseViewUpdater : ViewUpdater {
    private var _viewRef: WeakReference<View>? = null
    private var _updatable: Updatable? = null

    final override fun setUpdatable(updatable: Updatable?) {
        _updatable = updatable
    }

    final override fun notifyUpdatable() {
        if (isStarted) {
            _updatable?.update()
        }
    }

    final override var view: View?
        get() = _viewRef?.get()
        set(value) {
            val old = _viewRef?.get()
            if (old !== value) {
                stop()
                _viewRef = if (value == null) null else WeakReference(value)
                onViewChanged(old, value)
            }
        }

    final override var isStarted: Boolean = false
        get() {
            if (view == null) isStarted = false
            return field
        }
        private set(value) {
            if (field != value) {
                field = value
                onStateChanged(value)
            }
        }

    final override fun start(): Boolean {
        if (isStarted) return true
        val view = view ?: return false
        return startImpl(view).also { isStarted = it }
    }

    final override fun stop() {
        if (isStarted) {
            view?.let { stopImpl(it) }
            isStarted = false
        }
    }

    protected open fun onViewChanged(oldView: View?, newView: View?) {}

    protected open fun onStateChanged(started: Boolean) {}

    /**
     * 开始监听
     *
     * @return true-成功开始
     */
    protected abstract fun startImpl(view: View): Boolean

    /**
     * 停止监听
     */
    protected abstract fun stopImpl(view: View)
}