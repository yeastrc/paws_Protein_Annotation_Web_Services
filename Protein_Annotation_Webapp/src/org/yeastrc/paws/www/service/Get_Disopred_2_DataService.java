package org.yeastrc.paws.www.service;


import org.apache.log4j.Logger;
import org.yeastrc.paws.base.constants.JobcenterConstants;
import org.yeastrc.paws.base.constants.AnnotationTypeStringsConstants;

/**
 * 
 *
 */
public class Get_Disopred_2_DataService {

	private static final Logger log = Logger.getLogger(Get_Disopred_2_DataService.class);
	
	private Get_Disopred_2_DataService() { }
	public static Get_Disopred_2_DataService getInstance() { return new Get_Disopred_2_DataService(); }
	
	
	/**
	 * @param sequenceToProcess
	 * @param ncbiTaxonomyId
	 * @return
	 * @throws Exception
	 */
	public String get_Disopred_2_DataForSequence( String sequenceToProcess, int ncbiTaxonomyId ) throws Exception {
		
		
		
		int sequenceId = AddSequenceOrGetExistingSequenceId.getInstance().addSequenceOrGetExistingSequenceId( sequenceToProcess );
		
		return get_Disopred_2_DataForSequenceSequenceId( sequenceToProcess, sequenceId, ncbiTaxonomyId );
	}
	
	
	/**
	 * @param sequenceId
	 * @param ncbiTaxonomyId
	 * @return
	 * @throws Exception 
	 */
	public String get_Disopred_2_DataForSequenceId( int sequenceId, int ncbiTaxonomyId ) throws Exception {
		
		
		return get_Disopred_2_DataForSequenceSequenceId( null /* sequence */, sequenceId, ncbiTaxonomyId );
	}


	/**
	 * @param sequence - Must not be null if want to submit a job to process the sequence
	 * @param sequenceId
	 * @param ncbiTaxonomyId
	 * @return
	 * @throws Exception 
	 */
	private String get_Disopred_2_DataForSequenceSequenceId( String sequence, int sequenceId, int ncbiTaxonomyId ) throws Exception {

		String annotationType = AnnotationTypeStringsConstants.DISOPRED_2_TYPE;

		String jobcenterRequestType = JobcenterConstants.REQUEST_TYPE_NAME_PROTEIN_ANNOTATION_DISOPRED_2; 
		String jobcenterJobType = JobcenterConstants.JOB_TYPE_NAME_PROTEIN_ANNOTATION_DISOPRED_2;

		
		String response = GetOrSubmitCommonDataService.getInstance().getDataForSequenceOrSequenceId( sequence, sequenceId, ncbiTaxonomyId, annotationType, jobcenterRequestType, jobcenterJobType );
		
		return response;
	}

}
