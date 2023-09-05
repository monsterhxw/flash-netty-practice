package com.github.monsterhxw.ch02.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

/**
 * @author huangxuewei
 * @since 2023/9/5
 */
public class NettyServer {

    public static void main(String[] args) {
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        // boss group 处理 accept
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        // worker group 处理 read
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        serverBootstrap
                .group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new MyChannelInitializer())
                .bind(8080);
    }

    private static class MyChannelInitializer extends ChannelInitializer<NioSocketChannel> {

        @Override
        protected void initChannel(NioSocketChannel clientChannel) throws Exception {
            clientChannel
                    .pipeline()
                    .addLast(new StringDecoder())
                    .addLast(new MySimpleChannelInboundHandler());
        }
    }

    private static class MySimpleChannelInboundHandler extends SimpleChannelInboundHandler<String> {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
            String name = Thread.currentThread().getName();
            System.out.println(name + " handle receive client data: " + msg);
        }
    }
}
