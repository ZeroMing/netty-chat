package com.leolee.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @Description:
 * @Author: LeoLee
 * @Date: 2019年11月04 00时42分
 */
public class ChatClientInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast("frame",new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
        // 解码器
        pipeline.addLast("decode",new StringDecoder());
        pipeline.addLast("encode",new StringEncoder());
        pipeline.addLast("handler",new ChatClientHandler());
    }
}
