package com.github.monsterhxw.ch13to16.server.handler;

import com.github.monsterhxw.ch13to16.util.ThreadUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author huangxuewei
 * @since 2023/9/6
 */
public class LifeCycleTestHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ThreadUtil.getThreadName("Server") + "===============================>【逻辑处理器被添加: handlerAdded()】");
        super.handlerAdded(ctx);
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ThreadUtil.getThreadName("Server") + "===============================>【channel 绑定到线程 (NioEventLoop): channelRegistered()】");
        super.channelRegistered(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ThreadUtil.getThreadName("Server") + "===============================>【Channel 准备就绪: channelActive()】");
        super.channelActive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println(ThreadUtil.getThreadName("Server") + "===============================>【Channel 有数据可读: channelRead()】");
        super.channelRead(ctx, msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ThreadUtil.getThreadName("Server") + "===============================>【Channel 读完数据之后: channelReadComplete()】");
        super.channelReadComplete(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ThreadUtil.getThreadName("Server") + "===============================>【Channel 被关闭: channelInactive()】");
        super.channelInactive(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ThreadUtil.getThreadName("Server") + "===============================>【Channel 取消线程 (NioEventLoop) 的绑定: channelUnregistered()】");
        super.channelUnregistered(ctx);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ThreadUtil.getThreadName("Server") + "===============================>【逻辑处理器被移除: handlerRemoved()】");
        super.handlerRemoved(ctx);
    }
}
