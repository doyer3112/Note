package com.rdc.lichaojian.note.utils;

import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.Log;

import com.rdc.lichaojian.note.config.NoteApplication;
import com.rdc.lichaojian.note.data.BaseDrawData;
import com.rdc.lichaojian.note.data.CircleDrawData;
import com.rdc.lichaojian.note.data.LineDrawData;
import com.rdc.lichaojian.note.data.PathDrawData;
import com.rdc.lichaojian.note.data.RectangleDrawData;

import java.util.Iterator;

/**
 * Created by lichaojian on 16-8-28.
 */
public class JudgeAreaUtils {
    private static final String TAG = "JudgeAreaUtils";
    private static JudgeAreaUtils mJudgeAreaUtils = null;

    private JudgeAreaUtils() {

    }

    public static JudgeAreaUtils getInstance() {
        if (mJudgeAreaUtils == null) {
            mJudgeAreaUtils = new JudgeAreaUtils();
        }
        return mJudgeAreaUtils;
    }

    /**
     * 判断数据域里面有路径是在剪切区域内的
     *
     * @param shearPath
     * @return
     */
    public boolean isDrawDataInShearPath(Path shearPath) {

        Iterator<BaseDrawData> iterator = DrawDataUtils.getInstance().getSaveDrawDataList().iterator();
        while (iterator.hasNext()) {
            BaseDrawData baseDrawData = iterator.next();
            switch (baseDrawData.getMode()) {
                case NoteApplication.MODE_PATH:
                    baseDrawData.setInShear(judgePath((PathDrawData) baseDrawData, shearPath));
                    break;
                case NoteApplication.MODE_LINE:
                    baseDrawData.setInShear(judgeLine((LineDrawData) baseDrawData, shearPath));
                    break;
                case NoteApplication.MODE_RECTANGLE:
                    baseDrawData.setInShear(judgeRectangle((RectangleDrawData) baseDrawData, shearPath));
                    break;
                case NoteApplication.MODE_CIRCLE:
                    baseDrawData.setInShear(judgeCircle((CircleDrawData) baseDrawData, shearPath));
                    break;
                default:
                    Log.e(TAG, "isDrawDataInShearPath" + baseDrawData.getMode());
                    break;
            }
        }
        return false;
    }

    /**
     * 判断某点是否落在shearPath区域内
     *
     * @param x         点的横坐标
     * @param y         点的纵坐标
     * @param shearPath 构造的区域空间
     * @return
     */
    public boolean isInShearLocation(int x, int y, Path shearPath) {
        RectF r = new RectF();
        //计算控制点的边界
        shearPath.computeBounds(r, true);
        //设置区域路径和剪辑描述的区域
        Region re = new Region();
        re.setPath(shearPath, new Region((int) r.left, (int) r.top, (int) r.right, (int) r.bottom));
        return re.contains(x, y);
    }

    /**
     * 判断path里面的所有点是否都在该剪切区域内
     *
     * @param pathDrawData
     * @param shearPath
     * @return
     */
    private boolean judgePath(PathDrawData pathDrawData, Path shearPath) {
        boolean isInShear = false;
        String point = pathDrawData.getPoint();
        String[] arrPoint = point.split("]");
        for (int i = 0; i < arrPoint.length; ++i) {
            String[] pointXY = arrPoint[i].split("\\|");
            for (int j = 0; j < pointXY.length; ++j) {
                String pointX = pointXY[j].split(",")[0];
                String pointY = pointXY[j].split(",")[1];
                float fX = Float.parseFloat(pointX);
                float fY = Float.parseFloat(pointY);
                int x = Math.round(fX);
                int y = Math.round(fY);
                if (isInShearLocation(x, y, shearPath)) {
                    isInShear = true;
                } else {
                    return false;
                }
            }
        }
        return isInShear;
    }

    /**
     * 判断该线是否在该剪切区域内
     *
     * @param lineDrawData
     * @param shearPath
     * @return
     */
    private boolean judgeLine(LineDrawData lineDrawData, Path shearPath) {
        float fsx = lineDrawData.getStartX();
        float fsy = lineDrawData.getStartY();
        float fex = lineDrawData.getEndX();
        float fey = lineDrawData.getEndY();

        if (isInShearLocation(Math.round(fsx), Math.round(fsy), shearPath) && isInShearLocation(Math.round(fex), Math.round(fey), shearPath)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param rectangleDrawData
     * @param shearPath
     * @return
     */
    private boolean judgeRectangle(RectangleDrawData rectangleDrawData, Path shearPath) {
        boolean isInShear = false;
        float[] points = {rectangleDrawData.getLeft(), rectangleDrawData.getTop(),
                rectangleDrawData.getRight(), rectangleDrawData.getTop(),
                rectangleDrawData.getRight(), rectangleDrawData.getBottom(),
                rectangleDrawData.getLeft(), rectangleDrawData.getBottom()};
        for (int i = 0; i < 8; i += 2) {
            if (isInShearLocation(Math.round(points[i]), Math.round(Math.round(points[i + 1])), shearPath)) {
                isInShear = true;
            } else {
                return false;
            }
        }
        return isInShear;
    }

    /**
     * 判断圆形的四个切点是否在该区域内
     *
     * @param circleDrawData
     * @param shearPath
     * @return
     */
    private boolean judgeCircle(CircleDrawData circleDrawData, Path shearPath) {
        boolean isInShear = false;
        float[] points = {circleDrawData.getCircleX() - circleDrawData.getRadius(), circleDrawData.getCircleY(),
                circleDrawData.getCircleX(), circleDrawData.getCircleY() + circleDrawData.getRadius(),
                circleDrawData.getCircleX() + circleDrawData.getRadius(), circleDrawData.getCircleY(),
                circleDrawData.getCircleX(), circleDrawData.getCircleY() - circleDrawData.getRadius()};

        for (int i = 0; i < 8; i += 2) {
            if (isInShearLocation(Math.round(points[i]), Math.round(Math.round(points[i + 1])), shearPath)) {
                isInShear = true;
            } else {
                return false;
            }
        }
        return isInShear;
    }
}
