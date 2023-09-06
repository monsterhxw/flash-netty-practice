package com.github.monsterhxw.ch13to15.demo.server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.Charset;

import static com.github.monsterhxw.ch13to15.util.ThreadUtil.getThreadName;

public class FirstServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ByteBuf out = getByteBuf(ctx, "正义联盟第一次通知，请宣告你的大名!");
        ctx.channel().writeAndFlush(out);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println(getThreadName("Server") + "channelRead-receive client data: " + byteBuf.toString(Charset.forName("utf-8")));

//        ByteBuf out = getByteBuf(ctx, "你好，闪电侠, 我是蝙蝠侠隔壁的神奇女侠的隔壁的超人！");
//        ctx.channel().writeAndFlush(out);
    }

    private ByteBuf getByteBuf(ChannelHandlerContext ctx, String msg) {
        byte[] bytes = msg.getBytes(Charset.forName("utf-8"));

        ByteBuf buffer = ctx.alloc().buffer();
        buffer.writeBytes(bytes);

        return buffer;
    }
}
