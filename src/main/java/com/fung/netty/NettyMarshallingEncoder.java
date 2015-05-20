package com.fung.netty;

public class NettyMarshallingEncoder extends MarshallingEncoder{

		public NettyMarshallingEncoder(MarshallerProvider provider) {
			super(provider);
		}

		public void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception{
			super.encode(ctx, msg, out);
		}
		
	}
}
