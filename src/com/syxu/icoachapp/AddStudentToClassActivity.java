package com.syxu.icoachapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import com.syxu.database.Student;

public class AddStudentToClassActivity extends Activity {
	static String TAG = "AddStudentToClassActivity";
	private Spinner spinner;
	private Button addbutton, okbutton;
	private ListView lv;
	private HashMap<String, Student> studentMap = ApplicationWrapper.studentInSchoolMap;
	private List<String> studentIDList;
	private ArrayAdapter<String> studentIDAdapter;

	private StudentListAdapter myadapter;
	private List<Student> oldStudents;

	
	private List<String> oldJoinedStudentList;
	private List<String> addedStudentList  ;

	private Context mContext = this;
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//	    requestWindowFeature(Window.FEATURE_NO_TITLE);
		WindowManager.LayoutParams params = getWindow().getAttributes();  
		params.x = -20;  
		params.height = 100;  
		params.width = 550;  
		params.y = -20;  
		this.getWindow().setAttributes(params); 
	    setContentView(R.layout.activity_addstudenttoactivity);

		
		
//		studentMap = new HashMap<String, Student>();
//		List<Student> studentInSchoolList = new ArrayList<Student>( 
//							ApplicationWrapper.studentInSchoolMap.values());
//		for(Student stu: studentInSchoolList){
//			studentMap.put(stu.getStudentID(), stu);
//		}
		
		//current list is empty but will get added later on
	    addedStudentList  = new ArrayList<String>(); //holder to record what studentid is added
//	    myadapter = new StudentListAdapter(mContext, addedStudentList);
//	    lv.setAdapter(myadapter);
	    lv = (ListView) findViewById(R.id.studentLV);
	    oldJoinedStudentList = getIntent().getStringArrayListExtra("joinedSIDList");
	    if(oldJoinedStudentList == null){
	    	oldJoinedStudentList = new ArrayList<String>();
	    }
	    oldStudents = new ArrayList<Student>();
	    for(String sid: oldJoinedStudentList){
	    	oldStudents.add(ApplicationWrapper.studentOnCoachMap.get(sid));
	    }
	    myadapter = new StudentListAdapter(mContext, oldStudents);
	    lv.setAdapter(myadapter);
	    
	    
	    spinner = (Spinner) findViewById(R.id.studentSpiner);
	    studentIDList = new ArrayList<String>(studentMap.keySet());
	    for(String id: oldJoinedStudentList){
	    	studentIDList.remove(id);
	    }
	    studentIDAdapter = new ArrayAdapter<String>
		(this, android.R.layout.simple_spinner_item, studentIDList);
	    studentIDAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		spinner.setAdapter(studentIDAdapter);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener(){
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
				// TODO Auto-generated method stub
//				String selectedStudentID = (String) parent.getItemAtPosition(position);
			}
		
		     @Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
			}
		});
		
	    addbutton = (Button) findViewById(R.id.add_button);
	    addbutton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(spinner.getSelectedItem()!=null){
					String studentid = spinner.getSelectedItem().toString();
					Student stu = studentMap.get(studentid);
					addedStudentList.add(studentid);
					oldStudents.add(stu);
					myadapter.notifyDataSetChanged();
					studentIDList.remove(studentid);
					studentIDAdapter.notifyDataSetChanged();
				}
			}
	    });
	    
	    okbutton = (Button) findViewById(R.id.okbutton);
	    okbutton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent returnIntent = new Intent();
				
				returnIntent.putStringArrayListExtra("studentid", (ArrayList<String>) addedStudentList);
//				returnIntent.putStringArrayListExtra("schoolid", returnSchoolIDList);
				setResult(RESULT_OK,returnIntent);
				finish();
			}
	    });
	    
	    
	    

	}
}
