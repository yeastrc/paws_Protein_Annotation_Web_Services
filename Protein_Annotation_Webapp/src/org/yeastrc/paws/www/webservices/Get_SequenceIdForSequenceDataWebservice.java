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
import org.yeastrc.paws.www.service.Get_SequenceIdForSequence;


@Path( RestWebServicePathsConstants.GET_SEQUENCE_ID )

public class Get_SequenceIdForSequenceDataWebservice {

	Logger log = Logger.getLogger(Get_SequenceIdForSequenceDataWebservice.class);


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
			@QueryParam( RestWebServiceQueryStringAndFormFieldParamsConstants.REQUEST_PARAM_SEQUENCE ) @DefaultValue("") String sequence,
			@Context HttpServletRequest request ) {

		if ( log.isDebugEnabled() ) {

			log.debug( "processGetWithGETJSON(...) called, " + RestWebServiceQueryStringAndFormFieldParamsConstants.REQUEST_PARAM_SEQUENCE + ": " + sequence );
		}

		return processGetInternal( sequence, request );
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
			@QueryParam( RestWebServiceQueryStringAndFormFieldParamsConstants.REQUEST_PARAM_SEQUENCE ) @DefaultValue("") String sequence,
			@Context HttpServletRequest request ) {

		if ( log.isDebugEnabled() ) {

			log.debug( "processGetWithGETJSONP(...) called, " + RestWebServiceQueryStringAndFormFieldParamsConstants.REQUEST_PARAM_SEQUENCE + ": " + sequence );
		}
		
//		String callbackQueryParamValue = request.getParameter( RestWebServiceJSONP_Constants.CALLBACK_QUERY_PARAMETER );

		return processGetInternal( sequence, request );
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
			@FormParam( RestWebServiceQueryStringAndFormFieldParamsConstants.REQUEST_PARAM_SEQUENCE ) @DefaultValue("") String sequence,
			@Context HttpServletRequest request ) {

		if ( log.isDebugEnabled() ) {

			log.debug( "processGetWithPOSTJSON(...) called, " + RestWebServiceQueryStringAndFormFieldParamsConstants.REQUEST_PARAM_SEQUENCE + ": " + sequence );
		}

		return processGetInternal( sequence, request );
	}


	/**
	 * The internal method that handles all types of requests
	 *
	 * @param sequence
	 * @param request
	 * @return
	 */
	public String processGetInternal( String sequence,
			HttpServletRequest request ) {

		if ( log.isDebugEnabled() ) {

			log.debug( "processGetInternal(...) called, " 
					+ RestWebServiceQueryStringAndFormFieldParamsConstants.REQUEST_PARAM_SEQUENCE + ": " + sequence );
		}

		String accept = request.getHeader("accept");

		if ( log.isDebugEnabled() ) {

			log.debug( "processGetInternal(...) called, accept: " + accept );
		}

		if ( StringUtils.isEmpty( sequence ) ) {

			String msg = "query or form param "
							+ RestWebServiceQueryStringAndFormFieldParamsConstants.REQUEST_PARAM_SEQUENCE
							+ "' is not provided or is empty.";

			log.error( msg );


		    throw new WebApplicationException(
		    	      Response.status(javax.ws.rs.core.Response.Status.BAD_REQUEST)  //  return 400 error
		    	        .entity( msg )
		    	        .build()
		    	        );
		}

		try {
			
			String response = Get_SequenceIdForSequence.getInstance().get_SequenceIdForSequence( sequence );

			return response;

		} catch (Exception e) {

			String msg = "Server Error: Fail to retrieve results for " + RestWebServiceQueryStringAndFormFieldParamsConstants.REQUEST_PARAM_SEQUENCE + " = " + sequence;

			log.error( msg, e );

		    throw new WebApplicationException(
		    	      Response.status(javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR)
		    	        .entity( msg )
		    	        .build()
		    	        );
		}

	}
}
