package com.sd.lib.vtrack.updater

import android.view.View

interface ViewUpdater {
    /**
     * 设置更新对象
     */
    fun setUpdatable(updatable: Updatable?)

    /**
     * 通知更新对象[setUpdatable]
     */
    fun notifyUpdatable()

    /**
     * 监听的view
     */
    var view: View?

    /**
     * 是否已经开始监听
     *
     * @return true-已经开始
     */
    val isStarted: Boolean

    /**
     * 开始监听
     *
     * @return true-已经开始
     */
    fun start(): Boolean

    /**
     * 停止监听
     */
    fun stop()

    fun interface Updatable {
        /**
         * 更新回调
         */
        fun update()
    }
}