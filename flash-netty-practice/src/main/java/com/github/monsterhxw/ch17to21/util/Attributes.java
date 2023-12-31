package com.github.monsterhxw.ch17to21.util;

import com.github.monsterhxw.ch17to21.session.Session;
import io.netty.util.AttributeKey;

/**
 * @author huangxuewei
 * @since 2023/9/6
 */
public interface Attributes {

    AttributeKey<Boolean> LOGIN = AttributeKey.newInstance("LOGIN");

    AttributeKey<Session> SESSION = AttributeKey.newInstance("SESSION");
}
