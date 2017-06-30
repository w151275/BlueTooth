package com.ingwu.bluetoothclient.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.media.ThumbnailUtils;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

import com.ingwu.bluetoothclient.R;


public class SlideView extends View {

    public interface SlideListener {
        void onDone(boolean isFinish);
    }

    private static final int MSG_REDRAW = 1;
    private static final int DRAW_INTERVAL = 50;
    private static final int STEP_LENGTH = 5;

    private Paint mPaint;
    private VelocityTracker mVelocityTracker;
    private int mMaxVelocity;
    private LinearGradient mGradient;
    private int[] mGradientColors;
    private int mGradientIndex;
    private Interpolator mInterpolator;
    private SlideListener mSlideListener;
    private float mDensity;
    private Matrix mMatrix;
    private ValueAnimator mValueAnimator;

    private String mText = "";
    private int mTextSize;
    private int mTextLeft;
    private int mTextTop;

    private int mSlider;
    private Bitmap mSliderBitmap;
    private int mSliderLeft;
    private int mSliderTop;
    private Rect mSliderRect;
    private int mSlidableLength;    // SlidableLength = BackgroundWidth - LeftMagins - RightMagins - SliderWidth
    private int mEffectiveLength;
    private float mEffectiveVelocity;

    private float mStartX;
    private float mStartY;
    private float mLastX;
    private float mMoveX;

    private boolean mIsInit;

    private Handler mHandler = new Handler() {

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_REDRAW:
                    mMatrix.setTranslate(mGradientIndex, 0);
                    mGradient.setLocalMatrix(mMatrix);
                    invalidate();
                    mGradientIndex += STEP_LENGTH * mDensity;
                    if (mGradientIndex > mSlidableLength) {
                        mGradientIndex = 0;
                    }
                    mHandler.sendEmptyMessageDelayed(MSG_REDRAW, DRAW_INTERVAL);
                    break;
            }
        }
    };

    public SlideView(Context context, AttributeSet attrs) {
        super(context, attrs);
        ViewConfiguration configuration = ViewConfiguration.get(context);
        mMaxVelocity = configuration.getScaledMaximumFlingVelocity();
        mInterpolator = new AccelerateDecelerateInterpolator();
        mDensity = getResources().getDisplayMetrics().density;
        setClickable(true);
        setFocusable(true);
        setFocusableInTouchMode(true);

        TypedArray typeArray = context.obtainStyledAttributes(attrs, R.styleable.SlideView);
//        mText = typeArray.getString(R.styleable.SlideView_maskText);
//        mTextSize = typeArray.getDimensionPixelSize(R.styleable.SlideView_maskTextSize, R.dimen.mask_text_size);
//        mTextLeft = typeArray.getDimensionPixelSize(R.styleable.SlideView_maskTextMarginLeft, R.dimen.mask_text_margin_left);
//        mTextTop = typeArray.getDimensionPixelSize(R.styleable.SlideView_maskTextMarginTop, R.dimen.mask_text_margin_top);

        mSlider = typeArray.getResourceId(R.styleable.SlideView_slider, R.mipmap.ic_launcher);
        mSliderLeft = typeArray.getDimensionPixelSize(R.styleable.SlideView_sliderMarginLeft, R.dimen.activity_horizontal_margin);
        mSliderTop = typeArray.getDimensionPixelSize(R.styleable.SlideView_sliderMarginTop, R.dimen.activity_horizontal_margin);
        typeArray.recycle();
    }

    public void setSlideListener(SlideListener slideListener) {
        mSlideListener = slideListener;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mIsInit == false) {
            mSlidableLength = getWidth() - getHeight()-mSliderLeft;
            mEffectiveLength = getWidth() - getHeight() - mSliderLeft * 2;
            mEffectiveVelocity=getWidth();

            mSliderBitmap = BitmapFactory.decodeResource(getResources(), mSlider);
            mSliderBitmap = ThumbnailUtils.extractThumbnail(mSliderBitmap, getHeight() - mSliderTop * 2, getHeight() - mSliderTop * 2,
                    ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
            mSliderRect = new Rect(mSliderLeft, mSliderTop, mSliderTop * 2 + mSliderBitmap.getHeight(),
                    mSliderTop * 2 + mSliderBitmap.getHeight());

            mGradientColors = new int[]{Color.argb(255, 120, 120, 120),
                    Color.argb(255, 120, 120, 120), Color.argb(255, 255, 255, 255)};
            mGradient = new LinearGradient(0, 0, 100 * mDensity, 0, mGradientColors,
                    new float[]{0, 0.7f, 1}, Shader.TileMode.MIRROR);
            mGradientIndex = 0;
            mPaint = new Paint();
            mMatrix = new Matrix();
            mPaint.setTextSize(mTextSize);
            mHandler.sendEmptyMessageDelayed(MSG_REDRAW, DRAW_INTERVAL);
            mIsInit = true;
        }
        mPaint.setShader(mGradient);
        canvas.drawText(mText, mTextLeft, mTextTop, mPaint);
        // 滑块的移动依赖mMoveX。
        canvas.drawBitmap(mSliderBitmap, mSliderLeft + mMoveX, mSliderTop, null);
    }

    public void reset() {
        if (mValueAnimator != null) {
            mValueAnimator.cancel();
        }
        mMoveX = 0;
        mPaint.setAlpha(255);
        mHandler.removeMessages(MSG_REDRAW);
        mHandler.sendEmptyMessage(MSG_REDRAW);
    }
    public void onReset(String name){
        mText=name;
        onReset();
    }

    public void onReset(){
        mMoveX=0;
        mIsInit=false;
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //如果起点没有滑块,滑块移动将不会被执行。
        boolean contains = mSliderRect.contains((int) mStartX, (int) mStartY);
        if (event.getAction() != MotionEvent.ACTION_DOWN
                && !mSliderRect.contains((int) mStartX, (int) mStartY)) {
            if (event.getAction() == MotionEvent.ACTION_UP
                    || event.getAction() == MotionEvent.ACTION_CANCEL) {
                mHandler.sendEmptyMessageDelayed(MSG_REDRAW, DRAW_INTERVAL);
            }
            return super.onTouchEvent(event);
        }
        acquireVelocityTrackerAndAddMovement(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mStartX = event.getX();
                mStartY = event.getY();
                mLastX = mStartX;
                mHandler.removeMessages(MSG_REDRAW);
                break;
            case MotionEvent.ACTION_MOVE:
                mLastX = event.getX();
                if (mLastX > mStartX) { // 不可以超过左边界,否则,mMoveX将得到一个最小值。
                    // 文本将被改变的透明度以及移动滑块
                    int alpha = (int) (255 - (mLastX - mStartX) * 3 / mDensity);
                    if (alpha > 1) {
                        mPaint.setAlpha(alpha);
                    } else {
                        mPaint.setAlpha(0);
                    }
                    // 不可以超过边界,否则,mMoveX将获得最大价值。
                    if (mLastX - mStartX > mSlidableLength) {
                        mLastX = mStartX + mSlidableLength;
                        mMoveX = mSlidableLength;
                    } else {
                        mMoveX = (int) (mLastX - mStartX);
                    }
                } else {
                    mLastX = mStartX;
                    mMoveX = 0;
                }
                float velocityXX = mVelocityTracker.getXVelocity();
                if (mLastX - mStartX > mEffectiveLength || velocityXX > mEffectiveVelocity) {
                    if(mSlideListener != null){
                        mSlideListener.onDone(true);
                    }
                } else {
                    if(mSlideListener != null){
                        mSlideListener.onDone(false);
                    }
                }
                    invalidate();
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                //手指划过--惯性作用的精度  尝试修改下边数值 快速拖动按钮理解
                mVelocityTracker.computeCurrentVelocity(180, mMaxVelocity);
                float velocityX = mVelocityTracker.getXVelocity();
                if (mLastX - mStartX > mEffectiveLength || velocityX > mEffectiveVelocity) {
                    startAnimator(mLastX - mStartX, mSlidableLength, velocityX, true);
                } else {
                    startAnimator(mLastX - mStartX, 0, velocityX, false);
                    mHandler.sendEmptyMessageDelayed(MSG_REDRAW, DRAW_INTERVAL);
                }
                releaseVelocityTracker();
                break;
        }
        return super.onTouchEvent(event);
    }

    private void startAnimator(float start, float end, float velocity, boolean isRightMoving) {
        if (velocity < mEffectiveVelocity) {
            velocity = mEffectiveVelocity;
        }
        int duration = (int) (Math.abs(end - start) * 1000 / velocity);
        mValueAnimator = ValueAnimator.ofFloat(start, end);
        mValueAnimator.addUpdateListener(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mMoveX = (Float) animation.getAnimatedValue();
                int alpha = (int) (255 - (mMoveX) * 3 / mDensity);
                if (alpha > 1) {
                    mPaint.setAlpha(alpha);
                } else {
                    mPaint.setAlpha(0);
                }
                invalidate();
            }
        });
        mValueAnimator.setDuration(duration);
        mValueAnimator.setInterpolator(mInterpolator);
        if (isRightMoving) {
            mValueAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if(mSlideListener != null){
                        mSlideListener.onDone(false);
                    }
                    reset();
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });
        }
        mValueAnimator.start();
    }

    private void acquireVelocityTrackerAndAddMovement(MotionEvent ev) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(ev);
    }

    private void releaseVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

}
