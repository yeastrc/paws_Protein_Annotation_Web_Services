package org.yeastrc.paws.www.dto;

import java.util.Date;

/**
 * annotation_data
 *
 */
public class AnnotationDataDTO {

	private int sequenceId;
	private int annotationTypeId;
	private int ncbiTaxonomyId;
	private String runStatus;
	private Date lastRunDate;
	private String annotationData;
	
	
	public String getRunStatus() {
		return runStatus;
	}
	public void setRunStatus(String runStatus) {
		this.runStatus = runStatus;
	}
	public Date getLastRunDate() {
		return lastRunDate;
	}
	public void setLastRunDate(Date lastRunDate) {
		this.lastRunDate = lastRunDate;
	}
	public int getSequenceId() {
		return sequenceId;
	}
	public void setSequenceId(int sequenceId) {
		this.sequenceId = sequenceId;
	}
	public int getAnnotationTypeId() {
		return annotationTypeId;
	}
	public void setAnnotationTypeId(int annotationTypeId) {
		this.annotationTypeId = annotationTypeId;
	}
	public int getNcbiTaxonomyId() {
		return ncbiTaxonomyId;
	}
	public void setNcbiTaxonomyId(int ncbiTaxonomyId) {
		this.ncbiTaxonomyId = ncbiTaxonomyId;
	}
	public String getAnnotationData() {
		return annotationData;
	}
	public void setAnnotationData(String annotationData) {
		this.annotationData = annotationData;
	}
}



//CREATE TABLE annotation_data (
//		  sequence_id INT UNSIGNED NOT NULL,
//		  annotation_type_id INT UNSIGNED NOT NULL,
//		  ncbi_taxonomy_id INT UNSIGNED NOT NULL,
//		  run_status ENUM('submitted','complete','fail') NOT NULL,
//		  last_run_date DATETIME NOT NULL,
//		  annotation_data LONGTEXT NULL,
