package com.syxu.icoachapp;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import com.syxu.database.Student;

public class StudentListActivity extends Activity {
	Button newstudentButton;
	Context mcontext = this;
	List<Student> studentlist;
	ListView lv;
	Button gobackbutton;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_studentlist);
	    newstudentButton = (Button) findViewById(R.id.newstudentbutton);
	    newstudentButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent studentCreateIntent = new Intent( mcontext, StudentCreateActivity.class );
				studentCreateIntent.putExtra(StudentCreateActivity.ACT_CODE, StudentCreateActivity.STUDENT_CREATE);
				startActivity(studentCreateIntent);
			}
	    	
	    });
	    studentlist = new ArrayList<Student>( ApplicationWrapper.studentOnCoachMap.values());
	    lv = (ListView) findViewById(R.id.studentlistview);
		lv.setAdapter(new StudentListAdapter(mcontext,studentlist));
		lv.setOnItemClickListener(new OnItemClickListener() {
		      public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {
		    	  Student selectedStudent =(Student) (lv.getItemAtPosition(myItemInt));
		    	  Intent editStudentIntent = new Intent(mcontext, StudentCreateActivity.class);
		    	  editStudentIntent.putExtra(StudentCreateActivity.ACT_CODE, StudentCreateActivity.STUDENT_EDIT);
		    	  editStudentIntent.putExtra(StudentCreateActivity.PASS_STUDENT, selectedStudent);
		    	  startActivity(editStudentIntent);
		      }                 
		});
		
		gobackbutton = (Button) findViewById(R.id.backbutton);
		gobackbutton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
	
	
	
}
