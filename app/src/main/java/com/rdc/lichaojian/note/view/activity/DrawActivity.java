package com.rdc.lichaojian.note.view.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.OpacityBar;
import com.larswerkman.holocolorpicker.SVBar;
import com.larswerkman.holocolorpicker.SaturationBar;
import com.larswerkman.holocolorpicker.ValueBar;
import com.rdc.lichaojian.note.activity.R;
import com.rdc.lichaojian.note.adapter.PictureAdapter;
import com.rdc.lichaojian.note.config.NoteApplication;
import com.rdc.lichaojian.note.state.BaseState;
import com.rdc.lichaojian.note.state.CircleState;
import com.rdc.lichaojian.note.state.EraserState;
import com.rdc.lichaojian.note.state.LineState;
import com.rdc.lichaojian.note.state.PathState;
import com.rdc.lichaojian.note.state.RectangleState;
import com.rdc.lichaojian.note.state.ShearState;
import com.rdc.lichaojian.note.utils.CommandUtils;
import com.rdc.lichaojian.note.utils.DrawDataUtils;
import com.rdc.lichaojian.note.utils.FileUtils;
import com.rdc.lichaojian.note.view.widget.DrawView;
import com.rdc.lichaojian.note.view.widget.ModeSelectWindow;
import com.rdc.lichaojian.note.view.widget.VerticalSeekBar;
import com.rdc.lichaojian.note.view.widget.impl.ModeSelectCallBack;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DrawActivity extends BaseActivity implements View.OnClickListener, ColorPicker.OnColorChangedListener, ModeSelectCallBack, SeekBar.OnSeekBarChangeListener, AdapterView.OnItemClickListener {

    private static final String TAG = "DrawActivity";
    //初始化对话框
    private AppCompatDialog mAppCompatDialog;
    private VerticalSeekBar mVerticalSeekBar;
    private DrawView mDrawView;

    private TextView mTVSelectMode;
    private TextView mTVPageSize;
    private String mPicturePath = null;
    private int mPageSize = 1;
    private ModeSelectWindow mModeSelectWindow;
    //抽屉
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private PictureAdapter mPageAdapter;
    private List<String> list = null;
    private AlertDialog.Builder mBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_draw);
        initView();
        initDrawData();
    }

    private void initDrawData() {
        Intent intent = getIntent();
        mPicturePath = intent.getStringExtra("path");

        String filePath = intent.getStringExtra("filePath");
        if (mPicturePath != null) {
            Log.e("lichaojian--path",mPicturePath);
            String xmlPath = mPicturePath.substring(0, mPicturePath.length() - 3) + "xml";
            DrawDataUtils.getInstance().structureReReadXMLData("file://" + xmlPath);
            mDrawView.drawFromData();
            mDrawView.addCommand();
            if (!filePath.equals("null") && filePath != null) {
                File file = new File(filePath);
                File[] allFiles = file.listFiles();
                for (int i = 0; i < allFiles.length; ++i) {
                    if (allFiles[i].getPath().contains("png")) {
                        list.add(allFiles[i].getPath());
                    }
                }
            }
        }

        mPageSize = list.size();
        if (mPageSize == 0) {
            mPageSize = 1;
        }
        mTVPageSize.setText(Integer.toString(mPageSize));
    }

    @Override
    public void initView() {
        //初始化颜色板
        initColorPickerDialog();
        //初始化自定义的ToolBar
        initToolbar();
        mDrawView = (DrawView) findViewById(R.id.draw_view);
        mVerticalSeekBar = (VerticalSeekBar) findViewById(R.id.seekBar);
        mVerticalSeekBar.setOnSeekBarChangeListener(this);
        mDrawView.changePaintSize(mVerticalSeekBar.getProgress());
        //初始化TAB
        initNavigationTab();
        //初始化模式选择的PopupWindow
        initModeSelectWindow();
        initDrawerLayout();
        initDialog();
    }

    private void initDialog() {
        mBuilder = new AlertDialog.Builder(this);
        mBuilder.setTitle("提示");
        mBuilder.setMessage("是否保存当前画布");
        mBuilder.setPositiveButton("保存", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                save();
            }
        });
        mBuilder.setNegativeButton("不保存", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DrawActivity.this.finish();
            }
        });
    }

    private void initModeSelectWindow() {
        mModeSelectWindow = new ModeSelectWindow(this, R.layout.mode_popuwindow);
        mModeSelectWindow.setModeSelectCallBack(this);
    }

    /**
     * 初始化自定义toolbar
     */
    private void initToolbar() {
        findViewById(R.id.ll_back).setOnClickListener(this);
        findViewById(R.id.ll_num).setOnClickListener(this);
        findViewById(R.id.ll_add).setOnClickListener(this);
        findViewById(R.id.ll_undo).setOnClickListener(this);
        findViewById(R.id.ll_redo).setOnClickListener(this);
        findViewById(R.id.ll_reset).setOnClickListener(this);
        findViewById(R.id.ll_save).setOnClickListener(this);
        mTVPageSize = (TextView) findViewById(R.id.tv_page_size);
    }

    /**
     * 初始化导航Tab
     */
    private void initNavigationTab() {
        findViewById(R.id.rl_color_select_dialog).setOnClickListener(this);
        findViewById(R.id.rl_pencil_menu_select).setOnClickListener(this);
        findViewById(R.id.rl_mode_select).setOnClickListener(this);
        mTVSelectMode = (TextView) findViewById(R.id.tv_select_mode);
        findViewById(R.id.rl_shear).setOnClickListener(this);
        findViewById(R.id.rl_hard_eraser).setOnClickListener(this);
    }

    /**
     * 初始化一个颜色选择板块
     */
    private void initColorPickerDialog() {
        mAppCompatDialog = new AppCompatDialog(this);
        mAppCompatDialog.setContentView(R.layout.dialog_color_picker);
        ColorPicker colorPicker = (ColorPicker) mAppCompatDialog.findViewById(R.id.picker);
        SVBar svBar = (SVBar) mAppCompatDialog.findViewById(R.id.sv_bar);
        OpacityBar opacityBar = (OpacityBar) mAppCompatDialog.findViewById(R.id.opacity_bar);
        SaturationBar saturationBar = (SaturationBar) mAppCompatDialog.findViewById(R.id.saturation_bar);//饱和度
        ValueBar valueBar = (ValueBar) mAppCompatDialog.findViewById(R.id.value_bar);

        colorPicker.addSVBar(svBar);
        colorPicker.addOpacityBar(opacityBar);
        colorPicker.addSaturationBar(saturationBar);
        colorPicker.addValueBar(valueBar);

        colorPicker.getColor();
        colorPicker.setOldCenterColor(colorPicker.getColor());
        colorPicker.setOnColorChangedListener(this);
        colorPicker.setShowOldCenterColor(false);
    }

    private void initDrawerLayout() {

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        final ListView mListView = (ListView) findViewById(R.id.listView);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                null, R.string.open, R.string.close) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
                mPageAdapter.notifyDataSetChanged();
            }

        };

        list = new ArrayList<>();
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        mPageAdapter = new PictureAdapter(this, list);
        mListView.setAdapter(mPageAdapter);
        mListView.setOnItemClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                mBuilder.show();
                break;
            case R.id.ll_num:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.ll_add:
                mTVPageSize.setText(Integer.toString(++mPageSize));
                String fileName = mDrawView.save(mPicturePath, NoteApplication.TEMPORARY_PATH);
                list.add(NoteApplication.TEMPORARY_PATH + "/" + fileName + ".png");
                mDrawView.setCanvasCode(NoteApplication.CANVAS_RESET);
                mDrawView.invalidate();
                dataReset();
                break;
            case R.id.ll_undo:
                mDrawView.setCanvasCode(NoteApplication.CANVAS_UNDO);
                mDrawView.invalidate();
                break;
            case R.id.ll_redo:
                mDrawView.setCanvasCode(NoteApplication.CANVAS_REDO);
                mDrawView.invalidate();
                break;
            case R.id.ll_reset:
                // TODO: 2016/8/8 在此处弹出dialog让用户确认清空画布
                mDrawView.setCanvasCode(NoteApplication.CANVAS_RESET);
                mDrawView.invalidate();
                dataReset();
                break;
            case R.id.ll_save:
                if (mPageSize > 1) {
                    copy();
                } else {
                    save();
                }
                break;
            case R.id.rl_pencil_menu_select:
                break;
            case R.id.rl_color_select_dialog:
                mAppCompatDialog.show();
                break;
            case R.id.rl_mode_select:
                mModeSelectWindow.showPopupWindow(v);
                break;
            case R.id.rl_shear:
                //mDrawView.setCurrentState(ShearState.getInstance(mDrawView));
                break;
            case R.id.rl_hard_eraser:
                mDrawView.setCurrentState(EraserState.getInstance());
                break;
            default:
                Log.e(TAG, Integer.toString(v.getId()));
                break;
        }
    }

    @Override
    public void onColorChanged(int color) {
        mDrawView.changePaintColor(color);
    }

    @Override
    public void onSelectMode(int id) {
        BaseState baseState;
        switch (id) {
            case R.id.rl_normal_mode_select:
                mTVSelectMode.setText(R.string.mode_path);
                baseState = PathState.getInstance();
                break;
            case R.id.rl_line_mode_select:
                mTVSelectMode.setText(R.string.mode_line);
                baseState = LineState.getInstance();
                break;
            case R.id.rl_rectangle_mode_select:
                mTVSelectMode.setText(R.string.mode_rectangle);
                baseState = RectangleState.getInstance();
                break;
            case R.id.rl_circle_mode_select:
                mTVSelectMode.setText(R.string.mode_circle);
                baseState = CircleState.getInstance();
                break;
            default:
                baseState = PathState.getInstance();
                break;
        }

        mDrawView.setCurrentState(baseState);
    }

    @Override
    public void finish() {
        super.finish();
        dataReset();
        stateReset();
        File temporaryFile = new File(NoteApplication.TEMPORARY_PATH);
        if (temporaryFile.exists()) {
            FileUtils.delete(temporaryFile);
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        mDrawView.changePaintSize(progress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void dataReset() {
        DrawDataUtils.getInstance().getSaveDrawDataList().clear();
        DrawDataUtils.getInstance().getShearDrawDataList().clear();
        CommandUtils.getInstance().getRedoCommandList().clear();
        CommandUtils.getInstance().getUndoCommandList().clear();
    }

    private void stateReset() {
        PathState.getInstance().destroy();
        LineState.getInstance().destroy();
        RectangleState.getInstance().destroy();
        CircleState.getInstance().destroy();
        EraserState.getInstance().destroy();
        ShearState.getInstance(mDrawView).destroy();
    }

    private void save() {
        String fileName = mDrawView.save(mPicturePath, NoteApplication.ROOT_DIRECTORY);
        if (DrawDataUtils.getInstance().getSaveDrawDataList().size() > 0 && fileName != null) {
            showToast("保存成功");
            Intent intent = new Intent();
            intent.putExtra("path", NoteApplication.ROOT_DIRECTORY + "/" + fileName + ".png");
            intent.putExtra("filePath", "null");
            if (mPicturePath == null) {
                setResult(NoteApplication.OK, intent);
            } else {
                setResult(NoteApplication.CANCEL, intent);
            }
            finish();
        } else {
            showToast("保存失败,请检查当前画布是否为空。");
        }
        mPicturePath = null;
    }

    private void copy() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date currentDate = new Date(System.currentTimeMillis());
        String newFilePath = simpleDateFormat.format(currentDate);
        File bookFile = new File(NoteApplication.ROOT_DIRECTORY + "/" + newFilePath);
        if (!bookFile.exists()) {
            bookFile.mkdir();
        }
        FileUtils.fileCopy(NoteApplication.TEMPORARY_PATH, NoteApplication.ROOT_DIRECTORY + "/" + newFilePath);
        Intent intent = new Intent();
        String path = list.get(0).split("/")[list.get(0).split("/").length-1];
        intent.putExtra("path", NoteApplication.ROOT_DIRECTORY + "/" + newFilePath+"/"+path);
        intent.putExtra("filePath", NoteApplication.ROOT_DIRECTORY + "/" + newFilePath);
        setResult(NoteApplication.OK, intent);
        finish();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mTVPageSize.setText(Integer.toString(position + 1));
        String pngPath = list.get(position);
        String xmlPath = pngPath.substring(0, pngPath.length() - 3) + "xml";
        mDrawView.reset();
        dataReset();
        DrawDataUtils.getInstance().structureReReadXMLData("file://" + xmlPath);
        mDrawView.drawFromData();
        mDrawView.addCommand();
        mDrawerLayout.closeDrawer(Gravity.LEFT);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (DrawDataUtils.getInstance().getSaveDrawDataList().size() > 0) {
                mBuilder.show();
            } else {
                DrawActivity.this.finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
