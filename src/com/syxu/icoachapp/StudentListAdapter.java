package com.syxu.icoachapp;

import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.syxu.database.Student;

public class StudentListAdapter extends BaseAdapter{

	List<Student> list;
	Context mContext;
	private LayoutInflater myInflater;
	
	public StudentListAdapter(Context c, List<Student> list){
		mContext = c;
		myInflater = LayoutInflater.from(c);
		this.list = list;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewTag viewTag;
        
        if(convertView == null){
            convertView = myInflater.inflate(R.layout.student_view, null);
            
            viewTag = new ViewTag(
            		(ImageView) convertView.findViewById(
            				R.id.profileIV),
            		(TextView) convertView.findViewById(
            				R.id.idTV),
            	    (TextView) convertView.findViewById(
            				R.id.nameTV),
					(TextView) convertView.findViewById(
            				R.id.ageTV),
            		(TextView) convertView.findViewById(
            				R.id.addrTV)); 
            
            convertView.setTag(viewTag);
        }
        else{
            viewTag = (ViewTag) convertView.getTag();
        }
        
        //set the view now
        Student astudent = list.get(position);
		try {
			 new createDrawable(viewTag).execute(astudent);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        viewTag.idTV.setText(astudent.getStudentID());
        viewTag.nameTV.setText(astudent.getStudentName());
        
        Calendar dob = Calendar.getInstance();
        
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-DD");
        Date dobDate;
		try {
			dobDate = df.parse(astudent.getDob());
	        dob.setTime(dobDate);
	        Calendar today = Calendar.getInstance();
	        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
	        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
	            age--; 
	        }
	        viewTag.ageTV.setText("Age: " + String.valueOf(age));
	        viewTag.addrTV.setText(astudent.getAddress());
			return convertView;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
        
	}
	
	private class ViewTag{
        ImageView profileIV;
        TextView idTV, nameTV, ageTV, addrTV;
        
		public ViewTag(ImageView profileIV, TextView idTV, TextView nameTV,
				TextView ageTV, TextView addrTV) {
			super();
			this.profileIV = profileIV;
			this.idTV = idTV;
			this.nameTV = nameTV;
			this.ageTV = ageTV;
			this.addrTV = addrTV;
		}
    }
	
	private class createDrawable extends AsyncTask<Student, Drawable, Bitmap>{
		private ViewTag viewTag;
		
		createDrawable(ViewTag vt){
			viewTag = vt;
		}
		@Override
		protected Bitmap doInBackground(Student... student) {
			// TODO Auto-generated method stub
			Drawable d = null;
			Bitmap bm = null;
			Student astudent = student[0];
		 	URL url;
			try {
				String urlstr = astudent.getProfilPic();
					url = new URL(urlstr);
			        InputStream content = (InputStream)url.getContent();
			        d = Drawable.createFromStream(content , "src"); 
			        bm = ((BitmapDrawable) d).getBitmap();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				bm = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.profile_default);
				e.printStackTrace();
			}
			return bm;
		}
		
		protected void onPostExecute(Bitmap bm){
			if(bm!=null)
				viewTag.profileIV.setImageBitmap(bm);
		}

	}
}