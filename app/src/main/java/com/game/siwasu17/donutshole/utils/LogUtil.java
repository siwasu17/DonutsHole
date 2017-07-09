package com.game.siwasu17.donutshole.utils;


import com.game.siwasu17.donutshole.BuildConfig;

public class LogUtil {
    private static final int MAX_TAG_SIZE = 23;
    public static final String PACKAGE_NAME = BuildConfig.APPLICATION_ID;

    public static String TAG(Object obj) {
        String objName = obj.getClass().getSimpleName();
        return objName.length() > MAX_TAG_SIZE ? objName.substring(0, MAX_TAG_SIZE) : objName;
    }

    public static String CLASS_LINE() {
        try {
            StackTraceElement[] elements = Thread.currentThread().getStackTrace();
            for (StackTraceElement ste : elements) {
                String cls = ste.getClassName();
                if (cls.startsWith(PACKAGE_NAME)) {
                    cls = cls.replace(PACKAGE_NAME, "");
                    return cls + "." + ste.getMethodName() + "(" + ste.getLineNumber() + ")";
                }
            }
        } catch (Exception e) {
            return "null";
        }
        return "null";
    }

    public static String METHOD_INFO() {
        try {
            StackTraceElement[] elements = Thread.currentThread().getStackTrace();
            for (StackTraceElement ste : elements) {
                if (ste.getClassName().startsWith(PACKAGE_NAME)) return ste.toString() + "\n";
            }
        } catch (Exception e) {
            return "null";
        }
        return "null";
    }

    public static String CALLED_BY() {
        try {
            StackTraceElement[] elements = Thread.currentThread().getStackTrace();

            boolean breakFlg = false;
            for (StackTraceElement ste : elements) {
                String cls = ste.getClassName();
                if (breakFlg) {
                    cls = cls.replace(PACKAGE_NAME, "");
                    return cls + "." + ste.getMethodName() + "(" + ste.getLineNumber() + ")";
                }
                if (cls.startsWith(PACKAGE_NAME)) breakFlg = true;
            }
        } catch (Exception e) {
            return "null";
        }
        return "null";
    }
}