import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/*
 * (C) Quartet FS 2015
 * ALL RIGHTS RESERVED. This material is the CONFIDENTIAL and PROPRIETARY
 * property of Quartet Financial Systems Limited. Any unauthorized use,
 * reproduction or transfer of this material is strictly prohibited
 */

/**
 * @author Quartet FS
 *
 */
public class Master implements Runnable {
	
	private SocketChannel slaveCommunication = null;
	private ServerSocketChannel ssc = null;
	private ObjectInputStream in = null;
	private ObjectOutputStream out = null;	
	private int state = Constant.STATE_INIT;
	
	private final int PORT = 12345;

	public static void main(String[] args) throws IOException {
		new Thread(new Master(), "Master").start();
	}

	Master() throws IOException {
		System.out.println("Listening at port " + PORT);
		ssc = ServerSocketChannel.open();
		ssc.bind(new InetSocketAddress(PORT));
		slaveCommunication = ssc.accept();
		slaveCommunication.configureBlocking(true);
		
		System.out.println("Slave connected");
		
		// in = new ObjectInputStream(slaveCommunication.socket().getInputStream());
		// out = new ObjectOutputStream(slaveCommunication.socket().getOutputStream());
	}
	
	private void sendIntToSlave(int intMessage) throws IOException {
		out.writeInt(intMessage);
		out.flush();
	}
	
	private int readIntFromSlave() throws IOException {
		return in.readInt();
	}

	@Override
	public void run() {
		try {
			boolean communicationEnd = false;
			while (!communicationEnd) {
				switch(state) {
				case (Constant.STATE_INIT):
					System.out.println(slaveCommunication.isConnected());
					communicationEnd = true;
					break;
				case (Constant.STATE_PREP):
					break;
				case (Constant.STATE_WAIT):
					break;
				default:
					break;
				}
				Thread.sleep(1000);
			}
		} catch (InterruptedException e) {
		} finally {
			try {
				//in.close();
				//out.close();
				slaveCommunication.close();
				ssc.close();
			} catch (IOException e) {
				
			}
		}
	}

}
