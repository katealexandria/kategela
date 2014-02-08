package com.malabon.pos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class Functions extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_functions);
	}
	
	public void cancel(View view) {
        finish();
    }
	
	public void closeDay(View view) {
		Intent intent = new Intent(this, CloseDay.class);
        startActivity(intent);
    }
}
