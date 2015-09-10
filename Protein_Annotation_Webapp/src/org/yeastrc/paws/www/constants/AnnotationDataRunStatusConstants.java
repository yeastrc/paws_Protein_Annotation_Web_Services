package org.yeastrc.paws.www.constants;

public class AnnotationDataRunStatusConstants {

	public static final String STATUS_SUBMITTED = "submitted";
	public static final String STATUS_COMPLETE = "complete";
	public static final String STATUS_FAIL = "fail";
	
	
	/**
	 * Status no record in the database (not put in the database)
	 */
	public static final String STATUS_NO_RECORD = "no_record";
}


//CREATE TABLE annotation_data (
//		  sequence_id INT UNSIGNED NOT NULL,
//		  annotation_type_id INT UNSIGNED NOT NULL,
//		  ncbi_taxonomy_id INT UNSIGNED NOT NULL,
//		  run_status ENUM('submitted','complete','fail') NOT NULL,
//		  last_run_date DATETIME NOT NULL,
//		  annotation_data LONGTEXT NOT NULL,
