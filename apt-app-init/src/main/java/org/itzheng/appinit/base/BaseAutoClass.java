package org.itzheng.appinit.base;

import org.itzheng.appinit.AutoConstant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 封装add 函数，方便调用
 */
public abstract class BaseAutoClass {
    public BaseAutoClass() {
    }

    /**
     * 将所有需要添加的类放到这里，到时候直接 addAll
     */
    public abstract void addAll();

    private List<HashMap<String, Object>> mInitClasses = new ArrayList<>();

    /**
     * 定义 add 方法，方便添加 注解方法
     *
     * @param clsName
     * @param priority
     */
    public void add(String clsName, int priority) {
        HashMap<String, Object> map = new HashMap<>();
        map.put(AutoConstant.getInstance().AUTO_CLASS_FIELD_CLASS_NAME, clsName);
        map.put(AutoConstant.getInstance().AUTO_CLASS_FIELD_PRIORITY, priority);
        mInitClasses.add(map);
    }

    /**
     * 获取所有类
     *
     * @return
     */
    public List<HashMap<String, Object>> getAll() {
        return mInitClasses;
    }

//    /**
//     * 用于保存所有带注解的类
//     */
//    private class _InitClass {
//        public String name;
//        public int priority;
//
//        public _InitClass(String fullClassName, int priority) {
//            this.name = fullClassName;
//            this.priority = priority;
//        }
//    }

}
