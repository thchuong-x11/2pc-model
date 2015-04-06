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
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	Master() {

	}

	private SocketChannel slaveCommunication = null;
	private ServerSocketChannel ssc;
	private int State = Constant.STATE_INIT;


	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

}
