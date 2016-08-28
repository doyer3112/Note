package com.rdc.lichaojian.note.data;

import android.graphics.Canvas;

import com.rdc.lichaojian.note.config.NoteApplication;

/**
 * Created by lichaojian on 16-8-28.
 */
public class RectangleDrawData extends BaseDrawData {
    private float mTop;
    private float mLeft;
    private float mRight;
    private float mBottom;

    public RectangleDrawData() {
    }

    public float getTop() {
        return mTop;
    }

    public void setTop(float top) {
        mTop = top;
    }

    public float getLeft() {
        return mLeft;
    }

    public void setLeft(float left) {
        mLeft = left;
    }

    public float getRight() {
        return mRight;
    }

    public void setRight(float right) {
        mRight = right;
    }

    public float getBottom() {
        return mBottom;
    }

    public void setBottom(float bottom) {
        mBottom = bottom;
    }

    public void offSet(float dx, float dy) {
        this.mLeft += dx;
        this.mTop += dy;
        this.mRight += dx;
        this.mBottom += dy;
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawRect(mLeft, mTop, mRight, mBottom, mPaint);
    }

    @Override
    public int getMode() {
        return NoteApplication.MODE_RECTANGLE;
    }
}
