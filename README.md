# About
让源view追踪目标view，回调源view的x和y相对于父布局需要是什么值才可以到达指定位置

# Gradle
[![](https://jitpack.io/v/zj565061763/view-track.svg)](https://jitpack.io/#zj565061763/view-track)

# 接口
```java
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
     * 设置要追踪的位置{@link Position}，默认左上角对齐
     */
    void setPosition(@NonNull Position position);

    /**
     * 返回想要追踪目标的源view
     */
    @Nullable
    View getSource();

    /**
     * 返回目标view
     */
    @Nullable
    View getTarget();

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

    abstract class Callback {
        /**
         * 源view变化回调
         *
         * @param oldSource 旧的源view
         * @param newSource 新的源view
         */
        public void onSourceChanged(@Nullable View oldSource, @Nullable View newSource) {
        }

        /**
         * 目标view变化回调
         *
         * @param oldTarget 旧的目标view
         * @param newTarget 新的目标view
         */
        public void onTargetChanged(@Nullable View oldTarget, @Nullable View newTarget) {
        }

        /**
         * 在更新追踪信息之前会调用此方法来决定可不可以更新，默认true-可以更新
         *
         * @param source 源view
         * @param target 目标view
         * @return true-可以更新，false-不要更新
         */
        public boolean canUpdate(@NonNull View source, @NonNull View target) {
            return true;
        }

        /**
         * 按照指定的位置{@link Position}追踪到target后回调，回调source相对于父布局的x和y值
         *
         * @param x      source相对于父布局的x值
         * @param y      source相对于父布局的y值
         * @param source 源view
         * @param target 目标view
         */
        public abstract void onUpdate(int x, int y, @NonNull View source, @NonNull View target);
    }
}
```