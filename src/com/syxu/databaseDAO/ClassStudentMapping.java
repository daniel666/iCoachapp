package com.syxu.databaseDAO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.syxu.database.Class;
import com.syxu.icoachapp.ApplicationWrapper;
import com.syxu.icoachapp.JSONParser;

public class ClassStudentMapping {
	private String TAG = "ClassStudentMapping";
	private static String getClassStudentMapping="http://www.skycssc.com/webservice/getClassStudentMapping.php";

	JSONParser jsonParser;
	String schoolID;
	String joinedclassid;
	private HashMap<String, List<String>>classToStudents;
	private HashMap<String, List<String>>studentToClasses;
	
	public ClassStudentMapping(String joinedclassid, String schoolID) {
		jsonParser = new JSONParser();
		this.joinedclassid = joinedclassid;
		this.schoolID = schoolID;
		classToStudents = new HashMap<String, List<String>>();
		studentToClasses = new HashMap<String, List<String>>();
	}

	public HashMap<String, List<String>> getClassToStudents() {
		return classToStudents;
	}

	public void setClassToStudents(
			HashMap<String, List<String>> classToStudents) {
		this.classToStudents = classToStudents;
	}

	public HashMap<String, List<String>> getStudentToClasses() {
		return studentToClasses;
	}

	public void setStudentToClasses(
			HashMap<String, List<String>> studentToClasses) {
		this.studentToClasses = studentToClasses;
	}

	public void getMapping(){
		//Get mapping
        List<NameValuePair> postparam = new ArrayList<NameValuePair>();
		postparam.add( new BasicNameValuePair("schoolid", schoolID));
        postparam.add(new BasicNameValuePair("classIDArray", joinedclassid));
        JSONObject myjason = jsonParser.makeHttpRequest(getClassStudentMapping, "POST", postparam);
        try {
			if(myjason.getInt("success")==1){
				JSONArray mapping = myjason.getJSONArray("posts");
				for(int t=0;t<mapping.length();t++){
					JSONObject jsonobject = mapping.getJSONObject(t);
					
					Iterator<String> itr =(Iterator<String>) jsonobject.keys();
					while(itr.hasNext()){
						String classid = itr.next();
						JSONArray students = jsonobject.getJSONArray(classid);
							for(int i=0;i<students.length();i++){
								String studentid = students.getString(i);
								if(!classToStudents.containsKey(classid)){
									classToStudents.put(classid, new ArrayList<String>());
								}
								classToStudents.get(classid).add(studentid);
								if(!studentToClasses.containsKey(studentid)){
									studentToClasses.put(studentid, new ArrayList<String>());
								}
								studentToClasses.get(studentid).add(classid);
							}
						}
				}
//				for(String classid:studentToClasses.keySet()){
//					if(studentToClasses.get(classid)==null)
//						studentToClasses.remove(classid);
//				}
				
			}else{
				Log.w(TAG, myjason.getString("message"));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
