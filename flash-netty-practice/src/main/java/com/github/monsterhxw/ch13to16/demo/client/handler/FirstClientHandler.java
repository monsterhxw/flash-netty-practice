package com.github.monsterhxw.ch13to16.demo.client.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.Charset;

import static com.github.monsterhxw.ch13to16.util.ThreadUtil.getThreadName;

public class FirstClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(getThreadName("Client") + "channelActive-" + "client write data");
        // getByteBuf
        for (int i = 0; i < 1_000; i++) {
            ByteBuf byteBuf = getByteBuf(ctx, "正义联盟，我是闪电侠，我要找大超｜");
            ctx.channel().writeAndFlush(byteBuf);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println(getThreadName("Client") + "channelRead-receive server data: " + byteBuf.toString(Charset.forName("utf-8")));
    }

    private ByteBuf getByteBuf(ChannelHandlerContext ctx, String msg) {
        ByteBuf buffer = ctx.alloc().buffer();
        byte[] bytes = msg.getBytes(Charset.forName("utf-8"));
        buffer.writeBytes(bytes);
        return buffer;
    }
}
