//package com.syxu.databaseDAO;
//
//import java.io.File;
//import java.nio.charset.Charset;
//import java.util.concurrent.ExecutionException;
//
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.entity.ContentType;
//import org.apache.http.entity.mime.HttpMultipartMode;
//import org.apache.http.entity.mime.MultipartEntityBuilder;
//import org.apache.http.entity.mime.content.FileBody;
//import org.apache.http.protocol.HTTP;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import android.app.Activity;
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.os.AsyncTask;
//import android.util.Log;
//import android.widget.Toast;
//
//import com.syxu.database.Comment;
//import com.syxu.icoachapp.ApplicationWrapper;
//import com.syxu.icoachapp.JSONParser;
//
////package com.syxu.databaseDAO;
////
////import java.util.ArrayList;
//
//public class CommentDAO{
//	static String TAG = "CommentDAO";
//	
//	ApplicationWrapper ap;
//	private Context mContext;
//	private boolean success=false;
//	public CommentDAO(Context mContext) {
//		super();
//		this.mContext = mContext;
//		ap = (ApplicationWrapper) mContext.getApplicationContext();
//	}
//
//
//	public boolean addComment(Comment cmt){
//		try {
//			new addCommentTask(cmt).execute().get();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (ExecutionException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return success;
//
//	}
//		
//
//	
//	
//	private class addCommentTask extends AsyncTask<Void,Void,Boolean>{
//		private static final String uploadImageUrl = "http://www.skycssc.com/webservice/uploadImage.php";
//		private static final String ADDCOMMENT_URL ="http://www.skycssc.com/webservice/addComment.php";
//		private final ProgressDialog dialog = new ProgressDialog(mContext);
//		private Comment cmt;
//		
//		
//		public addCommentTask(Comment cmt) {
//			super();
//			this.cmt = cmt;
//		}
//
//		@Override
//		protected void onPreExecute(){
//			
//			dialog.setMessage("Adding comment..");
//			dialog.setIndeterminate(true);
//			dialog.show();
//			return ;
//		}
//		
//		@Override
//		protected Boolean doInBackground(Void... placeholder) {
//			// TODO Auto-generated method stub
//			
//			
//			HttpPost httpPostRequest = new HttpPost(ADDCOMMENT_URL);
//			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
//			builder.setCharset(Charset.forName("UTF-8"));
//			builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
//			
//			String path = cmt.getPhoto();
//			if(path!=null && path!=""){
//				FileBody fileBody = new FileBody(new File(cmt.getPhoto()));
//				builder.addPart("uploaded_file", fileBody);
//			}
//			builder.addTextBody("classID", cmt.getClassID(), ContentType.create("text/plain", Charset.forName(HTTP.UTF_8)));
//			builder.addTextBody("lectureNote", cmt.getLectureNote(), ContentType.create("text/plain", Charset.forName(HTTP.UTF_8)));
//			builder.addTextBody("studentID", cmt.getStudentID(), ContentType.create("text/plain", Charset.forName(HTTP.UTF_8)));
//			builder.addTextBody("grade", cmt.getGrade(), ContentType.create("text/plain", Charset.forName(HTTP.UTF_8)));			
//			builder.addTextBody("cmtContent", cmt.getCmtContent(), ContentType.create("text/plain", Charset.forName(HTTP.UTF_8)));
//			builder.addTextBody("coachID", cmt.getCoachID(), ContentType.create("text/plain", Charset.forName(HTTP.UTF_8)));
//			builder.addTextBody("schoolID", ap.getSchoolID(), ContentType.create("text/plain", Charset.forName(HTTP.UTF_8)));
//
//			httpPostRequest.setEntity(builder.build());
//			
//			final JSONObject jsonObject = new JSONParser().makeHttpRequest(httpPostRequest);
//            try {
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
//					//success
//					success = true;
//					((Activity) mContext).runOnUiThread(new Runnable() {
//				        public void run() {
//				        	String message=null;
//							try {
//								message = jsonObject.getString("message");
//
//
//							} catch (JSONException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							}
//				            Toast.makeText(mContext, message,Toast.LENGTH_SHORT).show();
//				        }
//				    }); 
//					return true;
//				}
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			return false;
//		}
//		
//		protected void onPostExecute(Boolean result){
//			if (dialog.isShowing()) {
//		        dialog.dismiss();
//		     }
//				success = result;
//		}
//
//	}
//
//}
//
////import java.util.Date;
////
////import android.content.Context;
////import android.database.Cursor;
////import android.database.sqlite.SQLiteDatabase;
////import android.util.Log;
////
////import com.syxu.database.Comment;
////import com.syxu.database.Student;
////import com.syxu.database.User;
////import com.syxu.databasehelper.DatabaseHelper;
////
////public class CommentDAO {
////	private String TABLE_NAME = "Comment";
////	private String COLUMN_CLASSID = "classid";
////	private String COLUMN_LESSONID = "lessonid";
////	private String COLUMN_STUDENTID = "studentid";
////	private String COLUMN_CONTENT = "content";
////	private String COLUMN_GRADE = "grade";
////	private String COLUMN_DATE = "date";
////
////
////	private SQLiteDatabase db;
////	private DatabaseHelper dbhelper;
////	
////	public CommentDAO(Context context){
////		dbhelper = new DatabaseHelper(context);
////	}
////	
////	public void open(){
//////		db = dbhelper.getWritableDatabase();
////		db = dbhelper.openDataBase();
////	}
////	
////	public void close(){
////		dbhelper.close();
////	}
////	
////	public Boolean addComment(Comment acomment){
////		return null;
////		
////	}
////
////	public ArrayList<Comment> getCommentByClassidAndStudentid(String classid,
////			String studentid) {
////		
////		ArrayList<Comment> commentlist = new ArrayList<Comment>();
////		
////		String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_CLASSID + " =? AND " + COLUMN_STUDENTID + "=?";
////		
//////		Cursor c = db.query(TABLE_NAME, null, new String[]{COLUMN_NAME, COLUMN_PASSWORD}, new String[]{user.getName(), user.getPassword()}, null, null, null);
////		Cursor c = db.rawQuery(selectQuery, new String[]{classid, studentid});
////		if(c.moveToFirst()){
////			do{
////				Comment acomment = new Comment(c.getString(0), c.getString(1),c.getString(2),c.getString(3), c.getString(4), c.getString(5));
////				commentlist.add(acomment);
////			}while(c.moveToNext());
////		}
////		
////		return commentlist;
////	}
////
////	public ArrayList<Comment> getCommentByClassid(String classid) {
////		// TODO Auto-generated method stub
////		ArrayList<Comment> commentlist = new ArrayList<Comment>();
////		
////		String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_CLASSID + "=?";
////		
//////		Cursor c = db.query(TABLE_NAME, null, new String[]{COLUMN_NAME, COLUMN_PASSWORD}, new String[]{user.getName(), user.getPassword()}, null, null, null);
////		Cursor c = db.rawQuery(selectQuery, new String[]{classid});
////		if(c.moveToFirst()){
////			do{
////				Comment acomment = new Comment(c.getString(0), c.getString(1),c.getString(2),c.getString(3), c.getString(4), c.getString(5));
////				commentlist.add(acomment);
////			}while(c.moveToNext());
////		}
////		
////		return commentlist;
////	}
////
////	public ArrayList<Comment> getCommentByStudentid(String studentid) {
////		// TODO Auto-generated method stub
////		ArrayList<Comment> commentlist = new ArrayList<Comment>();
////		
////		String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_STUDENTID + "=?";
////		
//////		Cursor c = db.query(TABLE_NAME, null, new String[]{COLUMN_NAME, COLUMN_PASSWORD}, new String[]{user.getName(), user.getPassword()}, null, null, null);
////		Cursor c = db.rawQuery(selectQuery, new String[]{studentid});
////		if(c.moveToFirst()){
////			do{
////				Comment acomment = new Comment(c.getString(0), c.getString(1),c.getString(2),c.getString(3), c.getString(4), c.getString(5));
////				commentlist.add(acomment);
////			}while(c.moveToNext());
////		}
////		
////		return commentlist;
////	}
////
////	public ArrayList<Comment> getAllComments() {
////		// TODO Auto-generated method stub
////		return null;
////	}
////}
