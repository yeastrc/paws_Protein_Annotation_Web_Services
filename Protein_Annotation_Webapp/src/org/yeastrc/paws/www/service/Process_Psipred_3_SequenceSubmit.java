package org.yeastrc.paws.www.service;


/**
 * 
 *
 */
public class Process_Psipred_3_SequenceSubmit {

	private Process_Psipred_3_SequenceSubmit() { }
	public static Process_Psipred_3_SequenceSubmit getInstance() { return new Process_Psipred_3_SequenceSubmit(); }
	
	
	/**
	 * @param sequenceToProcess
	 * @param ncbiTaxonomyId
	 * @return
	 * @throws Exception 
	 */
	public String process_Psipred_3_SequenceSubmit( String sequenceToProcess, int ncbiTaxonomyId,
			String requestingIP, boolean batchRequest, String batchRequestId ) throws Exception {
		
		String response = 
				Get_Psipred_3_DataService.getInstance()
				.get_Psipred_3_DataForSequence( sequenceToProcess, ncbiTaxonomyId, requestingIP, batchRequest, batchRequestId );
		
		return response;
	}
}
