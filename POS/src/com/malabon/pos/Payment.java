package com.malabon.pos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class Payment extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_payment);
	}
	
	public void enterCash(View view) {
		Intent intent = new Intent(this, AddPayment.class);
        startActivity(intent);
    }
	
	public void exactCash(View view) {
        finish();
    }
	
	public void cancel(View view) {
        finish();
    }
	
	public void confirm(View view) {
        finish();
    }
}
