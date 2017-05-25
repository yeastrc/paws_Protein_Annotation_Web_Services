package org.yeastrc.paws.www.dto;

import java.util.Date;

/**
 * annotation_processing_tracking
 *
 */
public class AnnotationProcessingTrackingDTO {

	private int id;
	private int sequenceId;
	private int annotationTypeId;
	private int ncbiTaxonomyId;
	private int jobcenterRequestId;
	
	private String requestingIP;
	private boolean batchRequest;
	private String batchRequestId;

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
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getRequestingIP() {
		return requestingIP;
	}
	public void setRequestingIP(String requestingIP) {
		this.requestingIP = requestingIP;
	}
	public boolean isBatchRequest() {
		return batchRequest;
	}
	public void setBatchRequest(boolean batchRequest) {
		this.batchRequest = batchRequest;
	}
	public String getBatchRequestId() {
		return batchRequestId;
	}
	public void setBatchRequestId(String batchRequestId) {
		this.batchRequestId = batchRequestId;
	}
	
}
