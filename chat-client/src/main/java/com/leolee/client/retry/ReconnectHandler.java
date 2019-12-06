package com.leolee.client.retry;

import com.leolee.client.ChatClient;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.EventLoop;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 重连
 * @author: LeoLee <zeroming@163.com>
 * @date: 2019/12/6 18:24
 */
@ChannelHandler.Sharable
public class ReconnectHandler extends ChannelInboundHandlerAdapter {

    private int retries = 0;
    private RetryPolicy retryPolicy;

    private ChatClient chatClient;

    public ReconnectHandler(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Successfully established a connection to the server.");
        retries = 0;
        ctx.fireChannelActive();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        if (retries == 0) {
            System.err.println("Lost the TCP connection with the server.");
            ctx.close();
        }

        boolean allowRetry = getRetryPolicy().allowRetry(retries);
        if (allowRetry) {
            long sleepTimeMs = getRetryPolicy().getSleepTimeMs(retries);
            System.out.println(String.format("Try to reconnect to the server after %dms. Retry count: %d.", sleepTimeMs, ++retries));
            final EventLoop eventLoop = ctx.channel().eventLoop();
            eventLoop.schedule(new Runnable() {
                public void run() {
                    System.out.println("Reconnecting ...");
                    try {
                        chatClient.connect();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }, sleepTimeMs, TimeUnit.MILLISECONDS);
        }
        ctx.fireChannelInactive();
    }


    private RetryPolicy getRetryPolicy() {
        if (this.retryPolicy == null) {
            this.retryPolicy = chatClient.getRetryPolicy();
        }
        return this.retryPolicy;
    }
}
