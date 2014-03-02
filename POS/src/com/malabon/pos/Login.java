package com.malabon.pos;

import java.io.Serializable;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.malabon.database.CustomerDB;
import com.malabon.database.DBAdapter;
import com.malabon.database.UserDB;
import com.malabon.object.Sync;
import com.malabon.object.User;

public class Login extends Activity {

	UserDB userDB;
	EditText txtUsername, txtPassword;
	String called_from = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		called_from = getIntent().getStringExtra("called");
		if (called_from == "cashauth") {
			super.onCreate(savedInstanceState);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.activity_login);
		} else {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_login);
		}

		Initialize();

		// TempData();
	}

	public void TempData() {
		// userDB.tempAdd();

		// CustomerDB cutomerDB = new CustomerDB(this);
		// cutomerDB.open();
		// cutomerDB.tempAdd();
	}

	private void Initialize() {
		// DBAdapter db = new DBAdapter(this).open();
		// userDB = new UserDB(this);
		// userDB.open();

		txtUsername = (EditText) findViewById(R.id.txtUsername);
		txtPassword = (EditText) findViewById(R.id.txtPassword);

		SharedPreferences prefs = this.getSharedPreferences("com.malabon.pos",
				Context.MODE_PRIVATE);
		String currentUsername = prefs.getString("CurrentUser", null);
		Boolean lockRegister = prefs.getBoolean("lockRegister", false);

		txtUsername.setText(currentUsername);
		txtUsername.setEnabled(!lockRegister);

		if (lockRegister)
			txtPassword.requestFocus();
		else
			txtUsername.requestFocus();
	}

	public void validateLogin(View view) {
		String user_name = txtUsername.getText().toString();
		String user_password = txtPassword.getText().toString();

		if (called_from == "cashauth") {
			validateAdmin(user_name, user_password);
		} else {
			if (user_name.length() > 0 && user_password.length() > 0) {
				// User u = userDB.validateLogin(user_name, user_password);

				// if (u != null) {
				Intent resultIntent = new Intent();
				SharedPreferences prefs = this.getSharedPreferences(
						"com.malabon.pos", Context.MODE_PRIVATE);
				prefs.edit().putString("CurrentUser", user_name).commit();
				setResult(Activity.RESULT_OK, resultIntent);
				finish();
				// } else
				// showToast("Incorrect username and/or password");
			} else
				showToast("Complete all required fields.");
		}
	}
	
	private void validateAdmin(String user_name, String user_password){
		//Boolean isadmin = userDB.validateAdmin(user_name, user_password);
		//if (isadmin){
			finish();
		//}
		//else{
		//	showToast("You have incorrect administrative username and/or password. " +
		//			"You may press the BACK button on your device to cancel.");
		//}
	}

	private void showToast(String message) {
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT)
				.show();
	}
}
