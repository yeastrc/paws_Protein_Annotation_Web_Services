package org.yeastrc.paws.www.service;

import org.apache.log4j.Logger;
import org.yeastrc.paws.base.constants.InternalRestWebServiceResponseConstants;
import org.yeastrc.paws.www.constants.AnnotationDataRunStatusConstants;
import org.yeastrc.paws.www.dao.AnnotationDataDAO;
import org.yeastrc.paws.www.dao.AnnotationProcessingTrackingDAO;
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
	 * @param annotationData
	 * @param newRunStatus
	 * @param annotationProcessingTrackingDTO
	 * @param annotationDataDTO
	 * @return
	 * @throws Exception
	 */
	public String saveAnnotationJSONToDB( String annotationData, String newRunStatus, AnnotationProcessingTrackingDTO annotationProcessingTrackingDTO, AnnotationDataDTO annotationDataDTO ) throws Exception {
		try {
			if ( log.isInfoEnabled() ) {
				log.info( "saveAnnotationJSONToDB(...): trackingId: " + annotationProcessingTrackingDTO.getId() + ", newRunStatus: " + newRunStatus + ", annotationData: " + annotationData );
			}
			
			if ( AnnotationDataRunStatusConstants.STATUS_COMPLETE.equals( annotationDataDTO.getRunStatus() )
					|| newRunStatus.equals( annotationDataDTO.getRunStatus() ) ) {
				//  The the existing run status is successful or run status didn't change from the existing value on annotationDataDTO so don't change the record.  
				//  Only update tracking record and exit

				if ( log.isInfoEnabled() ) {
					log.info( "saveAnnotationJSONToDB(..): Existing Run status is " + AnnotationDataRunStatusConstants.STATUS_COMPLETE
							+ ", or is same as new run status so don't update annotationDataDTO in DB, and only update status in annotationProcessingTrackingDTO in DB.  "
							+ "  trackingId: " + annotationProcessingTrackingDTO.getId() + ", newRunStatus: " + newRunStatus + ", annotationData: " + annotationData );
				}

				annotationProcessingTrackingDTO.setRunStatus( newRunStatus );
				AnnotationProcessingTrackingDAO.getInstance().updateRunStatus( annotationProcessingTrackingDTO );

				//  Update any other records with same sequenceId, ann type id, and tax id
				AnnotationProcessingTrackingDAO.getInstance()
				.updateRunStatusBySequenceIdAnnotationTypeNCBITaxonomyId( 
						AnnotationDataRunStatusConstants.STATUS_DATA_ALREADY_PROCESSED, 
						annotationProcessingTrackingDTO.getSequenceId(), annotationProcessingTrackingDTO.getAnnotationTypeId(), annotationProcessingTrackingDTO.getNcbiTaxonomyId() );

				return InternalRestWebServiceResponseConstants.SUCCESS;  //  EARLY EXIT
			};

			//  Update annotationDataDTO
			
			annotationDataDTO.setRunStatus( newRunStatus );
			annotationDataDTO.setAnnotationData( annotationData );
			AnnotationDataDAO.getInstance().updateAnnotationDataAnnDataRunStatus( annotationDataDTO );
			
			annotationProcessingTrackingDTO.setRunStatus( newRunStatus );
			
			AnnotationProcessingTrackingDAO.getInstance().updateRunStatus( annotationProcessingTrackingDTO );
			
			//  Update any other records with same sequenceId, ann type id, and tax id
			AnnotationProcessingTrackingDAO.getInstance()
			.updateRunStatusBySequenceIdAnnotationTypeNCBITaxonomyId( 
					AnnotationDataRunStatusConstants.STATUS_DATA_ALREADY_PROCESSED, 
					annotationProcessingTrackingDTO.getSequenceId(), annotationProcessingTrackingDTO.getAnnotationTypeId(), annotationProcessingTrackingDTO.getNcbiTaxonomyId() );

			return InternalRestWebServiceResponseConstants.SUCCESS;
			
		} catch ( Exception e ) {

			String msg = "Failed saving annotation data for tracking id: " + annotationProcessingTrackingDTO.getId();
			log.error( msg, e );
			
			throw e;
		}
	}
	
}
