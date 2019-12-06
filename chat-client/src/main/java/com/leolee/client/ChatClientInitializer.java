package com.leolee.client;

import com.leolee.client.heartbeat.HeartBeatClientHandler;
import com.leolee.client.retry.ReconnectHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Author: LeoLee
 * @Date: 2019年11月04 00时42分
 */
public class ChatClientInitializer extends ChannelInitializer<SocketChannel> {

    private ReconnectHandler reconnectHandler;

    public ChatClientInitializer(ChatClient chatClient) {
        this.reconnectHandler = new ReconnectHandler(chatClient);
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        // pipeline.addLast("frame",new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
        // 解码器
        pipeline.addLast(new IdleStateHandler(0,50,0, TimeUnit.SECONDS));
        pipeline.addLast("decode",new StringDecoder());
        pipeline.addLast("encode",new StringEncoder());
        pipeline.addLast("handler",new ChatClientHandler());
        pipeline.addLast("heartBeat",new HeartBeatClientHandler());
        pipeline.addLast("reconnect",reconnectHandler);
    }
}
