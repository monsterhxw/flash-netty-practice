package com.github.monsterhxw.ch04to06;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.nio.charset.Charset;

/**
 * @author huangxuewei
 * @since 2023/9/5
 */
public class NettyServer {

    private static final int port = 8080;

    public static void main(String[] args) {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();

        serverBootstrap
                .group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .handler(new ServerChannelInitializer())
                .childHandler(new ClientChannelInitializer());

        bind(serverBootstrap, port);
    }

    private static class ServerChannelInitializer extends ChannelInitializer<NioServerSocketChannel> {

        @Override
        protected void initChannel(NioServerSocketChannel nioServerSocketChannel) throws Exception {
            System.out.println(getThreadName() + "init server(listen) socket: " + nioServerSocketChannel);
        }
    }

    private static String getThreadName() {
        return "Server-[" + Thread.currentThread().getName() + "]-" + System.currentTimeMillis() + "-";
    }

    private static class ClientChannelInitializer extends ChannelInitializer<NioSocketChannel> {

        @Override
        protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
            System.out.println(getThreadName() + "init client socket: " + nioSocketChannel);
            nioSocketChannel.pipeline()
                    .addLast(new FirstServerHandler());
        }
    }

    private static class FirstServerHandler extends ChannelInboundHandlerAdapter {

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            ByteBuf out = getByteBuf(ctx, "正义联盟第一次通知，请宣告你的大名!");
            ctx.channel().writeAndFlush(out);
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            ByteBuf byteBuf = (ByteBuf) msg;
            System.out.println(getThreadName() + "channelRead-receive client data: " + byteBuf.toString(Charset.forName("utf-8")));

            ByteBuf out = getByteBuf(ctx, "你好，闪电侠, 我是蝙蝠侠隔壁的神奇女侠的隔壁的超人！");
            ctx.channel().writeAndFlush(out);
        }

        private ByteBuf getByteBuf(ChannelHandlerContext ctx, String msg) {
            byte[] bytes = msg.getBytes(Charset.forName("utf-8"));

            ByteBuf buffer = ctx.alloc().buffer();
            buffer.writeBytes(bytes);

            return buffer;
        }
    }

    private static class BindGenericFutureListener implements GenericFutureListener<Future<? super Void>> {

        private final ServerBootstrap serverBootstrap;

        private final int port;

        BindGenericFutureListener(ServerBootstrap serverBootstrap, int port) {
            this.serverBootstrap = serverBootstrap;
            this.port = port;
        }

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
    }

    private static void bind(final ServerBootstrap serverBootstrap, final int port) {
        if (port <= 65_535) {
            serverBootstrap.bind(port).addListener(new BindGenericFutureListener(serverBootstrap, port));
        }
    }
}
