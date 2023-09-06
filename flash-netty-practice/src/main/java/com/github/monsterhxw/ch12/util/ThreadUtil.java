package com.github.monsterhxw.ch12.util;

/**
 * @author huangxuewei
 * @since 2023/9/6
 */
public class ThreadUtil {

    public static String getThreadName(String name) {
        return name + "-[" + Thread.currentThread().getName() + "]-" + System.currentTimeMillis() + "-";
    }
}
