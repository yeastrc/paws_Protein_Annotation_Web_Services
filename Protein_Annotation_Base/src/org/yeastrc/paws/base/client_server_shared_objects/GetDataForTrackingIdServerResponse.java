package org.yeastrc.paws.base.client_server_shared_objects;

/**
 * Used for transfering Data from server to Jobcenter modules for processing a request
 *
 */
public class GetDataForTrackingIdServerResponse {

	private boolean success;
	private boolean noRecordForTrackingId;
	private boolean dataAlreadyProcessed;
	private String sequence;
	
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public boolean isNoRecordForTrackingId() {
		return noRecordForTrackingId;
	}
	public void setNoRecordForTrackingId(boolean noRecordForTrackingId) {
		this.noRecordForTrackingId = noRecordForTrackingId;
	}
	public boolean isDataAlreadyProcessed() {
		return dataAlreadyProcessed;
	}
	public void setDataAlreadyProcessed(boolean dataAlreadyProcessed) {
		this.dataAlreadyProcessed = dataAlreadyProcessed;
	}
	public String getSequence() {
		return sequence;
	}
	public void setSequence(String sequence) {
		this.sequence = sequence;
	}
}
