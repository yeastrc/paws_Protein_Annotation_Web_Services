package org.yeastrc.paws.dto;

import java.util.List;

public class DisopredParsedProteinBindingDisorderedResiduesPredictions {

	private List<DisopredParsedProteinBindingDisorderedResiduesPredictionEntry> entries;
	
	private String filename;
	private List<String> fileHeaderLines;

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

	public List<DisopredParsedProteinBindingDisorderedResiduesPredictionEntry> getEntries() {
		return entries;
	}

	public void setEntries(
			List<DisopredParsedProteinBindingDisorderedResiduesPredictionEntry> entries) {
		this.entries = entries;
	}
}
