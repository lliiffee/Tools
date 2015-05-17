package com.fung.netty;

public class NettyMarshallingDecoder extends MarshallingDecoder{

	public NettyMarshallingDecoder(UnmarshallerProvider provider) {
		super(provider);
	}

	public NettyMarshallingDecoder(UnmarshallerProvider provider, int maxObjectSize){
		super(provider, maxObjectSize);
	}
	
	public Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
		return super.decode(ctx, in);
	}
	
}
