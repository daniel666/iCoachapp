package com.syxu.icoachapp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;


public class InputStudentIDActivity extends Activity implements OnClickListener,OnItemSelectedListener {
	private List<String>  classlist;
	private List<String>  orgclasslist;
	ArrayAdapter<String> cidadapter;
	private List<String> studentlist;
	private List<String>  orgstudentlist;
	ArrayAdapter<String> sidadapter;
	private Spinner classspinner;
	private Spinner studentspinner;
	private String selectedclassid;
	private String selectedstudentid;
	private Button gobutton, classlistbutton, studentlistbutton, scanQRbutton;
	Context mContext = this;
	int check=-1;
	boolean changeable=false;
	boolean change=true;
	
	public static String CLASSID = "classid";
	public static String STUDENTID = "studentid";
//	private class QueryDBTask extends AsyncTask<Void, Void, Void >{
//
//		@Override
//		protected Void doInBackground(Void... params) {
//			// TODO Auto-generated method stub
//			ClassDAO classhandler = new ClassDAO(mcontext);
//			List<Class>  classes = classhandler.getAllClasss();
//			classlist = new List<String>();
//			for(Class aclass:classlist){
//				
//			}
//			return null;
//		}
//		
//	}
	
	
	
	protected void onCreate(Bundle savedInstanceState)  {
		Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
	        @Override
	        public void uncaughtException(Thread paramThread, Throwable paramThrowable) {
	            paramThrowable.printStackTrace();
	        }
	    });
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_inputstudentid);

//	    QueryDBTask qdbtask = new QueryDBTask();
//		qdbtask.execute();
		
	    gobutton = (Button) findViewById(R.id.gobutton);
	    gobutton.setOnClickListener(this);
	    classlistbutton = (Button) findViewById(R.id.classlistbutton);
	    classlistbutton.setOnClickListener(this);
	    studentlistbutton = (Button) findViewById(R.id.studentlistbutton);
	    studentlistbutton.setOnClickListener(this);
	    
	    Set<String> tmpset = ApplicationWrapper.classOnCoachMap.keySet();
	    if(tmpset!=null){
		    classlist = new ArrayList<String>(tmpset );
	    }
	    Collections.sort(classlist);
	    orgclasslist = new ArrayList<String>(classlist);
		cidadapter = new ArrayAdapter<String>
							(this, android.R.layout.simple_spinner_item, classlist);
		cidadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		classspinner = (Spinner) findViewById(R.id.classIDspinner);
		classspinner.setAdapter(cidadapter);
		
		studentlist = new ArrayList<String>(ApplicationWrapper.studentOnCoachMap.keySet());
		Collections.sort(studentlist);
		orgstudentlist = new ArrayList<String>(studentlist);
		sidadapter = new ArrayAdapter<String>(this, 
				android.R.layout.simple_spinner_item, studentlist);
		sidadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		studentspinner = (Spinner) findViewById(R.id.studentIDspinner);
		studentspinner.setAdapter(sidadapter);
		//set listener
		classspinner.setOnItemSelectedListener(this);
		studentspinner.setOnItemSelectedListener(this);
		//select first classid
		classspinner.setSelection(0);
		scanQRbutton = (Button) findViewById(R.id.scanQRbutton);
		scanQRbutton.setOnClickListener(this);
		
	}
	
	public void onItemSelected(AdapterView<?> parent, View view,
			int position, long id) {
		// TODO Auto-generated method stub
		switch(parent.getId()){
		case R.id.classIDspinner:
			selectedclassid = (String) parent.getItemAtPosition(position);
			List<String> studentList_2 = ApplicationWrapper.classToStudents.
											get(selectedclassid);
			studentlist.clear();
			if(studentList_2==null){
				studentList_2 = new ArrayList<String>();
			}else{
				Collections.sort(studentlist);
				studentlist.addAll(studentList_2);
			}
		    sidadapter.notifyDataSetChanged();
			break;
			
		case R.id.studentIDspinner:
			break;
		}
	}
	

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent nextactivity;
		switch(v.getId()){
		case R.id.gobutton: 
			nextactivity = new Intent(getApplicationContext(), MainActivity.class); 
			selectedstudentid = studentspinner.getSelectedItem().toString();
			selectedclassid = classspinner.getSelectedItem().toString();
			nextactivity.putExtra(CLASSID, selectedclassid);
			nextactivity.putExtra(STUDENTID, selectedstudentid);
			startActivity(nextactivity);
			break;
		case R.id.classlistbutton: 
			nextactivity = new Intent(getApplicationContext(), ClassListActivity.class);
			startActivity(nextactivity);
			break;
		case R.id.studentlistbutton: 
			nextactivity = new Intent(getApplicationContext(), StudentListActivity.class);
			startActivity(nextactivity);
			break;
		case R.id.scanQRbutton:
			Intent intent = new Intent("com.google.zxing.client.android.SCAN");
		    intent.putExtra("SCAN_MODE", "QR_CODE_MODE"); // 
		    startActivityForResult(intent, 0);
		}
	}
	
	 	@Override
	    protected void onActivityResult(int requestCode, int resultCode, Intent data) {           
	 		super.onActivityResult(requestCode, resultCode, data);
	 		if (requestCode == 0) {
	 			if (resultCode == RESULT_OK) {

	 				String selectedstudentid = data.getStringExtra("SCAN_RESULT");
//	 				check++;
	 				int pos = sidadapter.getPosition(selectedstudentid);
	 				if(pos<0){
	 					String msg = "The StudentID \"" +selectedstudentid+ "\"is not in class" +"\"" + classspinner.getSelectedItem().toString() +"\"";
	 					Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
	 					return;
	 				}
	 				Intent nextactivity = new Intent(getApplicationContext(), MainActivity.class); 
	 				selectedclassid = classspinner.getSelectedItem().toString();
	 				nextactivity.putExtra(CLASSID, selectedclassid);
	 				nextactivity.putExtra(STUDENTID, selectedstudentid);
	 				startActivity(nextactivity);
	 			}
	 		}
	    }
	 	
	 	
	
	
	
	
}
