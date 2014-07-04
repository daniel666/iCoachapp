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
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.syxu.database.Comment;
import com.syxu.database.Lecture;
//import com.syxu.databaseDAO.CommentDAO;
//import com.syxu.databaseDAO.LectureDAO;

//import com.syxu.databaseDAO.StudentDAO;

public class CreateCommentActivity extends Activity implements OnClickListener {
	private TextView lecturetimeTV, classtv;
	private AutoCompleteTextView lessonautotv, studentautotv ;
	private Spinner gradespinner;
	private Spinner defaultCmtSpinner;
	private EditText commentet;
//	private LessonDAO lessonhandler;
//	private StudentDAO studenthandler;
	private Button  cancelbutton, postbutton;
	private ImageButton addpicbutton;
	protected Context mContext = this;
	private static String TAG = "CreateCommentActivity";
	private int REQUEST_CAMERA = 0;
	private int CHOOSE_GALLERY = 1;
	private String mCurrentPhotoPath;
	String aggrlessonid;
	String classID;
	String lectureID;
	String lectureNote;
	String studentID;
	String selectedgrade;
	String cmtContent;
	String photo;
	boolean createlecture;
	private String today;
	ApplicationWrapper ap;
//	private List<String> lecturelist = ApplicationWrapper.lecturelist;
	static String[] gradelist = { "A", "A-", "B+","B","B-","C+","C", "C-", "D+", "D", "D-", "E", "F", "U"};
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_createcomment);
	    
	    ap = (ApplicationWrapper) getApplicationContext();

	    lecturetimeTV = (TextView) findViewById(R.id.lecturetimeTV);
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    today = sdf.format(new Date());
	    lecturetimeTV.setText(today);
	    
	    classID = getIntent().getStringExtra("classid");
	    studentID = getIntent().getStringExtra("studentid");
	    classtv = (TextView) findViewById(R.id.classtv);
	    classtv.setText(classID);
	    
	    lessonautotv = (AutoCompleteTextView) findViewById(R.id.lessontv);
	    HashMap<String, Lecture> map =  ApplicationWrapper.classLectureMapOnCoach.get(classID);
	    if (map == null){
	    	createlecture = true;
	    }else{
	    	Lecture thelecture = map.get(today);
		    if(thelecture != null){
		    	lectureID = thelecture.getLectureid();
		    	lectureNote = thelecture.getLectureNote();
		    	lessonautotv.setText(thelecture.getLectureNote());
		    	lessonautotv.setEnabled(false);
		    	createlecture = false;
		    }else{
		    	createlecture = true;
		    }
	    }
	    
	    
        studentautotv = (AutoCompleteTextView) findViewById(R.id.studentautotv);
        studentautotv.setText(studentID);
        studentautotv.setEnabled(false);

        //populate grade spinner
        gradespinner = (Spinner) findViewById(R.id.gradespinner);
        ArrayAdapter<String> gradeadapter = new ArrayAdapter<String>
				(this, android.R.layout.simple_spinner_item, gradelist);
		gradeadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		gradespinner.setAdapter(gradeadapter);
		
		//comment edittext
		 commentet = (EditText) findViewById(R.id.commentet);
		 defaultCmtSpinner  = (Spinner) findViewById(R.id.defaultCmtSpinner);
			if(ApplicationWrapper.defaultCmt!=null){
				defaultCmtSpinner.setVisibility(View.VISIBLE);
				ArrayAdapter<String> cmtAdapter = new ArrayAdapter<String>(this, 
						android.R.layout.simple_spinner_dropdown_item, ApplicationWrapper.defaultCmt);
				cmtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				defaultCmtSpinner.setAdapter(cmtAdapter);
				defaultCmtSpinner.setOnItemSelectedListener(new OnItemSelectedListener(){
					@Override
					public void onItemSelected(AdapterView<?> parent, View view,
							int position, long id) {
						// TODO Auto-generated method stub
						commentet.setText(defaultCmtSpinner.getSelectedItem().toString());
					}
					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						// TODO Auto-generated method stub
					}
				});
			}
			
		//implement button functuns
		 addpicbutton = (ImageButton) findViewById(R.id.addpicbutton);
		 cancelbutton = (Button) findViewById(R.id.cancelbutton);
		 postbutton = (Button) findViewById(R.id.postbutton);
		 addpicbutton.setOnClickListener(this);
		 cancelbutton.setOnClickListener(this);
		 postbutton.setOnClickListener(this);
	}
	
	
	private void startNext(){
		Intent setIntent = new Intent(mContext, MainActivity.class);
		setIntent.putExtra(InputStudentIDActivity.CLASSID, classID );
		setIntent.putExtra(InputStudentIDActivity.STUDENTID, studentID);
		setIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(setIntent);
	}
	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()){
		case R.id.addpicbutton:
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
		case R.id.cancelbutton:
		    finish();
		    startActivity(getIntent());
			break;
		case R.id.postbutton:
//			aggrlessonid = lessonautotv.getText().toString();
//			StringTokenizer st = new StringTokenizer(aggrlessonid, ":");
//			classID = st.nextToken();
			lectureNote = lessonautotv.isEnabled()? lessonautotv.getText().toString():lectureNote;
//			studentID = studentID;
			selectedgrade = gradespinner.getSelectedItem().toString();
			cmtContent = commentet.getText().toString();
			photo = mCurrentPhotoPath;
			String coachID= ((ApplicationWrapper)getApplicationContext()).getCoachID();
			
			Comment newcomment = new Comment(
					classID,
					"", //need an lectureID;
					lectureNote,
					studentID,
					selectedgrade,
					cmtContent,
					photo,
					"",
					coachID,
					""
					);
			
//			if(createlecture == true){
//				if(new LectureDAO(mContext).addLecture(new Lecture("", lectureNote, classID, today, "")) == false)
//						return;
//			}
			
//			boolean result = new CommentDAO(mContext).addComment(newcomment);
//			if(result==true){
//					File cacheDir = mContext.getCacheDir();
//					File tmpFile = new File(mCurrentPhotoPath);
//					if(tmpFile.getAbsolutePath().startsWith(cacheDir.getAbsolutePath())){
//						tmpFile.delete();
//					}
//					ap.refresh();
//					startNext();
//			}/
			
			Lecture newlecture = createlecture==true?new Lecture("", lectureNote, classID, today, ""):null;
			new addCommentLectureTask(newcomment, newlecture).execute();
			break;
		}
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
		        		getPath(selectedImageUri, mContext);
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
	    int targetW = addpicbutton.getWidth();
	    int targetH = addpicbutton.getHeight();

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
	    addpicbutton.setImageBitmap(bitmap);

	    
	    
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
	
	
	private class addCommentLectureTask extends AsyncTask<Void,Void,Boolean>{
//		private static final String uploadImageUrl = "http://www.skycssc.com/webservice/uploadImage.php";
		private static final String ADDCOMMENT_URL ="http://www.skycssc.com/webservice/addComment.php";
		private static final String  ADDLECTURE_URL = "http://www.skycssc.com/webservice/addLecture.php";

		private final ProgressDialog dialog = new ProgressDialog(mContext);
		private Comment cmt;
		private Lecture lecture;
		private JSONParser js;
		
		
		public addCommentLectureTask(Comment cmt, Lecture lecture) {
			super();
			this.cmt = cmt;
			this.lecture = lecture;
			js = new JSONParser();
		}

		@Override
		protected void onPreExecute(){
			
			dialog.setMessage("Adding comment..");
			dialog.setIndeterminate(true);
			dialog.show();
			return ;
		}
		
		@Override
		protected Boolean doInBackground(Void... placeholder) {
			// TODO Auto-generated method stub
			boolean result1=true;
			boolean result2 = true;
			
			if(lecture != null){
				result1 = addLecture();
			}
			if(cmt !=null){
				result2 = addComment();
			}
			return result1 && result2;
		}
		
		protected void onPostExecute(Boolean result){
			if(result == true){
				File cacheDir = mContext.getCacheDir();
				if(mCurrentPhotoPath!=null){
					File tmpFile = new File(mCurrentPhotoPath);
					if(tmpFile.getAbsolutePath().startsWith(cacheDir.getAbsolutePath())){
						tmpFile.delete();
					}	
				}
				ap.refresh();
				if (dialog.isShowing()) {
			        dialog.dismiss();
			     }
				startNext();
			}
		}
		
		boolean addLecture(){
			List<NameValuePair> postparam = new ArrayList<NameValuePair>();
			postparam.add( new BasicNameValuePair("lectureID", lecture.getLectureid()));
			postparam.add( new BasicNameValuePair("lectureNote", lecture.getLectureNote()));
			postparam.add( new BasicNameValuePair("classID", lecture.getClassID()));			
			postparam.add( new BasicNameValuePair("lectureDate", lecture.getLectureDate()));			
	        final JSONObject jsonObject = js.makeHttpRequest(ADDLECTURE_URL, "POST", postparam);
            
	        try {
				if(jsonObject.getInt("success") == 0){
					((Activity) mContext).runOnUiThread(new Runnable() {
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
					return true;
				}

	        }catch(JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return false;
		}
		
		boolean addComment(){
			HttpPost httpPostRequest = new HttpPost(ADDCOMMENT_URL);
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			builder.setCharset(Charset.forName("UTF-8"));
			builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			
			String path = cmt.getPhoto();
			if(path!=null && path!=""){
				FileBody fileBody = new FileBody(new File(cmt.getPhoto()));
				builder.addPart("uploaded_file", fileBody);
			}
			builder.addTextBody("classID", cmt.getClassID(), ContentType.create("text/plain", Charset.forName(HTTP.UTF_8)));
			builder.addTextBody("lectureNote", cmt.getLectureNote(), ContentType.create("text/plain", Charset.forName(HTTP.UTF_8)));
			builder.addTextBody("studentID", cmt.getStudentID(), ContentType.create("text/plain", Charset.forName(HTTP.UTF_8)));
			builder.addTextBody("grade", cmt.getGrade(), ContentType.create("text/plain", Charset.forName(HTTP.UTF_8)));			
			builder.addTextBody("cmtContent", cmt.getCmtContent(), ContentType.create("text/plain", Charset.forName(HTTP.UTF_8)));
			builder.addTextBody("coachID", cmt.getCoachID(), ContentType.create("text/plain", Charset.forName(HTTP.UTF_8)));
			builder.addTextBody("schoolID", ap.getSchoolID(), ContentType.create("text/plain", Charset.forName(HTTP.UTF_8)));

			httpPostRequest.setEntity(builder.build());
			
			final JSONObject jsonObject = js.makeHttpRequest(httpPostRequest);
            try {
				if(jsonObject.getInt("success") == 0){
					((Activity) mContext).runOnUiThread(new Runnable() {
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
					return true;
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return false;

		}

	}
	
	
	
}
