package com.github.monsterhxw.ch04to06;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

/**
 * @author huangxuewei
 * @since 2023/9/5
 */
public class NettyClient {

    private static final int PORT = 8080;

    private static final int MAX_RETRY = 5;

    public static void main(String[] args) {
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();
        bootstrap
                .group(workerGroup)
                .channel(NioSocketChannel.class)
                .handler(new ClientChannelInitializer());

        connect(bootstrap, "127.0.0.1", PORT, MAX_RETRY);
    }

    private static class ClientChannelInitializer extends ChannelInitializer<SocketChannel> {

        @Override
        protected void initChannel(SocketChannel nioSocketChannel) throws Exception {
            System.out.println(getThreadName() + "init client socket: " + nioSocketChannel);

            nioSocketChannel.pipeline()
                    .addLast(new FirstClientHandler());
        }
    }

    private static void connect(final Bootstrap bootstrap, String hostname, int port, int retry) {
        bootstrap
                .connect(hostname, port)
                .addListener(new ConnGenericFutureListener(bootstrap, hostname, port, retry));
    }

    private static class ConnGenericFutureListener implements GenericFutureListener<Future<? super Void>> {

        private final Bootstrap bootstrap;

        private final String hostname;

        private final int port;

        private final int retry;

        ConnGenericFutureListener(Bootstrap bootstrap, String hostname, int port, int retry) {
            this.bootstrap = bootstrap;
            this.hostname = hostname;
            this.port = port;
            this.retry = retry;
        }

        @Override
        public void operationComplete(Future<? super Void> future) throws Exception {
            boolean success = future.isSuccess();
            if (success) {
                System.out.println(getThreadName() + " connect " + port + " success!");
            } else {
                int order = (MAX_RETRY - retry) + 1;
                if (order <= MAX_RETRY) {
                    int delay = 1 << order;
                    System.out.println(getThreadName() + "connect " + port + " failed" + ", reconnect " + order + ", retry after " + delay + "s");
                    bootstrap.config()
                            .group()
                            .schedule((() -> connect(bootstrap, hostname, port, retry - 1)), delay, TimeUnit.SECONDS);
                } else {
                    System.out.println(getThreadName() + "connect " + port + " failed, shutdown....");
                    bootstrap.group().shutdownGracefully();
                }
            }
        }
    }

    private static String getThreadName() {
        return "Client-[" + Thread.currentThread().getName() + "]-" + System.currentTimeMillis() + "-";
    }

    private static class FirstClientHandler extends ChannelInboundHandlerAdapter {

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            System.out.println(getThreadName() + "channelActive-" + "client write data");
            // getByteBuf
            ByteBuf byteBuf = getByteBuf(ctx, "正义联盟，我是闪电侠，我要找大超！");

            ctx.channel().writeAndFlush(byteBuf);
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            ByteBuf byteBuf = (ByteBuf) msg;
            System.out.println(getThreadName() + "channelRead-receive server data: " + byteBuf.toString(Charset.forName("utf-8")));
        }

        private ByteBuf getByteBuf(ChannelHandlerContext ctx, String msg) {
            ByteBuf buffer = ctx.alloc().buffer();

            byte[] bytes = msg.getBytes(Charset.forName("utf-8"));

            buffer.writeBytes(bytes);

            return buffer;
        }
    }
}
