package com.leolee.server;

import com.leolee.server.heartbeat.HeartBeatServerHandler;
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
 * @Date: 2019年11月04 00时32分
 */
public class ChatServerInitializer  extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        System.out.println("客户端连接：" + socketChannel.remoteAddress());
        // 用户定义的ChannelInitailizer加入到这个channel的pipeline上面去，这个handler就可以用于处理当前这个channel上面的一些事件
        ChannelPipeline pipeline = socketChannel.pipeline();
        // ChannelPipeline类似于一个管道，管道中存放的是一系列对读取数据进行业务操作的ChannelHandler。
        /**
         * 发送的数据在管道里是无缝流动的，在数据量很大时，为了分割数据，采用以下几种方法
         * 定长方法
         * 固定分隔符
         * 将消息分成消息体和消息头，在消息头中用一个数组说明消息体的长度
         */
        // pipeline.addLast("frame",new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
        pipeline.addLast(new IdleStateHandler(5, 0, 0, TimeUnit.SECONDS));
        // 解码器
        pipeline.addLast("decode",new StringDecoder());
        pipeline.addLast("encode",new StringEncoder());
        pipeline.addLast("handler",new ChatServerHandler());
        pipeline.addLast("heartBeat",new HeartBeatServerHandler());
    }
}
