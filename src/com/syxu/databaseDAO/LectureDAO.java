//package com.syxu.databaseDAO;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.ExecutionException;
//
//import org.apache.http.NameValuePair;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.message.BasicNameValuePair;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import android.app.Activity;
//import android.content.Context;
//import android.os.AsyncTask;
//import android.util.Log;
//import android.widget.Toast;
//
//import com.syxu.database.Lecture;
//import com.syxu.icoachapp.JSONParser;
//
//public class LectureDAO{
//	Context mContext;
//	Lecture lecture;
//    private boolean result=false;
//	static String TAG = "LectureDAO";
//	
//	public LectureDAO(Context mContext){
//		this.mContext = mContext;
//	}
//	
//	public boolean addLecture(Lecture alecture){
//		lecture = alecture;
//		try {
//			new AddLectureTask().execute().get();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (ExecutionException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return result;
//	}
//	
//	private class AddLectureTask extends AsyncTask<Void,Boolean,Boolean> {
//		private static final String  ADDLECTURE_URL = "http://www.skycssc.com/webservice/addLecture.php";
//		@Override
//		protected Boolean doInBackground(Void... params) {
//			// TODO Auto-generated method stub
//			List<NameValuePair> postparam = new ArrayList<NameValuePair>();
//			postparam.add( new BasicNameValuePair("lectureID", lecture.getLectureid()));
//			postparam.add( new BasicNameValuePair("lectureNote", lecture.getLectureNote()));
//			postparam.add( new BasicNameValuePair("classID", lecture.getClassID()));			
//			postparam.add( new BasicNameValuePair("lectureDate", lecture.getLectureDate()));			
//	        final JSONObject jsonObject = new JSONParser().makeHttpRequest(ADDLECTURE_URL, "POST", postparam);
//            
//	        try {
//				if(jsonObject.getInt("success") == 0){
//					((Activity) mContext).runOnUiThread(new Runnable() {
//				        public void run() {
//				        	String message=null;
//							try {
//								message = jsonObject.getString("message");
//					            Log.w(TAG, jsonObject.getString("debug"));
//							} catch (JSONException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							}
//				            Toast.makeText(mContext, message,Toast.LENGTH_SHORT).show();
//				        }
//				    }); 
//				}else{
//					result = true;
//				}
//
//	        }catch(JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			return false;
//		}
//	}
//}
//
//
////
////import java.util.ArrayList;
////import java.util.List;
////
////import android.content.Context;
////import android.database.Cursor;
////import android.util.Log;
////
////import com.syxu.database.Lecture;
////import com.syxu.database.Lecture;
////
////public class LectureDAO extends ClassDAO {
////	public static String TABLE_NAME = "Class";
////	public static String COLUMN_CLASSID = "classid";
////	public static String COLUMN_LESSONID = "lessonid"; 
////	public static String COLUMN_CLASSNAME = "classname"; 
////	public static String COLUMN_COACHNAME = "coachname"; 
////	private static String LOG = "LessonDAO";
////	
////	public LectureDAO(Context context) {
////		super(context);
////		// TODO Auto-generated constructor stub
////	}
////	
////	public void open(){
//////		db = dbhelper.getWritableDatabase();
////		db = dbhelper.openDataBase();
////	}
////	
////	public void close(){
////		db.close();
////	}
////	
////	
////	public List<Lecture> getLessonsByCoachname(String coachname){
////		List<Lecture> lessonlist = new ArrayList<Lecture>();
////
////		String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE "+ COLUMN_COACHNAME +"=?";
////		Log.d(LOG, selectQuery);
////		Cursor c = db.rawQuery(selectQuery, new String[]{ coachname});
////		if(c.moveToFirst()){
////			do{
////				int classididx = c.getColumnIndex(COLUMN_CLASSID);
////				int classnameidx = c.getColumnIndex(COLUMN_CLASSNAME);
////				int lessonidx = c.getColumnIndex(COLUMN_LESSONID);
////				int count = c.getColumnCount();
////				String[] str = c.getColumnNames();
////				Lecture alesson = new Lecture(c.getString(classididx), c.getString(classnameidx), c.getString(lessonidx));//classid, ,classname, lessonid in Lesson constructor
////				lessonlist.add(alesson);
////			}while(c.moveToNext());
////		}
////		
////		return lessonlist;
////	}
////	
////}
