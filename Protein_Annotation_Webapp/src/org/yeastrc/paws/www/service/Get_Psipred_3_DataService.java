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
	public String get_Psipred_3_DataForSequence( String sequenceToProcess, int ncbiTaxonomyId ) throws Exception {
		
		
		
		int sequenceId = AddSequenceOrGetExistingSequenceId.getInstance().addSequenceOrGetExistingSequenceId( sequenceToProcess );
		
		return get_Psipred_3_DataForSequenceSequenceId( sequenceToProcess, sequenceId, ncbiTaxonomyId );
	}
	
	
	/**
	 * @param sequenceId
	 * @param ncbiTaxonomyId
	 * @return
	 * @throws Exception 
	 */
	public String get_Psipred_3_DataForSequenceId( int sequenceId, int ncbiTaxonomyId ) throws Exception {
		
		
		return get_Psipred_3_DataForSequenceSequenceId( null /* sequence */, sequenceId, ncbiTaxonomyId );
	}


	/**
	 * @param sequence - Must not be null if want to submit a job to process the sequence
	 * @param sequenceId
	 * @param ncbiTaxonomyId
	 * @return
	 * @throws Exception 
	 */
	private String get_Psipred_3_DataForSequenceSequenceId( String sequence, int sequenceId, int ncbiTaxonomyId ) throws Exception {

		String annotationType = AnnotationTypeStringsConstants.PSIPRED_3_TYPE;

		String jobcenterRequestType = JobcenterConstants.REQUEST_TYPE_NAME_PROTEIN_ANNOTATION_PSIPRED_3; 
		String jobcenterJobType = JobcenterConstants.JOB_TYPE_NAME_PROTEIN_ANNOTATION_PSIPRED_3;

		
		String response = GetOrSubmitCommonDataService.getInstance().getDataForSequenceOrSequenceId( sequence, sequenceId, ncbiTaxonomyId, annotationType, jobcenterRequestType, jobcenterJobType );
		
		return response;
	}

}
