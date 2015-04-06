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
public final class Constant {
	public static final int COM_PORT = 12345;
	public static final int STATE_INIT = 2;
	public static final int STATE_WAIT = 0;
	public static final int STATE_PREP = 1;

	public static final int MSG_EOF = -1;
	public static final int MSG_COMMIT = 0;
	public static final int MSG_ABORT = 1;
	public static final int MSG_OK = 2;
	public static final int MSG_PREP = 3;
	public static final int TIME_OUT = 1000;
	
	public static final int BUFFER_SIZE = 83780;

}
