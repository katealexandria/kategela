package com.malabon.pos;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.malabon.function.UserFunc;
import com.malabon.object.Sync;

public class Login extends Activity {

	EditText txtUsername, txtPassword;
	String called_from = null;
	String currentUsername;
	Boolean logout = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		called_from = getIntent().getStringExtra("called");

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);

		Initialize();
	}

	private void Initialize() {
		txtUsername = (EditText) findViewById(R.id.txtUsername);
		txtPassword = (EditText) findViewById(R.id.txtPassword);

		SharedPreferences prefs = this.getSharedPreferences("com.malabon.pos",
				Context.MODE_PRIVATE);
		currentUsername = prefs.getString("CurrentUser", null);
		Boolean lockRegister = prefs.getBoolean("lockRegister", false);

		txtUsername.setText(currentUsername);
		txtUsername.setEnabled(!lockRegister);

		if (lockRegister)
			txtPassword.requestFocus();
		else
		{ // face capture on log-out
			logout = true;
			dispatchTakePictureIntent();
		}
	}

	public void validateLogin(View view) {
		String user_name = txtUsername.getText().toString();
		String user_password = txtPassword.getText().toString();

		if (user_name.length() > 0 && user_password.length() > 0) {
			if (called_from != null) {
				if (called_from.equals("cashauth")) {
					validateAdmin(user_name, user_password);
				}
			} else {
				if (new UserFunc().isValidUser(this, user_name, user_password))
					dispatchTakePictureIntent();
				else
					showToast("Incorrect username and/or password");
			}
		} else
			showToast("Complete all required fields.");
	}

	private void validateAdmin(String user_name, String user_password) {
		if (new UserFunc().isAdmin(this, user_name, user_password))
		{
			Intent resultIntent = new Intent();
			setResult(Activity.RESULT_OK, resultIntent);
			finish();
		}
		else
			showToast("Incorrect username and/or password");
	}

	private void showToast(String message) {
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT)
				.show();
	}

	// -----FACE CAPTURE-----//

	static final int REQUEST_TAKE_PHOTO = 1;
	String mCurrentPhotoPath;

	@SuppressLint("SimpleDateFormat")
	private void saveImageFile(Bitmap bmp) throws IOException {
		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());
		String imageFileName = "LoginUserMug_" + timeStamp + "_.jpg";

		FileOutputStream out = openFileOutput(imageFileName,
				Context.MODE_PRIVATE);
		bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
		out.flush();
		out.close();

		mCurrentPhotoPath = getFileStreamPath(imageFileName).getAbsolutePath();
		// TODO: Save image path to DB.
	}

	private void saveTimein(Bitmap bmp) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		bmp.compress(Bitmap.CompressFormat.PNG, 100, bos);
		byte[] img = bos.toByteArray();

		new UserFunc().addTimein(this, Sync.user.user_id, img);
	}

	private void dispatchTakePictureIntent() {
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
			Bundle extras = data.getExtras();
			Sync.CurrentUserBitmap = (Bitmap) extras.get("data");
			try {
				saveImageFile(Sync.CurrentUserBitmap);
				if(logout){
					//TODO: saveTimeOut
					logout = false;
				}
				else{
					saveTimein(Sync.CurrentUserBitmap);
					String user_name = txtUsername.getText().toString();
					Intent resultIntent = new Intent();
					SharedPreferences prefs = this.getSharedPreferences(
							"com.malabon.pos", Context.MODE_PRIVATE);
					prefs.edit().putString("CurrentUser", user_name).commit();
					setResult(Activity.RESULT_OK, resultIntent);
					finish();
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void onBackPressed() {
	    showToast("Please log-in.");
	}
}
