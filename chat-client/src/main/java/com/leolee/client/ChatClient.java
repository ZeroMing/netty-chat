package com.leolee.client;

import com.leolee.client.retry.ExponentialBackOffRetry;
import com.leolee.client.retry.RetryPolicy;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @Description:
 * @Author: LeoLee
 * @Date: 2019年11月04 00时19分
 */
public class ChatClient {

    Bootstrap bootstrap;
    private String host;
    private int port;
    /** 重连策略 */
    private RetryPolicy retryPolicy;


    public ChatClient(String host, int port) {
        this(host, port, new ExponentialBackOffRetry(1000, Integer.MAX_VALUE, 60 * 1000));
    }

    public ChatClient(String host, int port, RetryPolicy retryPolicy) {
        this.host = host;
        this.port = port;
        this.retryPolicy = retryPolicy;
        init();
    }

    private void init() {
        EventLoopGroup group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChatClientInitializer(ChatClient.this));
    }


    public void connect() throws InterruptedException, IOException {
        synchronized (bootstrap) {
            Channel channel = bootstrap.connect(host,port).sync().channel();
            while (true){
                BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
                channel.writeAndFlush(input.readLine());
            }
        }
    }

    public RetryPolicy getRetryPolicy() {
        return retryPolicy;
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        new ChatClient("localhost",8888).connect();
    }
}
