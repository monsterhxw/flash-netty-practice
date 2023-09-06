package com.github.monsterhxw.ch13to15.server.handler;

import com.github.monsterhxw.ch13to15.util.LoginUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import static com.github.monsterhxw.ch13to15.util.ThreadUtil.getThreadName;

/**
 * @author huangxuewei
 * @since 2023/9/6
 */
public class AuthHandler extends SimpleChannelInboundHandler<Object> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object object) throws Exception {
        if (!LoginUtil.isLogin(ctx.channel())) {
            System.out.println(getThreadName("Server") + ctx.channel() + "需要登录: ");
            ctx.channel().close();
        }
        System.out.println(getThreadName("Server") + "AuthHandler: " + ctx.channel() + "已登录");
        ctx.pipeline().remove(this);
        ctx.fireChannelRead(object);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        if (LoginUtil.isLogin(ctx.channel())) {
            System.out.println(getThreadName("Server") + "AuthHandler: 当前连接登录验证完毕，无需再次验证, AuthHandler 已移除!");
        } else {
            System.out.println(getThreadName("Server") + "AuthHandler: 无登录验证，强制关闭连接!");
        }
    }
}
