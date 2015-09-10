package org.yeastrc.paws.dto;

import java.util.List;

public class PsipredParsedResults {

	private List<PsipredParsedDisorderedResiduesPredictionEntry> entries;

	private List<String> fileHeaderLines;
	private String filename;
	
	public List<String> getFileHeaderLines() {
		return fileHeaderLines;
	}

	public void setFileHeaderLines(List<String> fileHeaderLines) {
		this.fileHeaderLines = fileHeaderLines;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public List<PsipredParsedDisorderedResiduesPredictionEntry> getEntries() {
		return entries;
	}

	public void setEntries(
			List<PsipredParsedDisorderedResiduesPredictionEntry> entries) {
		this.entries = entries;
	}
}
