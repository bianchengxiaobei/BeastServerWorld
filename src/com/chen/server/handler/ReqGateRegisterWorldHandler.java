package com.chen.server.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.chen.command.Handler;
import com.chen.server.WorldServer;
import com.chen.server.message.req.ReqGateRegisterWorldMessage;
import com.chen.server.message.res.ResGateRegisterWorldMessage;

public class ReqGateRegisterWorldHandler extends Handler
{
	private Logger log = LogManager.getLogger(ReqGateRegisterWorldHandler.class);

	@Override
	public void action() {
		try {
			ReqGateRegisterWorldMessage msg = (ReqGateRegisterWorldMessage)this.getMessage();
			WorldServer.getInstance().registerGateServer(msg.getServerId(), msg.getSession());
			log.info("网关服务器:" + msg.getServerName() + "注册到" + WorldServer.getInstance().getServer_name() + "成功！");
			//返回给网关注册成功的消息
			ResGateRegisterWorldMessage return_msg = new ResGateRegisterWorldMessage();
			return_msg.setServerId(WorldServer.getInstance().getServer_id());
			return_msg.setServerName(WorldServer.getInstance().getServer_name());
			msg.getSession().write(return_msg);
		} catch (Exception e) {
			log.error(e,e);
		}
	}
}
