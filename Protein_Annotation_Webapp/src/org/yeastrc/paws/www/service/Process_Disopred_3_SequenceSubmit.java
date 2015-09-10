package org.yeastrc.paws.www.service;


/**
 * 
 *
 */
public class Process_Disopred_3_SequenceSubmit {

	private Process_Disopred_3_SequenceSubmit() { }
	public static Process_Disopred_3_SequenceSubmit getInstance() { return new Process_Disopred_3_SequenceSubmit(); }
	
	
	/**
	 * @param sequenceToProcess
	 * @param ncbiTaxonomyId
	 * @return
	 * @throws Exception 
	 */
	public String process_Disopred_3_SequenceSubmit( String sequenceToProcess, int ncbiTaxonomyId ) throws Exception {
		
		String response = Get_Disopred_3_DataService.getInstance().get_Disopred_3_DataForSequence( sequenceToProcess, ncbiTaxonomyId );
		
		return response;
	}
}
