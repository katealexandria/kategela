package com.malabon.pos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.malabon.database.LogCashDB;

public class CashInOut extends Activity {

	EditText txtCash, txtCashDescription;
	static final int REQUEST_CASH_IN = 98;
	static final int REQUEST_CASH_OUT = 98;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_cash_in_out);

		txtCash = (EditText) findViewById(R.id.txtCashInOut);
		txtCashDescription = (EditText) findViewById(R.id.txtCashDescription);
	}

	public void close(View view) {
		finish();
	}

	public void cashOut(View view) {
		cashInOutAuthorization(REQUEST_CASH_OUT);
	}

	public void cashIn(View view) {
		cashInOutAuthorization(REQUEST_CASH_IN);
	}

	private void addCashInOut(int iscashin) {
		LogCashDB logCashDB = new LogCashDB(this);
		logCashDB.open();
		
		if (logCashDB.addLogCash(iscashin,
				Double.parseDouble(txtCash.getText().toString().trim()),
				txtCashDescription.getText().toString().trim()) > 0){
			showToast("Transaction successful");
		}
		else
			showToast("Transaction unsuccessful. Please try again.");
	}

	private void cashInOutAuthorization(int request) {
		if (txtCash.getText().toString().length() > 0) {
			Intent intent = new Intent(this, Login.class);
			intent.putExtra("called", "cashauth");
			this.startActivityForResult(intent, request);
		}
	}
	
	private void showToast(String message) {
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT)
				.show();
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if (resultCode == Activity.RESULT_OK)
		{
			if(requestCode == REQUEST_CASH_IN)
				addCashInOut(1);
			if(requestCode == REQUEST_CASH_IN)
				addCashInOut(0);
		}
	}
}
