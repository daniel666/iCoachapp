//package com.syxu.databaseDAO;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import android.content.ContentValues;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.util.Log;
//
//import com.syxu.database.Student;
//import com.syxu.databasehelper.DatabaseHelper;
//
//public class StudentDAO {
//	public static String TABLE_NAME = "Student";
//	public static String TABLE_COACHSTUDENT = "CoachStudent";
//	public static String TABLE_COACH = "Coach";
//	public static String COLUMN_ID = "studentid";
//	public static String COLUMN_NAME = "studentname"; 
//	private static String LOG = "StudentDAO";
//	
//	private SQLiteDatabase db;
//	private DatabaseHelper dbhelper;
//	
//	public StudentDAO(Context context){
//		dbhelper = new DatabaseHelper(context);
//	}
//	
//	public void open(){
//		db = dbhelper.openDataBase();
//	}
//	
//	
//	public void close(){
//		dbhelper.close();
//	}
//	
//	
//public void addStudent(Student astudent){
//		
//		ContentValues values = new ContentValues();
//		values.put(COLUMN_ID, astudent.getId());
//		values.put(COLUMN_NAME, astudent.getName());
//		
//		Log.d("DATABASEHANDLER", "Adding astudent: name="+astudent.getName()+", id="+astudent.getId());
//		//Inserting row
//		if(db.insert(TABLE_NAME, null, values)==-1)
//			Log.e("DATABASEHANDLER", "insert into database failed!");
//	}
//
//
//	public Student getStudent(Student astudent){
//		
//
//		String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID +" =? AND " + COLUMN_NAME + " =?";
//		
//		Log.d(LOG, selectQuery);
//		
//		Cursor c = db.rawQuery(selectQuery, new String[]{ astudent.getId(), astudent.getName()});
//		if(c.moveToFirst()){
//			Student returnastudent = new Student(c.getString(0), c.getString(1));
//			return returnastudent;
//		}
//		return null;
//		
//	}
//	
//
//	public List<Student> getAllStudents(){
//		List<Student> studentlist = new ArrayList<Student>();
//		
//		String selectQuery = "SELECT * FROM " + TABLE_NAME + ";" ;
//		Cursor c = db.rawQuery(selectQuery, null);
//		if(c.moveToFirst()){
//			do{
//				Student astudent = new Student(c.getString(0), c.getString(1));
//				studentlist.add(astudent);
//			}while(c.moveToNext());
//		}
//		
//		return studentlist;
//	}
//	
//	//for debug purpose
//	public void walk(){
//		for(Student astudent : getAllStudents()){
//			Log.i(LOG, astudent.toString());
//
//		}
//	}
//	
//	public int getStudentCount(){
//		String countQuery = "SELECT * FROM " + TABLE_NAME +  ";" ;
//        Cursor cursor = db.rawQuery(countQuery, null);
//        cursor.close();
// 
//        // return count
//        return cursor.getCount();
//	}
//	
//	public int updateStudent(Student astudent) {
//	 
//	    ContentValues values = new ContentValues();
//	    values.put(COLUMN_NAME, astudent.getName());
//	    values.put(COLUMN_ID, astudent.getId());
//	 
//	    // updating row
//	    return db.update(TABLE_NAME, values, COLUMN_ID + " = ?",
//	            new String[] { String.valueOf(astudent.getId()) });
//	}
//	
//	public void deleteStudent(Student astudent) {
//	    db.delete(TABLE_NAME, COLUMN_ID + " = ?",
//	            new String[] { String.valueOf(astudent.getId()) });
//	    db.close();
//	}
//}
