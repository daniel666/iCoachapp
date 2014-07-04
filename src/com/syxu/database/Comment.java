package com.syxu.database;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class Comment {
	private String classID;
	private String lectureID;
	private String lectureNote;
	private String studentID;
	private String grade;
	private String cmtContent; //comment content
	private String photo;
	private String defaultCmt;
	private String coachID;
	private String dateInput;
	

	public Comment(String classID, String lectureID, String lectureNote,
			String studentID, String grade, String cmtContent, String photo,
			String defaultCmt, String coachID, String dateInput) {
		super();
		this.classID = classID;
		this.lectureID = lectureID;
		this.lectureNote = lectureNote;
		this.studentID = studentID;
		this.grade = grade;
		this.cmtContent = cmtContent;
		this.photo = photo;
		this.defaultCmt = defaultCmt;
		this.coachID = coachID;
		this.dateInput = dateInput;
	}
	
	
	
	public String getClassID() {
		return classID;
	}
	public void setClassID(String classID) {
		this.classID = classID;
	}
	public String getLectureID() {
		return lectureID;
	}
	public void setLectureID(String lectureID) {
		this.lectureID = lectureID;
	}
	public String getLectureNote() {
		return lectureNote;
	}
	public void setLectureNote(String lectureNote) {
		this.lectureNote = lectureNote;
	}
	public String getStudentID() {
		return studentID;
	}
	public void setStudentID(String studentID) {
		this.studentID = studentID;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getCmtContent() {
		return cmtContent;
	}
	public void setCmtContent(String cmtContent) {
		this.cmtContent = cmtContent;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public String getDefaultCmt() {
		return defaultCmt;
	}
	public void setDefaultCmt(String defaultCmt) {
		this.defaultCmt = defaultCmt;
	}
	public String getCoachID() {
		return coachID;
	}
	public void setCoachID(String coachID) {
		this.coachID = coachID;
	}
	public String getDateInput() {
		return dateInput;
	}
	public void setDateInput(String dateInput) {
		this.dateInput = dateInput;
	}
	public static Comparator<Comment> getDateComparator() {
		return DateComparator;
	}
	public static void setDateComparator(Comparator<Comment> dateComparator) {
		DateComparator = dateComparator;
	}
	public static Comparator<Comment> getGradeComparator() {
		return GradeComparator;
	}
	public static void setGradeComparator(Comparator<Comment> gradeComparator) {
		GradeComparator = gradeComparator;
	}
	public static Comparator<Comment> getLessonComparator() {
		return LessonComparator;
	}
	public static void setLessonComparator(Comparator<Comment> lessonComparator) {
		LessonComparator = lessonComparator;
	}
	
	public static Comparator<Comment> DateComparator 
		    = new Comparator<Comment>() {	
				public int compare(Comment comment1, Comment comment2) {
					String comment1Date = comment1.getDateInput().toUpperCase();
					String comment2Date = comment2.getDateInput().toUpperCase();
					SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date date1=null, date2=null;
					try {
						date1 = sf.parse(comment1Date);
						date2 = sf.parse(comment2Date);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return date2.compareTo(date1);
				}
				
	};
	
	public static int toInt(String grade){
		grade = grade.replaceAll("\\s+", "");
		int ret=0;
		if(grade.length()==1)
			return ret = (int) grade.charAt(0) +
					(int) ',' ;
		return ret = (int) grade.charAt(0) +
				(int) grade.charAt(1);
		
	}
	public static Comparator<Comment> GradeComparator 
    		= new Comparator<Comment>() {

				public int compare(Comment comment1, Comment comment2) {
					String comment1Grade = comment1.getGrade().toUpperCase();
					String comment2Grade = comment2.getGrade().toUpperCase();
					
					return toInt(comment1Grade) - toInt(comment2Grade);
				}
	};
	public static Comparator<Comment> LessonComparator 
			= new Comparator<Comment>() {
		
				public int compare(Comment comment1, Comment comment2) {
					String comment1Lesson = comment1.getClassID() ;
					String comment2Lesson = comment2.getClassID() ;
					
					int tmp = comment1Lesson.compareTo(comment2Lesson);
					if(tmp!=0)
						return tmp;
					else 
						return Integer.parseInt(comment1.getLectureID())
							  - Integer.parseInt(comment2.getLectureID());
					
				}
		};
	
	
}
