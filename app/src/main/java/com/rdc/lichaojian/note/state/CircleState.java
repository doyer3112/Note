package com.rdc.lichaojian.note.state;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;

import com.rdc.lichaojian.note.command.Command;
import com.rdc.lichaojian.note.config.NoteApplication;
import com.rdc.lichaojian.note.data.BaseDrawData;
import com.rdc.lichaojian.note.data.CircleDrawData;
import com.rdc.lichaojian.note.utils.CommandUtils;
import com.rdc.lichaojian.note.utils.DrawDataUtils;

/**
 * Created by lichaojian on 16-8-28.
 */
public class CircleState extends BaseState {
    private static CircleState mCircleState = null;
    private CircleDrawData mCircleDrawData;

    private CircleState() {
    }

    public static CircleState getInstance() {
        if (mCircleState == null) {
            mCircleState = new CircleState();
        }
        return mCircleState;
    }

    @Override
    public void onDraw(BaseDrawData baseDrawData, Canvas canvas) {
        CircleDrawData circleDrawData = (CircleDrawData) baseDrawData;
        canvas.drawCircle(circleDrawData.getCircleX(), circleDrawData.getCircleY(), circleDrawData.getRadius(), circleDrawData.getPaint());
    }

    @Override
    public BaseDrawData downDrawData(MotionEvent event, Paint paint) {
        Paint circlePaint = new Paint(paint);
        Command command = new Command();
        mCircleDrawData = new CircleDrawData();
        mCircleDrawData.setPaint(circlePaint);
        mCircleDrawData.setCircleX(event.getX());
        mCircleDrawData.setCircleY(event.getY());
        command.setCommand(NoteApplication.COMMAND_ADD);
        command.getCommandDrawList().add(mCircleDrawData);
        CommandUtils.getInstance().getUndoCommandList().add(command);
        DrawDataUtils.getInstance().getSaveDrawDataList().add(mCircleDrawData);
        return mCircleDrawData;
    }

    @Override
    public void moveDrawData(MotionEvent event, Paint paint, Canvas canvas) {
        float radiusX = (mCircleDrawData.getCircleX() - event.getX()) * (mCircleDrawData.getCircleX() - event.getX());
        float radiusY = (mCircleDrawData.getCircleY() - event.getY()) * (mCircleDrawData.getCircleY() - event.getY());
        float radius = (float) Math.sqrt(radiusX + radiusY);
        mCircleDrawData.setRadius(radius);
    }

    @Override
    public void upDrawData(MotionEvent event, Paint paint) {
        float radiusX = (mCircleDrawData.getCircleX() - event.getX()) * (mCircleDrawData.getCircleX() - event.getX());
        float radiusY = (mCircleDrawData.getCircleY() - event.getY()) * (mCircleDrawData.getCircleY() - event.getY());
        float radius = (float) Math.sqrt(radiusX + radiusY);
        mCircleDrawData.setRadius(radius);
    }

    @Override
    public void pointerDownDrawData(MotionEvent event) {

    }

    @Override
    public void pointerUpDrawData(MotionEvent event) {

    }

    @Override
    public void destroy() {
        mCircleState = null;
    }
}
