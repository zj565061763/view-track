package com.sd.lib.vtrack.updater;

import android.view.View;

import androidx.annotation.Nullable;

public interface ViewUpdater {
    /**
     * 设置更新对象
     */
    void setUpdatable(@Nullable Updatable updatable);

    /**
     * 通知更新对象{@link #setUpdatable(Updatable)}
     */
    void notifyUpdatable();

    /**
     * 返回设置的view
     */
    @Nullable
    View getView();

    /**
     * 设置view
     */
    void setView(@Nullable View view);

    /**
     * 是否已经开始监听
     *
     * @return true-已经开始
     */
    boolean isStarted();

    /**
     * 开始监听
     *
     * @return true-已经开始
     */
    boolean start();

    /**
     * 停止监听
     */
    void stop();

    interface Updatable {
        /**
         * 更新回调
         */
        void update();
    }
}