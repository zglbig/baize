package org.baize.server.manager;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.baize.server.message.TcpHandler;
import org.baize.utils.SpringUtils;

public class ServerHandlerManager extends SimpleChannelInboundHandler<Request>{
	/**消息分发器*/
	private final static TcpHandler TCP_HANDLER = SpringUtils.getBean(TcpHandler.class);
	private void process(Channel channel, Request request){
		TCP_HANDLER.messageRecieve(channel,request);
	}
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		//客户端在
		System.err.println("用户上线"+ctx.channel().remoteAddress());
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		ctx.channel().close();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		System.err.println("用户下线"+ctx.channel().remoteAddress());
		ctx.channel().close();
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Request request) throws Exception {
		//将消息发送到消息分发
		process(ctx.channel(), request);
	}
}