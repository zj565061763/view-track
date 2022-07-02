package com.sd.lib.vtrack.tracker

import android.view.View

/**
 * 位置追踪接口
 */
interface ViewTracker {
    /**
     * 回调对象
     */
    var callback: Callback?

    /**
     * 源view
     */
    var source: View?

    /**
     * 目标view
     */
    var target: View?

    /**
     * 源位置信息
     */
    var sourceLocationInfo: SourceLocationInfo?

    /**
     * 目标位置信息
     */
    var targetLocationInfo: LocationInfo?

    /**
     * 要追踪的位置，默认右上角对齐
     */
    var position: Position

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

    abstract class ViewCallback {
        /**
         * 源view变化回调
         */
        open fun onSourceChanged(oldSource: View?, newSource: View?) {}

        /**
         * 目标view变化回调
         */
        open fun onTargetChanged(oldTarget: View?, newTarget: View?) {}

        /**
         * 在更新之前触发，返回值true-可以更新；false-不可以，默认true。
         */
        open fun canUpdate(source: View, target: View): Boolean = true

        /**
         * [Callback.onUpdate]
         */
        open fun onUpdate(x: Int, y: Int, source: View, target: View) {}
    }

    abstract class Callback : ViewCallback() {
        /**
         * 在更新之前触发，返回值true-可以更新；false-不可以，默认true。
         *
         * @param source 源
         * @param target 目标
         * @return true-可以更新，false-不要更新
         */
        open fun canUpdate(source: SourceLocationInfo, target: LocationInfo): Boolean {
            if (source is ViewLocationInfo && target is ViewLocationInfo) {
                val sourceView = source.view ?: return false
                val targetView = target.view ?: return false
                return canUpdate(sourceView, targetView)
            } else if (source is ViewLocationInfo) {
                if (source.view == null) return false
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
        open fun onUpdate(x: Int?, y: Int?, source: SourceLocationInfo, target: LocationInfo) {
            if (source is ViewLocationInfo && target is ViewLocationInfo) {
                val sourceView = source.view ?: return
                val targetView = target.view ?: return
                onUpdate(x ?: sourceView.left, y ?: sourceView.top, sourceView, targetView)
            }
        }
    }
}