package org.yeastrc.paws.base.constants;

public class InternalRestWebServicePathsConstants {
	

//	private static final String PATH_PARAM_BEFORE_NAME = "/{";
//	private static final String PATH_PARAM_AFTER_NAME = "}";

	//  Internal Web services are only for use by the Jobcenter modules to get or send data to the server
	
	//  Internal Rest Service Path Prefix
	
	public static final String INTERNAL_REST_SERVICE_PREFIX = "internal_only/";

	public static final String INTERNAL_SAVE_ANNOTATION_JSON_TO_DB = INTERNAL_REST_SERVICE_PREFIX + "saveAnnotationJSONToDB";
	public static final String INTERNAL_IS_PROCESSED = INTERNAL_REST_SERVICE_PREFIX + "isProcessed";

}
