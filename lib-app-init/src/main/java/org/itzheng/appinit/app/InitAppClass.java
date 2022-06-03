package org.itzheng.appinit.app;

import android.util.Log;

import org.itzheng.appinit.AutoConstant;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dalvik.system.DexFile;

public class InitAppClass {
    private static final String TAG = "lib-app-init";

    public static InitAppClass getInstance() {
        return Holder._instance;
    }

    public void initAll() {
        final AutoConstant autoConstant = AutoConstant.getInstance();
        List<String> strings = getClassByPackageName(autoConstant.AUTO_CLASS_PACKAGE_NAME);
        List<HashMap<String, Object>> allResult = new ArrayList<>();
        for (String cls : strings) {
            Log.w(TAG, "initAll: " + cls);
            try {
                List<HashMap<String, Object>> result = getAllClass(cls);
                if (result != null) {
                    allResult.addAll(result);
                }
//                invokeMethodNoArgs(cls, "addAll");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

//        for (HashMap<String, Object> map : allResult) {
//            Log.w(TAG, "allResult: " + map.get(autoConstant.AUTO_CLASS_FIELD_CLASS_NAME));
//            Log.w(TAG, "allResult: " + map.get(autoConstant.AUTO_CLASS_FIELD_PRIORITY));
//        }
        Collections.sort(allResult, new Comparator<HashMap<String, Object>>() {
            @Override
            public int compare(HashMap<String, Object> o1, HashMap<String, Object> o2) {
                int a = (int) o1.get(autoConstant.AUTO_CLASS_FIELD_PRIORITY);
                int b = (int) o2.get(autoConstant.AUTO_CLASS_FIELD_PRIORITY);
                //数字越大越考前
                return b - a;
            }
        });

        for (HashMap<String, Object> map : allResult) {
            Log.w(TAG, "allResult: " + map.get(autoConstant.AUTO_CLASS_FIELD_CLASS_NAME));
            Log.w(TAG, "allResult: " + map.get(autoConstant.AUTO_CLASS_FIELD_PRIORITY));
        }
        initMap(allResult);
    }

    private void initMap(List<HashMap<String, Object>> allResult) {
        final AutoConstant autoConstant = AutoConstant.getInstance();
        for (HashMap<String, Object> map : allResult) {
            String classname = (String) map.get(autoConstant.AUTO_CLASS_FIELD_CLASS_NAME);
            try {
                invokeMethodNoArgs(classname, "init");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private List<HashMap<String, Object>> getAllClass(String clsName) throws Exception {
        //动态加载类，获取当前类的Class对象
        Class cls = Class.forName(clsName);
        //取得类方法
        Method[] methods = cls.getDeclaredMethods();
        Object instance = cls.newInstance();
        //获取类 获取本地方法
        Method method_addAll = cls.getDeclaredMethod("addAll");
        //获取父方法
        Method method_getAll = cls.getSuperclass().getMethod("getAll");
        //通过实例化的对象，调用无参数的方法
        method_addAll.invoke(instance);
        Object result = method_getAll.invoke(instance);
        if (result != null) {
            return (List<HashMap<String, Object>>) result;
        }
        return null;
    }

    private static class Holder {
        public static final InitAppClass _instance = new InitAppClass();
    }

//    private void get() {
//        Context context = AppContentProvider.getApplicationContext();
//        PathClassLoader pathClassLoader = new PathClassLoader(context.getPackageCodePath(),
//                context.getClassLoader());
//    }

    /**
     * 执行无参方法
     *
     * @param clsName
     * @param methodName
     * @return
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    private Object invokeMethodNoArgs(String clsName, String methodName)
            throws ClassNotFoundException, NoSuchMethodException, InstantiationException,
            IllegalAccessException, InvocationTargetException {
        //动态加载类，获取当前类的Class对象
        Class cls = Class.forName(clsName);
        //获取类 获取本地方法
        Method methods = cls.getMethod(methodName);
        //通过实例化的对象，调用无参数的方法
        return methods.invoke(cls.newInstance());
    }

    private List<String> getClassByPackageName(String packageName) {
        List<String> classNameList = new ArrayList<String>();
        try {

            DexFile df = new DexFile(AppContentProvider.getApplicationContext().getPackageCodePath());//通过DexFile查找当前的APK中可执行文件
            Enumeration<String> enumeration = df.entries();//获取df中的元素  这里包含了所有可执行的类名 该类名包含了包名+类名的方式
            while (enumeration.hasMoreElements()) {//遍历
                String className = (String) enumeration.nextElement();

                if (className.contains(packageName)) {//在当前所有可执行的类里面查找包含有该包名的所有类
                    classNameList.add(className);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return classNameList;
    }
}
