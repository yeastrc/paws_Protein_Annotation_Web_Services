package org.yeastrc.paws.www.service;


import org.apache.log4j.Logger;
import org.yeastrc.paws.base.constants.JobcenterConstants;
import org.yeastrc.paws.base.constants.AnnotationTypeStringsConstants;

/**
 * 
 *
 */
public class Get_Psipred_3_DataService {

	private static final Logger log = Logger.getLogger(Get_Psipred_3_DataService.class);
	
	private Get_Psipred_3_DataService() { }
	public static Get_Psipred_3_DataService getInstance() { return new Get_Psipred_3_DataService(); }
	
	
	/**
	 * @param sequenceToProcess
	 * @param ncbiTaxonomyId
	 * @return
	 * @throws Exception
	 */
	public String get_Psipred_3_DataForSequence( String sequenceToProcess, int ncbiTaxonomyId,
			String requestingIP, boolean batchRequest, String batchRequestId ) throws Exception {
		
		
		
		int sequenceId = AddSequenceOrGetExistingSequenceId.getInstance().addSequenceOrGetExistingSequenceId( sequenceToProcess );
		
		return get_Psipred_3_DataForSequenceSequenceId( sequenceToProcess, sequenceId, ncbiTaxonomyId,
				requestingIP, batchRequest, batchRequestId );
	}
	
	
	/**
	 * @param sequenceId
	 * @param ncbiTaxonomyId
	 * @return
	 * @throws Exception 
	 */
	public String get_Psipred_3_DataForSequenceId( int sequenceId, int ncbiTaxonomyId,
			String requestingIP, boolean batchRequest, String batchRequestId ) throws Exception {
		
		
		return get_Psipred_3_DataForSequenceSequenceId( null /* sequence */, sequenceId, ncbiTaxonomyId,
				requestingIP, batchRequest, batchRequestId );
	}


	/**
	 * @param sequence - Must not be null if want to submit a job to process the sequence
	 * @param sequenceId
	 * @param ncbiTaxonomyId
	 * @return
	 * @throws Exception 
	 */
	private String get_Psipred_3_DataForSequenceSequenceId( 
			String sequence, int sequenceId, int ncbiTaxonomyId,
			String requestingIP, boolean batchRequest, String batchRequestId
			) throws Exception {

		String annotationType = AnnotationTypeStringsConstants.PSIPRED_3_TYPE;

		String jobcenterRequestType = JobcenterConstants.REQUEST_TYPE_NAME_PROTEIN_ANNOTATION_PSIPRED_3; 
		String jobcenterJobType = JobcenterConstants.JOB_TYPE_NAME_PROTEIN_ANNOTATION_PSIPRED_3;
		String jobcenterJobTypeBatchRequest = JobcenterConstants.JOB_TYPE_NAME_PROTEIN_ANNOTATION_PSIPRED_3_BATCH;

		String response = 
				GetOrSubmitCommonDataService.getInstance()
				.getDataForSequenceOrSequenceId( sequence, sequenceId, ncbiTaxonomyId, 
						requestingIP, batchRequest, batchRequestId, annotationType,
						jobcenterRequestType, jobcenterJobType, jobcenterJobTypeBatchRequest );

		return response;
	}

}
