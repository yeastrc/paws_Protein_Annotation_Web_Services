package org.yeastrc.paws.www.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jobcenter.client.main.SubmissionClientConnectionToServer;
import org.yeastrc.paws.base.constants.JobcenterConstants;
import org.yeastrc.paws.www.constants.AnnotationDataRunStatusConstants;
import org.yeastrc.paws.www.constants.ConfigSystemsKeysConstants;
import org.yeastrc.paws.www.constants.JobcenterForWebAppConstants;
import org.yeastrc.paws.www.dao.AnnotationDataDAO;
import org.yeastrc.paws.www.dao.AnnotationProcessingTrackingDAO;
import org.yeastrc.paws.www.dao.AnnotationTypeDAO;
import org.yeastrc.paws.www.dao.ConfigSystemDAO;
import org.yeastrc.paws.www.dto.AnnotationDataDTO;
import org.yeastrc.paws.www.dto.AnnotationProcessingTrackingDTO;

/**
 * 
 *
 */
public class GetOrSubmitCommonDataService {

	private static final Logger log = Logger.getLogger(GetOrSubmitCommonDataService.class);
	
	private GetOrSubmitCommonDataService() { }
	public static GetOrSubmitCommonDataService getInstance() { return new GetOrSubmitCommonDataService(); }

	/**
	 * @param sequence - Must not be null if want to submit a job to process the sequence
	 * @param sequenceId
	 * @param ncbiTaxonomyId
	 * @param requestingIP
	 * @param batchRequest
	 * @param batchRequestId
	 * @param annotationType
	 * @param jobcenterRequestType
	 * @param jobcenterJobType
	 * @param jobcenterJobTypeBatchRequest - for batch request
	 * @return
	 * @throws Exception
	 */
	public String getDataForSequenceOrSequenceId( 
			String sequence, 
			int sequenceId, 
			int ncbiTaxonomyId, 
			String requestingIP,
			boolean batchRequest,
			String batchRequestId,
			String annotationType,
			String jobcenterRequestType,
			String jobcenterJobType,
			String jobcenterJobTypeBatchRequest
			) throws Exception {

		String response = null;
		
		String serverBaseUrl = ConfigSystemDAO.getInstance().getValueFromKey( ConfigSystemsKeysConstants.SERVER_BASE_URL_FOR_JC_MODULE_DATA_KEY );
		
		if ( serverBaseUrl == null ) {
			
			String msg = "Failed to get serverBaseUrl from ConfigSystemDAO for key '" + ConfigSystemsKeysConstants.SERVER_BASE_URL_FOR_JC_MODULE_DATA_KEY + "'." ;

			log.error( msg );
			
			throw new Exception( msg );
		}
		
		Integer annotationTypeId = AnnotationTypeDAO.getInstance().getAnnotationTypeIdByAnnotationType( annotationType );
		
		if ( annotationTypeId == null ) {
			
			String msg = "Failed to get annotation type id for type '" + annotationType + "'." ;

			log.error( msg );
			
			throw new Exception( msg );
		}
		
		boolean submitToJCModule = false;
		
		AnnotationDataDTO annotationDataDTO =
				AnnotationDataDAO.getInstance().getAnnotationDataDTOBySequenceIdAnnotationTypeNCBITaxonomyId( sequenceId, annotationTypeId, ncbiTaxonomyId );
		
		if ( annotationDataDTO == null ) {
			
			if ( sequence != null ) {
				
				// No data currently in annotation_data table and have sequence string so submit to get data
				submitToJCModule = true;
			}
			
		} else {
			//  Determine if existing requests are only batch requests

			List<AnnotationProcessingTrackingDTO> annotationProcessingTrackingDTOList = 
					AnnotationProcessingTrackingDAO.getInstance()
					.getAnnotationProcessingTrackingDTOBySequenceIdAnnotationTypeNCBITaxonomyId(
							sequenceId, annotationTypeId, ncbiTaxonomyId );
			// annotationProcessingTrackingDTOList is empty if the tracking entry has been removed 
			if ( ! annotationProcessingTrackingDTOList.isEmpty() ) {
				boolean onlyBatchRequests = true;
				for ( AnnotationProcessingTrackingDTO item : annotationProcessingTrackingDTOList ) {
					if ( ! item.isBatchRequest() ) {
						onlyBatchRequests = false;
						break;
					}
				}
				if ( onlyBatchRequests && ( ! batchRequest ) ) {
					// Only batch requests and this request not a batch request so submit to get data at higher priority
					submitToJCModule = true;
				}
			}
				
			
		}
		
		if ( ! submitToJCModule ) {
			
			if ( annotationDataDTO != null ) {

				if ( AnnotationDataRunStatusConstants.STATUS_SUBMITTED.equals( annotationDataDTO.getRunStatus() ) ) {

					response = "{" + createRunStatusSequenceIdResponse( annotationDataDTO, sequenceId ) + "}";
				} else {
					//  Return full response for annotation data record
					response = "{" + createRunStatusSequenceIdResponse( annotationDataDTO, sequenceId ) 
					+ createJSONFromDBResponse( annotationDataDTO ) + "}";
				}
			} else {
				response = "{" + createRunStatusSequenceIdResponse( annotationDataDTO, sequenceId ) + "}";
			}
			
			return response;   //  EARLY EXIT
		}
		
		if ( sequence == null ) {

			response = "{" + createRunStatusSequenceIdResponse( annotationDataDTO, sequenceId ) + "}";
			return response;   //  EARLY EXIT
		}
				
		if ( annotationDataDTO == null ) {

			annotationDataDTO = new AnnotationDataDTO();

			annotationDataDTO.setSequenceId( sequenceId );
			annotationDataDTO.setAnnotationTypeId( annotationTypeId );
			annotationDataDTO.setNcbiTaxonomyId( ncbiTaxonomyId );
			annotationDataDTO.setRunStatus( AnnotationDataRunStatusConstants.STATUS_SUBMITTED );

			AnnotationDataDAO.getInstance().save( annotationDataDTO );
		}
		
		try {

			AnnotationProcessingTrackingDTO annotationProcessingTrackingDTO = new AnnotationProcessingTrackingDTO();

			annotationProcessingTrackingDTO.setSequenceId( sequenceId );
			annotationProcessingTrackingDTO.setAnnotationTypeId( annotationTypeId );
			annotationProcessingTrackingDTO.setNcbiTaxonomyId( ncbiTaxonomyId );
			annotationProcessingTrackingDTO.setRequestingIP( requestingIP );
			annotationProcessingTrackingDTO.setBatchRequest( batchRequest );
			annotationProcessingTrackingDTO.setBatchRequestId( batchRequestId );

			annotationProcessingTrackingDTO.setRunStatus( AnnotationDataRunStatusConstants.STATUS_PENDING );
			AnnotationProcessingTrackingDAO.getInstance().save( annotationProcessingTrackingDTO );

			String jobcenterJobTypeLocal = jobcenterJobType;
			if ( batchRequest ) {
				jobcenterJobTypeLocal = jobcenterJobTypeBatchRequest;
			}

			int jobcenterRequestId = 0;

			try { 
				jobcenterRequestId = 
						submitJobToRunProgram( annotationProcessingTrackingDTO.getId(), 
								jobcenterRequestType, jobcenterJobTypeLocal, serverBaseUrl );

			} catch ( Exception e ) {

				annotationDataDTO.setRunStatus( AnnotationDataRunStatusConstants.STATUS_FAIL );
				AnnotationDataDAO.getInstance().updateAnnotationDataAnnDataRunStatus( annotationDataDTO );

				annotationProcessingTrackingDTO.setRunStatus( AnnotationDataRunStatusConstants.STATUS_FAIL );
				AnnotationProcessingTrackingDAO.getInstance().updateRunStatus(annotationProcessingTrackingDTO);

				throw e;
			}

			annotationProcessingTrackingDTO.setJobcenterRequestId( jobcenterRequestId );
			annotationProcessingTrackingDTO.setRunStatus( AnnotationDataRunStatusConstants.STATUS_SUBMITTED );

			AnnotationProcessingTrackingDAO.getInstance().updateJobcenterRequestId( annotationProcessingTrackingDTO );
			AnnotationProcessingTrackingDAO.getInstance().updateRunStatus( annotationProcessingTrackingDTO );

			response = "{" + createRunStatusSequenceIdResponse( annotationDataDTO, sequenceId ) + "}";

			return response;

		} catch ( Exception e ) {

			annotationDataDTO.setRunStatus( AnnotationDataRunStatusConstants.STATUS_FAIL );
			AnnotationDataDAO.getInstance().updateAnnotationDataAnnDataRunStatus( annotationDataDTO );

			throw e;
		}
	}
	

	

	/**
	 * @param annotationDataDTO
	 * @param sequenceId
	 * @return
	 */
	private String createRunStatusSequenceIdResponse( AnnotationDataDTO annotationDataDTO, int sequenceId ) {
		
		String runStatus = null;
		
		if ( annotationDataDTO != null ) {
			
			runStatus = annotationDataDTO.getRunStatus();
			
		} else {
			
			runStatus = AnnotationDataRunStatusConstants.STATUS_NO_RECORD;
		}
		
		
		String response = "\"pawsStatus\":\"" + runStatus + "\",\"sequenceId\":" + sequenceId;

		return response;
	}
	
	
	
	/**
	 * Assumes another parameter is before it
	 * 
	 * @param annotationDataDTO
	 * @return
	 */
	private String createJSONFromDBResponse( AnnotationDataDTO annotationDataDTO ) {

		if ( AnnotationDataRunStatusConstants.STATUS_COMPLETE.equals( annotationDataDTO.getRunStatus() ) ) {
			
			String response = ",\"data\":" + annotationDataDTO.getAnnotationData();
			
			return response;
		}

		return "";
	}

	

	/**
	 * @param trackingId
	 * @param annotationType
	 * @param jobcenterRequestType
	 * @param jobcenterJobType
	 * @param sendResultsURL
	 * @throws Exception
	 */
	private int submitJobToRunProgram( 
			int trackingId,
			String jobcenterRequestType, 
			String jobcenterJobType, 
			String serverBaseUrl ) throws Exception {
		

		int jobcenterRequestId = 0;
		
		try {
			
			String jobSubmissionURL = ConfigSystemDAO.getInstance().getValueFromKey( ConfigSystemsKeysConstants.JOB_SUBMISSION_URL );
			
			if ( jobSubmissionURL == null ) {
				
				String msg = "ConfigSystemKey '" + ConfigSystemsKeysConstants.JOB_SUBMISSION_URL + "' is not in the database";
				
				log.error( msg );
				throw new Exception(msg);
				
			}


			//  !!!!!!!!!!!!!!!!!!!!!!

			//    Submit the job

			Map<String, String> jobParameters = new HashMap<String, String> ();

			jobParameters.put( JobcenterConstants.JOB_PARAM_TRACKING_ID, Integer.toString( trackingId ) );

			jobParameters.put( JobcenterConstants.JOB_PARAM_SERVER_BASE_URL, serverBaseUrl );

			SubmissionClientConnectionToServer jobSubmissionClient = new SubmissionClientConnectionToServer();
			
			jobSubmissionClient.setNodeName( JobcenterForWebAppConstants.JOB_SUBMISSION_NODE_NAME );

			jobSubmissionClient.init( jobSubmissionURL );

			
			try {
				jobcenterRequestId = jobSubmissionClient.submitJob( jobcenterRequestType, 
						null /* no request id */, 
						jobcenterJobType, 
						"PAWS Webservices" /* submitter */, null /*  use default for priority from database config:  JobManagerConstants.JOB_TYPE_NAME_PHILIUS_PROCESS_UPLOAD_FILE_JOB_PRIORITY */, 
						jobParameters );

			} catch ( Exception e ) {

				String msg = "Submit 'Process Sequence' Job failed for jobcenterRequestType: '" + jobcenterRequestType + "'.  ";

				log.error( msg + e.toString(), e );

				throw new Exception( msg, e );
			} finally {
				
				try {
					jobSubmissionClient.destroy();
				
				} catch ( Throwable e ) {
					

					String msg = "jobSubmissionClient.destroy(); Exception: ";

					log.error( msg + e.toString(), e );
				}
			}
			
			if ( log.isDebugEnabled() ) {
			
				byte[] submitJobRequestMarshalledLastSent = jobSubmissionClient.getSubmitJobRequestMarshalledLastSent();
				
				String submitJobRequestMarshalledLastSentString = new String( submitJobRequestMarshalledLastSent, "UTF-8" );
				
				RuntimeException tempExc = new RuntimeException( "DEBUG Message, NOT an Error");
				
				log.debug( "!!!!!!!  DEBUG Message, NOT an Error:  XML sent to Jobcenter for request: trackingId: " + trackingId 
						+ ", returned jobcenterRequestId: " + jobcenterRequestId
						+ ", XML: "+  submitJobRequestMarshalledLastSentString, tempExc );
			}

		} catch ( Throwable e ) {

			String msg = "Submit 'Process Sequence' Job failed for jobcenterRequestType: '" + jobcenterRequestType + "'.  ";

			log.error( msg + e.toString(), e );


			throw new Exception( msg, e );
		}


		return jobcenterRequestId;
	}
}
