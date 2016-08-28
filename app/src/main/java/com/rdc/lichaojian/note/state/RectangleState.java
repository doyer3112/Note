package com.rdc.lichaojian.note.state;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;

import com.rdc.lichaojian.note.command.Command;
import com.rdc.lichaojian.note.config.NoteApplication;
import com.rdc.lichaojian.note.data.BaseDrawData;
import com.rdc.lichaojian.note.data.RectangleDrawData;
import com.rdc.lichaojian.note.utils.CommandUtils;
import com.rdc.lichaojian.note.utils.DrawDataUtils;

/**
 * Created by lichaojian on 16-8-28.
 */
public class RectangleState extends BaseState {
    private static RectangleState mRectangleState = null;
    private RectangleDrawData mRectangleDrawData;

    private RectangleState() {
    }

    public static RectangleState getInstance() {
        if (mRectangleState == null) {
            mRectangleState = new RectangleState();
        }
        return mRectangleState;
    }

    @Override
    public void onDraw(BaseDrawData baseDrawData, Canvas canvas) {
        RectangleDrawData rectangleDrawData = (RectangleDrawData) baseDrawData;
        canvas.drawRect(rectangleDrawData.getLeft(), rectangleDrawData.getTop(), rectangleDrawData.getRight(), rectangleDrawData.getBottom(), rectangleDrawData.getPaint());
    }

    @Override
    public BaseDrawData downDrawData(MotionEvent event, Paint paint) {
        Paint rectPaint = new Paint(paint);
        Command command = new Command();
        mRectangleDrawData = new RectangleDrawData();
        mRectangleDrawData.setPaint(rectPaint);
        mRectangleDrawData.setLeft(event.getX());
        mRectangleDrawData.setTop(event.getY());
        mRectangleDrawData.setRight(event.getX());
        mRectangleDrawData.setBottom(event.getY());
        command.setCommand(NoteApplication.COMMAND_ADD);
        command.getCommandDrawList().add(mRectangleDrawData);
        CommandUtils.getInstance().getUndoCommandList().add(command);
        DrawDataUtils.getInstance().getSaveDrawDataList().add(mRectangleDrawData);
        return mRectangleDrawData;
    }

    @Override
    public void moveDrawData(MotionEvent event, Paint paint, Canvas canvas) {
        mRectangleDrawData.setRight(event.getX());
        mRectangleDrawData.setBottom(event.getY());
    }

    @Override
    public void upDrawData(MotionEvent event, Paint paint) {
        mRectangleDrawData.setRight(event.getX());
        mRectangleDrawData.setBottom(event.getY());
    }

    @Override
    public void pointerDownDrawData(MotionEvent event) {

    }

    @Override
    public void pointerUpDrawData(MotionEvent event) {

    }

    @Override
    public void destroy() {
        mRectangleState = null;
    }
}
