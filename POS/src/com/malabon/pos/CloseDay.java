package com.malabon.pos;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class CloseDay extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_close_day);
	}

	public void cancel(View view) {
		finish();
	}

	public void endDay(View view) {
		finish();
	}
}
