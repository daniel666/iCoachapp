package com.syxu.icoachapp;


import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.syxu.database.User;

public class LoginActivity extends Activity {
	private EditText username;
	private EditText userpassword;
//	protected UserDAO userdao;
	private ProgressDialog pd;
	
	private static String LOG = "LoginActivity";
	private static final String LOGIN_URL = "http://www.skycssc.com/webservice/login.php";
	private Context mcontext = this;
	JSONParser jsonParser = new JSONParser();
	private String uname;
	private String password;

	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_login);
	    
//	    userdao = new UserDAO(this);
//	    userdao.open();

	    Button b = (Button) findViewById(R.id.button_login);
	    
	    b.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				uname = username.getEditableText().toString();
				password = userpassword.getEditableText().toString();

				User user = new User(uname, password);
				new QueryDBTask().execute(user);
			}   	
	    });
	    
	    username = (EditText) findViewById(R.id.login_username_input);
	    userpassword = (EditText) findViewById(R.id.login_password_input);
//	    userdao.close();
	}
	
	@Override
	protected void onResume() {
//	    userdao.open();
	    super.onResume();
	}
	
	@Override
	protected void onPause() {
//		userdao.close();
	    super.onPause();
	}
	
	private class QueryDBTask extends AsyncTask<User, User, JSONObject>{

		protected void onPreExecute() {
			pd = new ProgressDialog(mcontext);
			pd.setTitle("Log in...");
			pd.setMessage("Please wait.");
			pd.setCancelable(false);
			pd.setIndeterminate(true);
			pd.show();
		}
		
		@Override
		protected JSONObject doInBackground(User... user) {
			// TODO Auto-generated method stub
			
			try {
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				                params.add(new BasicNameValuePair("username", user[0].getName()));
				                params.add(new BasicNameValuePair("password", user[0].getPassword()));
				JSONObject json = jsonParser.makeHttpRequest(LOGIN_URL, "POST", params);
				if(json.getInt("success") == 1){
					ApplicationWrapper appwrapper = (ApplicationWrapper) getApplicationContext();
		        	JSONObject js = json.getJSONObject("posts");
		        	appwrapper.setSchoolID(js.getString("schoolID"));
		        	appwrapper.setCoachID(js.getString("coachID"));
//					appwrapper.setCoachname(uname);
		        	appwrapper.setCoachname(js.getString("coachName"));
		        	appwrapper.fetchProfile();
				}
				return json;

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("Exceptionrrr");
				Log.e(LOG, "Eception");
			}
			return null;
		}
		
        protected void onPostExecute(JSONObject json) { 
        	if (pd!=null) {
				pd.dismiss();
			}
        	try {
				if(json.getInt("success") == 0){
					Toast toast = Toast.makeText(getApplicationContext(), json.getString("message"),  Toast.LENGTH_LONG );
					toast.show();
					Log.e("LoginActivity", "User  is not found!");
					return;
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
			Intent goToNextActivity = new Intent(getApplicationContext(),InputStudentIDActivity.class);
			startActivity(goToNextActivity);
			finish();
        }	
	}
	
}
