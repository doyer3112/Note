package com.rdc.lichaojian.note.utils;

import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;

import com.rdc.lichaojian.note.command.Command;
import com.rdc.lichaojian.note.config.NoteApplication;
import com.rdc.lichaojian.note.data.BaseDrawData;
import com.rdc.lichaojian.note.data.CircleDrawData;
import com.rdc.lichaojian.note.data.EraserDrawData;
import com.rdc.lichaojian.note.data.LineDrawData;
import com.rdc.lichaojian.note.data.PathDrawData;
import com.rdc.lichaojian.note.data.RectangleDrawData;
import com.rdc.lichaojian.note.data.ShearDrawData;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by lichaojian on 16-8-28.
 */
public class DrawDataUtils {

    private static final String TAG = "DrawDataUtils";
    private static DrawDataUtils mDrawDataUtils = null;
    private List<BaseDrawData> mSaveDrawDataList = null;
    private List<BaseDrawData> mShearDrawDataList = null;

    private DrawDataUtils() {
        mSaveDrawDataList = new ArrayList<>();
        mShearDrawDataList = new ArrayList<>();
    }

    public static DrawDataUtils getInstance() {
        if (mDrawDataUtils == null) {
            mDrawDataUtils = new DrawDataUtils();
        }
        return mDrawDataUtils;
    }

    public List<BaseDrawData> getShearDrawDataList() {
        return mShearDrawDataList;
    }

    public List<BaseDrawData> getSaveDrawDataList() {
        return mSaveDrawDataList;
    }

    /**
     * 将数据从数据区转移到裁剪区
     */
    public void drawDataFromSaveToShear() {
        Iterator<BaseDrawData> iterator = DrawDataUtils.getInstance().getSaveDrawDataList().iterator();
        while (iterator.hasNext()) {
            BaseDrawData baseDrawData = iterator.next();
            if (baseDrawData.isInShear()) {
                DrawDataUtils.getInstance().getShearDrawDataList().add(baseDrawData);
                iterator.remove();
            }
        }

    }

    /**
     * 将数据从裁剪区转移到数据区
     */
    public void drawDataFromShearToSave() {
        Iterator<BaseDrawData> iterator = DrawDataUtils.getInstance().getShearDrawDataList().iterator();
        while (iterator.hasNext()) {
            BaseDrawData baseDrawData = iterator.next();
            if (baseDrawData.getMode() == NoteApplication.MODE_SHEAR) {

            } else {
                DrawDataUtils.getInstance().getSaveDrawDataList().add(baseDrawData);
            }
            iterator.remove();
        }
    }

    /**
     * 对圈选的数据进行缩放
     *
     * @param iterator 需要进行缩放的数据集
     * @param scaleX   缩放的X
     * @param scaleY   缩放的Y
     * @param matrix
     */
    public void scaleDrawData(Iterator<BaseDrawData> iterator, float scaleX, float scaleY, Matrix matrix) {
        while (iterator.hasNext()) {
            BaseDrawData baseDrawData = iterator.next();
            switch (baseDrawData.getMode()) {
                case NoteApplication.MODE_PATH:
                    PathDrawData pathDrawData = (PathDrawData) baseDrawData;
                    pathDrawData.getPath().transform(matrix);
                    break;
                case NoteApplication.MODE_LINE:
                    break;
                case NoteApplication.MODE_RECTANGLE:
                    break;
                case NoteApplication.MODE_CIRCLE:
                    break;
                case NoteApplication.MODE_ERASER:
                    break;
                case NoteApplication.MODE_SHEAR:
                    ShearDrawData shearDrawData = (ShearDrawData) baseDrawData;
                    shearDrawData.getPath().transform(matrix);
                    break;
                default:
                    Log.e(TAG, "offSetDrawData" + baseDrawData.getMode());
                    break;
            }
        }
    }


    /**
     * 平移数据
     *
     * @param iterator 需要平移的数据源
     * @param offSetX  平移的横坐标X
     * @param offSetY  平移的纵坐标Y
     */
    public void offSetDrawData(Iterator<BaseDrawData> iterator, float offSetX, float offSetY) {
        while (iterator.hasNext()) {
            BaseDrawData baseDrawData = iterator.next();
            baseDrawData.setOffSetX(offSetX);
            baseDrawData.setOffSetY(offSetY);
            switch (baseDrawData.getMode()) {
                case NoteApplication.MODE_PATH:
                    PathDrawData pathDrawData = (PathDrawData) baseDrawData;
                    pathDrawData.getPath().offset(offSetX, offSetY);
                    break;
                case NoteApplication.MODE_LINE:
                    LineDrawData lineDrawData = (LineDrawData) baseDrawData;
                    lineDrawData.offSet(offSetX, offSetY);
                    break;
                case NoteApplication.MODE_RECTANGLE:
                    RectangleDrawData rectangleDrawData = (RectangleDrawData) baseDrawData;
                    rectangleDrawData.offSet(offSetX, offSetY);
                    break;
                case NoteApplication.MODE_CIRCLE:
                    CircleDrawData circleDrawData = (CircleDrawData) baseDrawData;
                    circleDrawData.offSet(offSetX, offSetY);
                    break;
                case NoteApplication.MODE_ERASER:
                    EraserDrawData eraserDrawData = (EraserDrawData) baseDrawData;
                    eraserDrawData.getPath().offset(offSetX, offSetY);
                    break;
                case NoteApplication.MODE_SHEAR:
                    ((ShearDrawData) (baseDrawData)).getPath().offset(offSetX, offSetY);
                    break;
                default:
                    Log.e(TAG, "offSetDrawData" + baseDrawData.getMode());
                    break;
            }
        }
    }

    /**
     * 从Shear的数据里转移到命令数据集
     */
    public void drawDataFromShearToCommand() {
        Iterator<BaseDrawData> iterator = mShearDrawDataList.iterator();
        Command command = new Command();
        command.setCommand(NoteApplication.COMMAND_TRANSLATE);
        while (iterator.hasNext()) {
            BaseDrawData baseDrawData = iterator.next();
            command.setOffSetX(baseDrawData.getOffSetX());
            command.setOffSetY(baseDrawData.getOffSetY());
            switch (baseDrawData.getMode()) {
                case NoteApplication.MODE_PATH:
                    command.getCommandDrawList().add(baseDrawData);
                    break;
                case NoteApplication.MODE_LINE:
                    command.getCommandDrawList().add(baseDrawData);
                    break;
                case NoteApplication.MODE_RECTANGLE:
                    command.getCommandDrawList().add(baseDrawData);
                    break;
                case NoteApplication.MODE_CIRCLE:
                    command.getCommandDrawList().add(baseDrawData);
                    break;
                case NoteApplication.MODE_ERASER:
                    command.getCommandDrawList().add(baseDrawData);
                    break;
                case NoteApplication.MODE_SHEAR:
                    break;
                default:
                    break;
            }
        }
        CommandUtils.getInstance().getUndoCommandList().add(command);

    }

    /**
     * 获取需要生成的XML数据数组
     *
     * @return 点，画笔，模式
     */
    public String[] getXMLData() {
        String[] xmlData = {"", "", ""};
        Iterator<BaseDrawData> iterator = mSaveDrawDataList.iterator();
        while (iterator.hasNext()) {
            BaseDrawData baseDrawData = iterator.next();
            xmlData[1] += baseDrawData.getPaint().getStrokeWidth() + "," + baseDrawData.getPaint().getColor() + "]";
            xmlData[2] += baseDrawData.getMode() + "]";
            switch (baseDrawData.getMode()) {
                case NoteApplication.MODE_PATH:
                    PathDrawData pathDrawData = (PathDrawData) baseDrawData;
                    xmlData[0] += pathDrawData.getPoint();

                    break;
                case NoteApplication.MODE_LINE:
                    LineDrawData lineDrawData = (LineDrawData) baseDrawData;
                    xmlData[0] += (lineDrawData.getStartX() + "," + lineDrawData.getStartY() + "|" + lineDrawData.getEndX() + "," + lineDrawData.getEndY() + "]");

                    break;
                case NoteApplication.MODE_RECTANGLE:
                    RectangleDrawData rectangleDrawData = (RectangleDrawData) baseDrawData;
                    xmlData[0] += (rectangleDrawData.getLeft() + "," + rectangleDrawData.getTop() + "|" + rectangleDrawData.getRight() + "," + rectangleDrawData.getBottom() + "]");
                    break;
                case NoteApplication.MODE_CIRCLE:
                    CircleDrawData circleDrawData = (CircleDrawData) baseDrawData;
                    xmlData[0] += (circleDrawData.getCircleX() + "," + circleDrawData.getCircleY() + "|" + circleDrawData.getRadius() + "," + "0]");
                    break;
                case NoteApplication.MODE_ERASER:
                    EraserDrawData eraserDrawData = (EraserDrawData) baseDrawData;
                    xmlData[0] += eraserDrawData.getPoint();
                    break;
                default:
                    break;
            }
        }
        return xmlData;
    }

    /**
     * 二次编辑XML数据
     *
     * @param xmlPath
     */
    public void structureReReadXMLData(String xmlPath) {
        XMLUtils mXmlUtils = new XMLUtils();
        mXmlUtils.decodeXML(xmlPath);
        String[] paint = mXmlUtils.getPaint().split("]");
        String[] point = mXmlUtils.getPoint().split("]");
        String[] mode = mXmlUtils.getMode().split("]");
        for (int i = 0; i < point.length; ++i) {
            BaseDrawData baseDrawData = null;
            String[] pointXY = point[i].split("\\|");
            Path path = new Path();
            for (int j = 0; j < pointXY.length; ++j) {
                String[] dian = pointXY[j].split(",");
                int modeStyle = Integer.parseInt(mode[i]);

                switch (modeStyle) {
                    case NoteApplication.MODE_PATH:
                        if (j == 0) {
                            baseDrawData = new PathDrawData();
                            ((PathDrawData) baseDrawData).setPoint(point[i] + "]");
                            path.moveTo(Float.parseFloat(dian[0]), Float.parseFloat(dian[1]));
                        } else {
                            path.lineTo(Float.parseFloat(dian[0]), Float.parseFloat(dian[1]));
                            ((PathDrawData) baseDrawData).setPath(path);
                        }
                        break;
                    case NoteApplication.MODE_LINE:

                        if (j == 0) {
                            baseDrawData = new LineDrawData();
                            ((LineDrawData) baseDrawData).setStartX(Float.parseFloat(dian[0]));
                            ((LineDrawData) baseDrawData).setStartY(Float.parseFloat(dian[1]));
                        } else {
                            ((LineDrawData) baseDrawData).setEndX(Float.parseFloat(dian[0]));
                            ((LineDrawData) baseDrawData).setEndY(Float.parseFloat(dian[1]));
                        }
                        break;
                    case NoteApplication.MODE_RECTANGLE:

                        if (j == 0) {
                            baseDrawData = new RectangleDrawData();
                            ((RectangleDrawData) baseDrawData).setLeft(Float.parseFloat(dian[0]));
                            ((RectangleDrawData) baseDrawData).setTop(Float.parseFloat(dian[1]));
                        } else {
                            ((RectangleDrawData) baseDrawData).setRight(Float.parseFloat(dian[0]));
                            ((RectangleDrawData) baseDrawData).setBottom(Float.parseFloat(dian[1]));
                        }

                        break;
                    case NoteApplication.MODE_CIRCLE:

                        if (j == 0) {
                            baseDrawData = new CircleDrawData();
                            ((CircleDrawData) baseDrawData).setCircleX(Float.parseFloat(dian[0]));
                            ((CircleDrawData) baseDrawData).setCircleY(Float.parseFloat(dian[1]));
                        } else {
                            ((CircleDrawData) baseDrawData).setRadius(Float.parseFloat(dian[0]));
                        }
                        break;

                    case NoteApplication.MODE_ERASER:
                        if (j == 0) {
                            baseDrawData = new EraserDrawData();
                            ((EraserDrawData) baseDrawData).setPoint(point[i] + "]");
                            path.moveTo(Float.parseFloat(dian[0]), Float.parseFloat(dian[1]));
                        } else {
                            path.lineTo(Float.parseFloat(dian[0]), Float.parseFloat(dian[1]));
                            ((EraserDrawData) baseDrawData).setPath(path);
                        }
                        break;

                    default:
                        Log.e("modeStyle", modeStyle + "");
                        break;
                }
            }
            Paint paint1 = new Paint();
            paint1.setColor(Integer.parseInt(paint[i].split(",")[1]));
            paint1.setStrokeWidth(Float.parseFloat(paint[i].split(",")[0]));
            paint1.setStyle(Paint.Style.STROKE);
            baseDrawData.setPaint(paint1);
            mSaveDrawDataList.add(baseDrawData);
        }
    }}
