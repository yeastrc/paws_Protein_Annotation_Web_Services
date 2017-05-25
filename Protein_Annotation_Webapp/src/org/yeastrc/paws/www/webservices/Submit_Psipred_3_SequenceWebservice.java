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
import org.yeastrc.paws.www.service.Process_Psipred_3_SequenceSubmit;


@Path( RestWebServicePathsConstants.SUBMIT_PSIPRED_3 )

public class Submit_Psipred_3_SequenceWebservice {

	Logger log = Logger.getLogger(Submit_Psipred_3_SequenceWebservice.class);


	/////////////////////////////////////////////////////////

	////////////   Handle "GET"

	/**
	 * This method handles the request for JSON
	 * @param sequenceToProcess
	 * @param ncbiTaxonomyIdString
	 * @param request
	 * @return
	 */
	@Path( RestWebServicePathsConstants.JSON_PATH_EXTENSION )
	@GET
	@Produces( { MediaType.APPLICATION_JSON } )
	public String processSubmitWithGETJSON( 
			@QueryParam( RestWebServiceQueryStringAndFormFieldParamsConstants.REQUEST_PARAM_SEQUENCE ) @DefaultValue("") String sequenceToProcess,
			@QueryParam( RestWebServiceQueryStringAndFormFieldParamsConstants.REQUEST_PARAM_NCBI_TAXONOMY_ID ) @DefaultValue("") String ncbiTaxonomyIdString,
			@QueryParam( RestWebServiceQueryStringAndFormFieldParamsConstants.REQUEST_PARAM_BATCH_REQUEST) String batchRequestString,
			@QueryParam( RestWebServiceQueryStringAndFormFieldParamsConstants.REQUEST_PARAM_BATCH_REQUEST_ID ) String batchRequestId,
			@Context HttpServletRequest request ) {

		if ( log.isDebugEnabled() ) {

			log.debug( "processSubmitWithGETJSON(...) called, " + RestWebServiceQueryStringAndFormFieldParamsConstants.REQUEST_PARAM_SEQUENCE + ": " + sequenceToProcess );
		}

		return processSubmitInternal( sequenceToProcess, ncbiTaxonomyIdString, batchRequestString, batchRequestId, request );
	}

	/**
	 * This method handles the request for JSONP
	 *
	 * @param sequenceToProcess
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
	public String processSubmitWithGETJSONP( 
			@QueryParam( RestWebServiceQueryStringAndFormFieldParamsConstants.REQUEST_PARAM_SEQUENCE ) @DefaultValue("") String sequenceToProcess,
			@QueryParam( RestWebServiceQueryStringAndFormFieldParamsConstants.REQUEST_PARAM_NCBI_TAXONOMY_ID ) @DefaultValue("") String ncbiTaxonomyIdString,
			@QueryParam( RestWebServiceQueryStringAndFormFieldParamsConstants.REQUEST_PARAM_BATCH_REQUEST) String batchRequestString,
			@QueryParam( RestWebServiceQueryStringAndFormFieldParamsConstants.REQUEST_PARAM_BATCH_REQUEST_ID ) String batchRequestId,
			@Context HttpServletRequest request ) {

		if ( log.isDebugEnabled() ) {

			log.debug( "processSubmitWithGETJSONP(...) called, " + RestWebServiceQueryStringAndFormFieldParamsConstants.REQUEST_PARAM_SEQUENCE + ": " + sequenceToProcess );
		}
		
//		String callbackQueryParamValue = request.getParameter( RestWebServiceJSONP_Constants.CALLBACK_QUERY_PARAMETER );

		return processSubmitInternal( sequenceToProcess, ncbiTaxonomyIdString, batchRequestString, batchRequestId, request );
	}



	/////////////////////////////////////////////////////////

	////////////   Handle "POST" of form with form param "sequence"




	/**
	 * This method handles the request for JSON
	 * @param sequenceToProcess
	 * @param ncbiTaxonomyIdString
	 * @param request
	 * @return
	 */
	@Path( RestWebServicePathsConstants.JSON_PATH_EXTENSION )
	@POST
	@Consumes( MediaType.APPLICATION_FORM_URLENCODED )
	@Produces( { MediaType.APPLICATION_JSON } )
	public String processSubmitWithPOSTJSON( 
			@FormParam( RestWebServiceQueryStringAndFormFieldParamsConstants.REQUEST_PARAM_SEQUENCE ) @DefaultValue("") String sequenceToProcess,
			@FormParam( RestWebServiceQueryStringAndFormFieldParamsConstants.REQUEST_PARAM_NCBI_TAXONOMY_ID ) @DefaultValue("") String ncbiTaxonomyIdString,
			@FormParam( RestWebServiceQueryStringAndFormFieldParamsConstants.REQUEST_PARAM_BATCH_REQUEST) String batchRequestString,
			@FormParam( RestWebServiceQueryStringAndFormFieldParamsConstants.REQUEST_PARAM_BATCH_REQUEST_ID ) String batchRequestId,
			@Context HttpServletRequest request ) {

		if ( log.isDebugEnabled() ) {

			log.debug( "processSubmitWithPOSTJSON(...) called, " + RestWebServiceQueryStringAndFormFieldParamsConstants.REQUEST_PARAM_SEQUENCE + ": " + sequenceToProcess );
		}

		return processSubmitInternal( sequenceToProcess, ncbiTaxonomyIdString, batchRequestString, batchRequestId, request );
	}


	/**
	 * The internal method that handles all types of requests
	 *
	 * @param sequenceToProcess
	 * @param ncbiTaxonomyIdString
	 * @param request
	 * @return
	 */
	public String processSubmitInternal( 
			String sequenceToProcess,
			String ncbiTaxonomyIdString,
			String batchRequestString, 
			String batchRequestId,
			HttpServletRequest request ) {

		String requestingIP = request.getRemoteAddr();
				
		if ( log.isDebugEnabled() ) {

			log.debug( "processSubmitInternal(...) called, " 
					+ RestWebServiceQueryStringAndFormFieldParamsConstants.REQUEST_PARAM_NCBI_TAXONOMY_ID + ": " + ncbiTaxonomyIdString 
					+ RestWebServiceQueryStringAndFormFieldParamsConstants.REQUEST_PARAM_SEQUENCE + ": " + sequenceToProcess );
		}

		String accept = request.getHeader("accept");

		if ( log.isDebugEnabled() ) {

			log.debug( "processSubmitInternal(...) called, accept: " + accept );
		}

		if ( StringUtils.isEmpty( sequenceToProcess ) ) {

			String msg = "'" + RestWebServicePathsConstants.SUBMIT_PSIPRED_3
							+ "' query param is not provided or is empty.  URL must be '"
							+ RestWebServicePathsConstants.SUBMIT_PSIPRED_3
							+ "?" + RestWebServiceQueryStringAndFormFieldParamsConstants.REQUEST_PARAM_SEQUENCE + "=xxxx'";

			log.error( msg );


		    throw new WebApplicationException(
		    	      Response.status(javax.ws.rs.core.Response.Status.BAD_REQUEST)  //  return 400 error
		    	        .entity( msg )
		    	        .build()
		    	        );
		}


		if ( StringUtils.isEmpty( ncbiTaxonomyIdString ) ) {

			String msg = "'" + RestWebServicePathsConstants.SUBMIT_PSIPRED_3
							+ "' query param is not provided or is empty.  URL must be '"
							+ RestWebServicePathsConstants.SUBMIT_PSIPRED_3
							+ "?" + RestWebServiceQueryStringAndFormFieldParamsConstants.REQUEST_PARAM_NCBI_TAXONOMY_ID + "=###'";

			log.error( msg );


		    throw new WebApplicationException(
		    	      Response.status(javax.ws.rs.core.Response.Status.BAD_REQUEST)  //  return 400 error
		    	        .entity( msg )
		    	        .build()
		    	        );
		}

		boolean batchRequest = false;
		
		int ncbiTaxonomyId = 0;

		if ( batchRequestString != null && batchRequestString.length() > 0 ) {
			String batchRequestFirstChar = batchRequestString.substring(0, 1);
			
			if ( RestWebServiceQueryStringAndFormFieldParamsConstants.REQUEST_PARAM_BATCH_REQUEST_TRUE_Y.equalsIgnoreCase( batchRequestFirstChar )
					||  RestWebServiceQueryStringAndFormFieldParamsConstants.REQUEST_PARAM_BATCH_REQUEST_TRUE_T.equalsIgnoreCase( batchRequestFirstChar ) ) {
				
				batchRequest = true;
			}
		}
		
		try {
			ncbiTaxonomyId = Integer.parseInt( ncbiTaxonomyIdString );
			
		} catch ( Exception e ) {
			String msg = "'" + RestWebServicePathsConstants.SUBMIT_PSIPRED_3
					+ "' query param '" 
					+ RestWebServiceQueryStringAndFormFieldParamsConstants.REQUEST_PARAM_NCBI_TAXONOMY_ID 
					+ "' is not an integer.  URL must be '"
					+ RestWebServicePathsConstants.SUBMIT_PSIPRED_3
					+ "?" + RestWebServiceQueryStringAndFormFieldParamsConstants.REQUEST_PARAM_NCBI_TAXONOMY_ID + "=###'";
			log.error( msg );
			throw new WebApplicationException(
					Response.status(javax.ws.rs.core.Response.Status.BAD_REQUEST)  //  return 400 error
					.entity( msg )
					.build()
					);
			
		}


		try {
			
			String response = 
					Process_Psipred_3_SequenceSubmit.getInstance()
					.process_Psipred_3_SequenceSubmit( sequenceToProcess, ncbiTaxonomyId, requestingIP, batchRequest, batchRequestId );

			return response;

		} catch (Exception e) {

			String msg = "Server Error: Fail to retrieve results for " + RestWebServiceQueryStringAndFormFieldParamsConstants.REQUEST_PARAM_SEQUENCE + " = " + sequenceToProcess;

			log.error( msg, e );

		    throw new WebApplicationException(
		    	      Response.status(javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR)
		    	        .entity( msg )
		    	        .build()
		    	        );
		}

	}
}
