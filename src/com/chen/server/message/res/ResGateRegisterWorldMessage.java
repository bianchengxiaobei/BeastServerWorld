package com.chen.server.message.res;

import org.apache.mina.core.buffer.IoBuffer;

import com.chen.message.Message;

public class ResGateRegisterWorldMessage extends Message
{
	private int serverId;//服务器编号
	private String serverName;//服务器名字
	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 10002;
	}

	public int getServerId() {
		return serverId;
	}

	public void setServerId(int serverId) {
		this.serverId = serverId;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	@Override
	public String getQueue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getServer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean read(IoBuffer buf) {
		this.serverId = readInt(buf);
		this.serverName = readString(buf);
		return true;
	}

	@Override
	public boolean write(IoBuffer buf) {
		writeInt(buf, this.serverId);
		writeString(buf, this.serverName);
		return true;
	}
}
