import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
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
public class Slave implements Runnable {

	private final int PORT = 12345;
	private final String HOST = "localhost";
	
	private SocketChannel masterCommunication = null;
	private ObjectInputStream in = null;
	private ObjectOutputStream out = null;
	private int state = Constant.STATE_INIT;
	
	public static void main(String[] args) throws IOException {
		new Thread(new Slave(), "Slave").start();
	}
	
	Slave() throws IOException {
		masterCommunication = SocketChannel.open(new InetSocketAddress(HOST, PORT));
		masterCommunication.configureBlocking(true);
		
		System.out.println("Connected to " + HOST + " at port " + PORT);
		
		// in = new ObjectInputStream(masterCommunication.socket().getInputStream());
		// out = new ObjectOutputStream(masterCommunication.socket().getOutputStream());
	}
	
	private void sendIntToMaster(int intMessage) throws IOException {
		out.writeInt(intMessage);
		out.flush();
	}
	
	private int readIntFromMaster() throws IOException {
		return in.readInt();
	}

	@Override
	public void run() {
		try{
			boolean communicationEnd = false;
			while (!communicationEnd) {
				switch (state) {
				case Constant.STATE_INIT:
					System.out.println(masterCommunication.isConnected());
					communicationEnd = true;
					break;
				case Constant.STATE_PREP:
					break;
				case Constant.STATE_WAIT:
					break;
				default:
					break;
				}
			}
		} finally {
			try {
				//in.close();
				//out.close();
				masterCommunication.close();
			} catch (IOException e) {
			}
		}

	}

}
