package com.leolee.server.heartbeat;

import com.leolee.server.ChatServerContainer;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * 心跳处理
 * @author: LeoLee <zeroming@163.com>
 * @date: 2019/12/6 17:08
 */
public class HeartBeatServerHandler extends ChannelInboundHandlerAdapter{
    private int lossConnectCount = 0;

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        System.out.println("已经5秒未收到客户端的消息了！"+evt);
        if (evt instanceof IdleStateEvent){
            IdleStateEvent event = (IdleStateEvent)evt;
            if (event.state()== IdleState.READER_IDLE){
                lossConnectCount++;
                if (lossConnectCount > 2){
                    System.out.println("关闭这个不活跃通道！");
                    Channel channel = ctx.channel();
                    // 移除
                    ChatServerContainer.remove(channel);
                    channel.close();
                }
            }
        }else {
            super.userEventTriggered(ctx,evt);
        }
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        lossConnectCount = 0;
        System.out.println("client says: "+msg.toString());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
