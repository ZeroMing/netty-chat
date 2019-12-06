package com.leolee.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author: LeoLee <zeroming@163.com>
 * @date: 2019/12/6 14:27
 */
@RestController
public class IndexController {

    @GetMapping("/index")
    public void collect(@RequestBody Map<String,String> data) throws InterruptedException {
        String clientId = data.get("id");
        Channel channel = ChatServerContainer.get(clientId);
        ChannelFuture channelFuture = channel.writeAndFlush("实时采集");
    }
}
