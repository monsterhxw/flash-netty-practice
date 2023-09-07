package com.github.monsterhxw.ch17to18.util;

import com.github.monsterhxw.ch17to18.session.Session;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author huangxuewei
 * @since 2023/9/7
 */
public class SessionUtil {

    private static final Map<Integer, Channel> CHANNEL_MAP = new ConcurrentHashMap<>();

    private static final Map<String, ChannelGroup> CHANNEL_GROUP_MAP = new ConcurrentHashMap<>();

    public static void bindSession(Session session, Channel channel) {
        CHANNEL_MAP.put(session.getUserId(), channel);
        channel.attr(Attributes.SESSION).set(session);
    }

    public static void unbindSession(Channel channel) {
        if (existsSession(channel)) {
            CHANNEL_MAP.remove(channel.attr(Attributes.SESSION).get().getUserId());
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
        return CHANNEL_MAP.get(userId);
    }

    public static void bindChannelGroup(String groupId, ChannelGroup channelGroup) {
        CHANNEL_GROUP_MAP.put(groupId, channelGroup);
    }

    public static ChannelGroup getChannelGroup(String groupId) {
        return CHANNEL_GROUP_MAP.get(groupId);
    }
}
