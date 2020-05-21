package com.selector.picture.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Path;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import androidx.appcompat.widget.AppCompatImageView;
import com.selector.picture.R;

/**
 * 圆角 比例 ImageView
 *
 * @author zhaod
 * @date 2019/9/23
 */
public class PressedImageView extends AppCompatImageView {
    private Context mContext;
    private float mWidth;
    private float mHeight;
    private float mAspectRatio;
    private float mCornerRadus;
    private float mLeftTopRadius;
    private float mRightTopRadius;
    private float mRightBottomRadius;
    private float mLeftBottomRadius;
    private float scaleSize;

    public PressedImageView(Context context) {
        this(context, null);
        init();
    }

    public PressedImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        init();
    }

    public PressedImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        mContext = context;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.PressedImageView);
        mAspectRatio = ta.getFloat(R.styleable.PressedImageView_aspectRatio, mAspectRatio);
        mCornerRadus =
          ta.getDimension(R.styleable.PressedImageView_cornerRadius, dp2px(mCornerRadus));
        mLeftTopRadius =
          ta.getDimension(R.styleable.PressedImageView_topLeftRadius, dp2px(mLeftTopRadius));
        mRightTopRadius =
          ta.getDimension(R.styleable.PressedImageView_topRightRadius, dp2px(mRightTopRadius));
        mLeftBottomRadius =
          ta.getDimension(R.styleable.PressedImageView_bottomLeftRadius, dp2px(mLeftBottomRadius));
        mRightBottomRadius =
          ta.getDimension(R.styleable.PressedImageView_bottomRightRadius,
            dp2px(mRightBottomRadius));

        if (mLeftTopRadius == 0f) {
            mLeftTopRadius = mCornerRadus;
        }
        if (mRightTopRadius == 0f) {
            mRightTopRadius = mCornerRadus;
        }
        if (mRightBottomRadius == 0f) {
            mRightBottomRadius = mCornerRadus;
        }
        if (mLeftBottomRadius == 0f) {
            mLeftBottomRadius = mCornerRadus;
        }
        ta.recycle();
    }

    private void init() {
        this.scaleSize = 0.97f;
        if (Build.VERSION.SDK_INT < 18) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mAspectRatio > 0) {
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height = (int) (width / mAspectRatio);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mWidth = getWidth();
        mHeight = getHeight();
    }

    /**
     * 设置宽高比例
     */
    public void setAspectRatio(float aspectRatio) {
        mAspectRatio = aspectRatio;
        requestLayout();
    }

    /**
     * 设置圆角
     */
    public void setCornerRadius(float radius) {
        mCornerRadus = radius;
        invalidate();
    }

    /**
     * 设置左上圆角
     */
    public void setLeftTopRadius(float radius) {
        mLeftTopRadius = radius;
        invalidate();
    }

    /**
     * 设置右上圆角
     */
    public void setRightTopRadius(float radius) {
        mRightTopRadius = radius;
        invalidate();
    }

    /**
     * 设置坐下圆角
     */
    public void setLeftBottomRadius(float radius) {
        mLeftBottomRadius = radius;
        invalidate();
    }

    /**
     * 设置右下圆角
     */
    public void setRightBottomRadius(float radius) {
        mRightBottomRadius = radius;
        invalidate();
    }

    /**
     * 画图
     */
    @Override
    protected void onDraw(Canvas canvas) {
        float maxLeft = Math.max(mLeftTopRadius, mLeftBottomRadius);
        float maxRight = Math.max(mRightTopRadius, mRightBottomRadius);
        float minWidth = maxLeft + maxRight;
        float maxTop = Math.max(mLeftTopRadius, mRightTopRadius);
        float maxBottom = Math.max(mLeftBottomRadius, mRightBottomRadius);
        float minHeight = maxTop + maxBottom;
        if (mWidth >= minWidth && mHeight > minHeight) {
            Path path = new Path();
            path.moveTo(mLeftTopRadius, 0);
            path.lineTo(mWidth - mRightTopRadius, 0);
            path.quadTo(mWidth, 0, mWidth, mRightTopRadius);

            path.lineTo(mWidth, mHeight - mRightBottomRadius);
            path.quadTo(mWidth, mHeight, mWidth - mRightBottomRadius, mHeight);

            path.lineTo(mLeftBottomRadius, mHeight);
            path.quadTo(0, mHeight, 0, mHeight - mLeftBottomRadius);

            path.lineTo(0, mLeftTopRadius);
            path.quadTo(0, 0, mLeftTopRadius, 0);

            canvas.clipPath(path);
        }

        super.onDraw(canvas);
    }

    @Override
    public void setPressed(boolean pressed) {
        super.setPressed(pressed);
        if (isPressed()) {
            setScaleX(this.scaleSize);
            setScaleY(this.scaleSize);
        } else {
            setScaleX(1.0f);
            setScaleY(1.0f);
        }
    }

    public void setScaleSize(float scaleSize) {
        this.scaleSize = scaleSize;
    }

    /**
     * dp 转 px
     */
    private int dp2px(float dpValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
