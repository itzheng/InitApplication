package org.itzheng.appinit;

import java.util.Date;

/**
 * 常量，用于定义自动生成类的位置，名称
 */
public final class AutoConstant {

    private AutoConstant() {

    }

    public static AutoConstant getInstance() {
        return Holder._instance;
    }

    private static class Holder {
        public static final AutoConstant _instance = new AutoConstant();
    }

    /**
     * 字段：类名
     */
    public final String AUTO_CLASS_FIELD_CLASS_NAME = "className";
    /**
     * 字段 优先级
     */
    public final String AUTO_CLASS_FIELD_PRIORITY = "priority";

    /**
     * 自动生成的包名，就是存放位置
     * 在 build\generated\ap_generated_sources\debug\out 目录下
     */
//    public final String AUTO_PACKAGE_NAME = "org.itzheng.appinit.annotations.auto";
    public final String AUTO_CLASS_PACKAGE_NAME = getPackageName();
    /**
     * 前缀，避免用户定义一样的类，也是为了方便查看
     */
    private final String SUFFIX = "AutoClass";

    public final String getNewClassName(String targetModuleName) {
        if (targetModuleName == null || targetModuleName.isEmpty()) {
            return SUFFIX + "_" + getCurrentDate();
        }
        targetModuleName = targetModuleName.replace(" ", "");
        if (targetModuleName == null || targetModuleName.isEmpty()) {
            return getNewClassName(targetModuleName);
        }
        return SUFFIX + "_" + targetModuleName + "_" + getCurrentDate();
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    private String getCurrentDate() {
        Date date = new Date();
        //time 是 13 位数 ，保留7位数足够了
        return date.getTime() % 10_000_000 + "";
    }

    private String getPackageName() {
//        return getClass().getPackage().toString();
        return "org.itzheng.appinit.annotations.auto";
    }


}
