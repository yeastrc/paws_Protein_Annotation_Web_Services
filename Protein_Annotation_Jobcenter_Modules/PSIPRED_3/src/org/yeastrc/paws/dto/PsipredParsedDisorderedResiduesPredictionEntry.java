package org.yeastrc.paws.dto;

import java.math.BigDecimal;

public class PsipredParsedDisorderedResiduesPredictionEntry {

	private int position;
	private String aminoAcid;
	private String type;
	private BigDecimal score1;
	private boolean score1NA;
	private BigDecimal score2;
	private boolean score2NA;
	private BigDecimal score3;
	private boolean score3NA;


	public BigDecimal getScore3() {
		return score3;
	}
	public void setScore3(BigDecimal score3) {
		this.score3 = score3;
	}
	public boolean isScore3NA() {
		return score3NA;
	}
	public void setScore3NA(boolean score3na) {
		score3NA = score3na;
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
