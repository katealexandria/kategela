package com.malabon.pos;

import com.malabon.object.Sync;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class Functions extends Activity {

	String username;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_functions);
		
		SharedPreferences prefs = this.getSharedPreferences(
				"com.malabon.pos", Context.MODE_PRIVATE);
		username = prefs.getString("username", "");
	}
	
	public void cashInOut(View view) {
		Intent intent = new Intent(this, CashInOut.class);
        startActivity(intent);
    }
	
	public void sync(View view) {
		Sync.DoSync(true, username);
        finish();
    }
	
	public void cancel(View view) {
        finish();
    }
	
	public void closeDay(View view) {
		Intent intent = new Intent(this, CloseDay.class);
        startActivity(intent);
    }
}
