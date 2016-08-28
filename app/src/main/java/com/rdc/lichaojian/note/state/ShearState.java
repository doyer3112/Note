package com.rdc.lichaojian.note.state;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.view.MotionEvent;

import com.rdc.lichaojian.note.data.BaseDrawData;
import com.rdc.lichaojian.note.data.ShearDrawData;
import com.rdc.lichaojian.note.utils.DrawDataUtils;
import com.rdc.lichaojian.note.utils.JudgeAreaUtils;
import com.rdc.lichaojian.note.view.widget.DrawView;

import java.util.Iterator;

/**
 * Created by lichaojian on 16-8-28.
 */
public class ShearState extends BaseState {
    private static ShearState mShearState = null;
    private ShearDrawData mShearPathDrawData;
    private float mStartX;
    private float mStartY;
    private Matrix mMatrix;
    private float mOldDist;
    private boolean isScale = false;
    private PointF mMid;
    private boolean isShear = false;//判断是否为裁剪状态
    private Path mShearPath;
    private DrawView mDrawView;

    private ShearState() {
    }

    public static ShearState getInstance(DrawView drawView) {
        if (mShearState == null) {
            mShearState = new ShearState();
            mShearState.mDrawView = drawView;
        }
        return mShearState;
    }

    @Override
    public void onDraw(BaseDrawData baseDrawData, Canvas canvas) {
        if (baseDrawData != null) {
            ShearDrawData pathDrawData = (ShearDrawData) baseDrawData;
            canvas.drawPath(pathDrawData.getPath(), pathDrawData.getPaint());
        }

    }

    @Override
    public BaseDrawData downDrawData(MotionEvent event, Paint paint) {
        if (isShear) {
            //如果当前为裁剪状态，判断手指down下的点是否在裁剪区域内
            if (JudgeAreaUtils.getInstance().isInShearLocation(Math.round(event.getX()), Math.round(event.getY()), mShearPath)) {

                mStartX = event.getX();
                mStartY = event.getY();
                return null;
            } else {
                DrawDataUtils.getInstance().drawDataFromShearToSave();
                isShear = false;
                return null;
            }
        } else {
            mStartX = event.getX();
            mStartY = event.getY();
            mShearPathDrawData = new ShearDrawData();
            Paint shearPaint = new Paint(paint);
            shearPaint.setStyle(Paint.Style.STROKE);
            shearPaint.setStrokeWidth(8);
            shearPaint.setColor(Color.BLACK);
            shearPaint.setPathEffect(new DashPathEffect(new float[]{5, 5, 5, 5}, 1));
            mShearPath = new Path();
            mShearPath.reset();
            mShearPath.moveTo(event.getX(), event.getY());
            mShearPathDrawData.setPaint(shearPaint);
            mShearPathDrawData.setPath(mShearPath);
            DrawDataUtils.getInstance().getShearDrawDataList().add(mShearPathDrawData);
            return mShearPathDrawData;
        }


    }

    private void drawFromData1(Canvas canvas, Iterator<BaseDrawData> iterator, boolean is) {
        if (is) {
            mDrawView.reset();
        }
        while (iterator.hasNext()) {
            BaseDrawData baseDrawData = iterator.next();
            baseDrawData.onDraw(canvas);
        }
    }

    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    @Override
    public void moveDrawData(MotionEvent event, Paint paint, Canvas canvas) {
        if (isShear) {
            if (isScale) {
                midPoint(mMid, event);
                float newNewDist = spacing(event);
                if (newNewDist > 10f) {
                    if (newNewDist / mOldDist > 0.3) {
                        mMatrix.postScale(newNewDist / mOldDist, newNewDist / mOldDist, mMid.x, mMid.y);
                        mOldDist = newNewDist;
                        Iterator<BaseDrawData> iterator = DrawDataUtils.getInstance().getShearDrawDataList().iterator();
                        DrawDataUtils.getInstance().scaleDrawData(iterator, 0, 0, mMatrix);
                        drawFromData1(canvas, DrawDataUtils.getInstance().getShearDrawDataList().iterator(), true);
                        drawFromData1(canvas, DrawDataUtils.getInstance().getSaveDrawDataList().iterator(), false);
                    }

                }

            } else {
                // TODO: 2016/8/11 在这里对shear数据以及非shear数据进行重绘
                float offsetX = event.getX() - mStartX;
                float offsetY = event.getY() - mStartY;
                Iterator<BaseDrawData> iterator = DrawDataUtils.getInstance().getShearDrawDataList().iterator();
                DrawDataUtils.getInstance().offSetDrawData(iterator, offsetX, offsetY);
                drawFromData(canvas, DrawDataUtils.getInstance().getShearDrawDataList().iterator(), true);
                drawFromData(canvas, DrawDataUtils.getInstance().getSaveDrawDataList().iterator(), false);
                mStartX = event.getX();
                mStartY = event.getY();
            }

        } else {
            mShearPathDrawData.getPath().lineTo(event.getX(), event.getY());
            canvas.drawPath(mShearPathDrawData.getPath(), mShearPathDrawData.getPaint());
        }

    }

    @Override
    public void upDrawData(MotionEvent event, Paint paint) {
        if (isShear) {
            DrawDataUtils.getInstance().drawDataFromShearToCommand();
        } else {
            mShearPathDrawData.getPath().lineTo(mStartX, mStartY);
            JudgeAreaUtils.getInstance().isDrawDataInShearPath(mShearPath);
            DrawDataUtils.getInstance().drawDataFromSaveToShear();
            isShear = true;
        }
    }

    @Override
    public void pointerDownDrawData(MotionEvent event) {
        mMid = new PointF();
        mMatrix = new Matrix();
        mOldDist = spacing(event);
        isScale = true;
    }

    @Override
    public void pointerUpDrawData(MotionEvent event) {
        isScale = false;
    }

    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    @Override
    public void destroy() {
        mShearState = null;
    }

    private void drawFromData(Canvas canvas, Iterator<BaseDrawData> iterator, boolean is) {
        if (is) {
            mDrawView.reset();
        }
        while (iterator.hasNext()) {
            BaseDrawData baseDrawData = iterator.next();
            baseDrawData.onDraw(canvas);
        }
    }

    @Override
    public boolean isShear() {
        return isShear;
    }
}
