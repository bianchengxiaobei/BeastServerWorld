package com.chen.message;

import java.util.HashMap;

import com.chen.command.Handler;
import com.chen.server.handler.ReqGateRegisterWorldHandler;
import com.chen.server.handler.ReqRegisterCenterHandler;
import com.chen.server.message.req.ReqGateRegisterWorldMessage;
import com.chen.server.message.req.ReqRegisterCenterMessage;


public class MessagePool 
{
	HashMap<Integer, Class<?>> messages = new HashMap<Integer, Class<?>>();
	HashMap<Integer, Class<?>> handlers = new HashMap<Integer, Class<?>>();
	public MessagePool()
	{
		register(10001, ReqGateRegisterWorldMessage.class, ReqGateRegisterWorldHandler.class);
		register(10005, ReqRegisterCenterMessage.class, ReqRegisterCenterHandler.class);
	}
	private void register(int id,Class<?> messageClass,Class<?> handlerClass)
	{
		messages.put(id, messageClass);
		if (handlerClass != null)
		{
			handlers.put(id, handlerClass);
		}
	}
	public Message getMessage(int id) throws InstantiationException, IllegalAccessException{
		if(!messages.containsKey(id)){
			return null;
		}else{
			return (Message)messages.get(id).newInstance();
		}
	}
	
	/**
	 * 获取处理函数
	 * @param id
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public Handler getHandler(int id) throws InstantiationException, IllegalAccessException{
		if(!handlers.containsKey(id)){
			return null;
		}else{
			return (Handler)handlers.get(id).newInstance();
		}
	}
}
