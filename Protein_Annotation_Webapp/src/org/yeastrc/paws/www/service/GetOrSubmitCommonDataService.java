package org.yeastrc.paws.www.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jobcenter.client.main.SubmissionClientConnectionToServer;
import org.jobcenter.coreinterfaces.JobSubmissionInterface;
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
	 * @param annotationType
	 * @param jobcenterRequestType
	 * @param jobcenterJobType
	 * @return
	 * @throws Exception
	 */
	public String getDataForSequenceOrSequenceId( 
			String sequence, 
			int sequenceId, 
			int ncbiTaxonomyId, 
			String annotationType,
			String jobcenterRequestType,
			String jobcenterJobType
			) throws Exception {

		String response = null;
		
		String sendResultsURL = ConfigSystemDAO.getInstance().getValueFromKey( ConfigSystemsKeysConstants.SERVER_URL_FOR_MODULE_DATA_KEY );
		
		if ( sendResultsURL == null ) {
			
			String msg = "Failed to get sendResultsURL from ConfigSystemDAO for key '" + ConfigSystemsKeysConstants.SERVER_URL_FOR_MODULE_DATA_KEY + "'." ;

			log.error( msg );
			
			throw new Exception( msg );
		}
		
		Integer annotationTypeId = AnnotationTypeDAO.getInstance().getAnnotationTypeIdByAnnotationType( annotationType );
		
		if ( annotationTypeId == null ) {
			
			String msg = "Failed to get annotation type id for type '" + annotationType + "'." ;

			log.error( msg );
			
			throw new Exception( msg );
		}
		
		AnnotationDataDTO annotationDataDTO =
				AnnotationDataDAO.getInstance().getAnnotationDataDTOBySequenceIdAnnotationTypeNCBITaxonomyId( sequenceId, annotationTypeId, ncbiTaxonomyId );
		
		if ( annotationDataDTO == null ) {
			
			if ( sequence != null ) {
				
				annotationDataDTO = new AnnotationDataDTO();
				
				annotationDataDTO.setSequenceId( sequenceId );
				annotationDataDTO.setAnnotationTypeId( annotationTypeId );
				annotationDataDTO.setNcbiTaxonomyId( ncbiTaxonomyId );
				annotationDataDTO.setRunStatus( AnnotationDataRunStatusConstants.STATUS_SUBMITTED );
				
				AnnotationDataDAO.getInstance().save( annotationDataDTO );
				
				int jobcenterRequestId = 
						submitJobToRunProgram( sequence, sequenceId, ncbiTaxonomyId, annotationType, annotationTypeId, 
												jobcenterRequestType, jobcenterJobType, sendResultsURL );
				
				AnnotationProcessingTrackingDTO annotationProcessingTrackingDTO = new AnnotationProcessingTrackingDTO();
				
				annotationProcessingTrackingDTO.setSequenceId( sequenceId );
				annotationProcessingTrackingDTO.setAnnotationTypeId( annotationTypeId );
				annotationProcessingTrackingDTO.setNcbiTaxonomyId( ncbiTaxonomyId );
				annotationProcessingTrackingDTO.setJobcenterRequestId( jobcenterRequestId );
				annotationProcessingTrackingDTO.setRunStatus( AnnotationDataRunStatusConstants.STATUS_SUBMITTED );
				
				AnnotationProcessingTrackingDAO.getInstance().save( annotationProcessingTrackingDTO );
				
				response = "{" + createRunStatusSequenceIdResponse( annotationDataDTO, sequenceId ) + "}";
				
			} else {
				
				response = "{" + createRunStatusSequenceIdResponse( annotationDataDTO, sequenceId ) + "}";
			}
			
		} else if ( AnnotationDataRunStatusConstants.STATUS_SUBMITTED.equals( annotationDataDTO.getRunStatus() ) ) {
			
			response = "{" + createRunStatusSequenceIdResponse( annotationDataDTO, sequenceId ) + "}";
			
		} else  {
			
			response = "{" + createRunStatusSequenceIdResponse( annotationDataDTO, sequenceId ) 
					+ createJSONFromDBResponse( annotationDataDTO ) + "}";
		}
		
		return response;
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
	 * @param sequence
	 * @param sequenceId
	 * @param ncbiTaxonomyId
	 * @param annotationType
	 * @param annotationTypeId
	 * @param jobcenterRequestType
	 * @param jobcenterJobType
	 * @param sendResultsURL
	 * @throws Exception
	 */
	private int submitJobToRunProgram( String sequence, int sequenceId, int ncbiTaxonomyId,
			String annotationType, int annotationTypeId, 
			String jobcenterRequestType, String jobcenterJobType, 
			String sendResultsURL ) throws Exception {
		

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

			jobParameters.put( JobcenterConstants.JOB_PARAM_SEQUENCE, sequence );

			jobParameters.put( JobcenterConstants.JOB_PARAM_SEQUENCE_ID, Integer.toString( sequenceId ) );

			jobParameters.put( JobcenterConstants.JOB_PARAM_NCBI_TAXONOMY_ID, Integer.toString( ncbiTaxonomyId ) );

			jobParameters.put( JobcenterConstants.JOB_PARAM_ANNOTATION_TYPE, annotationType );
			jobParameters.put( JobcenterConstants.JOB_PARAM_ANNOTATION_TYPE_ID, Integer.toString( annotationTypeId ) );

			jobParameters.put( JobcenterConstants.JOB_PARAM_SEND_RESULTS_URL, sendResultsURL );


			
			JobSubmissionInterface jobSubmissionClient = new SubmissionClientConnectionToServer();
			
			jobSubmissionClient.setNodeName( JobcenterForWebAppConstants.JOB_SUBMISSION_NODE_NAME );

			String connectionURL = jobSubmissionURL;

			jobSubmissionClient.init( connectionURL );


			
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

		} catch ( Throwable e ) {

			String msg = "Submit 'Process Sequence' Job failed for jobcenterRequestType: '" + jobcenterRequestType + "'.  ";

			log.error( msg + e.toString(), e );


			throw new Exception( msg, e );
		}


		return jobcenterRequestId;
	}
}
