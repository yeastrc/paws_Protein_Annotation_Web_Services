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
import org.yeastrc.paws.www.service.InternalSaveAnnotationJSONToDBService;


/**
 * Not for External Access
 *
 */
@Path( InternalRestWebServicePathsConstants.INTERNAL_SAVE_ANNOTATION_JSON_TO_DB )

public class InternalSaveAnnotationJSONToDBWebservice {

	Logger log = Logger.getLogger(InternalSaveAnnotationJSONToDBWebservice.class);



	/////////////////////////////////////////////////////////

	////////////   Handle "POST" of form 



	/**
	 * @param annotationType
	 * @param annotationData
	 * @param sequenceIdString
	 * @param ncbiTaxonomyIdString
	 * @param jobcenterRequestIdString
	 * @param request
	 * @return
	 */
	@POST
	@Consumes( MediaType.APPLICATION_FORM_URLENCODED )
	@Produces( { MediaType.TEXT_PLAIN } )
	public String processSubmitWithPOST( 
			@FormParam( RestWebServiceQueryStringAndFormFieldParamsConstants.REQUEST_PARAM_ANNOTATION_TYPE ) @DefaultValue("") String annotationType,
			@FormParam( RestWebServiceQueryStringAndFormFieldParamsConstants.REQUEST_PARAM_ANNOTATION_DATA ) @DefaultValue("") String annotationData,
			@FormParam( RestWebServiceQueryStringAndFormFieldParamsConstants.REQUEST_PARAM_SEQUENCE_ID ) @DefaultValue("") String sequenceIdString,
			@FormParam( RestWebServiceQueryStringAndFormFieldParamsConstants.REQUEST_PARAM_NCBI_TAXONOMY_ID ) @DefaultValue("") String ncbiTaxonomyIdString,
			@FormParam( RestWebServiceQueryStringAndFormFieldParamsConstants.REQUEST_PARAM_JOBCENTER_REQUEST_ID ) @DefaultValue("") String jobcenterRequestIdString,
			@FormParam( RestWebServiceQueryStringAndFormFieldParamsConstants.REQUEST_PARAM_STATUS ) @DefaultValue("") String status,
			@Context HttpServletRequest request ) {

		if ( log.isDebugEnabled() ) {

			log.debug( "processSubmitWithPOSTJSON(...) called, " + RestWebServiceQueryStringAndFormFieldParamsConstants.REQUEST_PARAM_SEQUENCE_ID + ": " + sequenceIdString );
		}

		return processSubmitInternal( annotationType, annotationData, sequenceIdString, ncbiTaxonomyIdString, jobcenterRequestIdString, status, request );
	}


	/**
	 * The internal method that handles all types of requests
	 *
	 * @param request
	 * @return
	 */
	public String processSubmitInternal(
			String annotationType,
			String annotationData,
			String sequenceIdString,
			String ncbiTaxonomyIdString,
			String jobcenterRequestIdString,
			String status,
			HttpServletRequest request ) {

		if ( log.isDebugEnabled() ) {

			log.debug( "processSubmitInternal(...) called, " 
					+ RestWebServiceQueryStringAndFormFieldParamsConstants.REQUEST_PARAM_NCBI_TAXONOMY_ID + ": " + ncbiTaxonomyIdString 
					+ RestWebServiceQueryStringAndFormFieldParamsConstants.REQUEST_PARAM_SEQUENCE_ID + ": " + sequenceIdString );
		}

		String accept = request.getHeader("accept");

		if ( log.isDebugEnabled() ) {

			log.debug( "processSubmitInternal(...) called, accept: " + accept );
		}

		

		if ( StringUtils.isEmpty( annotationData ) ) {
			
			annotationData = null; //  If is empty string, set to null to keep database field NULL
			
			if ( ModuleRunProgramStatusConstants.STATUS_SUCCESS.equals( status ) ) {
				
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
		
		

		if ( StringUtils.isEmpty( annotationType ) ) {

			String msg = "'" + InternalRestWebServicePathsConstants.INTERNAL_SAVE_ANNOTATION_JSON_TO_DB
					+ "' form param '" 
					+ RestWebServiceQueryStringAndFormFieldParamsConstants.REQUEST_PARAM_ANNOTATION_TYPE
					+ "' is not provided or is empty.";

			log.error( msg );


		    throw new WebApplicationException(
		    	      Response.status(javax.ws.rs.core.Response.Status.BAD_REQUEST)  //  return 400 error
		    	        .entity( msg )
		    	        .build()
		    	        );
		}
		
		if ( StringUtils.isEmpty( sequenceIdString ) ) {

			String msg = "'" + InternalRestWebServicePathsConstants.INTERNAL_SAVE_ANNOTATION_JSON_TO_DB
					+ "' form param '" 
					+ RestWebServiceQueryStringAndFormFieldParamsConstants.REQUEST_PARAM_SEQUENCE_ID
					+ "' is not provided or is empty.";

			log.error( msg );


		    throw new WebApplicationException(
		    	      Response.status(javax.ws.rs.core.Response.Status.BAD_REQUEST)  //  return 400 error
		    	        .entity( msg )
		    	        .build()
		    	        );
		}


		if ( StringUtils.isEmpty( ncbiTaxonomyIdString ) ) {

			String msg = "'" + InternalRestWebServicePathsConstants.INTERNAL_SAVE_ANNOTATION_JSON_TO_DB
					+ "' form param '" 
					+ RestWebServiceQueryStringAndFormFieldParamsConstants.REQUEST_PARAM_NCBI_TAXONOMY_ID
					+ "' is not provided or is empty.";

			log.error( msg );


		    throw new WebApplicationException(
		    	      Response.status(javax.ws.rs.core.Response.Status.BAD_REQUEST)  //  return 400 error
		    	        .entity( msg )
		    	        .build()
		    	        );
		}
		
		
		if ( StringUtils.isEmpty( jobcenterRequestIdString ) ) {

			String msg = "'" + InternalRestWebServicePathsConstants.INTERNAL_SAVE_ANNOTATION_JSON_TO_DB
					+ "' form param '" 
					+ RestWebServiceQueryStringAndFormFieldParamsConstants.REQUEST_PARAM_JOBCENTER_REQUEST_ID
					+ "' is not provided or is empty.";

			log.error( msg );


		    throw new WebApplicationException(
		    	      Response.status(javax.ws.rs.core.Response.Status.BAD_REQUEST)  //  return 400 error
		    	        .entity( msg )
		    	        .build()
		    	        );
		}
		
		int ncbiTaxonomyId = 0;
		int sequenceId = 0;
		int jobcenterRequestId = 0;
		
		try {
			
			ncbiTaxonomyId = Integer.parseInt( ncbiTaxonomyIdString );
			
		} catch ( Exception e ) {
			
			String msg = "'" + InternalRestWebServicePathsConstants.INTERNAL_SAVE_ANNOTATION_JSON_TO_DB
					+ "' form param '" 
					+ RestWebServiceQueryStringAndFormFieldParamsConstants.REQUEST_PARAM_NCBI_TAXONOMY_ID 
					+ "' is not an integer. ";
			log.error( msg );


			throw new WebApplicationException(
					Response.status(javax.ws.rs.core.Response.Status.BAD_REQUEST)  //  return 400 error
					.entity( msg )
					.build()
					);
			
		}
		
		
		
		try {
			
			sequenceId = Integer.parseInt( sequenceIdString );
			
		} catch ( Exception e ) {
			
			String msg = "'" + InternalRestWebServicePathsConstants.INTERNAL_SAVE_ANNOTATION_JSON_TO_DB
					+ "' form param '" 
					+ RestWebServiceQueryStringAndFormFieldParamsConstants.REQUEST_PARAM_SEQUENCE_ID 
					+ "' is not an integer. ";
			log.error( msg );


			throw new WebApplicationException(
					Response.status(javax.ws.rs.core.Response.Status.BAD_REQUEST)  //  return 400 error
					.entity( msg )
					.build()
					);
			
		}
		
		
		
		
		
		try {
			
			jobcenterRequestId = Integer.parseInt( jobcenterRequestIdString );
			
		} catch ( Exception e ) {
			
			String msg = "'" + InternalRestWebServicePathsConstants.INTERNAL_SAVE_ANNOTATION_JSON_TO_DB
					+ "' form param '" 
					+ RestWebServiceQueryStringAndFormFieldParamsConstants.REQUEST_PARAM_JOBCENTER_REQUEST_ID 
					+ "' is not an integer. ";
			log.error( msg );


			throw new WebApplicationException(
					Response.status(javax.ws.rs.core.Response.Status.BAD_REQUEST)  //  return 400 error
					.entity( msg )
					.build()
					);
			
		}

		try {
			
			String response = 
					InternalSaveAnnotationJSONToDBService.getInstance().saveAnnotationJSONToDB( sequenceId, ncbiTaxonomyId, jobcenterRequestId, annotationType, annotationData, status );

			return response;

		} catch (Exception e) {

			String msg = "Server Error: Fail to Save annotation data for " + RestWebServiceQueryStringAndFormFieldParamsConstants.REQUEST_PARAM_SEQUENCE_ID + " = " + sequenceId;

			log.error( msg, e );

		    throw new WebApplicationException(
		    	      Response.status(javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR)
		    	        .entity( msg )
		    	        .build()
		    	        );
		}

	}
}
