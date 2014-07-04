package com.syxu.icoachapp;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.syxu.database.Class;
import com.syxu.databaseDAO.ClassDAO;

public class ClassCreateActivity extends Activity implements OnClickListener{
	private EditText classidET, classnameET, descET;
	private TextView idTV, calenderTV, timeTV, studentNumTV, progress;
	private CheckedTextView pendingCB;
	private Button addclassbutton, addstudentbutton;
	private SeekBar lecturenumSB;
	private int active=1;
	boolean newStudents = false;
	List<String> joinedSIDList; //already joined student ID's
	ArrayList<String> schoolIDList ;
	ArrayList<String> newstudentIDList ;  //new student added returned from floating ACTivity
	ApplicationWrapper ap;
	Context mContext = this;
	int actioncode; //determine whether this activity is created by CLASSCREATE or CLASSEDIT intent
	int numStudents=0;
	private final Calendar c = Calendar.getInstance();
	private String TAG = "ClassCreateActivity";
	boolean need_refrash = false;
	public static String ACT_CODE = "ACT";
	public static final int CLASSCREATE = 1;
	public static final int CLASSEDIT = 0 ;
	public static String PASS_CLASS = "class";
	
	void setup(Intent intent){
		addstudentbutton.setEnabled(true);
		addclassbutton.setText("Save");
		idTV.setText("Save Class");
    	Class theclass = (Class) intent.getSerializableExtra(PASS_CLASS);
    	String classid = theclass.getClassID();
    	String classname = theclass.getClassName();
    	String startdate = theclass.getStartDate();
    	String classtime = theclass.getClassTime();
    	int numofcourse = theclass.getNumOfCourse();
    	String desc = theclass.getClassDesc();
    	int active = theclass.getActive();
    	classidET.setText(classid);
    	classnameET.setText(classname);
    	calenderTV.setText(startdate);
    	timeTV.setText(classtime);
    	lecturenumSB.setProgress(numofcourse);
//    	progress.setText(numofcourse);
    	progress.setText(String.valueOf(numofcourse));
    	descET.setText(desc);
    	if(active==0)
    		pendingCB.setChecked(true);
    	if(ApplicationWrapper.classToStudents.get(classid)!=null)
    		numStudents =ApplicationWrapper.classToStudents.get(classid).size();
    	studentNumTV.setText(String.valueOf(numStudents));
    	
    	joinedSIDList=ApplicationWrapper.classToStudents.get(classid);
	}
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_classcreate);
	    ap = (ApplicationWrapper) getApplicationContext();
		
	    classidET = (EditText) findViewById(R.id.classidET);
	    classnameET = (EditText) findViewById(R.id.classnameET);
	    descET = (EditText) findViewById(R.id.descET);
	    
	    idTV = (TextView) findViewById(R.id.idTV);
	    calenderTV = (TextView) findViewById(R.id.calenderTV);
	    calenderTV.setOnClickListener(this);
	    timeTV = (TextView) findViewById(R.id.timeTV);
	    timeTV.setOnClickListener(this);
	    studentNumTV = (TextView) findViewById(R.id.studentNumTV);
	    
	    pendingCB = (CheckedTextView) findViewById(R.id.pendingCB);
	    pendingCB.setOnClickListener(this);
	    
	    
	    lecturenumSB = (SeekBar) findViewById(R.id.lecturenumSB);
	    lecturenumSB.incrementProgressBy(10);
	    lecturenumSB.setProgress(0);
	    lecturenumSB.setMax(20);

	    progress = (TextView) findViewById(R.id.progress);
	    lecturenumSB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){ 
	    	   @Override 
	    	   public void onProgressChanged(SeekBar seekBar, int progressval, 
	    	     boolean fromUser) { 
	    	    // TODO Auto-generated method stub 
	    		   progress.setText(String.valueOf(progressval)); 
	    	   } 
	    	   @Override 
	    	   public void onStartTrackingTouch(SeekBar seekBar) { 
	    	    // TODO Auto-generated method stub 
	    	   } 
	    	   @Override 
	    	   public void onStopTrackingTouch(SeekBar seekBar) { 
	    	    // TODO Auto-generated method stub 
	    	   } 
	    }); 
	    
	    addstudentbutton = (Button) findViewById(R.id.addstudentbutton);
	    addstudentbutton.setEnabled(false);
	    addstudentbutton.setOnClickListener(this);
	    addclassbutton = (Button) findViewById(R.id.addclassbutton);
	    Intent intent = getIntent();
	    actioncode = intent.getIntExtra(ACT_CODE, CLASSCREATE);
	    if(actioncode==CLASSEDIT)
	    	setup(intent); //set it to be an edit class screen with selected class info
	    addclassbutton.setOnClickListener(this);
	    
	   
//	    addclassbutton.setOnClickListener(this);
	    
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.calenderTV:
			// Launch Date Picker Dialog
	        DatePickerDialog dpd = new DatePickerDialog(this,
	                new DatePickerDialog.OnDateSetListener() {

	                    @Override
	                    public void onDateSet(DatePicker view, int year,
	                            int monthOfYear, int dayOfMonth) {
	                        // Display Selected date in textbox
	                    	String date = year + "-"+ String.format("%02d", monthOfYear+1) +  "-" + String.format("%02d", dayOfMonth) ;
	                    	calenderTV.setText(date);

	                    }
	                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
	        dpd.show();
			break;
		case R.id.timeTV:
			int mHour = c.get(Calendar.HOUR_OF_DAY);
	        int mMinute = c.get(Calendar.MINUTE);
	
	        // Launch Time Picker Dialog
	        TimePickerDialog tpd = new TimePickerDialog(this,
	                new TimePickerDialog.OnTimeSetListener() {
	                    @Override
	                    public void onTimeSet(TimePicker view, int hourOfDay,
	                            int minute) {
	                    	String time = String.format("%02d", hourOfDay) +  ":" + String.format("%02d", minute) ;
	                    	timeTV.setText(time);
	                    }
	                }, mHour, mMinute, true);
	        tpd.show();
			
			break;
			
		case R.id.addclassbutton:
			String classid = classidET.getText().toString();
			String coachid = ap.getCoachID();
			String schoolid = ap.getSchoolID();
			String classname = classnameET.getText().toString();
			String startdate = calenderTV.getText().toString();
			String time = timeTV.getText().toString();
			int numOfLecture = Integer.parseInt( progress.getText().toString());
			String desc = descET.getText().toString();
			Class newclass = new Class(
					classid, coachid, schoolid, classname,
					startdate, time, numOfLecture,
					desc, "", 0, active
					);
			switch (actioncode){
		    case CLASSCREATE:
//		    	new ClassDAO(mContext).createClass(newclass, actioncode);
		    	new CreateClassTask(newclass).execute();
		    	
		    	break;
		    case CLASSEDIT:
				if(newStudents == true){
					new AddStudentTask().execute();
				}
//				new ClassDAO(mContext).createClass(newclass, actioncode);
				new CreateClassTask(newclass).execute();
		    	break;
			}
			
			break;
			
		case R.id.pendingCB:
			active = (active+1)%2;
			((CheckedTextView) v).toggle();
			break;
			
		case R.id.addstudentbutton:
			Intent addStudentIntent = new Intent(mContext, AddStudentToClassActivity.class);
			
			addStudentIntent.putStringArrayListExtra("joinedSIDList", (ArrayList<String>) joinedSIDList);
			startActivityForResult(addStudentIntent, 0);
			break;
		}
	}
	
	public void onBackPressed() {
		   Log.d("CDA", "onBackPressed Called");
		   if(need_refrash)
			   ap.refresh();
		   startNext();
	}
	
	public void startNext(){
		Intent setIntent = new Intent(mContext, ClassListActivity.class);
		setIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(setIntent);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

	    if (requestCode == 0) {
	        if(resultCode == RESULT_OK){
//	            String result=data.getStringExtra("result");
	        	schoolIDList = new ArrayList<String>() ;
	    		newstudentIDList = new ArrayList<String>();
	    		
//	    		ArrayList<String> tmp = data.getStringArrayListExtra("schoolid");
//	        	schoolIDList.addAll(tmp);
	        	
	        	ArrayList<String> tmp2 = data.getStringArrayListExtra("studentid");
	        	newstudentIDList.addAll(tmp2);
	        	int newnumStudents = newstudentIDList.size();
	        	if(newnumStudents > 0){
	        		newStudents = true;
	        		numStudents = newnumStudents + numStudents;
	        		studentNumTV.setText(String.valueOf(numStudents));
	        	}

	        }
	        if (resultCode == RESULT_CANCELED) {
	            //Write your code if there's no result
	        }
	    }
	}
	
	private class CreateClassTask extends AsyncTask<Void,Void,Boolean>{
		private static final String ADDCLASS_URL = "http://www.skycssc.com/webservice/addClass.php";
		private static final String UPDATECLASS_URL = "http://www.skycssc.com/webservice/updateClass.php";
		private final ProgressDialog dialog = new ProgressDialog(mContext);
		Class theclass;
//		int actioncode;
		CreateClassTask(Class theclass){
			this.theclass = theclass;
//			actioncode = this.actioncode;
		}
		
		@Override
		protected void onPreExecute(){
			if(actioncode==CLASSCREATE)
				dialog.setMessage("Adding Class..");
			else
				dialog.setMessage("Saving Class");
			dialog.setIndeterminate(true);
			dialog.show();
			return ;
		}
		
		@Override
		protected Boolean doInBackground(Void... paramsholder) {
			// TODO Auto-generated method stub
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("classid", theclass.getClassID()));
	        params.add(new BasicNameValuePair("coachid", theclass.getCoachID()));
	        params.add(new BasicNameValuePair("schoolid", theclass.getSchoolID()));
	        params.add(new BasicNameValuePair("classname", theclass.getClassName()));
	        params.add(new BasicNameValuePair("startdate", theclass.getStartDate()));
	        params.add(new BasicNameValuePair("classtime", theclass.getClassTime()));
	        params.add(new BasicNameValuePair("numofcourse",String.valueOf( theclass.getNumOfCourse())));
	        params.add(new BasicNameValuePair("classdesc", theclass.getClassDesc()));
	        params.add(new BasicNameValuePair("remarks", theclass.getRemarks()));
	        params.add(new BasicNameValuePair("fee", String.valueOf(theclass.getFee())));
	        params.add(new BasicNameValuePair("active", String.valueOf(theclass.getActive())));
	        
	        JSONObject jsonObject=null;
	        
	        if(actioncode == CLASSCREATE){
	        	jsonObject  = new JSONParser().makeHttpRequest(ADDCLASS_URL, "POST", params);
	        }
	        else{
	        	jsonObject  = new JSONParser().makeHttpRequest(UPDATECLASS_URL, "POST", params);

	        }
            try {
				if(jsonObject.getInt("success") == 0){
					final String message = jsonObject.getString("debug");
					((Activity)mContext).runOnUiThread(new Runnable() {
				        public void run() {
				            Toast.makeText(mContext, message,Toast.LENGTH_SHORT).show();
				        }
				    }); 
					return false;
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
		}
		
		protected void onPostExecute(Boolean result){
			if (dialog.isShowing()) {
		        dialog.dismiss();
		     }
			if(result==true){
				need_refrash = true;
				if(actioncode == CLASSCREATE){
					Toast.makeText(mContext, "Class successfully created", Toast.LENGTH_LONG).show();
					addstudentbutton.setEnabled(true);
					addclassbutton.setText("Save");
					actioncode = CLASSEDIT;
				}else{
					Toast.makeText(mContext, "Class successfully Saved", Toast.LENGTH_LONG).show();
					ap.refresh();
					startNext();
//					((Activity)mContext).finish();
				}
			}
		}
	}
	
	private class AddStudentTask extends AsyncTask<Void, Boolean,Boolean>{
		private static final String ADD_STUDENT_TO_CLASS_URL = "http://www.skycssc.com/webservice/addStudentToClass.php";
		@Override
		protected Boolean doInBackground(Void... tmp) {
			// TODO Auto-generated method stub
			Boolean success = true;
			String classid = classidET.getText().toString();
			JSONParser jp = new JSONParser();
			for(int i=0; i<newstudentIDList.size(); i++){
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("classID", classid));
		        params.add(new BasicNameValuePair("studentID", newstudentIDList.get(i)));
		        params.add(new BasicNameValuePair("schoolID", ap.getSchoolID()));
		        final JSONObject jsonObject = jp.makeHttpRequest(ADD_STUDENT_TO_CLASS_URL, "POST", params);
		        try {
					if(jsonObject.getInt("success") == 0){
						success = false;
						runOnUiThread(new Runnable() {
					        public void run() {
					        	String message=null;
								try {
									message = jsonObject.getString("message");
						            Log.e(TAG, jsonObject.getString("debug"));

								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
					            Toast.makeText(mContext, message,Toast.LENGTH_SHORT).show();
					        }
					    }); 
					}else{
						need_refrash = true;
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return success;
		}
		
	}
	
}
