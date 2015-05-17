package com.fung.netty;

public class NettyServer {
	public void bind(int port) throws Exception {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup)
					.channel(NioServerSocketChannel.class)
					.option(ChannelOption.SO_BACKLOG, 1024)
					.childHandler(new ChildChannelHandler());
			
			ChannelFuture f = b.bind(port).sync();
			System.out.println("Netty time Server started at port " + port);
			f.channel().closeFuture().sync();
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}

	public static class ChildChannelHandler extends
			ChannelInitializer<SocketChannel> {

		@Override
		protected void initChannel(SocketChannel ch) throws Exception {
			ch.pipeline().addLast(new NettyMessageDecoder(1024 * 1024, 4, 4, -8, 0))
			.addLast(new NettyMessageEncoder())
			.addLast(new LoginAuthRespHandler());
		}

	}
	
	public static void main(String[] args){
		try {
			new NettyServer().bind(9080);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
