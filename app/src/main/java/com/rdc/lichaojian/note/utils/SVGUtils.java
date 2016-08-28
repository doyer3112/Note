package com.rdc.lichaojian.note.utils;

import android.graphics.Paint;
import android.util.Log;

import com.rdc.lichaojian.note.config.NoteApplication;
import com.rdc.lichaojian.note.data.BaseDrawData;
import com.rdc.lichaojian.note.data.CircleDrawData;
import com.rdc.lichaojian.note.data.EraserDrawData;
import com.rdc.lichaojian.note.data.LineDrawData;
import com.rdc.lichaojian.note.data.PathDrawData;
import com.rdc.lichaojian.note.data.RectangleDrawData;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Created by lichaojian on 16-8-28.
 */
public class SVGUtils {
    private static final String TAG = "SVGUtils";
    private String d = "";
    private Document document;
    private String mElementSVG = "svg";
    private String mElementPath = "path";
    private String mElementLine = "line";
    private String mElementRect = "rect";
    private String mElementCircle = "circle";

    public SVGUtils() {
        try {
            DocumentBuilderFactory dom = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = dom.newDocumentBuilder();
            document = builder.newDocument();
        } catch (ParserConfigurationException e) {
            Log.e(TAG, e.toString());
        }
    }

    public void encodeSVG(List<BaseDrawData> list, String path) {
        int size = list.size();
        Element svg = document.createElement(mElementSVG);
        Element svgSon = null;
        svg.setAttribute("xmlns", "http://www.w3.org/2000/svg");
        svg.setAttribute("version", "1.1");

        for (int i = 0; i < size; ++i) {
            BaseDrawData baseDrawData = list.get(i);
            Paint paint = baseDrawData.getPaint();
            float strokeWidth = paint.getStrokeWidth();
            String fill = "none";
            switch (baseDrawData.getMode()) {
                case NoteApplication.MODE_PATH:
                    PathDrawData pathDrawData = (PathDrawData) baseDrawData;
                    svgSon = document.createElement(mElementPath);
                    svgSon.setAttribute("d", structureSVGPath(pathDrawData.getPoint()));
                    break;
                case NoteApplication.MODE_LINE:
                    LineDrawData lineDrawData = (LineDrawData) baseDrawData;
                    svgSon = document.createElement(mElementLine);
                    svgSon.setAttribute("x1", lineDrawData.getStartX() + "");
                    svgSon.setAttribute("y1", lineDrawData.getStartY() + "");
                    svgSon.setAttribute("x2", lineDrawData.getEndX() + "");
                    svgSon.setAttribute("y2", lineDrawData.getEndY() + "");
                    break;
                case NoteApplication.MODE_RECTANGLE:
                    RectangleDrawData rectangleDrawData = (RectangleDrawData) baseDrawData;
                    svgSon = document.createElement(mElementRect);
                    svgSon.setAttribute("x", rectangleDrawData.getLeft() + "");
                    svgSon.setAttribute("y", rectangleDrawData.getTop() + "");
                    svgSon.setAttribute("width", (rectangleDrawData.getRight() - rectangleDrawData.getLeft()) + "");
                    svgSon.setAttribute("height", (rectangleDrawData.getBottom() - rectangleDrawData.getTop()) + "");
                    break;
                case NoteApplication.MODE_CIRCLE:
                    CircleDrawData circleDrawData = (CircleDrawData) baseDrawData;
                    svgSon = document.createElement(mElementCircle);
                    svgSon.setAttribute("cx", circleDrawData.getCircleX() + "");
                    svgSon.setAttribute("cy", circleDrawData.getCircleY() + "");
                    svgSon.setAttribute("r", circleDrawData.getRadius() + "");
                    break;
                case NoteApplication.MODE_ERASER:
                    EraserDrawData eraserPathDrawData = (EraserDrawData) baseDrawData;
                    svgSon = document.createElement(mElementPath);
                    svgSon.setAttribute("d", structureSVGPath(eraserPathDrawData.getPoint()));
                    break;
                default:
                    Log.e(TAG, Integer.toString(baseDrawData.getMode()));
                    break;
            }
            svgSon.setAttribute("stroke", "green");
            svgSon.setAttribute("stroke-width", strokeWidth + "");
            svgSon.setAttribute("fill", fill);
            svg.appendChild(svgSon);
        }
        document.appendChild(svg);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        try {
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            PrintWriter printWriter = new PrintWriter(new FileOutputStream(path));
            StreamResult result = new StreamResult(printWriter);
            transformer.transform(domSource, result);
        } catch (TransformerConfigurationException e) {
            Log.e(TAG, e.toString());
        } catch (FileNotFoundException e) {
            Log.e(TAG, e.toString());
        } catch (TransformerException e) {
            Log.e(TAG, e.toString());
        }
    }

    /**
     * 构造svg格式的Path
     *
     * @param point path里面所有的点
     * @return
     */
    private String structureSVGPath(String point) {
        String[] arrPoint = point.split("]");

        for (int i = 0; i < arrPoint.length; ++i) {
            String[] strPoint = arrPoint[i].split("\\|");
            for (int j = 0; j < strPoint.length; ++j) {
                String[] pointXY = strPoint[j].split(",");
                if (j == 0) {
                    d += "M " + pointXY[0] + " " + pointXY[1] + " ";
                } else {
                    if (j == strPoint.length - 1) {
                        d += "M " + pointXY[0] + " " + pointXY[1] + "Z";
                    } else {
                        d += "L " + pointXY[0] + " " + pointXY[1] + " ";
                    }
                }
            }
        }
        return d;
    }
}
