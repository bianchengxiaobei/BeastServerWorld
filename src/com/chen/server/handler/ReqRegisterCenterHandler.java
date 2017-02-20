package com.chen.server.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.chen.command.Handler;
import com.chen.server.WorldServer;
import com.chen.server.message.req.ReqRegisterCenterMessage;
import com.chen.server.message.res.ResRegisterCenterMessage;

public class ReqRegisterCenterHandler extends Handler
{
	private Logger log = LogManager.getLogger(ReqRegisterCenterHandler.class);
	@Override
	public void action()
	{
		try {
			ReqRegisterCenterMessage msg = (ReqRegisterCenterMessage)this.getMessage();
			WorldServer.getInstance().registerGameServer(msg.getServerId(), msg.getSession());
			log.info("游戏服务器："+msg.getServerName()+"注册到"+WorldServer.getInstance().getServer_name()+"成功");
			//返回成功消息
			ResRegisterCenterMessage return_msg = new ResRegisterCenterMessage();
			return_msg.setServerId(WorldServer.getInstance().getServer_id());;
			return_msg.setServerName(WorldServer.getInstance().getServer_name());;
			msg.getSession().write(return_msg);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error(e,e);
		}
	}
	
}
