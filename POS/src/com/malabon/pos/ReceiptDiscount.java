package com.malabon.pos;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

public class ReceiptDiscount extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_receipt_discount);
	}
	
	public void confirm(View view) {
		EditText percent = (EditText)findViewById(R.id.txtDiscountPercent);
		EditText php = (EditText)findViewById(R.id.txtDiscountPhp);
		Float floatPercent = 0f;
		Float floatPhp = 0f;
		
		if(!percent.getText().toString().isEmpty())
		{
			 floatPercent = Float.parseFloat(percent.getText().toString());
			 floatPercent /= 100; 
		}
		
		if(!php.getText().toString().isEmpty())
		{
			 floatPhp = Float.parseFloat(php.getText().toString());
		}
		
	
		SharedPreferences prefs = this.getSharedPreferences("com.malabon.pos", Context.MODE_PRIVATE);
		prefs.edit().putFloat("discountPercent", floatPercent).commit();
		prefs.edit().putFloat("discountPhp", floatPhp).commit();
		
		setResult(Activity.RESULT_OK);
        finish();
    }
	
	public void cancel(View view) {
		setResult(Activity.RESULT_CANCELED);
        finish();
    }
	
	
}
