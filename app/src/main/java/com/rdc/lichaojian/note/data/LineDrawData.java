package com.rdc.lichaojian.note.data;

import android.graphics.Canvas;

import com.rdc.lichaojian.note.config.NoteApplication;

/**
 * Created by lichaojian on 16-8-28.
 */
public class LineDrawData extends BaseDrawData {
    private float mStartX;
    private float mStartY;
    private float mEndX;
    private float mEndY;

    public float getStartX() {
        return mStartX;
    }

    public void setStartX(float startX) {
        mStartX = startX;
    }

    public float getStartY() {
        return mStartY;
    }

    public void setStartY(float startY) {
        mStartY = startY;
    }

    public float getEndX() {
        return mEndX;
    }

    public void setEndX(float endX) {
        mEndX = endX;
    }

    public float getEndY() {
        return mEndY;
    }

    public void setEndY(float endY) {
        mEndY = endY;
    }

    public void offSet(float dx, float dy) {
        this.mStartX += dx;
        this.mStartY += dy;
        this.mEndX += dx;
        this.mEndY += dy;
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawLine(mStartX, mStartY, mEndX, mEndY, mPaint);
    }

    @Override
    public int getMode() {
        return NoteApplication.MODE_LINE;
    }
}
