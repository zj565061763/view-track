package com.sd.lib.vtrack.updater.impl

import android.view.ViewTreeObserver
import com.sd.lib.vtrack.updater.ViewTreeObserverUpdater

/**
 * 通过[ViewTreeObserver.OnPreDrawListener]来实现更新
 */
class OnPreDrawUpdater : ViewTreeObserverUpdater() {

    override fun register(observer: ViewTreeObserver) {
        observer.addOnPreDrawListener(_listener)
    }

    override fun unregister(observer: ViewTreeObserver) {
        observer.removeOnPreDrawListener(_listener)
    }

    private val _listener = ViewTreeObserver.OnPreDrawListener {
        notifyUpdatable()
        true
    }
}