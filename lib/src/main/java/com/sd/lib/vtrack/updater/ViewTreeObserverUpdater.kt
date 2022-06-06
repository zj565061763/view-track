package com.sd.lib.vtrack.updater

import android.view.View
import android.view.View.OnAttachStateChangeListener
import android.view.ViewTreeObserver

abstract class ViewTreeObserverUpdater : BaseViewUpdater() {

    override fun onStateChanged(started: Boolean) {
        super.onStateChanged(started)
        view?.let { view ->
            view.removeOnAttachStateChangeListener(_onAttachStateChangeListener)
            if (started) {
                view.addOnAttachStateChangeListener(_onAttachStateChangeListener)
            }
        }
    }

    private val _onAttachStateChangeListener: OnAttachStateChangeListener = object : OnAttachStateChangeListener {
        override fun onViewAttachedToWindow(v: View) {
            if (v === view) {
                startImpl(v)
            }
        }

        override fun onViewDetachedFromWindow(v: View) {
            if (v === view) {
                stopImpl(v)
            }
        }
    }

    final override fun startImpl(view: View): Boolean {
        val observer = view.viewTreeObserver ?: return false
        if (observer.isAlive) {
            unregister(observer)
            register(observer)
            return true
        }
        return false
    }

    final override fun stopImpl(view: View) {
        val observer = view.viewTreeObserver ?: return
        if (observer.isAlive) {
            unregister(observer)
        }
    }

    /**
     * 注册监听
     */
    protected abstract fun register(observer: ViewTreeObserver)

    /**
     * 取消注册监听
     */
    protected abstract fun unregister(observer: ViewTreeObserver)
}