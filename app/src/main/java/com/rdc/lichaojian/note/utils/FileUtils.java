package com.rdc.lichaojian.note.utils;

import android.graphics.Bitmap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lichaojian on 16-8-28.
 */
public class FileUtils {
    /**
     * 获取note文件夹下面的所有图片
     *
     * @param path 文件夹路径
     * @return
     */
    public static List<Map<String, String>> getPicturesPath(String path) {
        List<Map<String, String>> list = new ArrayList<>();
        Map map ;
        File file = new File(path);
        File[] allFiles = file.listFiles();
        if (allFiles == null){
            return list;
        }
        for (int i = 0; i < allFiles.length; ++i) {
            if (allFiles[i].getPath().contains("png")) {
                map = new HashMap();
                map.put("filePath", "null");
                map.put("picturePath", allFiles[i].getPath());
                list.add(map);
            } else if (!allFiles[i].getPath().contains("xml") && !allFiles[i].getPath().contains("svg") && !allFiles[i].getPath().contains("png")) {
                for (int j = 0; j < allFiles[i].listFiles().length; ++j) {
                    if (allFiles[i].listFiles()[j].getPath().contains("png")) {
                        map = new HashMap();
                        map.put("filePath", allFiles[i].getPath());
                        map.put("picturePath", allFiles[i].listFiles()[j].getPath());
                        list.add(map);
                        break;
                    }
                }

            }

        }
        return list;
    }

    /**
     * 把bitmap封装成图片
     *
     * @param bitmap
     * @param path
     * @param rootPath
     * @return
     */
    public static String saveAsPng(Bitmap bitmap, String path, String rootPath) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date currentDate = new Date(System.currentTimeMillis());
        String fileName = null;
        if (path != null) {
            fileName = path.split("/")[path.split("/").length - 1];
            fileName = fileName.substring(0, fileName.length() - 4);
        } else {
            fileName = simpleDateFormat.format(currentDate);
        }
        File rootFile = new File(rootPath);
        File file = new File(rootPath, fileName + ".png");
        if (!rootFile.exists()) {
            rootFile.mkdir();
        } else {
            if (file.exists()) {
                file.delete();
                File xmlFile = new File(rootPath, fileName + ".xml");
                if (xmlFile.exists())
                    xmlFile.delete();
            }
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return fileName;
    }

    /**
     * 复制文件夹
     *
     * @param oldPath 临时文件夹的路径
     * @param newPath 放note文件夹的路径
     * @return
     */
    public static boolean fileCopy(String oldPath, String newPath) {
        boolean isOk = true;
        try {
            (new File(newPath)).mkdirs(); //如果文件夹不存在 则建立新文件夹
            File a = new File(oldPath);
            String[] file = a.list();
            File temp = null;
            for (int i = 0; i < file.length; i++) {
                if (oldPath.endsWith(File.separator)) {
                    temp = new File(oldPath + file[i]);
                } else {
                    temp = new File(oldPath + File.separator + file[i]);
                }

                if (temp.isFile()) {
                    FileInputStream input = new FileInputStream(temp);
                    FileOutputStream output = new FileOutputStream(newPath + "/" +
                            (temp.getName()).toString());
                    byte[] b = new byte[1024 * 5];
                    int len;
                    while ((len = input.read(b)) != -1) {
                        output.write(b, 0, len);
                    }
                    output.flush();
                    output.close();
                    input.close();
                }
                if (temp.isDirectory()) {//如果是子文件夹
                    fileCopy(oldPath + "/" + file[i], newPath + "/" + file[i]);
                }
            }
        } catch (Exception e) {
            isOk = false;
        }
        return isOk;
    }

    /**
     * 删除文件夹下的文件
     *
     * @param file
     */
    public static void delete(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }

        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                file.delete();
                return;
            }

            for (int i = 0; i < childFiles.length; i++) {
                delete(childFiles[i]);
            }
            file.delete();
        }
    }
}
