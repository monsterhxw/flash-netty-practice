package com.github.monsterhxw.ch13to15.util;

import io.netty.channel.Channel;
import io.netty.util.Attribute;

/**
 * @author huangxuewei
 * @since 2023/9/6
 */
public class LoginUtil {

    public static void markAsLogin(Channel channel) {
        channel.attr(Attributes.LOGIN).set(true);
    }

    public static boolean isLogin(Channel channel) {
        Attribute<Boolean> attr = channel.attr(Attributes.LOGIN);
        return attr.get() != null && attr.get();
    }
}
