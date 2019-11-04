package com.leolee.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @Description:
 * @Author: LeoLee
 * @Date: 2019年11月04 00时43分
 */
public class ChatClientHandler extends SimpleChannelInboundHandler<String> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        // 直接输出消息
        System.out.println(s);
    }
}