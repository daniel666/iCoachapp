package com.syxu.icoachapp;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.syxu.database.Class;

public class ClassListActivity extends Activity {
	public static final String TAG = "ClassListActivity";
	private ListView lv;
	private Button newclass;
	private Button backbutton;
	private Context mcontext = this;
	private List<Class> classlist;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_classlist);
	    newclass = (Button) findViewById(R.id.newstudentbutton);
	    newclass.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent classcreateIntent = new Intent( mcontext, ClassCreateActivity.class );
				classcreateIntent.putExtra(ClassCreateActivity.ACT_CODE, ClassCreateActivity.CLASSCREATE);
				startActivity(classcreateIntent);
			}
	    	
	    });
//	    classlist = ApplicationWrapper.classlist;
	    classlist = new ArrayList<Class>(ApplicationWrapper.classOnCoachMap.values());
	    lv = (ListView) findViewById(R.id.studentlistview);
		lv.setAdapter(new ClassListAdapter(mcontext,classlist));
		lv.setOnItemClickListener(new OnItemClickListener() {
		      public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {
		    	  Class selectedFromList =(Class) (lv.getItemAtPosition(myItemInt));
		    	  Intent classeditIntent = new Intent(mcontext, ClassCreateActivity.class);
		    	  classeditIntent.putExtra(ClassCreateActivity.ACT_CODE, ClassCreateActivity.CLASSEDIT);
		    	  classeditIntent.putExtra(ClassCreateActivity.PASS_CLASS, selectedFromList);
		    	  startActivity(classeditIntent);
		      }                 
		});
		
		 backbutton = (Button) findViewById(R.id.backbutton);
		 backbutton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		 });

	}
	
	
	
}
