package com.chen.server.config;

import java.text.SimpleDateFormat;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GameConfig 
{
	private Logger log = LogManager.getLogger(GameConfig.class);
	//服务器索引 key为服务器Id value为地区id（0-为公共区）
	private HashMap<Integer, Integer> servers = new HashMap<Integer, Integer>();
	//服务器索引 key为服务器Id value为服务器开服时间
	private HashMap<Integer, String> serverTimes = new HashMap<Integer, String>();
	private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public HashMap<Integer, String> getServerTimes() {
		return serverTimes;
	}
	public void setServerTimes(HashMap<Integer, String> serverTimes) {
		this.serverTimes = serverTimes;
	}
	public HashMap<Integer, Integer> getServers() {
		return servers;
	}
	public void setServers(HashMap<Integer, Integer> servers) {
		this.servers = servers;
	}
	public java.util.Date getServerTimeByServer(int server)
	{
		if (serverTimes.containsKey(server))
		{
			String str = serverTimes.get(server);
			if (str != null && !str.equals(""))
			{
				try {
					return format.parse(str);
				} catch (Exception e) {
					log.error(e,e);
				}
			}
		}
		return null;
	}
}
