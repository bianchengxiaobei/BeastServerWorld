/**
 * 
 */
package com.chen.server;

import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import com.chen.cache.executor.NonOrderedQueuePoolExecutor;
import com.chen.command.Handler;
import com.chen.message.Message;
import com.chen.message.MessagePool;
import com.chen.mina.impl.InnerServer;
import com.chen.server.config.GameConfig;
import com.chen.server.loader.GameConfigXmlLoader;
import com.chen.server.thread.SchedularThread;
import com.chen.server.thread.ServerThread;

/**
 * 中心服务器
 * @author chen
 *
 */
public class WorldServer extends InnerServer
{
	private static Logger log = LogManager.getLogger(WorldServer.class);
	private static Object obj = new Object();
	private static GameConfig config;
	private static final String defaultGameConfig = "world-config/game-config.xml";
	private static final String defaultInnerServerConfig = "world-config/inner-server-config.xml";
	private static WorldServer server;
	//消息池
	private static MessagePool messagePool = new MessagePool();
	//GameServer通信列表
	private static ConcurrentHashMap<Integer, Vector<IoSession>> gameSessions = new ConcurrentHashMap<Integer, Vector<IoSession>>();
	//GateServer通信列表
	private static ConcurrentHashMap<Integer, Vector<IoSession>> gateSessions = new ConcurrentHashMap<Integer, Vector<IoSession>>();
	private ServerThread wServerThread;
	private SchedularThread wSchedularThread;
	//服务器启动线程组
	private ThreadGroup threadGroup;
	private NonOrderedQueuePoolExecutor commandExecutor = new NonOrderedQueuePoolExecutor(100); 
	public WorldServer()
	{
		this(defaultInnerServerConfig);
	}
	public WorldServer(String innerServerConfig)
	{
		super(innerServerConfig);
	}
	public static WorldServer getInstance()
	{
		synchronized (obj)
		{
			if (null == server)
			{
				config = new GameConfigXmlLoader().load(defaultGameConfig);
				server = new WorldServer();
			}
		}
		return server;
	}
	protected void init()
	{
		super.init();
		threadGroup = new ThreadGroup(this.getServer_name());
		wServerThread = new ServerThread(threadGroup, this.getServer_name(), 1000);
		wSchedularThread = new SchedularThread();
	}
	public void run() 
	{
		super.run();
		wServerThread.start();
		//wSchedularThread.start();
	}
	@Override
	public void sessionCreate(IoSession arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void sessionIdle(IoSession arg0, IdleStatus arg1) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void sessionOpened(IoSession arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void doCommand(IoSession session, IoBuffer buf)
	{		
		try {
			int id = buf.getInt();
			Message msg = messagePool.getMessage(id);
			if (msg == null)
			{
				log.error("收到消息为空Id："+id);
			}
			msg.setSendId(buf.getLong());
			int roleNum = buf.getInt();
			for (int i = 0; i < roleNum; i++) {
				msg.getRoleId().add(buf.getLong());
			}
			msg.read(buf);
			msg.setSession(session);
			log.debug("收到消息id："+msg.getId()+"-->"+msg.getClass().getSimpleName());
			Handler handler = messagePool.getHandler(id);
			handler.setMessage(msg);
			if ("Local".equalsIgnoreCase(msg.getQueue()))
			{
				commandExecutor.execute(handler);		
			}else
			{
				wServerThread.addCommand(handler);
			}
		} catch (Exception e) {
			log.error(e,e);
		}
	}
	@Override
	public void exceptionCaught(IoSession arg0, Throwable arg1) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void sessionClosed(IoSession arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	protected void stop() {
		// TODO Auto-generated method stub
		
	}
	public GameConfig getConfig() {
		return config;
	}	
	/**
	 * 网关服务器注册，添加到map
	 * @param id
	 * @param session
	 */
	public synchronized void registerGateServer(int id,IoSession session)
	{
		session.setAttribute("server_id", id);
		synchronized (gateSessions)
		{
			Vector<IoSession> sessions = null;
			if (gateSessions.containsKey(id))
			{
				sessions = gateSessions.get(id);
			}else
			{
				sessions = new Vector<IoSession>();
				gateSessions.put(id, sessions);
			}
			sessions.add(session);
		}
	}
	/**
	 * 游戏服务器注册到中心服务器里面，添加到vector里面
	 * @param id
	 * @param session
	 */
	public synchronized void registerGameServer(int id,IoSession session)
	{
		session.setAttribute("server_id", id);
		synchronized (gameSessions) 
		{
			Vector<IoSession> sessions = null;
			if (gameSessions.containsKey(id))
			{
				sessions = gameSessions.get(id);
			}
			else
			{
				sessions = new Vector<IoSession>();
				gameSessions.put(id, sessions);
			}
			sessions.add(session);
		}
	}
}
