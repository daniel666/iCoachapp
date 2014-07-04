package com.syxu.databaseDAO;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.syxu.database.User;
import com.syxu.databasehelper.DatabaseHelper;

public class UserDAO {
	public static String TABLE_NAME = "User";
	public static String COLUMN_ID = "id";
	public static String COLUMN_NAME = "name"; 
	public static String COLUMN_PASSWORD= "password"; 
	public static String COLUMN_ROLE= "role"; 
	private static String LOG = "UserDAO";

	
	private SQLiteDatabase db;
	private DatabaseHelper dbhelper;
	
	public UserDAO(Context context){
		dbhelper = new DatabaseHelper(context);
	}
	
	public void open(){
//		db = dbhelper.getWritableDatabase();
		db = dbhelper.openDataBase();
	}
	
	
	public void close(){
		dbhelper.close();
	}
	
	
	public void addUser(User user){
		
		ContentValues values = new ContentValues();
		values.put(COLUMN_ID, user.getId());
		values.put(COLUMN_NAME, user.getName());
		values.put(COLUMN_PASSWORD, user.getPassword());
		values.put(COLUMN_ROLE, user.getRole());
		
		Log.d("DATABASEHANDLER", "Adding user: name="+user.getName()+", password="+user.getPassword());
		//Inserting row
		if(db.insert(TABLE_NAME, null, values)==-1)
			Log.e("DATABASEHANDLER", "insert into database failed!");
	}
	
	public User getUser(User user){
		
//		String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE " 
//								+ COLUMN_NAME + " =? " + user.getName() +" AND "
//								+ COLUMN_PASSWORD + "="  + user.getPassword() 
//								+ ";";
		
		String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_NAME +" =? AND " + COLUMN_PASSWORD + " =?";
		
		Log.d(LOG, selectQuery);
		
//		Cursor c = db.query(TABLE_NAME, null, new String[]{COLUMN_NAME, COLUMN_PASSWORD}, new String[]{user.getName(), user.getPassword()}, null, null, null);
		Cursor c = db.rawQuery(selectQuery, new String[]{user.getName(), user.getPassword()});
		if(c.moveToFirst()){			
			User returnuser = new User(c.getString(1), c.getString(2), c.getString(3));
			return returnuser;
		}
		return null;
	}
	
	
	public List<User> getAllUsers(){
		List<User> userlist = new ArrayList<User>();
		
		String selectQuery = "SELECT * FROM " + TABLE_NAME + ";" ;
		Cursor c = db.rawQuery(selectQuery, null);
		if(c.moveToFirst()){
			do{
				User auser = new User(c.getString(1), c.getString(2), c.getString(3));
				userlist.add(auser);
			}while(c.moveToNext());
		}
		
		return userlist;
	}
	
	//for debug purpose
	public void walk(){
		for(User user : getAllUsers()){
			Log.i(LOG, user.toString());

		}
	}
	
	public int getUserCount(){
		String countQuery = "SELECT * FROM " + TABLE_NAME +  ";" ;
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
 
        // return count
        return cursor.getCount();
	}
	
	public int updateUser(User user) {
	 
	    ContentValues values = new ContentValues();
	    values.put(COLUMN_NAME, user.getName());
	    values.put(COLUMN_PASSWORD, user.getPassword());
	 
	    // updating row
	    return db.update(TABLE_NAME, values, COLUMN_ID + " = ?",
	            new String[] { String.valueOf(user.getId()) });
	}
	
	public void deleteUser(User user) {
	    db.delete(TABLE_NAME, COLUMN_ID + " = ?",
	            new String[] { String.valueOf(user.getId()) });
	    db.close();
	}
}
