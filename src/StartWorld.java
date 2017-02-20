import com.chen.server.WorldServer;


public class StartWorld {

	public static void main(String[] args) {
		new Thread((Runnable)WorldServer.getInstance()).start();
	}
}
