package org.yeastrc.paws.server_communication;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.log4j.Logger;
import org.yeastrc.paws.base.client_server_shared_objects.GetDataForTrackingIdServerResponse;
import org.yeastrc.paws.base.constants.InternalRestWebServicePathsConstants;
import org.yeastrc.paws.base.constants.RestWebServiceQueryStringAndFormFieldParamsConstants;
import org.yeastrc.paws.constants.ServerSendReceiveConstants;
import org.yeastrc.paws.exceptions.PawsTrackingIdNotFoundException;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Get Annotation Parts from server for Tracking Id
 *
 * If the Annotation data hasn't been computed, returns the protein sequence
 */
public class GetAnnotationDataPartsForTrackingId {

	private static Logger log = Logger.getLogger(GetAnnotationDataPartsForTrackingId.class);
	
	
	public static class GetAnnotationDataPartsForTrackingIdResult {
		String sequence;
		boolean alreadyComputed;
		
		public String getSequence() {
			return sequence;
		}
		public boolean isAlreadyComputed() {
			return alreadyComputed;
		}
	}
	

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
	 * @param trackingId
	 * @return
	 * @throws Throwable 
	 */
	public static GetAnnotationDataPartsForTrackingIdResult getProteinSequenceForTrackingId( 
			int trackingId, 
			String serverBaseURL ) throws Throwable {
		

		String serverURL = serverBaseURL + InternalRestWebServicePathsConstants.SERVER_URL_EXTENSION__GET_DATA_TO_PROCESS 
				+ "?" + RestWebServiceQueryStringAndFormFieldParamsConstants.REQUEST_PARAM_TRACKING_ID + "=" + trackingId;
		
		GetAnnotationDataPartsForTrackingIdResult getAnnotationDataPartsForTrackingIdResult = new GetAnnotationDataPartsForTrackingIdResult();

		if ( log.isInfoEnabled() ) {
			
			
			log.info( "getProteinSequenceForTrackingId. trackingId: " + trackingId );
			
		}

		HttpClient client = null;
		HttpGet httpGet = null;

		try {

			client = new DefaultHttpClient();

			HttpParams httpParams = client.getParams();

			HttpConnectionParams.setConnectionTimeout(httpParams, ServerSendReceiveConstants.HTTP_CONNECTION_TIMEOUT_MILLIS);
			HttpConnectionParams.setSoTimeout(httpParams, ServerSendReceiveConstants.HTTP_SOCKET_TIMEOUT_MILLIS);

			httpGet = new HttpGet( serverURL );

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
							
							
							log.info( "About to Send: timeoutRetryCount: " + timeoutRetryCount 
									+ ", Send the result to the server. trackingId: " + trackingId );
						}

						
						httpResponse = client.execute(httpGet);

						sendWithoutException = true; // if got response without exception, exit loop
						
						
						if ( log.isInfoEnabled() ) {
							
							
							log.info( "Send without Exception: Send the result to the server. trackingId: " + trackingId );
						}

					} catch (Throwable t) {

						if ( timeoutRetryCount > ServerSendReceiveConstants.SEND_RECEIVE_RESULTS_TIMEOUT_RETRY_COUNT ) {

							//  if retry count exceeded, rethrow the exception to exit the retry loop

							String msg = "Timeout in HTTP Send.  Failed to send Program Results String, retry count exceeded so failing job. "
									+ " trackingId = "
									+ trackingId + ", timeoutRetryCount = " + timeoutRetryCount + ", timeout retry count max = "
									+ ServerSendReceiveConstants.SEND_RECEIVE_RESULTS_TIMEOUT_RETRY_COUNT;

							log.error( msg, t);

							throw new Exception( msg, t );
						}

						log.error("Timeout in HTTP Send.  Failed to send Program Results String, retry count NOT exceeded so retrying send. "
								+ " trackingId = "
								+ trackingId + ", timeoutRetryCount = " + timeoutRetryCount + ", timeout retry count max = "
								+ ServerSendReceiveConstants.SEND_RECEIVE_RESULTS_TIMEOUT_RETRY_COUNT, t);
					}
				}
				
				int httpStatusCode = httpResponse.getStatusLine().getStatusCode();
			
				
				ByteArrayOutputStream serverResponse = new ByteArrayOutputStream( 40000 );
				InputStream responseInputStream = null;
				byte[] responseBytes = new byte[100000];

				try {
					responseInputStream = httpResponse.getEntity().getContent();
					while (true) {
						int bytesRead = responseInputStream.read( responseBytes );
						if ( bytesRead == -1 ) {
							break;
						}
						serverResponse.write(responseBytes, 0, bytesRead);
					}
				} catch ( Exception e ) {
					String msg = "Failed reading response from server";
					log.error( msg, e );
					throw e;
				} finally {
					if ( responseInputStream != null ) {
						responseInputStream.close();
					}
					
				}
				
				// The HttpStatus should be 200 ( HttpStatus.SC_OK )

				if (  httpStatusCode != HttpStatus.SC_OK ) {
					

					String msg = "Fail Get data from server. httpStatusCode != HttpStatus.SC_OK.  httpStatusCode: " + httpStatusCode
							+ ",  serverURL: " + serverURL + ", trackingId: " + trackingId
							;
					
					log.error( msg );

					throw new Exception( msg );
					
				}
				

				ObjectMapper jacksonJSON_Mapper = new ObjectMapper();  //  Jackson JSON library object
//				validationResponse = jacksonJSON_Mapper.readValue( responseInputStream, ValidationResponse.class );
				GetDataForTrackingIdServerResponse getDataForTrackingIdServerResponse = 
						jacksonJSON_Mapper.readValue( serverResponse.toByteArray(), GetDataForTrackingIdServerResponse.class );


				if ( getDataForTrackingIdServerResponse.isNoRecordForTrackingId() ) {
					String msg = "No record for tracking id: " + trackingId;
					log.error( msg );
					throw new PawsTrackingIdNotFoundException( msg );
				}

				if ( ! getDataForTrackingIdServerResponse.isSuccess() ) {
					String msg = "Get record for tracking id failed.  tracking id: " + trackingId;
					log.error( msg );
					throw new PawsTrackingIdNotFoundException( msg );
				}
				
				getAnnotationDataPartsForTrackingIdResult.alreadyComputed = getDataForTrackingIdServerResponse.isDataAlreadyProcessed();
				getAnnotationDataPartsForTrackingIdResult.sequence = getDataForTrackingIdServerResponse.getSequence();
				
				return getAnnotationDataPartsForTrackingIdResult;

			} //

		} catch (Throwable t) {
			log.error("Failed to get data.  trackingId = " + trackingId, t);
			throw t;
		} finally {

		
		}
		

		
		if ( log.isInfoEnabled() ) {
			
			
			log.info( "SUCCESSFUL: Get without Exception: Get the data to process from the server. trackingId: " + trackingId ); 
			
		}
		
		
		return getAnnotationDataPartsForTrackingIdResult;
	}

}
