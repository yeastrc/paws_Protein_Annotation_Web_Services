package org.yeastrc.paws.www.service;


/**
 * 
 *
 */
public class Process_Disopred_2_SequenceSubmit {

	private Process_Disopred_2_SequenceSubmit() { }
	public static Process_Disopred_2_SequenceSubmit getInstance() { return new Process_Disopred_2_SequenceSubmit(); }
	
	
	/**
	 * @param sequenceToProcess
	 * @param ncbiTaxonomyId
	 * @return
	 * @throws Exception 
	 */
	public String process_Disopred_2_SequenceSubmit( String sequenceToProcess, int ncbiTaxonomyId ) throws Exception {
		
		String response = Get_Disopred_2_DataService.getInstance().get_Disopred_2_DataForSequence( sequenceToProcess, ncbiTaxonomyId );
		
		return response;
	}
}
