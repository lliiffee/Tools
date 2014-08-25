package com.fung.jsoup;

import java.math.BigDecimal;

public class ScoreBean {

	  String teamHome;
	  String teamAway;
	  String leagueName;
	  
	  BigDecimal rateHomeWin;
	  BigDecimal rateHomeDraw;
	  BigDecimal rateHomeLose;
	  
	  String teamToWin; //胜率较高球队
	  String teamToLose; //胜率低球队
	  
	  
	  BigDecimal rateTeamWin ; //胜率较高球队获胜赔率
	  BigDecimal rateTeamDaw ; //两队打和赔率
	public String getTeamHome() {
		return teamHome;
	}
	public void setTeamHome(String teamHome) {
		this.teamHome = teamHome;
	}
	public String getTeamAway() {
		return teamAway;
	}
	public void setTeamAway(String teamAway) {
		this.teamAway = teamAway;
	}
	public String getLeagueName() {
		return leagueName;
	}
	public void setLeagueName(String leagueName) {
		this.leagueName = leagueName;
	}
	public BigDecimal getRateHomeWin() {
		return rateHomeWin;
	}
	public void setRateHomeWin(BigDecimal rateHomeWin) {
		this.rateHomeWin = rateHomeWin;
	}
	public BigDecimal getRateHomeDraw() {
		return rateHomeDraw;
	}
	public void setRateHomeDraw(BigDecimal rateHomeDraw) {
		this.rateHomeDraw = rateHomeDraw;
	}
	public BigDecimal getRateHomeLose() {
		return rateHomeLose;
	}
	public void setRateHomeLose(BigDecimal rateHomeLose) {
		this.rateHomeLose = rateHomeLose;
	}
	public String getTeamToWin() {
		return teamToWin;
	}
	public void setTeamToWin(String teamToWin) {
		this.teamToWin = teamToWin;
	}
	public String getTeamToLose() {
		return teamToLose;
	}
	public void setTeamToLose(String teamToLose) {
		this.teamToLose = teamToLose;
	}
	public BigDecimal getRateTeamWin() {
		return rateTeamWin;
	}
	public void setRateTeamWin(BigDecimal rateTeamWin) {
		this.rateTeamWin = rateTeamWin;
	}
	public BigDecimal getRateTeamDaw() {
		return rateTeamDaw;
	}
	public void setRateTeamDaw(BigDecimal rateTeamDaw) {
		this.rateTeamDaw = rateTeamDaw;
	}
	 
	  
	  
	  
	   
}
