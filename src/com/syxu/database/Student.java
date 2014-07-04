package com.syxu.database;

import java.io.Serializable;

public class Student implements Serializable {
	private String studentID;
	private String studentName;
	private String tel;
	private String profilPic;
	private String dob;
	private String gender;
	private String address;
	private String email;
	private String remarks;
	private String joinDate;
	private String schoolID;
	private int active;
	
	public Student(String studentID, String studentName, String tel,
			String profilPic, String dob, String gender, String address,
			String email, String remarks, String joinDate, String schoolID, int active) {
		super();
		this.studentID = studentID;
		this.studentName = studentName;
		this.tel = tel;
		this.profilPic = profilPic;   //full url because of applicationwrapper
		this.dob = dob;
		this.gender = gender;
		this.address = address;
		this.email = email;
		this.remarks = remarks;
		this.joinDate = joinDate;
		this.schoolID = schoolID;
		this.active = active;
	}

	public String getStudentID() {
		return studentID;
	}

	public void setStudentID(String studentID) {
		this.studentID = studentID;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getProfilPic() {
		return profilPic;
	}

	public void setProfilPic(String profilPic) {
		this.profilPic = profilPic;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getSchoolID() {
		return schoolID;
	}

	public void setSchoolID(String schoolID) {
		this.schoolID = schoolID;
	}

	public int getActive() {
		return active;
	}

	public void setActive(int active) {
		this.active = active;
	}

	public String getJoinDate() {
		return joinDate;
	}

	public void setJoinDate(String joinDate) {
		this.joinDate = joinDate;
	}
	
	
	
	
	
}
