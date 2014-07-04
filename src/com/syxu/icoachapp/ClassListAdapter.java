package com.syxu.icoachapp;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.syxu.database.Class;



public class ClassListAdapter extends BaseAdapter{

	List<Class> list;
	private LayoutInflater myInflater;
	
	public ClassListAdapter(Context c, List<Class> list){
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
            convertView = myInflater.inflate(R.layout.class_view, null);
            
            viewTag = new ViewTag(
                    (TextView) convertView.findViewById(
                        R.id.classidTV),
                    (TextView) convertView.findViewById(
		                R.id.classdescTV),
		            (TextView) convertView.findViewById(
				        R.id.classnameTV),
				    (TextView) convertView.findViewById(
				        R.id.startdateTV),
				    (ImageButton) convertView.findViewById(
						R.id.expandbutton)
                    );
            
            convertView.setTag(viewTag);
        }
        else{
            viewTag = (ViewTag) convertView.getTag();
        }
        Class aclass = list.get(position);
        viewTag.classidTV.setText(aclass.getClassID()  );
        viewTag.classdescTV.setText(aclass.getClassDesc());
        viewTag.classnameTV.setText(aclass.getClassName());
        viewTag.startdateTV.setText(aclass.getStartDate());
//        viewTag.expandbutton.setOnClickListener(this);
        viewTag.expandbutton.setFocusable(false);
		return convertView;
	}
	
	private class ViewTag{
	    TextView classidTV,classdescTV,classnameTV,startdateTV;
	    ImageButton expandbutton;

	    
		public ViewTag(TextView classidTV,TextView classdescTV, 
				TextView classnameTV, TextView starttimeTV, ImageButton expandbutton) {
			super();
			this.expandbutton = expandbutton;
			this.classidTV = classidTV;
			this.classdescTV = classdescTV;
			this.classnameTV = classnameTV;
			this.startdateTV = starttimeTV;
		}
	}

}

