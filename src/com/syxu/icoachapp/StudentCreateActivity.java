package com.syxu.icoachapp;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.syxu.database.Class;
import com.syxu.database.Student;

public class StudentCreateActivity extends Activity implements OnClickListener{

	protected static final String ACT_CODE = "student";
	protected static final int STUDENT_CREATE = 0;
	protected static final int STUDENT_EDIT = 1;
	public static final String PASS_STUDENT = "stduent";
	static final String TAG = "StudentCreateActivity";
	final Calendar c = Calendar.getInstance();

	Context mContext = this;
	int actioncode;
	ApplicationWrapper ap;
	Button addStudentButton;
	EditText studentIDInput, nameInput, addressInput;
	TextView dobInput;
	ImageView photoInput;
	
	Spinner classspinner;
	ImageButton addClassButton;
	List<String> classidList;
	ArrayAdapter<String> classidAdapter;
	List<String> netAddedClassIdList; //
	List<String> classjoinedIDList;
	List<Class> classjoinedList;
	ClassListAdapter classjoinedAdapter;
	ListView listview;
	private HashMap<String, Class> classHashMap= ApplicationWrapper.classOnCoachMap;
	
	private String mCurrentPhotoPath; //used to store decoded photo path
	private int REQUEST_CAMERA = 0;
	private int CHOOSE_GALLERY = 1;
	private boolean need_refresh = false;
	
	String studentid ;
	String studentname ;
	String dob ;
	String address ;
	
	void setup(Intent intent){ //setup editstudent mode layout
		addStudentButton.setText("Save");
		
    	Student selectedStudent = (Student) intent.getSerializableExtra(PASS_STUDENT);
    	studentid = selectedStudent.getStudentID();
    	studentname = selectedStudent.getStudentName();
    	dob = selectedStudent.getDob();
    	address = selectedStudent.getAddress();
    	String profilPic = selectedStudent.getProfilPic();
    	
    	studentIDInput.setText(studentid);
    	nameInput.setText(studentname);
    	dobInput.setText(dob);
    	addressInput.setText(address);
    	ImageLoader.getInstance().displayImage(profilPic, photoInput);
//    	new GetClassByStudentTask().execute();
    	
	}
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_studentcreate);
		ap = (ApplicationWrapper) getApplicationContext();
		Intent intent = getIntent();
		actioncode = intent.getIntExtra(ACT_CODE, STUDENT_CREATE);
		addStudentButton = (Button)findViewById(R.id.addStudentButton);
		addStudentButton.setOnClickListener(this);
		
		studentIDInput = (EditText) findViewById(R.id.studentIDInput);
		nameInput = (EditText) findViewById(R.id.nameInput);
		addressInput = (EditText) findViewById(R.id.addressInput);

		dobInput = (TextView) findViewById(R.id.dobInput);
		dobInput.setOnClickListener(this);
		
		photoInput = (ImageView) findViewById(R.id.photoInput);
		photoInput.setOnClickListener(this);
		
		classspinner = (Spinner) findViewById(R.id.classspinner);
		classidList = new ArrayList<String>(ApplicationWrapper.classOnCoachMap.keySet());
		classidAdapter =  new ArrayAdapter<String>
		(this, android.R.layout.simple_spinner_item, classidList);
		classidAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		classspinner.setAdapter(classidAdapter);
		addClassButton = (ImageButton) findViewById(R.id.addClassToStudent);
		addClassButton.setOnClickListener(this);
		if(actioncode == STUDENT_CREATE)
			addClassButton.setEnabled(false);
		else
			setup(intent);
		
		//listview Get the classes the to be saved student has already joined
		listview = (ListView) findViewById(R.id.listview);
		classjoinedIDList = ApplicationWrapper.studentToClasses.get(studentid);
		classjoinedList = new ArrayList<Class>();
		if(classjoinedIDList!=null){
			for(String id:classjoinedIDList){
				classjoinedList.add(ApplicationWrapper.classOnCoachMap.get(id));
				if(classidList.contains(id)){
					classidList.remove(id);
				}
				classidAdapter.notifyDataSetChanged();
			}
		}
		classjoinedAdapter = new ClassListAdapter(mContext, classjoinedList);
		listview.setAdapter(classjoinedAdapter);
		
		//the list containing classes that are being newly added by user
		netAddedClassIdList = new ArrayList<String>();
		
	}
	

	protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) { 
		super.onActivityResult(requestCode, resultCode, imageReturnedIntent); 
		switch(requestCode) {
		case 0:
		    if(resultCode == RESULT_OK){ 
		    	setPic();
		    }
		    break; 
		case 1:
		    if(resultCode == RESULT_OK){  
		        Uri selectedImageUri = imageReturnedIntent.getData();
		        Bitmap bitmap = null;
		        //if from google+, pull it to local
		        if (selectedImageUri.toString().startsWith("content://com.google.android.apps.photos.content")){
			           	InputStream is = null;
		               	try {
			               	File photoFile = null;
				            BufferedOutputStream bos;
				            BufferedInputStream bis;
				            is = getContentResolver().openInputStream(selectedImageUri);
							bis = new BufferedInputStream(is);
							photoFile = createImageFile();
							bos = new BufferedOutputStream(new FileOutputStream(photoFile, false));
				            byte[] buf = new byte[1024];
				            bis.read(buf);
				            do {
				                bos.write(buf);
				            } while(bis.read(buf) != -1);
				            if (bis != null) bis.close();
				            if (bos != null) bos.close();
						}catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					    setPic();
		        }
		        //else local file
		        else{
		        		String localimagepath = getPath(selectedImageUri, mContext);
		        		setPic();
		        }
		    }
		break;
		}
	}
	
	private String getPath(Uri selectedImageUri, Context context) {
		// TODO Auto-generated method stub
		String[] projection = { MediaColumns.DATA };
		ContentResolver re = context.getContentResolver();
		Cursor cursor = re.query(selectedImageUri, projection, null, null, null);
		int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
		cursor.moveToFirst();
		mCurrentPhotoPath =  cursor.getString(column_index);
		return mCurrentPhotoPath;
		
	}

	private File createImageFile() throws IOException {
	    // Create an image file name
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    String imageFileName = "IMG_" + timeStamp +"_" ;
	    File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
	    if(storageDir.exists() == false){
//	    	storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
	    	storageDir.mkdir();
	    }
	    File image = File.createTempFile(
	        imageFileName,  /* prefix */
	        ".jpg",         /* suffix */
	        storageDir      /* directory */
	    );

	    // Save a file: path for use with ACTION_VIEW intents
	    mCurrentPhotoPath = image.getAbsolutePath();
	    return image;
	}
	
	private void setPic() {
		if(mCurrentPhotoPath == null || mCurrentPhotoPath==""){
	    	return;
	    }
		
	    // Get the dimensions of the View
	    int targetW = photoInput.getWidth();
	    int targetH = photoInput.getHeight();

	    // Get the dimensions of the bitmap
	    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
	    bmOptions.inJustDecodeBounds = true;
	    BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
	    int photoW = bmOptions.outWidth;
	    int photoH = bmOptions.outHeight;

	    // Determine how much to scale down the image
	    int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

	    // Decode the image file into a Bitmap sized to fill the View
	    bmOptions.inJustDecodeBounds = false;
	    bmOptions.inSampleSize = scaleFactor;
	    bmOptions.inPurgeable = true;

	    Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
	    photoInput.setImageBitmap(bitmap);
	    
	    
	    //save the scaled down bitmap to cached dir
	    // Determine how much to scale down the image
		    int length = Math.max(photoH, photoW);
		    if (length<1000)
		    	return ;  //not scale it
		    int scaleFactor2 = length/1000;

		    // Decode the image file into a Bitmap sized to fill the View
		    bmOptions.inJustDecodeBounds = false;
		    bmOptions.inSampleSize = scaleFactor2;
		    bmOptions.inPurgeable = true;
		    Bitmap bitmapToSave = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

		    
		    FileOutputStream out;
		    File cacheDir = mContext.getCacheDir();
		    File cacheFile=null;
			try {
				int pos = mCurrentPhotoPath.lastIndexOf('/');
				int pos2 = mCurrentPhotoPath.lastIndexOf('.');
				pos = pos>=0? pos:0;
				pos2= pos>=0? pos2:mCurrentPhotoPath.length()+1;
				String name = mCurrentPhotoPath.substring(pos,pos2);//include the '.'
				cacheFile = File.createTempFile(name, ".jpg", cacheDir);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			if(cacheFile == null){
				return;
			}
		    try {
				out = new FileOutputStream(cacheFile.getAbsolutePath());
				bitmapToSave.compress(Bitmap.CompressFormat.JPEG, 100, out);
				mCurrentPhotoPath = cacheFile.getAbsolutePath();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.addStudentButton:
			studentid = studentIDInput.getEditableText().toString();
			studentname = nameInput.getEditableText().toString();
			dob = dobInput.getText().toString();
			address = addressInput.getEditableText().toString();
			new AddStudentTask().execute();
			if(netAddedClassIdList.size()!=0)
				new AddClassStudentTask().execute();
			break;
		case R.id.dobInput:
			DatePickerDialog dpd = new DatePickerDialog(this,
	                new DatePickerDialog.OnDateSetListener() {
	                    @Override
	                    public void onDateSet(DatePicker view, int year,
	                            int monthOfYear, int dayOfMonth) {
	                    	String date = year + "-"+ String.format("%02d", monthOfYear+1) +  "-" + String.format("%02d", dayOfMonth) ;
	                    	dobInput.setText(date);
	                    }
	                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
	        dpd.show();
			break;
		case R.id.photoInput:
			final CharSequence[] items = { "Take Photo", "Choose from Gallery",
			"Cancel" };

			AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
			builder.setTitle("Add Picture!");
			builder.setItems(items, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int item) {
					if (items[item].equals("Take Photo")) {
						File photoFile = null;
						try {
							photoFile = createImageFile();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if(photoFile != null){
							Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
							takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
							startActivityForResult(takePictureIntent, REQUEST_CAMERA);
		
						}
					} else if (items[item].equals("Choose from Gallery")) {
						Intent intent = new Intent(Intent.ACTION_PICK);
						intent.setType("image/*");
						startActivityForResult(
								Intent.createChooser(intent, "Choose from Gallery"),
								CHOOSE_GALLERY);
					} else if (items[item].equals("Cancel")) {
						dialog.dismiss();
					}
				}
			});
			builder.create().show();
			break;
		case R.id.addClassToStudent:
			if(classspinner.getSelectedItem()!=null){
				String selectedclassid = classspinner.getSelectedItem().toString();
				Class selectedclass = classHashMap.get(selectedclassid);
				classjoinedList.add(selectedclass);
				classjoinedAdapter.notifyDataSetChanged();
				classidList.remove(selectedclassid);
				classidAdapter.notifyDataSetChanged();
				netAddedClassIdList.add(selectedclassid);
			}
			break;
		}
	}
	
	
//	private class GetClassByStudentTask extends AsyncTask<Void,Boolean,Boolean>{
//		private static final String GetClassByStudentTaskURL = "http://www.skycssc.com/webservice/getClassesByStudentID.php";
//		@Override
//		protected Boolean doInBackground(Void... paramsholder) {
//			// TODO Auto-generated method stub
//			List<NameValuePair> params = new ArrayList<NameValuePair>();
//			
//            params.add(new BasicNameValuePair("studentID", studentid));
//            params.add(new BasicNameValuePair("schoolID", ap.getSchoolID()));
//            JSONObject jsonObject = new JSONParser().makeHttpRequest(GetClassByStudentTaskURL, "POST", params);
//            try {
//				if(jsonObject.getInt("success") != 1){
//					Log.w(TAG, jsonObject.getString("debug"));
//					return false;
//				}else{
//					JSONArray js = jsonObject.getJSONArray("posts");
//					for(int i=0; i<js.length(); i++){
//						JSONObject jo = js.getJSONObject(i);
//						String classID = jo.getString("classid");
//						String schoolID = jo.getString("schoolid");
//						String className = jo.getString("classname");
//						String startDate = jo.getString("startdate");
//						String classTime = jo.getString("classtime");
//						int numOfCourse = jo.getInt("numofcourse");
//						String classDesc = jo.getString("classdesc");
//						String remarks =jo.getString("remarks");
//						int fee = jo.getInt("fee");
//						int active = jo.getInt("active");
//						String coachID = jo.getString("coachid");
//						
//						Class aJoinedClass = new Class(
//								classID, schoolID, className, startDate,
//								classTime, numOfCourse, classDesc, remarks, 
//								fee, active
////								, coachID
//								);
//						classjoinedList.add(aJoinedClass);
//					}
//				}
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//            
//            return true;
//		}
//		
//		protected void onPostExecute(Boolean result){
//			if(result==true){
//				classjoinedAdapter = new ClassListAdapter(mContext, classjoinedList);
//				listview.setAdapter(classjoinedAdapter);
//			}
//		}
//		
//	}
	
	private class AddClassStudentTask extends AsyncTask<Void,Boolean,Boolean>{
		private static final String ADD_STUDENT_TO_CLASS_URL = "http://www.skycssc.com/webservice/addStudentToClass.php";

		@Override
		protected Boolean doInBackground(Void... paramsholder) {
			// TODO Auto-generated method stub
			JSONParser jp =  new JSONParser();
			for(int i=0;i<netAddedClassIdList.size();i++){
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("classID", netAddedClassIdList.get(i)));
				params.add(new BasicNameValuePair("studentID", studentid));
	            params.add(new BasicNameValuePair("schoolID", ap.getSchoolID()));
		        final JSONObject jsonObject = jp.makeHttpRequest(ADD_STUDENT_TO_CLASS_URL, "POST", params);
		        try {
					if(jsonObject.getInt("success") == 0){
						runOnUiThread(new Runnable() {
					        public void run() {
					        	String message=null;
								try {
									message = jsonObject.getString("message");
						            Log.w(TAG, jsonObject.getString("debug"));

								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
					            Toast.makeText(mContext, message,Toast.LENGTH_SHORT).show();
					        }
					    }); 
					}else{
						need_refresh = true;
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return null;
		}
		
	}
	
	public void onBackPressed() {
		   Log.d("CDA", "onBackPressed Called");
		   if(need_refresh){
			   ap.refresh();
			   startNext(); 
		   }else{
			   super.onBackPressed();
		   }
			   
	}
	
	private void startNext(){
		Intent setIntent = new Intent(mContext, StudentListActivity.class);
		setIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(setIntent);
	}
	
	private class AddStudentTask extends AsyncTask<Void,Boolean,Boolean>{
//		private static final String uploadProfilePicUrl = "http://www.skycssc.com/webservice/uploadProfilePic.php";
		private static final String addStudentURL = "http://www.skycssc.com/webservice/addStudent.php";
		private static final String updateStudentURL = "http://www.skycssc.com/webservice/updateStudent.php";
		private final ProgressDialog dialog = new ProgressDialog(mContext);
//		int actioncode;
		
		@Override
		protected void onPreExecute(){
			if(actioncode==STUDENT_CREATE)
				dialog.setMessage("Adding Student..");
			else
				dialog.setMessage("Saving Student");
			dialog.setIndeterminate(true);
			dialog.show();
			return ;
		}
		
		@Override
		protected Boolean doInBackground(Void... paramsholder) {
			// TODO Auto-generated method stub
			HttpPost httpPostRequest;
			if(actioncode==STUDENT_CREATE){
				httpPostRequest = new HttpPost(addStudentURL);
			}
			else{
				httpPostRequest = new HttpPost(updateStudentURL);
			}
			
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			builder.setCharset(Charset.forName("UTF-8"));
			builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			
			
			if(mCurrentPhotoPath!=null && mCurrentPhotoPath!=""){
				FileBody fileBody = new FileBody(new File(mCurrentPhotoPath));
				builder.addPart("uploaded_file", fileBody);
			}
			builder.addTextBody("studentID", studentid, ContentType.create("text/plain", Charset.forName(HTTP.UTF_8)));
			builder.addTextBody("schoolID", ap.getSchoolID(), ContentType.create("text/plain", Charset.forName(HTTP.UTF_8)));
			builder.addTextBody("studentName", studentname, ContentType.create("text/plain", Charset.forName(HTTP.UTF_8)));
			builder.addTextBody("tel", "", ContentType.create("text/plain", Charset.forName(HTTP.UTF_8)));			
			builder.addTextBody("dob", dob, ContentType.create("text/plain", Charset.forName(HTTP.UTF_8)));
			builder.addTextBody("gender", "", ContentType.create("text/plain", Charset.forName(HTTP.UTF_8)));
			builder.addTextBody("address", address, ContentType.create("text/plain", Charset.forName(HTTP.UTF_8)));
			builder.addTextBody("email", "", ContentType.create("text/plain", Charset.forName(HTTP.UTF_8)));
			builder.addTextBody("remarks", "", ContentType.create("text/plain", Charset.forName(HTTP.UTF_8)));
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
            String today = sf.format(new Date());
            builder.addTextBody("joinDate", today, ContentType.create("text/plain", Charset.forName(HTTP.UTF_8)));
			builder.addTextBody("active", String.valueOf(1), ContentType.create("text/plain", Charset.forName(HTTP.UTF_8)));
			builder.addTextBody("profilePic", "", ContentType.create("text/plain", Charset.forName(HTTP.UTF_8)));

			httpPostRequest.setEntity(builder.build());
			
            JSONObject jsonObject = new JSONParser().makeHttpRequest(httpPostRequest);
			
            try {
				if(jsonObject.getInt("success") == 0){
					Log.w(TAG, jsonObject.getString("debug"));
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
			
			if(result == true){
				if(actioncode == STUDENT_CREATE){
					actioncode = STUDENT_EDIT;
					addStudentButton.setText("Save");
					addClassButton.setEnabled(true);
					classjoinedList = new ArrayList<Class>();
					classjoinedAdapter = new ClassListAdapter(mContext, classjoinedList);
					listview.setAdapter(classjoinedAdapter);
					studentIDInput.setEnabled(false);//lock the studentID 
					Toast.makeText(mContext, "Student Succesfully Created", Toast.LENGTH_SHORT).show();
					need_refresh = true;
				}else{
					Toast.makeText(mContext, "Student Successfully Saved", Toast.LENGTH_SHORT).show();
//					finish();
					ap.refresh();
					startNext();
				}
				
			}
		}
		

	}
	

}
