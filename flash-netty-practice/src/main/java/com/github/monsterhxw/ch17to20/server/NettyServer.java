package com.github.monsterhxw.ch17to20.server;

import com.github.monsterhxw.ch17to20.parser.PacketCodecHandler;
import com.github.monsterhxw.ch17to20.parser.Splitter;
import com.github.monsterhxw.ch17to20.server.handler.*;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import static com.github.monsterhxw.ch17to20.util.ThreadUtil.getThreadName;


public class NettyServer {

    private static final int port = 8080;

    public static void main(String[] args) {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();

        serverBootstrap
                .group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<ServerSocketChannel>() {
                    @Override
                    protected void initChannel(ServerSocketChannel ch) throws Exception {
                        System.out.println(getThreadName("Server") + "init server(listen) socket: " + ch);
                    }
                })
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        System.out.println(getThreadName("Server") + "init client socket: " + ch);
                        ch.pipeline()
                                .addLast(new Splitter())
                                .addLast(PacketCodecHandler.getInstance())
                                .addLast(LoginRequestHandler.getInstance())
                                .addLast(AuthHandler.getInstance())
                                .addLast(IMHandler.getInstance());
//                                .addLast(MessageRequestHandler.getInstance())
//                                .addLast(CreateGroupRequestHandler.getInstance())
//                                .addLast(JoinGroupRequestHandler.getInstance())
//                                .addLast(QuitGroupRequestHandler.getInstance())
//                                .addLast(ListGroupMembersRequestHandler.getInstance())
//                                .addLast(GroupMessageRequestHandler.getInstance())
//                                .addLast(LogoutRequestHandler.getInstance());
                    }
                });

        bind(serverBootstrap, port);
    }


    private static void bind(final ServerBootstrap serverBootstrap, final int port) {
        if (port <= 65_535) {
            serverBootstrap.bind(port).addListener(new GenericFutureListener<Future<? super Void>>() {
                @Override
                public void operationComplete(Future<? super Void> future) throws Exception {
                    boolean success = future.isSuccess();
                    if (success) {
                        System.out.println(getThreadName("Server") + "bind [" + port + "] success!");
                    } else {
                        System.out.println(getThreadName("Server") + "bind [" + port + "] failed!");
                        bind(serverBootstrap, port + 1);
                    }
                }
            });
        }
    }
}