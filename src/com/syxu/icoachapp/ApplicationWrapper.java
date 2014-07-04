package com.syxu.icoachapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.syxu.database.Class;
import com.syxu.database.Lecture;
import com.syxu.database.Student;
import com.syxu.databaseDAO.ClassStudentMapping;

public class ApplicationWrapper extends Application {
	public  String coachname;
	public  String coachID;
	public  String schoolID;
	
	public String getCoachID() {
		return coachID;
	}

	public String getSchoolID() {
		return schoolID;
	}

	public  void setCoachID(String coachID) {
		this.coachID = coachID;
	}

	public  void setSchoolID(String schoolID) {
		this.schoolID = schoolID;
	}
	public void setCoachname(String coachname) {
		this.coachname = coachname;
	}
//	public static List<String> lecturelist;
	public static HashMap<String, HashMap<String, Lecture>> classLectureMapOnCoach;
	public static HashMap<String, Class> classOnCoachMap;
//	public static List<Class> classlist;
	public static HashMap<String, Student> studentOnCoachMap;
//	public static List<Student> studentInSchoolList;
	public static HashMap<String, Student> studentInSchoolMap;
	public static HashMap<String, List<String>>classToStudents;
	public static HashMap<String, List<String>>studentToClasses;
	public static ArrayList<String> defaultCmt;

	private JSONParser jsonParser = new JSONParser();
	
	private String getLectureByCoachURL = "http://www.skycssc.com/webservice/getLectureByCoach.php";
	private String getStudentByCoachURL = "http://www.skycssc.com/webservice/getStudentByCoach.php";
	private String getClassesByCoachIDURL = "http://www.skycssc.com/webservice/getClassesByCoachID.php";
	private String getStduentsBySchoolIDDURL = "http://www.skycssc.com/webservice/getStudentsBySchoolID.php";
	private String getClassStudentMapping="http://www.skycssc.com/webservice/getClassStudentMapping.php";
	private String getDefaultCmtByCoachID = "http://www.skycssc.com/webservice/getDefaultCmtByCoachID.php";
//	public static final String PROFILE_DIR = "http://www.skycssc.com/webservice/profilePic/";
	public static final String PROFILE_DIR = "http://www.skycssc.com/webservice/";
	private int SUCCESS = 1;
	private String POST = "posts";
	private String CLASSID = "classid";
	private String LECTURENOTE = "lecturenote";
	public String getCoachname() {
		return coachname;
	}

	 @Override
	    public void onCreate() {
	        super.onCreate();

	        // Create global configuration and initialize ImageLoader with this configuration
//	        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
//	            .build();
//	        ImageLoader.getInstance().init(config);
	        
	        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
	        .cacheInMemory(true)
	        .cacheOnDisk(true)
	        .build();
	        
	        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
			.threadPriority(Thread.NORM_PRIORITY - 2)
			.denyCacheImageMultipleSizesInMemory()
			.defaultDisplayImageOptions(defaultOptions)
			.tasksProcessingOrder(QueueProcessingType.LIFO)
			.build();
			
			ImageLoader.getInstance().init(config);
	    }
	
	//must be called from background asynctask
	public void fetchProfile(){
		List<NameValuePair> postparam = new ArrayList<NameValuePair>();
		postparam.add( new BasicNameValuePair("coachid", coachID));
		postparam.add( new BasicNameValuePair("schoolid", schoolID));
		postparam.add( new BasicNameValuePair("coachname", coachname));//this is unnecessary
        JSONObject json = jsonParser.makeHttpRequest(getLectureByCoachURL, "POST", postparam);
        try {
			if(json.getInt("success")==SUCCESS){
				JSONArray classarray = json.getJSONArray(POST);
//				classlist = new ArrayList<String>();
//				lecturelist = new ArrayList<String>();
				classLectureMapOnCoach = new HashMap<String, HashMap<String, Lecture>>();
				for(int i=0; i<classarray.length(); i++){
					JSONObject js = classarray.getJSONObject(i);
					String lectureID = js.getString("lectureID");
					String lectureNote = js.getString("lectureNote");
					String classID = js.getString("classID");
					String lectureDate = js.getString("lectureDate");
					String dateInput = js.getString("dateInput");
					Lecture alecture = new Lecture(
							lectureID, lectureNote, classID,
							lectureDate, dateInput
							);
					if(!classLectureMapOnCoach.containsKey(classID)){
						classLectureMapOnCoach.put(classID, new HashMap<String, Lecture>());
					}
					classLectureMapOnCoach.get(classID).put(alecture.getLectureDate(), alecture);
//					if(! classlist.contains(classid) ){
//						classlist.add(classid);							
//					}
//					lecturelist.add(classid + ":" +js.getString(LECTURENOTE));
				}
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        JSONObject jsonresponse = jsonParser.makeHttpRequest(getStudentByCoachURL, "POST", postparam);
        try {
			if(jsonresponse.getInt("success")==SUCCESS){
				JSONArray classarray = jsonresponse.getJSONArray(POST);
				studentOnCoachMap = new HashMap<String, Student>();
				for(int i=0; i<classarray.length(); i++){
//					String js = (String) classarray.get(i);
//					studentOnCoachList.add(js); //add student id to this list
					
					JSONObject js = classarray.getJSONObject(i);
					String studentID = js.getString("studentID");
					String studentName = js.getString("studentName");
					String tel = js.getString("tel");
					String profilePic = PROFILE_DIR + js.getString("profilePic");
					String dob = js.getString("dob");
					String gender = js.getString("gender");
					String address = js.getString("address");
					String email = js.getString("email");
					String remarks = js.getString("remarks");
					String joinDate = js.getString("joinDate");
					String schoolid = js.getString("schoolid");
					int active = js.getInt("active");
					
					Student astudent = new Student(
							studentID, studentName, tel,profilePic,
							dob,gender, address, email, remarks, 
							joinDate, schoolid, active
					);
//					studentInSchoolList.add(astudent);
					studentOnCoachMap.put(studentID, astudent);
					
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        //get class full information;
        
        JSONObject jsonresponse2 = jsonParser.makeHttpRequest(
        						getClassesByCoachIDURL, "POST", postparam);
        try {
			if(jsonresponse2.getInt("success")==SUCCESS){
				JSONArray classarray = jsonresponse2.getJSONArray(POST);
//				classlist = new ArrayList<Class>();
				classOnCoachMap = new HashMap<String, Class>();
				for(int i=0; i<classarray.length(); i++){
					JSONObject js = classarray.getJSONObject(i);
					String classid = js.getString("classid");
					String schoolid = js.getString("schoolid");
					String classname = js.getString("classname");
					String startdate = js.getString("startdate");
					String classtime = js.getString("classtime");
					int numofcourse = js.getInt("numofcourse");
					String classdesc = js.getString("classdesc");
					String remarks = js.getString("remarks");
					int fee = js.getInt("fee");
					int active = js.getInt("active");
					String coachid = js.getString("coachid");
					Class theclass = new Class(
							classid, schoolid, coachid , classname,startdate, classtime,
							numofcourse, classdesc, remarks, fee, active
//							
							);
//					classlist.add(theclass);
					classOnCoachMap.put(classid, theclass);

				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
      //get all students in the school;
        JSONObject jsonresponse3 = jsonParser.makeHttpRequest(
        		getStduentsBySchoolIDDURL, "POST", postparam);
        try {
			if(jsonresponse3.getInt("success")==SUCCESS){
				JSONArray classarray = jsonresponse3.getJSONArray(POST);
//				studentInSchoolList = new ArrayList<Student>();
				studentInSchoolMap = new HashMap<String, Student>();
				for(int i=0; i<classarray.length(); i++){
					JSONObject js = classarray.getJSONObject(i);
					String studentID = js.getString("studentID");
					String studentName = js.getString("studentName");
					String tel = js.getString("tel");
					String profilePic = PROFILE_DIR + js.getString("profilePic");
					String dob = js.getString("dob");
					String gender = js.getString("gender");
					String address = js.getString("address");
					String email = js.getString("email");
					String remarks = js.getString("remarks");
					String joinDate = js.getString("joinDate");
					String schoolid = js.getString("schoolid");
					int active = js.getInt("active");
					
					Student astudent = new Student(studentID, studentName, tel,profilePic,
							dob,gender, address, email, remarks, joinDate, schoolid, active);
//					studentInSchoolList.add(astudent);
					studentInSchoolMap.put(studentID, astudent);
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
        //get mapping
        String joinedclassid="";
        for(String classid: classOnCoachMap.keySet()){
        	joinedclassid = joinedclassid + classid+",";
        }
        joinedclassid = joinedclassid.substring(0, joinedclassid.length()-1);
        ClassStudentMapping mapping = new ClassStudentMapping(joinedclassid, schoolID);
        mapping.getMapping();
        classToStudents = mapping.getClassToStudents();
        studentToClasses = mapping.getStudentToClasses();
        
        //get defaultCmt
        JSONObject jsonresponse4 = jsonParser.makeHttpRequest(
				getDefaultCmtByCoachID, "POST", postparam);
		try {
			if(jsonresponse4.getInt("success")==SUCCESS){
				JSONArray jsarray= jsonresponse4.getJSONArray("posts");
				if(jsarray ==null){
					return;
				}
		        defaultCmt = new ArrayList<String>();
				for(int i=0;i<jsarray.length();i++){
					defaultCmt.add(jsarray.getString(i));
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	
	}
	
	public final boolean isInternetOn()
	{
	  ConnectivityManager connec = (ConnectivityManager)
	    getSystemService(Context.CONNECTIVITY_SERVICE);

	  // ARE WE CONNECTED TO THE NET
	  if ( connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED ||
	       connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED )
	  {
	    // MESSAGE TO SCREEN FOR TESTING (IF REQ)
	    //Toast.makeText(this, connectionType + ” connected”, Toast.LENGTH_SHORT).show();
	    return true;
	  }
	  
	  return false;
	}
	
	public void refresh(){
		try {
			new refreshTask().execute().get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private class refreshTask extends AsyncTask<Void,Void,Void>{

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			fetchProfile();
			return null;
		}
		
	}
}
