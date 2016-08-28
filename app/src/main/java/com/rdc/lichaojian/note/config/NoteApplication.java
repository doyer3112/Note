package com.rdc.lichaojian.note.config;

import android.app.Application;
import android.content.Context;
import android.os.Environment;

import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

/**
 * Created by lichaojian on 16-8-28.
 */
public class NoteApplication extends Application{

    public static final int MODE_PATH = 0;
    public static final int MODE_LINE = 1;
    public static final int MODE_RECTANGLE = 2;
    public static final int MODE_CIRCLE = 3;
    public static final int MODE_SHEAR = 4;
    public static final int MODE_ERASER = 5;

    public static final int CANVAS_NORMAL = 0;
    public static final int CANVAS_UNDO = 1;
    public static final int CANVAS_REDO = 2;
    public static final int CANVAS_RESET = 3;

    public static final int COMMAND_ADD = 0;
    public static final int COMMAND_TRANSLATE = 4;
    public static final int COMMAND_SCALE = 5;

    public static final String ROOT_DIRECTORY = Environment.getExternalStorageDirectory().toString() + "/note";
    public static final String TEMPORARY_PATH = ROOT_DIRECTORY + "/temporary";
    public static final int OK = 1;
    public static final int CANCEL = -1;

    public static ImageLoader getInstance(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(context)
                .memoryCacheExtraOptions(480, 800) // max width, max height，即保存的每个缓存文件的最大长宽
                .threadPoolSize(3)//线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // You can pass your own memory cache implementation/你可以通过自己的内存缓存实现
                .memoryCacheSize(2 * 1024 * 1024)
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .imageDownloader(new BaseImageDownloader(context, 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间
                .writeDebugLogs() // Remove for release app
                .build();//开始构建

        ImageLoader.getInstance().init(config);
        return ImageLoader.getInstance();
    }
}
