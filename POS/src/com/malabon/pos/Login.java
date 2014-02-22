package com.malabon.pos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.malabon.database.CustomerDB;
import com.malabon.database.DBAdapter;
import com.malabon.database.UserDB;
import com.malabon.object.User;

public class Login extends Activity {

	UserDB userDB;
	EditText txtUsername, txtPassword;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		Initialize();
		
		TempData();
	}
	
	public void TempData(){
		//userDB.tempAdd();
		
		//CustomerDB cutomerDB = new CustomerDB(this);
		//cutomerDB.open();
		//cutomerDB.tempAdd();
	}

	private void Initialize() {
		DBAdapter db = new DBAdapter(this).open();
		userDB = new UserDB(this);
		userDB.open();

		txtUsername = (EditText) findViewById(R.id.txtUsername);
		txtPassword = (EditText) findViewById(R.id.txtPassword);
	}

	public void validateLogin(View view) {
		String user_name = txtUsername.getText().toString();
		String user_password = txtPassword.getText().toString();

		if (user_name.length() > 0 && user_password.length() > 0) {
			User u = userDB.validateLogin(user_name, user_password);

			if (u != null) {
				Intent intent = new Intent(this, MainActivity.class);
				startActivity(intent);
			} else
				showToast("Incorrect username and/or password");
		} else
			showToast("Complete all required fields.");
	}

	private void showToast(String message) {
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT)
				.show();
	}
}
