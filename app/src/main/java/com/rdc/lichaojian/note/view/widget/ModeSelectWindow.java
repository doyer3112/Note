package com.rdc.lichaojian.note.view.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.rdc.lichaojian.note.activity.R;
import com.rdc.lichaojian.note.view.widget.impl.ModeSelectCallBack;

/**
 * Created by lichaojian on 16-8-28.
 */
public class ModeSelectWindow extends PopupWindow implements View.OnClickListener {
    private View mContentView;
    private ModeSelectCallBack mModeSelectCallBack;

    public ModeSelectWindow(Context context, int resourceId) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContentView = inflater.inflate(resourceId, null);
        setContentView(mContentView);
        setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        setOutsideTouchable(false);
        update();
        ColorDrawable dw = new ColorDrawable(0000000000);
        setBackgroundDrawable(dw);
        initView();
    }

    private void initView() {
        RelativeLayout rlNormalMode = (RelativeLayout) mContentView.findViewById(R.id.rl_normal_mode_select);
        rlNormalMode.setOnClickListener(this);
        RelativeLayout rlLineMode = (RelativeLayout) mContentView.findViewById(R.id.rl_line_mode_select);
        rlLineMode.setOnClickListener(this);
        RelativeLayout rlRectangle = (RelativeLayout) mContentView.findViewById(R.id.rl_rectangle_mode_select);
        rlRectangle.setOnClickListener(this);
        RelativeLayout rlCircleMode = (RelativeLayout) mContentView.findViewById(R.id.rl_circle_mode_select);
        rlCircleMode.setOnClickListener(this);
    }

    public void showPopupWindow(View parent) {
        if (!isShowing()) {
            showAtLocation(parent, Gravity.BOTTOM, 0, parent.getHeight() + 140);
        } else {
            dismiss();
        }
    }

    public void setModeSelectCallBack(ModeSelectCallBack modeSelectCallBack) {
        this.mModeSelectCallBack = modeSelectCallBack;
    }

    @Override
    public void onClick(View v) {
        dismiss();
        mModeSelectCallBack.onSelectMode(v.getId());
    }
}
