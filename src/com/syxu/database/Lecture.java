package com.syxu.database;

public class Lecture {
		private String lectureid;
		private String lectureNote;
		private String classID;
		private String lectureDate;
		private String dateInput;
		
		public Lecture(String lectureid, String lectureNote, String classID,
				String lectureDate, String dateInput) {
			super();
			this.lectureid = lectureid;
			this.lectureNote = lectureNote;
			this.classID = classID;
			this.lectureDate = lectureDate;
			this.dateInput = dateInput;
		}

		public String getLectureid() {
			return lectureid;
		}

		public void setLectureid(String lectureid) {
			this.lectureid = lectureid;
		}

		public String getLectureNote() {
			return lectureNote;
		}

		public void setLectureNote(String lectureNote) {
			this.lectureNote = lectureNote;
		}

		public String getClassID() {
			return classID;
		}

		public void setClassID(String classID) {
			this.classID = classID;
		}

		public String getLectureDate() {
			return lectureDate;
		}

		public void setLectureDate(String lectureDate) {
			this.lectureDate = lectureDate;
		}

		public String getDateInput() {
			return dateInput;
		}

		public void setDateInput(String dateInput) {
			this.dateInput = dateInput;
		}
		
		
		
		
		
		

		
}
