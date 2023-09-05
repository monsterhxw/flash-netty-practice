package com.github.monsterhxw.ch02.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;

/**
 * @author huangxuewei
 * @since 2023/9/5
 */
public class NettyClient {

    public static void main(String[] args) throws InterruptedException {
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 8080);
        Bootstrap bootstrap = new Bootstrap();

        NioEventLoopGroup group = new NioEventLoopGroup();

        Channel serverChannel = bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new MyChannelInitialize())
                .connect(inetSocketAddress)
                .channel();

        while (true) {
            serverChannel.writeAndFlush(System.currentTimeMillis() + ": hello world!");
            Thread.sleep(2_000L);
        }
    }

    private static class MyChannelInitialize extends ChannelInitializer<Channel> {

        @Override
        protected void initChannel(Channel serverChannel) throws Exception {
            serverChannel.pipeline()
                    .addLast(new StringEncoder());
        }
    }
}
