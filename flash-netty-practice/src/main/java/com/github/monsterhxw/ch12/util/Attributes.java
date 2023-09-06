package com.github.monsterhxw.ch12.util;

import io.netty.util.AttributeKey;

/**
 * @author huangxuewei
 * @since 2023/9/6
 */
public interface Attributes {

    AttributeKey<Boolean> LOGIN = AttributeKey.newInstance("LOGIN");
}
