package com.malabon.pos;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class CashInOut extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_cash_in_out);
	}
	
	public void close(View view) {
        finish();
    }
	
	public void cashOut(View view) {
        finish();
    }
	
	public void cashIn(View view) {
        finish();
    }

}
