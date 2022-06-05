package com.sd.lib.vtrack.tracker;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * view的位置追踪接口
 */
public interface ViewTracker {
    /**
     * 设置回调
     */
    void setCallback(@Nullable Callback callback);

    /**
     * 设置源view
     */
    void setSource(@Nullable View source);

    /**
     * 设置目标view
     */
    void setTarget(@Nullable View target);

    /**
     * 源view
     */
    @Nullable
    View getSource();

    /**
     * 目标view
     */
    @Nullable
    View getTarget();

    /**
     * 设置源位置信息
     */
    void setSourceLocationInfo(SourceLocationInfo locationInfo);

    /**
     * 设置目标位置信息
     */
    void setTargetLocationInfo(@Nullable LocationInfo locationInfo);

    /**
     * 设置要追踪的位置{@link Position}，默认右上角对齐
     */
    void setPosition(@NonNull Position position);

    /**
     * 触发一次追踪信息更新
     *
     * @return true-此次更新成功
     */
    boolean update();

    enum Position {
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
        Bottom,
    }

    /**
     * 位置信息
     */
    interface LocationInfo {
        /**
         * 是否已经准备好
         *
         * @return true-是；false-否
         */
        boolean isReady();

        /**
         * 宽度
         */
        int getWidth();

        /**
         * 高度
         */
        int getHeight();

        /**
         * 坐标
         */
        void getCoordinate(@NonNull int[] position);
    }

    /**
     * 源位置信息
     */
    interface SourceLocationInfo extends LocationInfo {
        /**
         * 返回父容器的信息
         */
        @Nullable
        LocationInfo getParent();
    }

    interface ViewLocationInfo extends LocationInfo {
        @Nullable
        View getView();

        void setView(@Nullable View view);
    }

    interface ViewCallback {
        /**
         * 源view变化回调
         *
         * @param oldSource 旧的源view
         * @param newSource 新的源view
         */
        void onSourceChanged(@Nullable View oldSource, @Nullable View newSource);

        /**
         * 目标view变化回调
         *
         * @param oldTarget 旧的目标view
         * @param newTarget 新的目标view
         */
        void onTargetChanged(@Nullable View oldTarget, @Nullable View newTarget);

        /**
         * {@link Callback#canUpdate(SourceLocationInfo, LocationInfo)}
         */
        boolean canUpdate(@NonNull View source, @NonNull View target);

        /**
         * {@link Callback#onUpdate(Integer, Integer, SourceLocationInfo, LocationInfo)}
         */
        void onUpdate(int x, int y, @NonNull View source, @NonNull View target);
    }

    abstract class Callback implements ViewCallback {
        @Override
        public void onSourceChanged(@Nullable View oldSource, @Nullable View newSource) {
        }

        @Override
        public void onTargetChanged(@Nullable View oldTarget, @Nullable View newTarget) {
        }

        @Override
        public boolean canUpdate(@NonNull View source, @NonNull View target) {
            return true;
        }

        @Override
        public void onUpdate(int x, int y, @NonNull View source, @NonNull View target) {
        }

        /**
         * 在更新追踪信息之前会调用此方法来决定可不可以更新，默认true-可以更新
         *
         * @param source 源
         * @param target 目标
         * @return true-可以更新，false-不要更新
         */
        public boolean canUpdate(@NonNull SourceLocationInfo source, @NonNull LocationInfo target) {
            if (source instanceof ViewLocationInfo && target instanceof ViewLocationInfo) {
                final ViewLocationInfo sourceInfo = (ViewLocationInfo) source;
                final View sourceView = sourceInfo.getView();
                if (sourceView == null) return false;

                final ViewLocationInfo targetInfo = (ViewLocationInfo) target;
                final View targetView = targetInfo.getView();
                if (targetView == null) return false;

                return canUpdate(sourceView, targetView);
            }
            return true;
        }

        /**
         * 按照指定的位置{@link Position}追踪到target后回调，回调source相对于父容器的x和y值
         *
         * @param x      source相对于父容器的x值，如果为null，表示该方向不需要处理
         * @param y      source相对于父容器的y值，如果为null，表示该方向不需要处理
         * @param source 源
         * @param target 目标
         */
        public void onUpdate(Integer x, Integer y, @NonNull SourceLocationInfo source, @NonNull LocationInfo target) {
            if (source instanceof ViewLocationInfo && target instanceof ViewLocationInfo) {
                final ViewLocationInfo sourceInfo = (ViewLocationInfo) source;
                final View sourceView = sourceInfo.getView();
                if (sourceView == null) return;

                final ViewLocationInfo targetInfo = (ViewLocationInfo) target;
                final View targetView = targetInfo.getView();
                if (targetView == null) return;

                final int xInt = x != null ? x : sourceView.getLeft();
                final int yInt = y != null ? y : sourceView.getTop();
                onUpdate(xInt, yInt, sourceView, targetView);
            }
        }
    }
}