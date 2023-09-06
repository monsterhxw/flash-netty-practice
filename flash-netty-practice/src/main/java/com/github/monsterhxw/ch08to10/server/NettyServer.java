package com.github.monsterhxw.ch08to10.server;

import com.github.monsterhxw.ch08to10.proto.*;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

public class NettyServer {

    private static final int port = 8080;

    public static void main(String[] args) {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();

        serverBootstrap
                .group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .handler(new ChannelInitializer<ServerSocketChannel>() {
                    @Override
                    protected void initChannel(ServerSocketChannel ch) throws Exception {
                        System.out.println(getThreadName() + "init server(listen) socket: " + ch);
                    }
                })
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        System.out.println(getThreadName() + "init client socket: " + ch);
                        ch.pipeline().addLast(new ServerChannelInboundHandler(bossGroup, workerGroup));
                    }
                });

        bind(serverBootstrap, port);
    }

    private static String getThreadName() {
        return "Server-[" + Thread.currentThread().getName() + "]-" + System.currentTimeMillis() + "-";
    }

    private static void bind(final ServerBootstrap serverBootstrap, final int port) {
        if (port <= 65_535) {
            serverBootstrap.bind(port).addListener(new GenericFutureListener<Future<? super Void>>() {
                @Override
                public void operationComplete(Future<? super Void> future) throws Exception {
                    boolean success = future.isSuccess();
                    if (success) {
                        System.out.println(getThreadName() + "bind [" + port + "] success!");
                    } else {
                        System.out.println(getThreadName() + "bind [" + port + "] failed!");
                        bind(serverBootstrap, port + 1);
                    }
                }
            });
        }
    }
}