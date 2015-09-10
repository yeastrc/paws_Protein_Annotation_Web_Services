package org.yeastrc.paws.www.webservices;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.glassfish.jersey.server.JSONP;
import org.yeastrc.paws.www.constants.RestWebServiceJSONP_Constants;
import org.yeastrc.paws.www.constants.RestWebServicePathsConstants;
import org.yeastrc.paws.www.constants.RestWebServiceProducesConstants;
import org.yeastrc.paws.base.constants.RestWebServiceQueryStringAndFormFieldParamsConstants;
import org.yeastrc.paws.www.service.Get_Disopred_2_DataService;


@Path( RestWebServicePathsConstants.GET_DISOPRED_2 )

public class Get_Disopred_2_AnnotationDataWebservice {

	Logger log = Logger.getLogger(Get_Disopred_2_AnnotationDataWebservice.class);


	/////////////////////////////////////////////////////////

	////////////   Handle "GET"

	/**
	 * This method handles the request for JSON
	 * @param sequenceIdString
	 * @param ncbiTaxonomyIdString
	 * @param request
	 * @return
	 */
	@Path( RestWebServicePathsConstants.JSON_PATH_EXTENSION )
	@GET
	@Produces( { MediaType.APPLICATION_JSON } )
	public String processGetWithGETJSON( 
			@QueryParam( RestWebServiceQueryStringAndFormFieldParamsConstants.REQUEST_PARAM_SEQUENCE_ID ) @DefaultValue("") String sequenceIdString,
			@QueryParam( RestWebServiceQueryStringAndFormFieldParamsConstants.REQUEST_PARAM_NCBI_TAXONOMY_ID ) @DefaultValue("") String ncbiTaxonomyIdString,
			@Context HttpServletRequest request ) {

		if ( log.isDebugEnabled() ) {

			log.debug( "processGetWithGETJSON(...) called, " + RestWebServiceQueryStringAndFormFieldParamsConstants.REQUEST_PARAM_SEQUENCE + ": " + sequenceIdString );
		}

		return processGetInternal( sequenceIdString, ncbiTaxonomyIdString, request );
	}

	/**
	 * This method handles the request for JSONP
	 *
	 * @param sequenceIdString
	 * @param ncbiTaxonomyIdString
	 * @param request
	 * @return
	 */
	@Path( RestWebServicePathsConstants.JSONP_PATH_EXTENSION )
	@GET
	@JSONP(callback = RestWebServiceJSONP_Constants.DEFAULT_CALLBACK_FUNCTION_NAME, queryParam = RestWebServiceJSONP_Constants.CALLBACK_QUERY_PARAMETER )
	@Produces(
			{ 	RestWebServiceProducesConstants.PRODUCES_APPLICATION__X_JAVASCRIPT,
				RestWebServiceProducesConstants.PRODUCES_APPLICATION__JAVASCRIPT,
				RestWebServiceProducesConstants.PRODUCES_APPLICATION__ECMASCRIPT,
				RestWebServiceProducesConstants.PRODUCES_TEXT__JAVASCRIPT,
				RestWebServiceProducesConstants.PRODUCES_TEXT__X_JAVASCRIPT,
				RestWebServiceProducesConstants.PRODUCES_TEXT__ECMASCRIPT,
				RestWebServiceProducesConstants.PRODUCES_TEXT__JSCRIPT
			}
			)
	public String processGetWithGETJSONP( 
			@QueryParam( RestWebServiceQueryStringAndFormFieldParamsConstants.REQUEST_PARAM_SEQUENCE_ID ) @DefaultValue("") String sequenceIdString,
			@QueryParam( RestWebServiceQueryStringAndFormFieldParamsConstants.REQUEST_PARAM_NCBI_TAXONOMY_ID ) @DefaultValue("") String ncbiTaxonomyIdString,
			@Context HttpServletRequest request ) {

		if ( log.isDebugEnabled() ) {

			log.debug( "processGetWithGETJSONP(...) called, " + RestWebServiceQueryStringAndFormFieldParamsConstants.REQUEST_PARAM_SEQUENCE_ID + ": " + sequenceIdString );
		}
		
//		String callbackQueryParamValue = request.getParameter( RestWebServiceJSONP_Constants.CALLBACK_QUERY_PARAMETER );

		return processGetInternal( sequenceIdString, ncbiTaxonomyIdString, request );
	}



	/////////////////////////////////////////////////////////

	////////////   Handle "POST" of form with form param "sequence"




	/**
	 * This method handles the request for JSON
	 * @param sequenceIdString
	 * @param ncbiTaxonomyIdString
	 * @param request
	 * @return
	 */
	@Path( RestWebServicePathsConstants.JSON_PATH_EXTENSION )
	@POST
	@Consumes( MediaType.APPLICATION_FORM_URLENCODED )
	@Produces( { MediaType.APPLICATION_JSON } )
	public String processGetWithPOSTJSON( 
			@FormParam( RestWebServiceQueryStringAndFormFieldParamsConstants.REQUEST_PARAM_SEQUENCE_ID ) @DefaultValue("") String sequenceIdString,
			@FormParam( RestWebServiceQueryStringAndFormFieldParamsConstants.REQUEST_PARAM_NCBI_TAXONOMY_ID ) @DefaultValue("") String ncbiTaxonomyIdString,
			@Context HttpServletRequest request ) {

		if ( log.isDebugEnabled() ) {

			log.debug( "processGetWithPOSTJSON(...) called, " + RestWebServiceQueryStringAndFormFieldParamsConstants.REQUEST_PARAM_SEQUENCE_ID + ": " + sequenceIdString );
		}

		return processGetInternal( sequenceIdString, ncbiTaxonomyIdString, request );
	}


	/**
	 * The internal method that handles all types of requests
	 *
	 * @param sequenceIdString
	 * @param ncbiTaxonomyIdString
	 * @param request
	 * @return
	 */
	public String processGetInternal( String sequenceIdString,
			String ncbiTaxonomyIdString,
			HttpServletRequest request ) {

		if ( log.isDebugEnabled() ) {

			log.debug( "processGetInternal(...) called, " 
					+ RestWebServiceQueryStringAndFormFieldParamsConstants.REQUEST_PARAM_NCBI_TAXONOMY_ID + ": " + ncbiTaxonomyIdString 
					+ RestWebServiceQueryStringAndFormFieldParamsConstants.REQUEST_PARAM_SEQUENCE_ID + ": " + sequenceIdString );
		}

		String accept = request.getHeader("accept");

		if ( log.isDebugEnabled() ) {

			log.debug( "processGetInternal(...) called, accept: " + accept );
		}

		if ( StringUtils.isEmpty( sequenceIdString ) ) {

			String msg = "'" + RestWebServicePathsConstants.GET_DISOPRED_2
							+ "' query param is not provided or is empty.  URL must be '"
							+ RestWebServicePathsConstants.GET_DISOPRED_2
							+ "?" + RestWebServiceQueryStringAndFormFieldParamsConstants.REQUEST_PARAM_SEQUENCE + "=xxxx'";

			log.error( msg );


		    throw new WebApplicationException(
		    	      Response.status(javax.ws.rs.core.Response.Status.BAD_REQUEST)  //  return 400 error
		    	        .entity( msg )
		    	        .build()
		    	        );
		}


		if ( StringUtils.isEmpty( ncbiTaxonomyIdString ) ) {

			String msg = "'" + RestWebServicePathsConstants.GET_DISOPRED_2
							+ "' query param is not provided or is empty.  URL must be '"
							+ RestWebServicePathsConstants.GET_DISOPRED_2
							+ "?" + RestWebServiceQueryStringAndFormFieldParamsConstants.REQUEST_PARAM_NCBI_TAXONOMY_ID + "=###'";

			log.error( msg );


		    throw new WebApplicationException(
		    	      Response.status(javax.ws.rs.core.Response.Status.BAD_REQUEST)  //  return 400 error
		    	        .entity( msg )
		    	        .build()
		    	        );
		}
		
		int sequenceId = 0;
		int ncbiTaxonomyId = 0;
		
		try {
			
			sequenceId = Integer.parseInt( sequenceIdString );
			
		} catch ( Exception e ) {
			
			String msg = "'" + RestWebServicePathsConstants.GET_DISOPRED_2
					+ "' query param '" 
					+ RestWebServiceQueryStringAndFormFieldParamsConstants.REQUEST_PARAM_SEQUENCE_ID 
					+ "' is not an integer.  URL must be '"
					+ RestWebServicePathsConstants.GET_DISOPRED_2
					+ "?" + RestWebServiceQueryStringAndFormFieldParamsConstants.REQUEST_PARAM_SEQUENCE_ID + "=###'";

			log.error( msg );


			throw new WebApplicationException(
					Response.status(javax.ws.rs.core.Response.Status.BAD_REQUEST)  //  return 400 error
					.entity( msg )
					.build()
					);
			
		}


		try {
			
			ncbiTaxonomyId = Integer.parseInt( ncbiTaxonomyIdString );
			
		} catch ( Exception e ) {
			
			String msg = "'" + RestWebServicePathsConstants.GET_DISOPRED_2
					+ "' query param '" 
					+ RestWebServiceQueryStringAndFormFieldParamsConstants.REQUEST_PARAM_NCBI_TAXONOMY_ID 
					+ "' is not an integer.  URL must be '"
					+ RestWebServicePathsConstants.GET_DISOPRED_2
					+ "?" + RestWebServiceQueryStringAndFormFieldParamsConstants.REQUEST_PARAM_NCBI_TAXONOMY_ID + "=###'";

			log.error( msg );


			throw new WebApplicationException(
					Response.status(javax.ws.rs.core.Response.Status.BAD_REQUEST)  //  return 400 error
					.entity( msg )
					.build()
					);
			
		}
		
		

		try {
			
			String response = Get_Disopred_2_DataService.getInstance().get_Disopred_2_DataForSequenceId( sequenceId, ncbiTaxonomyId );

			return response;

		} catch (Exception e) {

			String msg = "Server Error: Fail to retrieve results for " + RestWebServiceQueryStringAndFormFieldParamsConstants.REQUEST_PARAM_SEQUENCE_ID + " = " + sequenceIdString;

			log.error( msg, e );

		    throw new WebApplicationException(
		    	      Response.status(javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR)
		    	        .entity( msg )
		    	        .build()
		    	        );
		}

	}
}
