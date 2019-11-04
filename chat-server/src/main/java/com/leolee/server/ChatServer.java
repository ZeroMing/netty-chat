package com.leolee.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @Description: 聊天室服务端
 * @Author: LeoLee
 * @Date: 2019年11月04 00时18分
 */
public class ChatServer {

    private int port;

    public ChatServer(int port){
        this.port = port;
    }

    /**
     * 启动服务端
     */
    public void bootStrap(){
        //配置服务端的NIO线程组
        //实际上EventLoopGroup就是Reactor线程组
        //两个Reactor 一个用于服务端接收客户端的连接，另一个用于进行SocketChannel的网络读写
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();



        //绑定监听端口，调用sync同步阻塞方法等待绑定操作完成，完成后返回ChannelFuture类似于JDK中Future
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    // 回调请求
                    .childHandler(new ChatServerInitializer())
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .option(ChannelOption.SO_KEEPALIVE,true);
            ChannelFuture future = bootstrap.bind(port).sync();
            System.out.println("服务器启动：");
            //使用sync方法进行阻塞，等待服务端链路关闭之后Main函数才退出
            future.channel().closeFuture().sync();
            System.out.println("服务器关闭：");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            //优雅退出，释放线程池资源
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }



    public static void main(String[] args) {
        ChatServer chatServer = new ChatServer(9999);
        chatServer.bootStrap();
    }





}
