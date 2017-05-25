package org.yeastrc.paws.www.internal_webservices;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.yeastrc.paws.www.constants.AnnotationDataRunStatusConstants;
import org.yeastrc.paws.www.dao.AnnotationDataDAO;
import org.yeastrc.paws.www.dao.AnnotationProcessingTrackingDAO;
import org.yeastrc.paws.www.dao.SequenceDAO;
import org.yeastrc.paws.www.dto.AnnotationDataDTO;
import org.yeastrc.paws.www.dto.AnnotationProcessingTrackingDTO;
import org.yeastrc.paws.base.client_server_shared_objects.GetDataForTrackingIdServerResponse;
import org.yeastrc.paws.base.constants.InternalRestWebServicePathsConstants;
import org.yeastrc.paws.base.constants.RestWebServiceQueryStringAndFormFieldParamsConstants;


/**
 * 
 *
 */
@Path( InternalRestWebServicePathsConstants.INTERNAL_REST_EXTENSION_BASE_INTERNAL_ONLY )

public class InternalGetAnnotationDataForTrackingIdWebservice {

	Logger log = Logger.getLogger(InternalGetAnnotationDataForTrackingIdWebservice.class);


	/////////////////////////////////////////////////////////

	////////////   Handle "GET"

	/**
	 * 
	 * @param sequenceIdString
	 * @param ncbiTaxonomyIdString
	 * @param request
	 * @return
	 */
	@Path( InternalRestWebServicePathsConstants.INTERNAL_GET_DATA_TO_PROCESS )
	@GET
	@Produces( { MediaType.APPLICATION_JSON } )
	public GetDataForTrackingIdServerResponse processGet( 
			@QueryParam( RestWebServiceQueryStringAndFormFieldParamsConstants.REQUEST_PARAM_TRACKING_ID ) Integer trackingId,
			@Context HttpServletRequest request ) {

		if ( log.isDebugEnabled() ) {

			log.debug( "processGetWithGETJSON(...) called, " + RestWebServiceQueryStringAndFormFieldParamsConstants.REQUEST_PARAM_TRACKING_ID + ": " + trackingId );
		}

		return processGetInternal( trackingId, request );
	}

	/**
	 * The internal method that handles all types of requests
	 *
	 * @param trackingId
	 * @param request
	 * @return
	 */
	public GetDataForTrackingIdServerResponse processGetInternal( Integer trackingId,
			HttpServletRequest request ) {

		if ( log.isDebugEnabled() ) {

			log.debug( "processGetInternal(...) called, " 
					+ RestWebServiceQueryStringAndFormFieldParamsConstants.REQUEST_PARAM_TRACKING_ID + ": " + trackingId );
		}

		String accept = request.getHeader("accept");

		if ( log.isDebugEnabled() ) {

			log.debug( "processGetInternal(...) called, accept: " + accept );
		}
		if ( trackingId == null ) {

			String msg = "'" + InternalRestWebServicePathsConstants.INTERNAL_SAVE_ANNOTATION_JSON_TO_DB
					+ "' form param '" 
					+ RestWebServiceQueryStringAndFormFieldParamsConstants.REQUEST_PARAM_TRACKING_ID
					+ "' is not provided or is empty.";

			log.error( msg );
		    throw new WebApplicationException(
		    	      Response.status(javax.ws.rs.core.Response.Status.BAD_REQUEST)  //  return 400 error
		    	        .entity( msg )
		    	        .build()
		    	        );
		}

		try {
			GetDataForTrackingIdServerResponse getDataForTrackingIdServerResponse = new GetDataForTrackingIdServerResponse();
			
			AnnotationProcessingTrackingDTO annotationProcessingTrackingDTO = 
					AnnotationProcessingTrackingDAO.getInstance().getById( trackingId );
			
			if ( annotationProcessingTrackingDTO == null ) {
				getDataForTrackingIdServerResponse.setSuccess(false);
				getDataForTrackingIdServerResponse.setNoRecordForTrackingId(true);
				log.error( "No annotationProcessingTrackingDTO record found for tracking id: " + trackingId );
				return getDataForTrackingIdServerResponse; // EARLY EXIT
			}

			if ( AnnotationDataRunStatusConstants.STATUS_DATA_ALREADY_PROCESSED.equals( annotationProcessingTrackingDTO.getRunStatus() ) ) {
				getDataForTrackingIdServerResponse.setSuccess(true);
				getDataForTrackingIdServerResponse.setDataAlreadyProcessed(true);
				if ( log.isInfoEnabled() ) {
					log.info( "annotationProcessingTrackingDTO record already data_already_processed for tracking id: " + trackingId );
				}
				return getDataForTrackingIdServerResponse; // EARLY EXIT
			}
			
			AnnotationDataDTO annotationDataDTO = 
					AnnotationDataDAO.getInstance()
					.getAnnotationDataDTOBySequenceIdAnnotationTypeNCBITaxonomyId(
							annotationProcessingTrackingDTO.getSequenceId(),
							annotationProcessingTrackingDTO.getAnnotationTypeId(),
							annotationProcessingTrackingDTO.getNcbiTaxonomyId() );
			
			if ( annotationDataDTO == null ) {
				getDataForTrackingIdServerResponse.setSuccess(false);
				getDataForTrackingIdServerResponse.setNoRecordForTrackingId(true);
				log.error( "No annotationDataDTO record found for tracking id: " + trackingId );
				return getDataForTrackingIdServerResponse; // EARLY EXIT
			}
			
			if ( AnnotationDataRunStatusConstants.STATUS_COMPLETE.equals( annotationDataDTO.getRunStatus() )
					|| AnnotationDataRunStatusConstants.STATUS_FAIL.equals( annotationDataDTO.getRunStatus() ) ) {
				getDataForTrackingIdServerResponse.setSuccess(true);
				getDataForTrackingIdServerResponse.setDataAlreadyProcessed(true);
				if ( log.isInfoEnabled() ) {
					log.info( "annotationDataDTO record already complete or fail for tracking id: " + trackingId );
				}
				return getDataForTrackingIdServerResponse; // EARLY EXIT
			}
			
			String sequence = SequenceDAO.getInstance().getSequenceById( annotationDataDTO.getSequenceId() );
			
			if ( sequence == null ) {
				getDataForTrackingIdServerResponse.setSuccess(false);
				getDataForTrackingIdServerResponse.setNoRecordForTrackingId(true);
				log.error( "No sequence record found for sequence id: " 
						+ annotationDataDTO.getSequenceId()
						+ ", tracking id: " + trackingId );
				return getDataForTrackingIdServerResponse; // EARLY EXIT
			}

			getDataForTrackingIdServerResponse.setSuccess(true);
			getDataForTrackingIdServerResponse.setSequence( sequence );
			
			return getDataForTrackingIdServerResponse; 

		} catch (Exception e) {

			String msg = "Server Error: Fail to retrieve results for " + RestWebServiceQueryStringAndFormFieldParamsConstants.REQUEST_PARAM_TRACKING_ID + " = " + trackingId;

			log.error( msg, e );

		    throw new WebApplicationException(
		    	      Response.status(javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR)
		    	        .entity( msg )
		    	        .build()
		    	        );
		}

	}
}
