package com.github.monsterhxw.ch08to09.client;

import com.github.monsterhxw.ch08to09.proto.LoginRequestPacket;
import com.github.monsterhxw.ch08to09.proto.LoginResponsePacket;
import com.github.monsterhxw.ch08to09.proto.Packet;
import com.github.monsterhxw.ch08to09.proto.PacketCodeC;
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

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author huangxuewei
 * @since 2023/9/6
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
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        System.out.println(getThreadName() + "init client socket: " + ch);
                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                System.out.println(getThreadName() + "channelActive-" + "client write login_request");

                                // 编写 LoginRequestPacket
                                LoginRequestPacket packet = new LoginRequestPacket();
                                packet.setUserId(new Random().nextInt(Integer.MAX_VALUE));
                                packet.setUsername("闪电侠");
                                packet.setPassword("flash-man");

                                // 获取 ByteBuf from ChannelHandlerContext
                                ByteBuf byteBuf = ctx.alloc().ioBuffer();

                                PacketCodeC.encode(byteBuf, packet);

                                // 发送登录请求
                                ctx.channel().writeAndFlush(byteBuf);
                            }

                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                ByteBuf respByteBuf = (ByteBuf) msg;
                                Packet packet = PacketCodeC.decode(respByteBuf);
                                if (packet instanceof LoginResponsePacket) {
                                    LoginResponsePacket loginResponsePacket = (LoginResponsePacket) packet;
                                    if (loginResponsePacket.isSuccess()) {
                                        System.out.println(getThreadName() + "login success, " + loginResponsePacket);
                                    } else {
                                        System.out.println(getThreadName() + "login failed, " + loginResponsePacket);
                                    }
                                }
                                respByteBuf.release();
                            }
                        });
                    }
                });

        connect(bootstrap, "127.0.0.1", PORT, MAX_RETRY);
    }

    private static void connect(final Bootstrap bootstrap, final String hostname, final int port, final int retry) {
        bootstrap
                .connect(hostname, port)
                .addListener(new GenericFutureListener<Future<? super Void>>() {
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
                });
    }

    private static String getThreadName() {
        return "Client-[" + Thread.currentThread().getName() + "]-" + System.currentTimeMillis() + "-";
    }
}
