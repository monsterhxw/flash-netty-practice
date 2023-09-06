package com.github.monsterhxw.ch13.demo.client;

import com.github.monsterhxw.ch13.demo.client.handler.FirstClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.util.concurrent.TimeUnit;

import static com.github.monsterhxw.ch13.util.ThreadUtil.getThreadName;

/**
 * @author huangxuewei
 * @since 2023/9/5
 */
public class NettyClient {

    private static final String HOSTNAME = "127.0.0.1";
    private static final int PORT = 8080;

    private static final int MAX_RETRY = 5;

    public static void main(String[] args) {
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();
        bootstrap
                .group(workerGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        System.out.println(getThreadName("Client") + "init client socket: " + ch);
                        ch.pipeline().addLast(new FirstClientHandler());
                    }
                });

        connect(bootstrap, HOSTNAME, PORT, MAX_RETRY);
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
                System.out.println(getThreadName("Client") + " connect " + port + " success!");
            } else {
                int order = (MAX_RETRY - retry) + 1;
                if (order <= MAX_RETRY) {
                    int delay = 1 << order;
                    System.out.println(getThreadName("Client") + "connect " + port + " failed" + ", reconnect " + order + ", retry after " + delay + "s");
                    bootstrap.config()
                            .group()
                            .schedule((() -> connect(bootstrap, hostname, port, retry - 1)), delay, TimeUnit.SECONDS);
                } else {
                    System.out.println(getThreadName("Client") + "connect " + port + " failed, shutdown....");
                    bootstrap.group().shutdownGracefully();
                }
            }
        }
    }
}
