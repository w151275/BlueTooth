package com.ingwu.bluetoothclient.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Property;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;


/**
 * create time: 2017/1/10
 * author: liaoym
 * description:
 */

public class SlideLockView extends ViewGroup {

    private static final String TAG = "SlideLockView";

    private ViewDragHelper viewDragHelper;

    private ISlideLockListener listener;

    private Paint mPaint;

    /**
     * 解锁触发阀值
     * 就是说当用户滚动滑块占当前视图宽度的百分比
     * 比如说 当前 视图宽度 = 1000px
     * 触发比率 = 0.75
     * 那么 触发阀值就是 750 = 1000 * 0.75
     */
    private float unlockTriggerValue = 0.25f;

    /**
     * 动画时长
     */
    private int animationTimeDuration = 300;


    /**
     * 回弹动画实现
     */
    private ObjectAnimator oa;

    /**
     * 锁视图
     */
    private View mLockView;

    /**
     * 圆角背景矩形
     */
    private RectF mRoundRect = new RectF();

    private boolean hasReportedTop = false;


    public SlideLockView(Context context) {
        this(context, null);
    }

    public SlideLockView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideLockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    /**
     * 初始化
     */
    public void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);

        viewDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {


            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                int minX = 0;
                int maxX = getHeight() - mLockView.getHeight();
                return isEnabled() && (child.getTop() > minX || child.getBottom() < maxX) && child == mLockView;
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                super.onViewReleased(releasedChild, xvel, yvel);

                //到终点
//                if (movedDistance >= getHeight() * unlockTriggerValue) {
//                    animToYToPosition(releasedChild, getHeight() - mLockView.getHeight(), animationTimeDuration);
//                    Log.d("SlideLockView",movedDistance+"");
//                } else {

                if (listener != null) {
                    listener.toBottom();
                }
                animToYToPosition(releasedChild, getHeight() - mLockView.getHeight(), animationTimeDuration);
                hasReportedTop = false;


            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                return child.getLeft();
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                final int oldTop = child.getTop();
                int minX = 0;
                int maxX = getHeight() - mLockView.getHeight();

                if (top > minX && top < maxX) {
                    child.layout((getWidth() - child.getWidth()) / 2, top,
                            (getWidth() - child.getWidth()) / 2 + child.getWidth(), top + child.getHeight());
                }

                if (!hasReportedTop && listener != null && oldTop < getHeight() * unlockTriggerValue) {
                    hasReportedTop = true;
                    listener.toTop();
                } else if (hasReportedTop && listener != null && oldTop >= getHeight() * unlockTriggerValue) {
                    hasReportedTop = false;
                    listener.toBottom();
                }

                return oldTop;
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.AT_MOST);
        int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(heightMeasureSpec), MeasureSpec.AT_MOST);

        this.mLockView = getChildAt(0);

        mLockView.measure(childWidthMeasureSpec, childHeightMeasureSpec);
        int lockHeight = mLockView.getMeasuredHeight();
        int lockWidth = mLockView.getMeasuredWidth();
        int height;
        int width = lockWidth;

        switch (MeasureSpec.getMode(heightMeasureSpec)) {
            case MeasureSpec.EXACTLY:
                height = MeasureSpec.getSize(heightMeasureSpec);
                break;
            default:
                height = lockHeight;
                break;
        }

        setMeasuredDimension(width, height);
    }

    /**
     * 使用动画转到指定的位置
     *
     * @param view 需要作用动画的视图
     * @param toY  需要转到的位置
     */
    public void animToYToPosition(final View view, int toY, long animationTime) {
        Property<View, Integer> layoutProperty = new Property<View, Integer>(Integer.class, "layout") {

            @Override
            public void set(View object, Integer value) {
                object.layout(
                        (getWidth() - mLockView.getWidth()) / 2,
                        value,
                        (getWidth() - mLockView.getWidth()) / 2 + object.getWidth(),
                        value + object.getHeight());
            }

            @Override
            public Integer get(View object) {
                return view.getTop();
            }
        };

        //原来的动画正在执行
        //取消掉，防止多重动画冲突
        if (oa != null && oa.isRunning()) {
            oa.cancel();
        }
        oa = ObjectAnimator.ofInt(view, layoutProperty, view.getTop(), toY);
        oa.setInterpolator(new AccelerateInterpolator());
        oa.setDuration(animationTime);
        oa.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        viewDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return viewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
//        mRoundRect.left = 0;
//        mRoundRect.top = 0;
//        mRoundRect.bottom = getHeight();
//        mRoundRect.right = getWidth();
//
//        //计算圆角
//        float round = (Math.min(mLockView.getWidth(), mLockView.getHeight())) / 2;
//
//        //绘制背景
//        canvas.drawRoundRect(mRoundRect, round, round, mPaint);

        super.dispatchDraw(canvas);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int viewWidth = r - l;
        int viewHeight = b - t;

        int lockViewWidth = mLockView.getMeasuredWidth();
        int lockViewHeight = mLockView.getMeasuredHeight();

        mLockView.layout((viewWidth - lockViewHeight) / 2,
                viewHeight - lockViewHeight,
                (viewWidth - lockViewWidth) / 2 + lockViewWidth,
                viewHeight);
    }

    public ISlideLockListener getListener() {
        return listener;
    }

    public void setListener(ISlideLockListener listener) {
        this.listener = listener;
    }
}
