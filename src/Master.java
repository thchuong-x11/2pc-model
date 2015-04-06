import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
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
	private final ByteBuffer buf = ByteBuffer.allocate(Constant.BUFFER_SIZE);

	public static void main(String[] args) throws IOException {
		new Thread(new Master(), "Master").start();
	}

	Master() throws IOException {
		System.out.println("Listening at port " + PORT);
		ssc = ServerSocketChannel.open();
		ssc.bind(new InetSocketAddress(PORT));
		slaveCommunication = ssc.accept();
		slaveCommunication.configureBlocking(false);
		
		System.out.println("Slave connected");
		
	}
	
	private void writeIntToSlave(int intMessage) throws IOException {
		buf.clear();
		buf.putInt(intMessage);
		buf.flip();
		
		while (buf.hasRemaining()) {
			slaveCommunication.write(buf);
		}
	}
	
	private int readIntFromSlave() throws IOException {
		buf.clear();
		int bytesRead = slaveCommunication.read(buf);
		return buf.asIntBuffer().get(0);
	}

	@Override
	public void run() {
		try {
			boolean communicationEnd = false;
			
			while (!communicationEnd) {
				switch(state) {
				case (Constant.STATE_INIT):
					System.out.println("State INIT");
				
					writeIntToSlave(Constant.MSG_PREP);
					if (readIntFromSlave() == Constant.MSG_OK);
						state = Constant.STATE_PREP;
						
					break;
				case (Constant.STATE_PREP):
					System.out.println("State PREP");
				
					int vote;
					if ((vote = readIntFromSlave()) == Constant.MSG_COMMIT || vote == Constant.MSG_ABORT) {
						writeIntToSlave(Constant.MSG_OK);
						state = Constant.STATE_WAIT;
					}
				
					break;
				case (Constant.STATE_WAIT):
					System.out.println("State WAIT");
				
					writeIntToSlave(Constant.MSG_ABORT);
					if (readIntFromSlave() == Constant.MSG_OK) {
						communicationEnd = true;
					}
				
					break;
				default:
					break;
				}
				Thread.sleep(1000);
			}
		} catch (InterruptedException | IOException e) {
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
