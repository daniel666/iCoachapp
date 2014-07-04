package com.syxu.icoachapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.syxu.database.User;
import com.syxu.databaseDAO.UserDAO;
import com.syxu.databasehelper.DatabaseHelper;

public class RegisterActivity extends Activity {
	private Button buttonregister;
	private EditText username;
	private EditText userpassword;
	private UserDAO userdao;	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_register);
	    
	    userdao = new UserDAO(this);
	    userdao.open();
	    
	    Button buttonregister = (Button) findViewById(R.id.button_register);
	    buttonregister.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String uname = username.getEditableText().toString();
				String psword = userpassword.getEditableText().toString();
				
				userdao.addUser(new User(uname, psword, User.STUDENT));
//				userdao.walk();
				finish();			
			}
	    	
	    });
	    
	    username = (EditText) findViewById(R.id.register_username_input);
	    userpassword = (EditText) findViewById(R.id.register_password_input);
	    userdao.close();
	}
	
	@Override
	protected void onResume() {
	    userdao.open();
	    super.onResume();
	}
	
	@Override
	protected void onPause() {
		userdao.close();
	    super.onPause();
	}
}
