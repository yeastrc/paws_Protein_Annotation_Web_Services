package org.yeastrc.paws.dto;

import java.math.BigDecimal;

public class DisopredParsedDisorderedResiduesPredictionEntry {

	private int position;
	private String aminoAcid;
	private String type;
	private String typeRaw;
	private BigDecimal score1;
	private boolean score1NA;
	private BigDecimal score2;
	private boolean score2NA;


	public String getTypeRaw() {
		return typeRaw;
	}
	public void setTypeRaw(String typeRaw) {
		this.typeRaw = typeRaw;
	}
	public BigDecimal getScore1() {
		return score1;
	}
	public void setScore1(BigDecimal score1) {
		this.score1 = score1;
	}
	public boolean isScore1NA() {
		return score1NA;
	}
	public void setScore1NA(boolean score1na) {
		score1NA = score1na;
	}
	public BigDecimal getScore2() {
		return score2;
	}
	public void setScore2(BigDecimal score2) {
		this.score2 = score2;
	}
	public boolean isScore2NA() {
		return score2NA;
	}
	public void setScore2NA(boolean score2na) {
		score2NA = score2na;
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
