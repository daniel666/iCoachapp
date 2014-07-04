package com.syxu.databaseDAO;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.syxu.database.Class;
import com.syxu.icoachapp.ClassCreateActivity;
import com.syxu.icoachapp.JSONParser;

public class ClassDAO{
	Context mContext;

	public ClassDAO(Context mContext) {
		this.mContext = mContext;
	}

	public void createClass(Class theclass, int actioncode){
//		new CreateClassTask(theclass, actioncode).execute();
			
	}
	


}

//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//import android.content.ContentValues;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.util.Log;
//
//import com.syxu.database.Class;
//import com.syxu.databasehelper.DatabaseHelper;
//
//public class ClassDAO {
//	public static String TABLE_NAME = "Class";
//	public static String COLUMN_ID = "classid";
//	public static String COLUMN_NAME = "classname"; 
//	private static String LOG = "ClassDAO";
//	
//	protected SQLiteDatabase db;
//	protected DatabaseHelper dbhelper;
//	
//	public ClassDAO(Context context){
//		dbhelper = new DatabaseHelper(context);
//	}
//	
//	public void open(){
////		db = dbhelper.getWritableDatabase();
//		db = dbhelper.openDataBase();
//	}
//	
//	public void close(){
//		dbhelper.close();
//	}
//	
//	
//	public void addClass(Class aclass){
//		
//		ContentValues values = new ContentValues();
//		values.put(COLUMN_ID, aclass.getId());
//		values.put(COLUMN_NAME, aclass.getName());
//		
//		Log.d("DATABASEHANDLER", "Adding aclass: name="+aclass.getName()+", id="+aclass.getId());
//		//Inserting row
//		if(db.insert(TABLE_NAME, null, values)==-1)
//			Log.e("DATABASEHANDLER", "insert into database failed!");
//	}
//	
//	public Class getClass(Class aclass){
//		
////		String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE " 
////								+ COLUMN_NAME + " =? " + aclass.getName() +" AND "
////								+ COLUMN_PASSWORD + "="  + aclass.getPassword() 
////								+ ";";
//		
//		String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID +" =? AND " + COLUMN_NAME + " =?";
//		
//		Log.d(LOG, selectQuery);
//		
////		Cursor c = db.query(TABLE_NAME, null, new String[]{COLUMN_NAME, COLUMN_PASSWORD}, new String[]{aclass.getName(), aclass.getPassword()}, null, null, null);
//		Cursor c = db.rawQuery(selectQuery, new String[]{ aclass.getId(), aclass.getName()});
//		if(c.moveToFirst()){
//			Class returnaclass = new Class(c.getString(0), c.getString(1));
//			return returnaclass;
//		}
//		return null;
//		
//	}
//	
//	public List<Class> getAllClassses(){
//		List<Class> aclasslist = new ArrayList<Class>();
//		
//		String selectQuery = "SELECT * FROM " + TABLE_NAME + ";" ;
//		Cursor c = db.rawQuery(selectQuery, null);
//		if(c.moveToFirst()){
//			do{
//				Class aaclass = new Class(c.getString(0), c.getString(1));
//				aclasslist.add(aaclass);
//			}while(c.moveToNext());
//		}
//		
//		return aclasslist;
//	}
//	
//	//for debug purpose
//	public void walk(){
//		for(Class aclass : getAllClassses()){
//			Log.i(LOG, aclass.toString());
//
//		}
//	}
//	
//	public int getClassCount(){
//		String countQuery = "SELECT * FROM " + TABLE_NAME +  ";" ;
//        Cursor cursor = db.rawQuery(countQuery, null);
//        cursor.close();
// 
//        // return count
//        return cursor.getCount();
//	}
//	
//	public int updateClass(Class aclass) {
//	 
//	    ContentValues values = new ContentValues();
//	    values.put(COLUMN_NAME, aclass.getName());
//	    values.put(COLUMN_ID, aclass.getId());
//	 
//	    // updating row
//	    return db.update(TABLE_NAME, values, COLUMN_ID + " = ?",
//	            new String[] { String.valueOf(aclass.getId()) });
//	}
//	
//	public void deleteClass(Class aclass) {
//	    db.delete(TABLE_NAME, COLUMN_ID + " = ?",
//	            new String[] { String.valueOf(aclass.getId()) });
//	    db.close();
//	}
//	
//}
