package com.sd.lib.vtrack.updater.impl

import android.view.View
import android.view.View.OnLayoutChangeListener
import com.sd.lib.vtrack.updater.BaseViewUpdater

/**
 * 通过[View.OnLayoutChangeListener]来实现更新
 */
open class OnLayoutChangeUpdater : BaseViewUpdater() {

    final override fun startImpl(view: View): Boolean {
        view.addOnLayoutChangeListener(_listener)
        return true
    }

    final override fun stopImpl(view: View) {
        view.removeOnLayoutChangeListener(_listener)
    }

    private val _listener = OnLayoutChangeListener { _, _, _, _, _, _, _, _, _ -> notifyUpdatable() }
}