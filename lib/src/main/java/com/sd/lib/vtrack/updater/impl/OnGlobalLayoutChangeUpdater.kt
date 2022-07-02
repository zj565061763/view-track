package com.sd.lib.vtrack.updater.impl

import android.view.ViewTreeObserver
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import com.sd.lib.vtrack.updater.ViewTreeObserverUpdater

/**
 * 通过[ViewTreeObserver.OnGlobalLayoutListener]来实现更新
 */
open class OnGlobalLayoutChangeUpdater : ViewTreeObserverUpdater() {

    final override fun register(observer: ViewTreeObserver) {
        observer.addOnGlobalLayoutListener(_listener)
    }

    final override fun unregister(observer: ViewTreeObserver) {
        observer.removeOnGlobalLayoutListener(_listener)
    }

    private val _listener = OnGlobalLayoutListener { notifyUpdatable() }
}