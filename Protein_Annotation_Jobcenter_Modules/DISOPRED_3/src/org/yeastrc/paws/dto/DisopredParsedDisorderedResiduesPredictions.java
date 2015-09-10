package org.yeastrc.paws.dto;

import java.util.List;

public class DisopredParsedDisorderedResiduesPredictions {

	private List<DisopredParsedDisorderedResiduesPredictionEntry> entries;
	
	private String filename;
	private List<String> fileHeaderLines;

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public List<String> getFileHeaderLines() {
		return fileHeaderLines;
	}

	public void setFileHeaderLines(List<String> fileHeaderLines) {
		this.fileHeaderLines = fileHeaderLines;
	}

	public List<DisopredParsedDisorderedResiduesPredictionEntry> getEntries() {
		return entries;
	}

	public void setEntries(
			List<DisopredParsedDisorderedResiduesPredictionEntry> entries) {
		this.entries = entries;
	}
}
