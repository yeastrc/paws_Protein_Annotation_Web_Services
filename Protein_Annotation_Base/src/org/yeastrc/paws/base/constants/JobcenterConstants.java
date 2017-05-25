package org.yeastrc.paws.base.constants;

public class JobcenterConstants {

	//  Parameters sent to the Jobcenter modules
	
	public static final String JOB_PARAM_TRACKING_ID = "trackingId"; // annotation_processing_tracking.id
	
	public static final String JOB_PARAM_SERVER_BASE_URL = "serverBaseURL";
	
	/////////////////////

	//  The files named 'jobcenter_module_config_per_module.properties' in the projects must match the type names specified here

	//  The type names are also used in the database configuration of the Jobcenter system

	
	public static final String REQUEST_TYPE_NAME_PROTEIN_ANNOTATION_DISOPRED_2 = "ProteinAnnotation_Disopred_2";
	public static final String JOB_TYPE_NAME_PROTEIN_ANNOTATION_DISOPRED_2 = "ProteinAnnotation_Disopred_2";
	public static final String JOB_TYPE_NAME_PROTEIN_ANNOTATION_DISOPRED_2_BATCH = "ProteinAnnotation_Disopred_2_Batch";
	
	public static final String REQUEST_TYPE_NAME_PROTEIN_ANNOTATION_DISOPRED_3 = "ProteinAnnotation_Disopred_3";
	public static final String JOB_TYPE_NAME_PROTEIN_ANNOTATION_DISOPRED_3 = "ProteinAnnotation_Disopred_3";
	public static final String JOB_TYPE_NAME_PROTEIN_ANNOTATION_DISOPRED_3_BATCH = "ProteinAnnotation_Disopred_3_Batch";
	
	public static final String REQUEST_TYPE_NAME_PROTEIN_ANNOTATION_PSIPRED_3 = "ProteinAnnotation_Psipred_3";
	public static final String JOB_TYPE_NAME_PROTEIN_ANNOTATION_PSIPRED_3 = "ProteinAnnotation_Psipred_3";
	public static final String JOB_TYPE_NAME_PROTEIN_ANNOTATION_PSIPRED_3_BATCH = "ProteinAnnotation_Psipred_3_Batch";
	
}
