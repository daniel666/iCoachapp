package com.syxu.icoachapp;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.syxu.database.Comment;
import com.syxu.database.Student;

public class MainActivity extends Activity implements OnClickListener{
	
	
	//request code for taking photo
	final int CameraResult = 2;
	final int PhotoResult = 3;
	final static String PROFILE_URL = "http://www.skycssc.com/sphoto/";
	Student theStudent;
	private Context activitycontext = this;
	private ListView lv;
	ArrayList<Comment> commentlist;
	MyAdapter commentlistAdapter;
//	private CommentDAO commenthandler;
	private Spinner sortspinner;
	ArrayAdapter<String> sortadapter;
	private String[] sortby = {"Date", "Grade", "Lesson"};
	private Button addcommentbutton, backbutton;
	private ImageView studentProfile;
	
	String classid;
	String studentid;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Remove title bar
	    this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		//get comment list
		classid = getIntent().getStringExtra(InputStudentIDActivity.CLASSID);
		studentid = getIntent().getStringExtra(InputStudentIDActivity.STUDENTID);
//		commenthandler = new CommentDAO(this);
//		commenthandler.open();
//		if(classid!=null && studentid != null){
//			commentlist = commenthandler.getCommentByClassidAndStudentid(classid, studentid);
//		}else if(classid !=null && studentid ==null){
//			commentlist = commenthandler.getCommentByClassid(classid);
//		}else if(classid == null && studentid != null){
//			commentlist = commenthandler.getCommentByStudentid(studentid);
//		}else
//			commentlist = commenthandler.getAllComments();
//		commenthandler.close();
		commentlist = new ArrayList<Comment>();
		commentlistAdapter = new MyAdapter(activitycontext,commentlist);
		lv = (ListView) findViewById(R.id.listview);
		lv.setAdapter(commentlistAdapter);
		new GetCommentTask(classid, studentid).execute();

		//populate sort spinner
		sortadapter = new ArrayAdapter<String>(
												this, android.R.layout.simple_spinner_item, sortby);
		sortadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		sortspinner = (Spinner) findViewById(R.id.sortspinner);
		sortspinner.setAdapter(sortadapter);
		sortspinner.setOnItemSelectedListener(new OnItemSelectedListener(){
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
				// TODO Auto-generated method stub
				String sortfield= (String) parent.getItemAtPosition(position);
				if(sortfield.equals(sortby[0])){
					Collections.sort(commentlist, Comment.DateComparator);
				}else if(sortfield.equals(sortby[1])){
					Collections.sort(commentlist, Comment.GradeComparator);
				}else{
					Collections.sort(commentlist, Comment.LessonComparator);
				}
				commentlistAdapter.notifyDataSetChanged();
			}
		
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub
			}
		});
		
		//create comment button
		addcommentbutton = (Button) findViewById(R.id.createcommentbutton);
		backbutton = (Button) findViewById(R.id.backbutton);
		addcommentbutton.setOnClickListener(this);
		backbutton.setOnClickListener(this);
		
		//create personal profile
		if(studentid!=null){
//			new fetchStudentTask().execute();
			studentProfile = (ImageView) findViewById(R.id.account_people_icon);
			theStudent = ApplicationWrapper.studentOnCoachMap.get(studentid);
			String  imageURL =  theStudent.getProfilPic();
			ImageLoader.getInstance().displayImage(imageURL, studentProfile);
			TextView tv1 = (TextView) findViewById(R.id.account_people_name);
			tv1.setText(theStudent.getStudentName());
			TextView tv2 = (TextView) findViewById(R.id.account_ref_name);
			tv2.setText(theStudent.getStudentID());
			TextView tv3 = (TextView) findViewById(R.id.follower_no);
			tv3.setText(theStudent.getAddress());
		}
			
	}	
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.createcommentbutton:
			Intent createcommentintent = new Intent(getApplicationContext(), CreateCommentActivity.class);
			createcommentintent.putExtra("classid",classid);
			createcommentintent.putExtra("studentid", studentid);
			startActivity(createcommentintent);
			break;
		case R.id.backbutton:
			finish();
			break;
		}
	}
	
	private class MyAdapter extends BaseAdapter{
//		final String CMT_PHOTO_URL = "http://www.skycssc.com/webservice/image/";
		final String CMT_PHOTO_URL = "http://www.skycssc.com/webservice/";

		ArrayList<Comment> list;
		private LayoutInflater myInflater;
		
		public MyAdapter(Context c, ArrayList<Comment> list){
			myInflater = LayoutInflater.from(c);
			this.list = list;
		}
		
		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewTag viewTag;
	        
	        if(convertView == null){
	            convertView = myInflater.inflate(R.layout.simple_text, null);
	            
	            viewTag = new ViewTag(
	                    (ImageView)convertView.findViewById(
	                        R.id.people_icon),
	                    (TextView) convertView.findViewById(
	                        R.id.class_lecture_id),
	                    (TextView) convertView.findViewById(
			                R.id.comment),
			            (TextView) convertView.findViewById(
					        R.id.coach_name),
					    (TextView) convertView.findViewById(
					        R.id.lectureNote),
					    (TextView) convertView.findViewById(
							R.id.post_date),
						(TextView) convertView.findViewById(
							R.id.gradeTV)
	                    );
	            
	            convertView.setTag(viewTag);
	        }
	        else{
	            viewTag = (ViewTag) convertView.getTag();
	        }
	        Comment acomment = list.get(position);
	        String url = CMT_PHOTO_URL + acomment.getPhoto();
	        ImageLoader.getInstance().displayImage(url, viewTag.icon);
	        
	        viewTag.class_lecture_id.setText(acomment.getClassID().toUpperCase() +"-lecture "+acomment.getLectureID() );
	        viewTag.comment.setText(acomment.getCmtContent());
	        viewTag.coach_name.setText(acomment.getCoachID());
	        viewTag.lectureNote.setText(acomment.getLectureNote());
	        viewTag.post_date.setText(acomment.getDateInput().toString());
	        viewTag.gradeTV.setText(acomment.getGrade());

			return convertView;
		} 
	}
	
	private class ViewTag{
        ImageView icon;
        TextView class_lecture_id,comment,coach_name,lectureNote,post_date, gradeTV;
    
        public ViewTag(ImageView icon, TextView title, TextView comment,TextView coach_name,TextView lectureNote,TextView post_date, TextView gradeTV){
            this.icon = icon;
            this.class_lecture_id = title;
            this.comment = comment;
            this.coach_name = coach_name;
            this.lectureNote = lectureNote;
            this.post_date = post_date;
            this.gradeTV = gradeTV;
        }
    }

	

	//for demo, no photo in each comment post
//	public class MyListView{
//		
//		ArrayList<Comment> list;
		
//		public MyListView(){
//			ListView lv = (ListView) findViewById(R.id.listView1);
//			list = new ArrayList<Comment>();
//			list.add(new Comment("Lesson 1","Good, Keep it up!","Lee Sin","18:35","11/06/14"));
//			
//			lv.setAdapter(new MyAdapter(activitycontext,list));
//		}
		
//		public MyListView(ArrayList<Comment> alist){
//			ListView lv = (ListView) findViewById(R.id.listView1);
//			this.list = alist;
//			
//			lv.setAdapter(new MyAdapter(activitycontext,list));
//		}
		
//		//data class for a comment to student from coach
//		private class Comment {
//			String title,comment,coach_name,post_time,post_date;
//			
//			public Comment(String title,String comment,String coach_name,String post_time,String post_date){
//				this.title = title;
//				this.comment = comment;
//				this.coach_name = coach_name;
//				this.post_time = post_time;
//				this.post_date = post_date;
//			}
//		}

		
//		private class MyAdapter extends BaseAdapter{
//
//			ArrayList<Comment> list;
//			private LayoutInflater myInflater;
//			
//			public MyAdapter(Context c, ArrayList<Comment> list){
//				myInflater = LayoutInflater.from(c);
//				this.list = list;
//			}
//			
//			@Override
//			public int getCount() {
//				return list.size();
//			}
//
//			@Override
//			public Object getItem(int position) {
//				return list.get(position);
//			}
//
//			@Override
//			public long getItemId(int position) {
//				return position;
//			}
//
//			@Override
//			public View getView(int position, View convertView, ViewGroup parent) {
//
//				ViewTag viewTag;
//		        
//		        if(convertView == null){
//		            convertView = myInflater.inflate(R.layout.simple_text, null);
//		            
//		            viewTag = new ViewTag(
//		                    (ImageView)convertView.findViewById(
//		                        R.id.people_icon),
//		                    (TextView) convertView.findViewById(
//		                        R.id.lesson_title),
//		                    (TextView) convertView.findViewById(
//				                R.id.comment),
//				            (TextView) convertView.findViewById(
//						        R.id.coach_name),
//						    (TextView) convertView.findViewById(
//						        R.id.post_time),
//						    (TextView) convertView.findViewById(
//								R.id.post_date)
//		                    );
//		            
//		            convertView.setTag(viewTag);
//		        }
//		        else{
//		            viewTag = (ViewTag) convertView.getTag();
//		        }
//		        
//		        viewTag.title.setText(list.get(position).title);
//		        viewTag.comment.setText(list.get(position).comment);
//		        viewTag.coach_name.setText(list.get(position).coach_name);
//		        viewTag.post_time.setText(list.get(position).post_time);
//		        viewTag.post_date.setText(list.get(position).post_date);
//				return convertView;
//			} 
//		}
//		
//		private class ViewTag{
//	        ImageView icon;
//	        TextView title,comment,coach_name,post_time,post_date;
//	    
//	        public ViewTag(ImageView icon, TextView title, TextView comment,TextView coach_name,TextView post_time,TextView post_date){
//	            this.icon = icon;
//	            this.title = title;
//	            this.comment = comment;
//	            this.coach_name = coach_name;
//	            this.post_time = post_time;
//	            this.post_date = post_date;
//	        }
//	    }
		
//	}
	
	private class GetCommentTask extends AsyncTask<Void, List<Comment>,  List<Comment>>{
 		String classid;
 		String studentid;
 		
 		final static String getCommentByClassidStudentidURL = "http://www.skycssc.com/webservice/getCommentByClassidStudentid.php";
		public GetCommentTask(String classid, String studentid) {
			super();
			this.classid = classid;
			this.studentid = studentid;
		}
		@Override
		protected  List<Comment> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			List<NameValuePair> postparam = new ArrayList<NameValuePair>();
			postparam.add( new BasicNameValuePair("classID", classid));
			postparam.add( new BasicNameValuePair("studentID", studentid));//this is unnecessary
	        JSONObject json = new JSONParser().makeHttpRequest(getCommentByClassidStudentidURL, "POST", postparam);
	        try {
				if(json.getInt("success")==1){
					JSONArray post_resp = json.getJSONArray("posts");
					
					List<Comment> commentList = new ArrayList<Comment>();
					for(int i=0; i<post_resp.length(); i++){
						JSONObject js = post_resp.getJSONObject(i);
						String classID = js.getString("classID");
						String lectureID = js.getString("lectureID");
						String lectureNote = js.getString("lectureNote");
						String studentID = js.getString("studentID");
						String grade = js.getString("grade");
						String cmtContent = js.getString("cmtContent");
						String photo = js.getString("photo");
						String defaultCmt = js.getString("defaultCmt");
						String coachID = js.getString("coachID");
						String dateInput = js.getString("dateInput");
						
						commentList.add(new Comment(classID, lectureID, lectureNote,
								studentID, grade, cmtContent, photo, defaultCmt,
								coachID, dateInput));
					}
					return commentList;
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
	        return null;
		}
		
		protected void onPostExecute(List<Comment> cmt){
			if(cmt==null)
				return; 
			Collections.sort(cmt, Comment.DateComparator);
			commentlist.addAll(cmt);
			commentlistAdapter.notifyDataSetChanged();
//			commentlistAdapter = new MyAdapter(activitycontext,commentlist);
//			lv.setAdapter(commentlistAdapter);
			
//			commentlistAdapter.notifyDataSetChanged();
//			Intent next = new Intent(getApplicationContext(), MainActivity.class);
		}
 	}
	
//	private class FetchStudentTask extends AsyncTask<Void, Void, Void>{
//		String studentid;
//		private final String getStudentbyID = "http://www.skycssc.com/webservice/getStudentbyID.php";
//		FetchStudentTask(String s){
//			studentid = s;
//		}
//		@Override
//		protected Void doInBackground(Void... paramsholder) {
//			// TODO Auto-generated method stub
//			List<NameValuePair> postparam = new ArrayList<NameValuePair>();
//			postparam.add( new BasicNameValuePair("studentID", studentid));
//	        JSONObject json = new JSONParser().makeHttpRequest(getStudentbyID, "POST", postparam);
//	        try {
//				if(json.getInt("success")==1){
//					JSONArray post_resp = json.getJSONArray("post");
//						JSONObject js = post_resp.getJSONObject(0);
//						String classID = js.getString("classID");
//						String lectureID = js.getString("lectureID");
//						String lectureNote = js.getString("lectureNote");
//						String studentID = js.getString("studentID");
//						String grade = js.getString("grade");
//						String cmtContent = js.getString("cmtContent");
//						String photo = js.getString("photo");
//						String defaultCmt = js.getString("defaultCmt");
//						String coachID = js.getString("coachID");
//						String dateInput = js.getString("dateInput");
//						
//						Student theStudent = new Student
//					}
//					return commentList;
//				}
//				
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			return null;
//		}
		
	
}


