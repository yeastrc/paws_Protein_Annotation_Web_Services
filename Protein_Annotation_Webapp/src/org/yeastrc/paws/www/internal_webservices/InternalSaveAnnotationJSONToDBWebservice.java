package org.yeastrc.paws.www.internal_webservices;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.yeastrc.paws.base.constants.InternalRestWebServicePathsConstants;
import org.yeastrc.paws.base.constants.ModuleRunProgramStatusConstants;
import org.yeastrc.paws.base.constants.RestWebServiceQueryStringAndFormFieldParamsConstants;
import org.yeastrc.paws.www.constants.AnnotationDataRunStatusConstants;
import org.yeastrc.paws.www.dao.AnnotationDataDAO;
import org.yeastrc.paws.www.dao.AnnotationProcessingTrackingDAO;
import org.yeastrc.paws.www.dto.AnnotationDataDTO;
import org.yeastrc.paws.www.dto.AnnotationProcessingTrackingDTO;
import org.yeastrc.paws.www.service.InternalSaveAnnotationJSONToDBService;


/**
 * Not for External Access
 *
 */
@Path( InternalRestWebServicePathsConstants.INTERNAL_REST_EXTENSION_BASE_INTERNAL_ONLY 
		+ InternalRestWebServicePathsConstants.INTERNAL_SAVE_ANNOTATION_JSON_TO_DB )

public class InternalSaveAnnotationJSONToDBWebservice {

	Logger log = Logger.getLogger(InternalSaveAnnotationJSONToDBWebservice.class);

	/////////////////////////////////////////////////////////

	////////////   Handle "POST" of form 

	/**
	 * @param trackingIdString
	 * @param annotationData
	 * @param status
	 * @param request
	 * @return
	 */
	@POST
	@Consumes( MediaType.APPLICATION_FORM_URLENCODED )
	@Produces( { MediaType.TEXT_PLAIN } )
	public String processSubmitWithPOST( 
			@FormParam( RestWebServiceQueryStringAndFormFieldParamsConstants.REQUEST_PARAM_TRACKING_ID ) @DefaultValue("") String trackingIdString,
			@FormParam( RestWebServiceQueryStringAndFormFieldParamsConstants.REQUEST_PARAM_ANNOTATION_DATA ) @DefaultValue("") String annotationData,
			@FormParam( RestWebServiceQueryStringAndFormFieldParamsConstants.REQUEST_PARAM_STATUS ) @DefaultValue("") String status,
			@Context HttpServletRequest request ) {

		if ( log.isDebugEnabled() ) {

			log.debug( "processSubmitWithPOSTJSON(...) called, " + RestWebServiceQueryStringAndFormFieldParamsConstants.REQUEST_PARAM_TRACKING_ID + ": " + trackingIdString );
		}

		return processSubmitInternal( trackingIdString, annotationData, status, request );
	}


	/**
	 * The internal method that handles all types of requests
	 *
	 * @param request
	 * @return
	 */
	public String processSubmitInternal(
			String trackingIdString,
			String newAnnotationData,
			String statusFromModule,
			HttpServletRequest request ) {

		try {

			if ( log.isDebugEnabled() ) {

				log.debug( "processSubmitInternal(...) called, " 
						+ RestWebServiceQueryStringAndFormFieldParamsConstants.REQUEST_PARAM_TRACKING_ID + ": " + trackingIdString );
			}

			String accept = request.getHeader("accept");

			if ( log.isDebugEnabled() ) {

				log.debug( "processSubmitInternal(...) called, accept: " + accept );
			}


			if ( StringUtils.isEmpty( newAnnotationData ) ) {

				newAnnotationData = null; //  If is empty string, set to null to keep database field NULL

				if ( ModuleRunProgramStatusConstants.STATUS_SUCCESS.equals( statusFromModule ) ) {

					//  Require annotation data if status is success

					String msg = "'" + InternalRestWebServicePathsConstants.INTERNAL_SAVE_ANNOTATION_JSON_TO_DB
							+ "' form param '" 
							+ RestWebServiceQueryStringAndFormFieldParamsConstants.REQUEST_PARAM_ANNOTATION_DATA
							+ "' is not provided or is empty.";

					log.error( msg );

					throw new WebApplicationException(
							Response.status(javax.ws.rs.core.Response.Status.BAD_REQUEST)  //  return 400 error
							.entity( msg )
							.build()
							);
				}
			}



			if ( StringUtils.isEmpty( trackingIdString ) ) {

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

			int trackingId = 0;

			try {
				trackingId = Integer.parseInt( trackingIdString );

			} catch ( Exception e ) {
				String msg = "'" + InternalRestWebServicePathsConstants.INTERNAL_SAVE_ANNOTATION_JSON_TO_DB
						+ "' form param '" 
						+ RestWebServiceQueryStringAndFormFieldParamsConstants.REQUEST_PARAM_TRACKING_ID 
						+ "' is not an integer. Value passed in: " + trackingIdString;
				log.error( msg );
				throw new WebApplicationException(
						Response.status(javax.ws.rs.core.Response.Status.BAD_REQUEST)  //  return 400 error
						.entity( msg )
						.build()
						);
			}

			AnnotationProcessingTrackingDTO annotationProcessingTrackingDTO = null;

			try {
				annotationProcessingTrackingDTO = AnnotationProcessingTrackingDAO.getInstance().getById( trackingId );
			} catch (Exception e) {
				String msg = " Exception: Get AnnotationProcessingTrackingDTO from DB for: " + RestWebServiceQueryStringAndFormFieldParamsConstants.REQUEST_PARAM_TRACKING_ID + " = " + trackingId;
				log.error( msg, e );
				throw new WebApplicationException(
						Response.status(javax.ws.rs.core.Response.Status.BAD_REQUEST)
						.entity( msg )
						.build()
						);
			}
			if ( annotationProcessingTrackingDTO == null ) {
				String msg = " AnnotationProcessingTrackingDTO not found in DB for: " + RestWebServiceQueryStringAndFormFieldParamsConstants.REQUEST_PARAM_TRACKING_ID + " = " + trackingId;
				log.error( msg );
				throw new WebApplicationException(
						Response.status(javax.ws.rs.core.Response.Status.BAD_REQUEST)
						.entity( msg )
						.build()
						);
			}
			
			String newRunStatus = AnnotationDataRunStatusConstants.STATUS_COMPLETE;

			if ( ModuleRunProgramStatusConstants.STATUS_FAIL.equals( statusFromModule ) ) {

				newRunStatus = AnnotationDataRunStatusConstants.STATUS_FAIL;

			} else if ( ! ModuleRunProgramStatusConstants.STATUS_SUCCESS.equals( statusFromModule ) ) {
				String msg = "Failed:  Module Run status not recognized, status: " + statusFromModule;
				log.error( msg );
				throw new WebApplicationException(
						Response.status(javax.ws.rs.core.Response.Status.BAD_REQUEST)
						.entity( msg )
						.build()
						);
			}

			AnnotationDataDTO annotationDataDTO = null;
			try {
				annotationDataDTO = 
						AnnotationDataDAO.getInstance()
						.getAnnotationDataDTOBySequenceIdAnnotationTypeNCBITaxonomyId(
								annotationProcessingTrackingDTO.getSequenceId(), annotationProcessingTrackingDTO.getAnnotationTypeId(), annotationProcessingTrackingDTO.getNcbiTaxonomyId() );
			} catch (Exception e) {
				String msg = " Fail to Get AnnotationDataDTO from DB for: " + RestWebServiceQueryStringAndFormFieldParamsConstants.REQUEST_PARAM_TRACKING_ID + " = " + trackingId;
				log.error( msg, e );
				throw new WebApplicationException(
						Response.status(javax.ws.rs.core.Response.Status.BAD_REQUEST)
						.entity( msg )
						.build()
						);
			}
			if ( annotationDataDTO == null ) {
				String msg = " AnnotationDataDTO not found in DB for: " + RestWebServiceQueryStringAndFormFieldParamsConstants.REQUEST_PARAM_TRACKING_ID + " = " + trackingId;
				log.error( msg );
				throw new WebApplicationException(
						Response.status(javax.ws.rs.core.Response.Status.BAD_REQUEST)
						.entity( msg )
						.build()
						);
			}
	

			try {

				String response = 
						InternalSaveAnnotationJSONToDBService.getInstance().saveAnnotationJSONToDB( newAnnotationData, newRunStatus, annotationProcessingTrackingDTO, annotationDataDTO );

				return response;

			} catch (Exception e) {
				String msg = "Server Error: Fail to Save annotation data for " + RestWebServiceQueryStringAndFormFieldParamsConstants.REQUEST_PARAM_TRACKING_ID + " = " + trackingId;
				log.error( msg, e );
				throw new WebApplicationException(
						Response.status(javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR)
						.entity( msg )
						.build()
						);
			}
			
		} catch ( WebApplicationException e ) {
			throw e;
			
		} catch (Exception e) {
			String msg = "Server Error: " + RestWebServiceQueryStringAndFormFieldParamsConstants.REQUEST_PARAM_TRACKING_ID + " = " + trackingIdString;
			log.error( msg, e );
			throw new WebApplicationException(
					Response.status(javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR)
					.entity( msg )
					.build()
					);
		}
	}
}
