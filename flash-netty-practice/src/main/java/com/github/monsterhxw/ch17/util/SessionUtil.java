package com.github.monsterhxw.ch17.util;

import com.github.monsterhxw.ch17.session.Session;
import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author huangxuewei
 * @since 2023/9/7
 */
public class SessionUtil {

    private static final Map<Integer, Channel> channelMap = new ConcurrentHashMap<>();

    public static void bindSession(Session session, Channel channel) {
        channelMap.put(session.getUserId(), channel);
        channel.attr(Attributes.SESSION).set(session);
    }

    public static void unbindSession(Channel channel) {
        if (existsSession(channel)) {
            channelMap.remove(channel.attr(Attributes.SESSION).get().getUserId());
            channel.attr(Attributes.SESSION).set(null);
        }
    }

    public static boolean existsSession(Channel channel) {
        return channel.hasAttr(Attributes.SESSION);
    }

    public static Session getSession(Channel channel) {
        return channel.attr(Attributes.SESSION).get();
    }

    public static Channel getChannel(int userId) {
        return channelMap.get(userId);
    }
}
