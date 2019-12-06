package com.leolee.server.http;

import com.leolee.server.ChatServerHandler;
import com.leolee.server.NettyHttpServerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @author: LeoLee <zeroming@163.com>
 * @date: 2019/12/6 12:52
 */
public class HttpServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        System.out.println("客户端连接：" + socketChannel.remoteAddress());
        // 用户定义的ChannelInitailizer加入到这个channel的pipeline上面去，这个handler就可以用于处理当前这个channel上面的一些事件
        ChannelPipeline pipeline = socketChannel.pipeline();
        // 请求解码器
        socketChannel.pipeline().addLast("http-decoder", new HttpRequestDecoder());
        // 将HTTP消息的多个部分合成一条完整的HTTP消息
        socketChannel.pipeline().addLast("http-aggregator", new HttpObjectAggregator(65535));
        // 响应转码器
        socketChannel.pipeline().addLast("http-encoder", new HttpResponseEncoder());
        // 解决大码流的问题，ChunkedWriteHandler：向客户端发送HTML5文件
        socketChannel.pipeline().addLast("http-chunked", new ChunkedWriteHandler());
        // 自定义处理handler
//        socketChannel.pipeline().addLast("http-server", new NettyHttpServerHandler());
        socketChannel.pipeline().addLast("chat-server", new ChatServerHandler());

    }
}
