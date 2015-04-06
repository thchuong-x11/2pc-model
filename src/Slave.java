import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
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
	
	private final ByteBuffer buf = ByteBuffer.allocate(Constant.BUFFER_SIZE);
	
	public static void main(String[] args) throws IOException {
		new Thread(new Slave(), "Slave").start();
	}
	
	Slave() throws IOException {
		masterCommunication = SocketChannel.open(new InetSocketAddress(HOST, PORT));
		masterCommunication.configureBlocking(false);
		
		System.out.println("Connected to " + HOST + " at port " + PORT);
		
	}
	
	private void writeIntToMaster(int intMessage) throws IOException {
		buf.clear();
		buf.putInt(intMessage);
		buf.flip();
		
		while (buf.hasRemaining()) {
			masterCommunication.write(buf);
		}
		
	}
	
	private int readIntFromMaster() throws IOException {
		buf.clear();
		int bytesRead = masterCommunication.read(buf);
		return buf.asIntBuffer().get(0);
	}

	@Override
	public void run() {
		try{
			boolean communicationEnd = false;
			while (!communicationEnd) {
				switch (state) {
				case Constant.STATE_INIT:
					System.out.println("State INIT");
					
					if (readIntFromMaster() == Constant.MSG_PREP) {
						writeIntToMaster(Constant.MSG_OK);
						state = Constant.STATE_PREP;
					}
					break;
				case Constant.STATE_PREP:
					System.out.println("State PREP");

					writeIntToMaster(Constant.MSG_COMMIT);
					if (readIntFromMaster() == Constant.MSG_OK);
						state = Constant.STATE_WAIT;
					break;
				case Constant.STATE_WAIT:
					System.out.println("State WAIT");
					
					int vote;
					if ( (vote = readIntFromMaster()) == Constant.MSG_COMMIT || vote == Constant.MSG_COMMIT ) {
						writeIntToMaster(Constant.MSG_OK);
						communicationEnd = true;
					}
					break;
				default:
					break;
				}
				
				Thread.sleep(1000);
			}
		} catch (IOException e) {
		} catch (InterruptedException e) {
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
