package com.malabon.pos;

import android.app.Activity;
import android.content.Intent;
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
		cashInOutAuthorization();
    }
	
	public void cashIn(View view) {
		cashInOutAuthorization();
    }

	private void cashInOutAuthorization(){
		Intent intent = new Intent(this, Login.class);
		intent.putExtra("called", "cashauth");
		this.startActivity(intent);
	}
}
