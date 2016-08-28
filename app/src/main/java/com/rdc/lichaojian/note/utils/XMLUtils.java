package com.rdc.lichaojian.note.utils;

import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Created by lichaojian on 16-8-28.
 */
public class XMLUtils {
    private static final String TAG = "XMLUtils";
    private String point = null;
    private String paint = null;
    private String mode = null;
    private Document document;
    private String mElementRoot = "root";
    private String mElementData = "data";
    private String mElementPoint = "point";
    private String mElementPaint = "paint";
    private String mElementMode = "mode";

    public XMLUtils() {
        try {
            DocumentBuilderFactory dom = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = dom.newDocumentBuilder();
            document = builder.newDocument();
        } catch (ParserConfigurationException e) {
            Log.e(TAG, e.toString());
        }
    }

    public boolean encodeXML(String xmlPath, String strPoint, String strPaint, String strMode) {
        Element root = document.createElement(mElementRoot);
        document.appendChild(root);
        Element data = document.createElement(mElementData);
        Element point = document.createElement(mElementPoint);
        point.appendChild(document.createTextNode(strPoint));
        data.appendChild(point);
        Element paint = document.createElement(mElementPaint);
        paint.appendChild(document.createTextNode(strPaint));
        data.appendChild(paint);
        Element mode = document.createElement(mElementMode);
        mode.appendChild(document.createTextNode(strMode));
        data.appendChild(mode);
        root.appendChild(data);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        try {
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            transformer.setOutputProperty(OutputKeys.ENCODING, "gb2312");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            PrintWriter printWriter = new PrintWriter(new FileOutputStream(xmlPath));
            StreamResult result = new StreamResult(printWriter);
            transformer.transform(domSource, result);

        } catch (TransformerConfigurationException e) {
            Log.e(TAG, e.toString());
            return false;
        } catch (FileNotFoundException e) {
            Log.e(TAG, e.toString());
            return false;
        } catch (TransformerException e) {
            Log.e(TAG, e.toString());
            return false;
        }
        return true;
    }


    public void decodeXML(String xmlPath) {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(xmlPath);
            NodeList rootList = document.getChildNodes();
            for (int i = 0; i < rootList.getLength(); ++i) {
                Node root = rootList.item(i);
                NodeList dataList = root.getChildNodes();
                for (int j = 0; j < dataList.getLength(); ++j) {
                    Node data = dataList.item(j);
                    NodeList nodeList = data.getChildNodes();
                    for (int k = 0; k < nodeList.getLength(); ++k) {
                        Log.e(nodeList.item(k).getNodeName(), ";" + nodeList.item(k).getTextContent());
                        if (nodeList.item(k).getNodeName().equals(mElementPaint)) {
                            setPaint(nodeList.item(k).getTextContent());
                        }
                        if (nodeList.item(k).getNodeName().equals(mElementPoint)) {
                            setPoint(nodeList.item(k).getTextContent());
                        }
                        if (nodeList.item(k).getNodeName().equals(mElementMode)) {
                            setMode(nodeList.item(k).getTextContent());
                        }
                    }
                }
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getPaint() {
        return paint;
    }

    public void setPaint(String paint) {
        this.paint = paint;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}
