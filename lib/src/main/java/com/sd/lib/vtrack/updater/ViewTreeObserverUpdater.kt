package com.sd.lib.vtrack.updater

import android.view.View
import android.view.View.OnAttachStateChangeListener
import android.view.ViewTreeObserver

abstract class ViewTreeObserverUpdater : BaseViewUpdater() {
    private var hasRegister = false

    override fun onViewChanged(old: View?, view: View?) {
        super.onViewChanged(old, view)
        old?.removeOnAttachStateChangeListener(_onAttachStateChangeListener)
        view?.addOnAttachStateChangeListener(_onAttachStateChangeListener)
    }

    private val _onAttachStateChangeListener = object : OnAttachStateChangeListener {
        override fun onViewAttachedToWindow(v: View) {
            if (v == view) {
                if (isStarted && !hasRegister) {
                    startImpl(v)
                }
            }
        }

        override fun onViewDetachedFromWindow(v: View) {
            if (v == view) {
                stopImpl(v)
            }
        }
    }

    final override fun startImpl(view: View): Boolean {
        val observer = view.viewTreeObserver ?: return false
        return if (observer.isAlive) {
            unregister(observer)
            register(observer)
            hasRegister = true
            true
        } else {
            false
        }
    }

    final override fun stopImpl(view: View) {
        val observer = view.viewTreeObserver ?: return
        if (observer.isAlive) {
            unregister(observer)
        }
        hasRegister = false
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