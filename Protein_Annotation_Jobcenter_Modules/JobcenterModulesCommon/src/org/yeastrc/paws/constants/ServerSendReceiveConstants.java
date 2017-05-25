package org.yeastrc.paws.constants;

public class ServerSendReceiveConstants {
	
	
	public static final int SEND_RECEIVE_RESULTS_INSERT_IN_PROGRESS_RETRY_COUNT = 4;

//	public static final int HTTP_CONNECTION_INSERT_IN_PROGRESS_RETRY_DELAY_MILLIS = 1000 * 60 * 60 * 5;  // 5 HOURS


	public static final int SEND_RECEIVE_RESULTS_TIMEOUT_RETRY_COUNT = 4;

	public static final int HTTP_CONNECTION_TIMEOUT_MILLIS = 1000 * 60 * 60 * 5;  // 5 HOURS

	public static final int HTTP_SOCKET_TIMEOUT_MILLIS = 1000 * 60 * 60 * 5;  // 5 HOURS

	//  Test values
	
//	public static final int HTTP_CONNECTION_TIMEOUT_MILLIS = 1000 * 10 ;  // 10 seconds
//
//	public static final int HTTP_SOCKET_TIMEOUT_MILLIS = 1000 * 10 ;  // 10 seconds

	public static final int HTTP_CONNECTION_INSERT_IN_PROGRESS_RETRY_DELAY_MILLIS = 1000 * 5;  // 5 seconds

}
