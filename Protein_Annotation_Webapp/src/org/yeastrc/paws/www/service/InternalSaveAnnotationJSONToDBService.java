package org.yeastrc.paws.www.service;

import org.apache.log4j.Logger;
import org.yeastrc.paws.base.constants.InternalRestWebServiceResponseConstants;
import org.yeastrc.paws.base.constants.ModuleRunProgramStatusConstants;
import org.yeastrc.paws.www.constants.AnnotationDataRunStatusConstants;
import org.yeastrc.paws.www.dao.AnnotationDataDAO;
import org.yeastrc.paws.www.dao.AnnotationProcessingTrackingDAO;
import org.yeastrc.paws.www.dao.AnnotationTypeDAO;
import org.yeastrc.paws.www.dto.AnnotationDataDTO;
import org.yeastrc.paws.www.dto.AnnotationProcessingTrackingDTO;

/**
 * 
 *
 */
public class InternalSaveAnnotationJSONToDBService {

	private static final Logger log = Logger.getLogger(InternalSaveAnnotationJSONToDBService.class);
	
	private InternalSaveAnnotationJSONToDBService() { }
	public static InternalSaveAnnotationJSONToDBService getInstance() { return new InternalSaveAnnotationJSONToDBService(); }


	/**
	 * @param sequenceId
	 * @param ncbiTaxonomyId
	 * @param jobcenterRequestId
	 * @param annotationType
	 * @param annotationData
	 * @param moduleRunStatus
	 * @return
	 * @throws Exception
	 */
	public String saveAnnotationJSONToDB( int sequenceId, int ncbiTaxonomyId, int jobcenterRequestId, String annotationType, String annotationData, String moduleRunStatus ) throws Exception {

		
		try {
			Integer annotationTypeId = null;

			try {
				
				annotationTypeId = AnnotationTypeDAO.getInstance().getAnnotationTypeIdByAnnotationType( annotationType );
				
			} catch ( Exception e ) {
				
				String msg = "Failed getting annotation type id for annotationType: " + annotationType;
				log.error( msg, e );
				
				throw e;
			}
			
			if ( annotationTypeId == null ) {
				
				String msg = "Failed getting annotation type id for annotationType: " + annotationType;
				log.error( msg );
				
				throw new Exception(msg);
			}
			
			String runStatus = AnnotationDataRunStatusConstants.STATUS_COMPLETE;
			
			if ( ModuleRunProgramStatusConstants.STATUS_FAIL.equals( moduleRunStatus ) ) {
				
				runStatus = AnnotationDataRunStatusConstants.STATUS_FAIL;
				
			} else if ( ! ModuleRunProgramStatusConstants.STATUS_SUCCESS.equals( moduleRunStatus ) ) {
				
				String msg = "Failed:  Module Run status not recognized, moduleRunStatus: " + moduleRunStatus;
				log.error( msg );
				
				throw new Exception(msg);
			}
			
			AnnotationDataDTO item = new AnnotationDataDTO();

			item.setAnnotationTypeId( annotationTypeId );
			item.setNcbiTaxonomyId( ncbiTaxonomyId );
			item.setSequenceId( sequenceId );
			item.setRunStatus( runStatus );
			item.setAnnotationData( annotationData );

			AnnotationDataDAO.getInstance().updateAnnotationDataRunStatus( item );
			
			AnnotationProcessingTrackingDTO annotationProcessingTrackingDTO = new AnnotationProcessingTrackingDTO();
			
			annotationProcessingTrackingDTO.setAnnotationTypeId( annotationTypeId );
			annotationProcessingTrackingDTO.setNcbiTaxonomyId( ncbiTaxonomyId );
			annotationProcessingTrackingDTO.setSequenceId( sequenceId );
			annotationProcessingTrackingDTO.setJobcenterRequestId( jobcenterRequestId );
			annotationProcessingTrackingDTO.setRunStatus( runStatus );
			
			AnnotationProcessingTrackingDAO.getInstance().updateRunStatus( annotationProcessingTrackingDTO );

			return InternalRestWebServiceResponseConstants.SUCCESS;
			
		} catch ( Exception e ) {

			String msg = "Failed saving annotation data for annotationType: " + annotationType;
			log.error( msg, e );
			
			throw e;
		}
	}
	
}
