package org.yeastrc.paws.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.log4j.Logger;
import org.yeastrc.paws.base.constants.RestWebServiceQueryStringAndFormFieldParamsConstants;
import org.yeastrc.paws.base.constants.ModuleSendToServerResponseConstants;
import org.yeastrc.paws.constants.SendResultsToServerConstants;

public class SendResultsToServer {
	
	
	
	private static Logger log = Logger.getLogger(SendResultsToServer.class);
	
	


	private static volatile boolean shutdownRequested = false;




	/**
	 *
	 */
	public static void shutdown() {

		log.warn( "shutdown() called" );
		shutdownRequested = true;

		awaken();
	}

	
	
	/**
	 *
	 */
	public static void stopRunningAfterProcessingJob() {

		log.warn( "stopRunningAfterProcessingJob() called" );
		shutdownRequested = true;

		awaken();
	}
	

	/**
	 *
	 */
	public static void awaken() {

		log.warn( "awaken() called" );

		synchronized (SendResultsToServer.class) {

			SendResultsToServer.class.notifyAll();
		}
	}

	/**
	 * This method takes the program result string and sends it to the server using the HttpClient
	 *
	 * @param sequenceId
	 * @param ncbiTaxonomyId
	 * @param annotationType
	 * @param annotationTypeId
	 * @param annotationData
	 * @param status
	 * @param post_url
	 * @throws Throwable
	 */
	public static void send( 
			int sequenceId, int ncbiTaxonomyId, 
			String annotationType, int annotationTypeId, String annotationData, 
			int jobcenterRequestId,
			String status, 
			String post_url) throws Throwable {
		
		
		if ( log.isInfoEnabled() ) {
			
			
			log.info( "Sending the result to the server. sequenceId: " + sequenceId 
					+ ", ncbiTaxonomyId: " + ncbiTaxonomyId
					+ ", annotationType: " + annotationType
					+ ", annotationTypeId: " + annotationTypeId
					+ ", jobcenterRequestId: " + jobcenterRequestId
					+ ", status: " + status
					+ ", post_url: " + post_url
					+ " \n annotationData: " + annotationData
					
					);
			
		}

		HttpClient client = null;
		HttpPost post = null;
		BufferedReader rd = null;
		List<NameValuePair> nameValuePairs = null;

		try {

			client = new DefaultHttpClient();

			HttpParams httpParams = client.getParams();

			HttpConnectionParams.setConnectionTimeout(httpParams, SendResultsToServerConstants.HTTP_CONNECTION_TIMEOUT_MILLIS);
			HttpConnectionParams.setSoTimeout(httpParams, SendResultsToServerConstants.HTTP_SOCKET_TIMEOUT_MILLIS);

			post = new HttpPost(post_url);
			nameValuePairs = new ArrayList<NameValuePair>(1);

			nameValuePairs.add(new BasicNameValuePair(RestWebServiceQueryStringAndFormFieldParamsConstants.REQUEST_PARAM_SEQUENCE_ID, 
							Integer.toString( sequenceId ) ) );
			nameValuePairs.add(new BasicNameValuePair(RestWebServiceQueryStringAndFormFieldParamsConstants.REQUEST_PARAM_NCBI_TAXONOMY_ID, 
					Integer.toString( ncbiTaxonomyId ) ) );
			nameValuePairs.add(new BasicNameValuePair(RestWebServiceQueryStringAndFormFieldParamsConstants.REQUEST_PARAM_ANNOTATION_TYPE, 
					annotationType ) );
			nameValuePairs.add(new BasicNameValuePair(RestWebServiceQueryStringAndFormFieldParamsConstants.REQUEST_PARAM_ANNOTATION_TYPE_ID, 
					Integer.toString( annotationTypeId ) ) );
			nameValuePairs.add(new BasicNameValuePair(RestWebServiceQueryStringAndFormFieldParamsConstants.REQUEST_PARAM_ANNOTATION_DATA, 
					annotationData));
			
			nameValuePairs.add(new BasicNameValuePair(RestWebServiceQueryStringAndFormFieldParamsConstants.REQUEST_PARAM_JOBCENTER_REQUEST_ID,
					Integer.toString( jobcenterRequestId ) ) );

			nameValuePairs.add(new BasicNameValuePair(RestWebServiceQueryStringAndFormFieldParamsConstants.REQUEST_PARAM_STATUS, 
					status));

			post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			HttpResponse httpResponse = null;

			int insertInProgressRetryCount = 0;

			boolean sendWithoutInsertInProgressResponse = false;


			while ( ! sendWithoutInsertInProgressResponse ) {

				sendWithoutInsertInProgressResponse = true;

				insertInProgressRetryCount++;

				int timeoutRetryCount = 0;

				boolean sendWithoutException = false;

				while ( !shutdownRequested && ! sendWithoutException ) {

					timeoutRetryCount++;

					try {
						
						if ( log.isInfoEnabled() ) {
							
							
							log.info( "About to Send: timeoutRetryCount: " + timeoutRetryCount + ", Send the result to the server. sequenceId: " + sequenceId 
									+ ", ncbiTaxonomyId: " + ncbiTaxonomyId
									+ ", annotationType: " + annotationType
									+ ", annotationTypeId: " + annotationTypeId
									);
						}

						
						httpResponse = client.execute(post);

						sendWithoutException = true; // if got response without exception, exit loop
						
						
						if ( log.isInfoEnabled() ) {
							
							
							log.info( "Send without Exception: Send the result to the server. sequenceId: " + sequenceId 
									+ ", ncbiTaxonomyId: " + ncbiTaxonomyId
									+ ", annotationType: " + annotationType
									+ ", annotationTypeId: " + annotationTypeId
									);
						}

					} catch (Throwable t) {

						if ( timeoutRetryCount > SendResultsToServerConstants.SEND_RESULTS_TIMEOUT_RETRY_COUNT ) {

							//  if retry count exceeded, rethrow the exception to exit the retry loop

							String msg = "Timeout in HTTP Send.  Failed to send Program Results String, retry count exceeded so failing job. "
									+ " sequenceId = "
									+ sequenceId + ", timeoutRetryCount = " + timeoutRetryCount + ", timeout retry count max = "
									+ SendResultsToServerConstants.SEND_RESULTS_TIMEOUT_RETRY_COUNT;

							log.error( msg, t);

							throw new Exception( msg, t );
						}

						log.error("Timeout in HTTP Send.  Failed to send Program Results String, retry count NOT exceeded so retrying send. "
								+ " sequenceId = "
								+ sequenceId + ", timeoutRetryCount = " + timeoutRetryCount + ", timeout retry count max = "
								+ SendResultsToServerConstants.SEND_RESULTS_TIMEOUT_RETRY_COUNT, t);
					}
				}
				
				int httpStatusCode = httpResponse.getStatusLine().getStatusCode();

				// The HttpStatus should be 200 ( HttpStatus.SC_OK )

				if (  httpStatusCode != HttpStatus.SC_OK ) {
					

					String msg = "Fail Send results to server. httpStatusCode != HttpStatus.SC_OK.  httpStatusCode: " + httpStatusCode
							+ ",  post_url: " + post_url + ", sequenceId: " + sequenceId
							+ ", ncbiTaxonomyId: " + ncbiTaxonomyId
							+ ", annotationType: " + annotationType
							+ ", annotationTypeId: " + annotationTypeId
							+ ", jobcenterRequestId: " + jobcenterRequestId
							+ ", status: " + status
							+ ", annotationData: " + annotationData
							
							;
					
					log.error( msg );

					throw new Exception( msg );
					
				}
				

				rd = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));

				String lastLineRead = null;

				String line = "";
				while ((line = rd.readLine()) != null) {

					if ( log.isDebugEnabled() ) {
						log.debug("Response: " + line);
					}

					lastLineRead = line;
				}

				if ( lastLineRead != null && lastLineRead.startsWith( ModuleSendToServerResponseConstants.FAIL ) ) {

					String msg = "Status of 'failure' from server while inserting Program results to DB, Line from server = " + lastLineRead;

					log.error( msg );

					throw new Exception( msg );
				}
//
//				if ( lastLineRead != null && lastLineRead.startsWith( InsertProgramResultsResponseStringConstants.INSERT_IN_PROGRESS_RESPONSE_STRING ) ) {
//
//					sendWithoutInsertInProgressResponse = false;
//
//					if ( insertInProgressRetryCount > SendResultsToServerConstants.SEND_RESULTS_INSERT_IN_PROGRESS_RETRY_COUNT ) {
//
//						//  if retry count exceeded, rethrow the exception to exit the retry loop
//
//						String msg = "Status of 'insert in progress' from server while inserting Program results to DB."
//							+ "  sequenceId = "
//							+ sequenceId + ", insertInProgressRetryCount = " + insertInProgressRetryCount + ", insert in progress retry count max = "
//							+ SendResultsToServerConstants.SEND_RESULTS_INSERT_IN_PROGRESS_RETRY_COUNT;
//
//						log.error( msg );
//
//						throw new Exception( msg );
//					}
//
//					String msg = "Status of 'insert in progress' from server while inserting Program results to DB."
//						+ "  sequenceId = "
//						+ sequenceId + ", insertInProgressRetryCount = " + insertInProgressRetryCount + ", insert in progress retry count max = "
//						+ SendResultsToServerConstants.SEND_RESULTS_INSERT_IN_PROGRESS_RETRY_COUNT
//						+ ", waiting "
//						+ ( SendResultsToServerConstants.HTTP_CONNECTION_INSERT_IN_PROGRESS_RETRY_DELAY_MILLIS / 1000 )
//						+ " seconds before retrying.  "
//						+ " Line from server = " + lastLineRead;
//
//					log.error( msg );
//
//					try {
//						synchronized (SendResultsToServer.class) {
//
//							SendResultsToServer.class.wait( SendResultsToServerConstants.HTTP_CONNECTION_INSERT_IN_PROGRESS_RETRY_DELAY_MILLIS );
//						}
//
//
//					} catch (Throwable t) {
//						log.error("SendResultsToServer.class.wait( SendResultsToServerConstants.HTTP_CONNECTION_INSERT_IN_PROGRESS_RETRY_DELAY_MILLIS );" + sequenceId, t);
//					}
//
//					if ( shutdownRequested ) {
//
//						String msgShutdownRequested = "Shutdown requested while Status of 'insert in progress' from server while inserting Program results to DB."
//							+ "  sequenceId = " + sequenceId 
//							+ ", insertInProgressRetryCount = " + insertInProgressRetryCount 
//							+ ", insert in progress retry count max = "
//							+ SendResultsToServerConstants.SEND_RESULTS_INSERT_IN_PROGRESS_RETRY_COUNT;
//
//						log.error( msgShutdownRequested );
//
//						throw new Exception( msgShutdownRequested );
//					}
//
//				}

			} //

		} catch (Throwable t) {
			log.error("Failed to send String.  sequenceId = " + sequenceId, t);
			throw t;
		} finally {

			if ( rd != null ) {
				rd.close();
			}
		}
		

		
		if ( log.isInfoEnabled() ) {
			
			
			log.info( "SUCCESSFUL: Send without Exception: Send the result to the server. sequenceId: " + sequenceId 
					+ ", ncbiTaxonomyId: " + ncbiTaxonomyId
					+ ", annotationType: " + annotationType
					+ ", annotationTypeId: " + annotationTypeId
					);
			
		}

	}

}
