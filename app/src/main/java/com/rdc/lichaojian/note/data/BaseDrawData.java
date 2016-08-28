package com.rdc.lichaojian.note.data;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by lichaojian on 16-8-28.
 */
public abstract class BaseDrawData {
    protected Paint mPaint;
    private boolean isInShear = false;
    private float mOffSetX = 0;
    private float mOffSetY = 0;

    public Paint getPaint() {
        return mPaint;
    }

    public void setPaint(Paint paint) {
        mPaint = paint;
    }

    public float getOffSetX() {
        return mOffSetX;
    }

    public void setOffSetX(float offSetX) {
        mOffSetX += offSetX;
    }

    public float getOffSetY() {
        return mOffSetY;
    }

    public void setOffSetY(float offSetY) {
        mOffSetY += offSetY;
    }

    public boolean isInShear() {
        return isInShear;
    }

    public void setInShear(boolean inShear) {
        isInShear = inShear;
    }

    public abstract void onDraw(Canvas canvas);

    public abstract int getMode();
}
