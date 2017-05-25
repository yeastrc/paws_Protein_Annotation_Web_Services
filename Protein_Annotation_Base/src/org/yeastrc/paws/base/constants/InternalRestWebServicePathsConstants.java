package org.yeastrc.paws.base.constants;

public class InternalRestWebServicePathsConstants {
	

//	private static final String PATH_PARAM_BEFORE_NAME = "/{";
//	private static final String PATH_PARAM_AFTER_NAME = "}";

	//  Internal Web services are only for use by the Jobcenter modules to get or send data to the server
	
	//  Internal Rest Service Path Prefix
	
	public static final String INTERNAL_REST_EXTENSION_BASE_SERVICES = "/services/";
	
	public static final String INTERNAL_REST_EXTENSION_BASE_INTERNAL_ONLY = "internal_only";

	public static final String INTERNAL_GET_DATA_TO_PROCESS = "/getDataToProcess";
	public static final String INTERNAL_SAVE_ANNOTATION_JSON_TO_DB = "/saveAnnotationJSONToDB";


	
	//  Combined Strings for the client modules to use
	
	public static final String SERVER_URL_EXTENSION__GET_DATA_TO_PROCESS = 
			INTERNAL_REST_EXTENSION_BASE_SERVICES
			+ INTERNAL_REST_EXTENSION_BASE_INTERNAL_ONLY 
			+ INTERNAL_GET_DATA_TO_PROCESS;
	
	public static final String SERVER_URL_EXTENSION__SAVE_RESULTS = 
			INTERNAL_REST_EXTENSION_BASE_SERVICES
			+ INTERNAL_REST_EXTENSION_BASE_INTERNAL_ONLY 
			+ INTERNAL_SAVE_ANNOTATION_JSON_TO_DB;
	
	
}
