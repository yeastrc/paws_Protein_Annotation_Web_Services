package org.yeastrc.paws.base.constants;

public class JobcenterConstants {

	//  Parameters sent to the Jobcenter modules
	
	public static final String JOB_PARAM_SEQUENCE = "sequence";
	
	public static final String JOB_PARAM_SEQUENCE_ID = "sequenceId";
	
	public static final String JOB_PARAM_NCBI_TAXONOMY_ID = "ncbiTaxonomyId";
	
	public static final String JOB_PARAM_ANNOTATION_TYPE = "annotationType";
	public static final String JOB_PARAM_ANNOTATION_TYPE_ID = "annotationTypeId";
	
	public static final String JOB_PARAM_SEND_RESULTS_URL = "sendResultsURL";
	
	/////////////////////

	//  The files named 'jobcenter_module_config_per_module.properties' in the projects must match the type names specified here

	//  The type names are also used in the database configuration of the Jobcenter system

	
	public static final String REQUEST_TYPE_NAME_PROTEIN_ANNOTATION_DISOPRED_2 = "ProteinAnnotation_Disopred_2";
	public static final String JOB_TYPE_NAME_PROTEIN_ANNOTATION_DISOPRED_2 = "ProteinAnnotation_Disopred_2";
	
	public static final String REQUEST_TYPE_NAME_PROTEIN_ANNOTATION_DISOPRED_3 = "ProteinAnnotation_Disopred_3";
	public static final String JOB_TYPE_NAME_PROTEIN_ANNOTATION_DISOPRED_3 = "ProteinAnnotation_Disopred_3";
	
	public static final String REQUEST_TYPE_NAME_PROTEIN_ANNOTATION_PSIPRED_3 = "ProteinAnnotation_Psipred_3";
	public static final String JOB_TYPE_NAME_PROTEIN_ANNOTATION_PSIPRED_3 = "ProteinAnnotation_Psipred_3";

	
}
