package org.yeastrc.paws.dto;

import java.math.BigDecimal;

public class DisopredParsedDisorderedResiduesPredictionEntry {

	private int position;
	private String aminoAcid;
	private String type;
	private String typeRaw;
	private BigDecimal probability;
	private boolean probabilityNA;

	public String getTypeRaw() {
		return typeRaw;
	}
	public void setTypeRaw(String typeRaw) {
		this.typeRaw = typeRaw;
	}
	public boolean isProbabilityNA() {
		return probabilityNA;
	}
	public void setProbabilityNA(boolean probabilityNA) {
		this.probabilityNA = probabilityNA;
	}
	public BigDecimal getProbability() {
		return probability;
	}
	public void setProbability(BigDecimal probability) {
		this.probability = probability;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	public String getAminoAcid() {
		return aminoAcid;
	}
	public void setAminoAcid(String aminoAcid) {
		this.aminoAcid = aminoAcid;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
