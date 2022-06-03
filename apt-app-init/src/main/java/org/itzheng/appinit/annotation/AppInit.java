package org.itzheng.appinit.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 初始化注解，需要初始化的类添加这个注解，并且实现 {@link org.itzheng.appinit.IAppInit}
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface AppInit {
//    String name() default "";

    /**
     * 优先级，默认 0 ，值越大越先执行
     *
     * @return
     */
    int priority() default 0;

}