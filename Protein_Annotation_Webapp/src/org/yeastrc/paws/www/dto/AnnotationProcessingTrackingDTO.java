package org.yeastrc.paws.www.dto;

import java.util.Date;

/**
 * annotation_processing_tracking
 *
 */
public class AnnotationProcessingTrackingDTO {

	private int sequenceId;
	private int annotationTypeId;
	private int ncbiTaxonomyId;
	private int jobcenterRequestId;
	private String runStatus;
	private Date lastRunDate;
	
	
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
	public int getJobcenterRequestId() {
		return jobcenterRequestId;
	}
	public void setJobcenterRequestId(int jobcenterRequestId) {
		this.jobcenterRequestId = jobcenterRequestId;
	}
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
	
}


//
//CREATE TABLE annotation_processing_tracking (
//  sequence_id INT UNSIGNED NOT NULL,
//  annotation_type_id INT UNSIGNED NOT NULL,
//  ncbi_taxonomy_id INT UNSIGNED NOT NULL,
//  jobcenter_request_id INT UNSIGNED NOT NULL,
//  run_status ENUM('submitted','complete','fail') NOT NULL,
//  last_update_date DATETIME NOT NULL,
