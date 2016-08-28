package com.rdc.lichaojian.note.data;

import android.graphics.Canvas;
import android.graphics.Path;

import com.rdc.lichaojian.note.config.NoteApplication;

/**
 * Created by lichaojian on 16-8-28.
 */
public class ShearDrawData extends BaseDrawData {
    private Path mPath;

    public ShearDrawData() {
    }

    public Path getPath() {
        return mPath;
    }

    public void setPath(Path path) {
        mPath = path;
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawPath(mPath, mPaint);
    }

    @Override
    public int getMode() {
        return NoteApplication.MODE_SHEAR;
    }
}
