package com.syxu.database;

import java.io.Serializable;

public class Class implements Serializable {
	private String classID;
	private String coachID;
	private String schoolID;
	private String className;
	private String startDate;
	private String classTime;
	private int numOfCourse;
	private String classDesc;
	private String remarks;
	private int fee;
	private int active;
	
	public Class(String classID, String coachID, String schoolID, String className,
			String startDate, String classTime, int numOfCourse,
			String classDesc, String remarks, int fee, int active
			) {
		super();
		this.classID = classID;
		this.coachID = coachID;
		this.schoolID = schoolID;
		this.className = className;
		this.startDate = startDate;
		this.classTime = classTime;
		this.numOfCourse = numOfCourse;
		this.classDesc = classDesc;
		this.remarks = remarks;
		this.fee = fee;
		this.active = active;
//		this.coachID = coachID;
	}

	public String getClassID() {
		return classID;
	}

	public void setClassID(String classID) {
		this.classID = classID;
	}

	public String getCoachID(){
		return coachID;
	}
	
	public void setCoachID(String coachID){
		this.coachID = coachID;
	}
	public String getSchoolID() {
		return schoolID;
	}

	public void setSchoolID(String schoolID) {
		this.schoolID = schoolID;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getClassTime() {
		return classTime;
	}

	public void setClassTime(String classTime) {
		this.classTime = classTime;
	}

	public int getNumOfCourse() {
		return numOfCourse;
	}

	public void setNumOfCourse(int numOfCourse) {
		this.numOfCourse = numOfCourse;
	}

	public String getClassDesc() {
		return classDesc;
	}

	public void setClassDesc(String classDesc) {
		this.classDesc = classDesc;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public int getFee() {
		return fee;
	}

	public void setFee(int fee) {
		this.fee = fee;
	}

	public int getActive() {
		return active;
	}

	public void setActive(int active) {
		this.active = active;
	}

//	public String getCoachID() {
//		return coachID;
//	}
//
//	public void setCoachID(String coachID) {
//		this.coachID = coachID;
//	}
	
	
	

	
	
}