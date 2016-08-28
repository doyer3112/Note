package com.rdc.lichaojian.note.command;

import com.rdc.lichaojian.note.config.NoteApplication;
import com.rdc.lichaojian.note.data.BaseDrawData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lichaojian on 16-8-28.
 */
public class Command {
    private int mCommand = NoteApplication.COMMAND_ADD;
    private List<BaseDrawData> mCommandDrawList;
    private float mOffSetX;
    private float mOffSetY;

    public Command() {
        mCommandDrawList = new ArrayList<>();
    }

    public List<BaseDrawData> getCommandDrawList() {
        return mCommandDrawList;
    }

    public int getCommand() {
        return mCommand;
    }

    public void setCommand(int command) {
        mCommand = command;
    }

    public float getOffSetY() {
        return mOffSetY;
    }

    public void setOffSetY(float offSetY) {
        mOffSetY = offSetY;
    }

    public float getOffSetX() {
        return mOffSetX;
    }

    public void setOffSetX(float offSetX) {
        mOffSetX = offSetX;
    }
}
