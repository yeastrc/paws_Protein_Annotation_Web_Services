package org.yeastrc.paws.www.service;

import org.apache.log4j.Logger;
import org.yeastrc.paws.www.constants.AnnotationDataRunStatusConstants;
import org.yeastrc.paws.www.dao.SequenceDAO;

/**
 * 
 *
 */
public class Get_SequenceIdForSequence {
	
	private static final Logger log = Logger.getLogger(Get_SequenceIdForSequence.class);
	
	private Get_SequenceIdForSequence() { }
	public static Get_SequenceIdForSequence getInstance() { return new Get_SequenceIdForSequence(); }
	
	/**
	 * @param sequence
	 * @return
	 * @throws Exception
	 */
	public String get_SequenceIdForSequence( String sequence ) throws Exception {

		Integer sequenceId = SequenceDAO.getInstance().getSequenceIdBySequence( sequence );
		
		String response = null;
		
		if ( sequenceId != null ) {
			
			response = "{\"getStatus\":\"" + AnnotationDataRunStatusConstants.STATUS_COMPLETE + "\",\"sequenceId\":" + sequenceId + "}";
			
		} else {
			
			response = "{\"getStatus\":\"" + AnnotationDataRunStatusConstants.STATUS_NO_RECORD + "\"}";
		}
		
		
		return response;
		
	}
}
