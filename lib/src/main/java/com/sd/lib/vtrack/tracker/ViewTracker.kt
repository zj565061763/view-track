package com.sd.lib.vtrack.tracker

import android.view.View

/**
 * view的位置追踪接口
 */
interface ViewTracker {
    /**
     * 设置回调
     */
    fun setCallback(callback: Callback?)

    /**
     * 源view
     */
    var source: View?

    /**
     * 目标view
     */
    var target: View?

    /**
     * 设置源位置信息
     */
    fun setSourceLocationInfo(locationInfo: SourceLocationInfo?)

    /**
     * 设置目标位置信息
     */
    fun setTargetLocationInfo(locationInfo: LocationInfo?)

    /**
     * 源位置信息
     */
    fun getSourceLocationInfo(): SourceLocationInfo?

    /**
     * 目标位置信息
     */
    fun getTargetLocationInfo(): LocationInfo?

    /**
     * 设置要追踪的位置[Position]，默认右上角对齐
     */
    fun setPosition(position: Position)

    /**
     * 触发一次追踪信息更新，并返回是否更新成功
     */
    fun update(): Boolean

    enum class Position {
        /** 与target左上角对齐 */
        TopLeft,
        /** 与target顶部中间对齐 */
        TopCenter,
        /** 与target右上角对齐 */
        TopRight,

        /** 与target左边中间对齐 */
        LeftCenter,
        /** 中间对齐 */
        Center,
        /** 与target右边中间对齐 */
        RightCenter,

        /** 与target左下角对齐 */
        BottomLeft,
        /** 与target底部中间对齐 */
        BottomCenter,
        /** 与target右下角对齐 */
        BottomRight,

        /** 与target左边对齐 */
        Left,
        /** 与target顶部对齐 */
        Top,
        /** 与target右边对齐 */
        Right,
        /** 与target底部对齐 */
        Bottom
    }

    /**
     * 位置信息
     */
    interface LocationInfo {
        /** 是否已经准备好 */
        val isReady: Boolean

        /** 宽度 */
        val width: Int

        /** 高度 */
        val height: Int

        /** 坐标 */
        fun getCoordinate(position: IntArray)
    }

    /**
     * 源位置信息
     */
    interface SourceLocationInfo : LocationInfo {
        /** 返回父容器的信息 */
        val parentLocationInfo: LocationInfo?
    }

    interface ViewLocationInfo : LocationInfo {
        var view: View?
    }

    interface ViewCallback {
        /**
         * 源view变化回调
         */
        fun onSourceChanged(oldSource: View?, newSource: View?)

        /**
         * 目标view变化回调
         */
        fun onTargetChanged(oldTarget: View?, newTarget: View?)

        /**
         * [Callback.canUpdate]
         */
        fun canUpdate(source: View, target: View): Boolean

        /**
         * [Callback.onUpdate]
         */
        fun onUpdate(x: Int, y: Int, source: View, target: View)
    }

    abstract class Callback : ViewCallback {
        override fun onSourceChanged(oldSource: View?, newSource: View?) {}
        override fun onTargetChanged(oldTarget: View?, newTarget: View?) {}
        override fun canUpdate(source: View, target: View): Boolean {
            return true
        }

        override fun onUpdate(x: Int, y: Int, source: View, target: View) {}

        /**
         * 在更新追踪信息之前会调用此方法来决定可不可以更新，默认true-可以更新
         *
         * @param source 源
         * @param target 目标
         * @return true-可以更新，false-不要更新
         */
        fun canUpdate(source: SourceLocationInfo, target: LocationInfo): Boolean {
            if (source is ViewLocationInfo && target is ViewLocationInfo) {
                val sourceInfo = source as ViewLocationInfo
                val sourceView = sourceInfo.view ?: return false
                val targetView = target.view ?: return false
                return canUpdate(sourceView, targetView)
            } else if (source is ViewLocationInfo) {
                val sourceInfo = source as ViewLocationInfo
                if (sourceInfo.view == null) return false
            } else if (target is ViewLocationInfo) {
                if (target.view == null) return false
            }
            return true
        }

        /**
         * 按照指定的位置[Position]追踪到target后回调，回调source相对于父容器的x和y值
         *
         * @param x      source相对于父容器的x值，如果为null，表示该方向不需要处理
         * @param y      source相对于父容器的y值，如果为null，表示该方向不需要处理
         * @param source 源
         * @param target 目标
         */
        fun onUpdate(x: Int?, y: Int?, source: SourceLocationInfo, target: LocationInfo) {
            if (source is ViewLocationInfo && target is ViewLocationInfo) {
                val sourceInfo = source as ViewLocationInfo
                val sourceView = sourceInfo.view ?: return
                val targetView = target.view ?: return
                val xInt = x ?: sourceView.left
                val yInt = y ?: sourceView.top
                onUpdate(xInt, yInt, sourceView, targetView)
            }
        }
    }
}