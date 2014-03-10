package com.uwflow.flow_android.custom_view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

// Adapted from http://stackoverflow.com/questions/6650398/android-imageview-zoom-in-and-zoom-out
public class DragZoomImageView extends ImageView implements View.OnTouchListener {


    private Matrix matrix = new Matrix();
    private Matrix savedMatrix = new Matrix();

    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;

    private int mode = NONE;

    private PointF mStartPoint = new PointF();
    private PointF mMiddlePoint = new PointF();
    private Point mBitmapMiddlePoint = new Point();

    private float oldDist = 1f;
    private float matrixValues[] = {0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f};
    private float scale;
    private float oldEventX = 0;
    private float oldEventY = 0;
    private float oldStartPointX = 0;
    private float oldStartPointY = 0;
    private int mViewWidth = -1;
    private int mViewHeight = -1;
    private int mBitmapWidth = -1;
    private int mBitmapHeight = -1;
    private boolean mDraggable = false;


    public DragZoomImageView(Context context) {
        this(context, null, 0);
    }

    public DragZoomImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragZoomImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.setOnTouchListener(this);
    }

    @Override
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewWidth = w;
        mViewHeight = h;
    }

    public void setBitmap(Bitmap bitmap) {
        if (bitmap != null) {
            setImageBitmap(bitmap);

            mBitmapWidth = bitmap.getWidth();
            mBitmapHeight = bitmap.getHeight();
            mBitmapMiddlePoint.x = (mViewWidth / 2) - (mBitmapWidth / 2);
            mBitmapMiddlePoint.y = (mViewHeight / 2) - (mBitmapHeight / 2);

            matrix.postTranslate(mBitmapMiddlePoint.x, mBitmapMiddlePoint.y);
            this.setImageMatrix(matrix);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                savedMatrix.set(matrix);
                mStartPoint.set(event.getX(), event.getY());
                mode = DRAG;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                oldDist = spacing(event);
                if (oldDist > 10f) {
                    savedMatrix.set(matrix);
                    midPoint(mMiddlePoint, event);
                    mode = ZOOM;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mode == DRAG) {
                    drag(event);
                } else if (mode == ZOOM) {
                    zoom(event);
                }
                break;
        }

        return true;
    }


    public void drag(MotionEvent event) {
        matrix.getValues(matrixValues);

        float left = matrixValues[2];
        float top = matrixValues[5];
        float bottom = (top + (matrixValues[0] * mBitmapHeight)) - mViewHeight;
        float right = (left + (matrixValues[0] * mBitmapWidth)) - mViewWidth;

        float eventX = event.getX();
        float eventY = event.getY();
        float spacingX = eventX - mStartPoint.x;
        float spacingY = eventY - mStartPoint.y;
        float newPositionLeft = (left < 0 ? spacingX : spacingX * -1) + left;
        float newPositionRight = (spacingX) + right;
        float newPositionTop = (top < 0 ? spacingY : spacingY * -1) + top;
        float newPositionBottom = (spacingY) + bottom;
        boolean x = true;
        boolean y = true;

        if (newPositionRight < 0.0f || newPositionLeft > 0.0f) {
            if (newPositionRight < 0.0f && newPositionLeft > 0.0f) {
                x = false;
            } else {
                eventX = oldEventX;
                mStartPoint.x = oldStartPointX;
            }
        }
        if (newPositionBottom < 0.0f || newPositionTop > 0.0f) {
            if (newPositionBottom < 0.0f && newPositionTop > 0.0f) {
                y = false;
            } else {
                eventY = oldEventY;
                mStartPoint.y = oldStartPointY;
            }
        }

        if (mDraggable) {
            matrix.set(savedMatrix);
            matrix.postTranslate(x ? eventX - mStartPoint.x : 0, y ? eventY - mStartPoint.y : 0);
            this.setImageMatrix(matrix);
            if (x) oldEventX = eventX;
            if (y) oldEventY = eventY;
            if (x) oldStartPointX = mStartPoint.x;
            if (y) oldStartPointY = mStartPoint.y;
        }

    }

    public void zoom(MotionEvent event) {
        matrix.getValues(matrixValues);

        float newDist = spacing(event);
        float bitmapWidth = matrixValues[0] * mBitmapWidth;
        float bimtapHeight = matrixValues[0] * mBitmapHeight;
        boolean in = newDist > oldDist;

        if (!in && matrixValues[0] < 1) {
            return;
        }
        if (bitmapWidth > mViewWidth || bimtapHeight > mViewHeight) {
            mDraggable = true;
        } else {
            mDraggable = false;
        }

        float midX = (mViewWidth / 2);
        float midY = (mViewHeight / 2);

        matrix.set(savedMatrix);
        scale = newDist / oldDist;
        matrix.postScale(scale, scale, bitmapWidth > mViewWidth ? mMiddlePoint.x : midX, bimtapHeight > mViewHeight ? mMiddlePoint.y : midY);

        this.setImageMatrix(matrix);


    }


    /**
     * Determine the space between the first two fingers
     */
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);

        return (float) Math.sqrt(x * x + y * y);
    }

    /**
     * Calculate the mid point of the first two fingers
     */
    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }
}
