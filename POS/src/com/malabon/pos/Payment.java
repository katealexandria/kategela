package com.malabon.pos;

import java.text.DecimalFormat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.malabon.object.ClsPayment;

public class Payment extends Activity {
	
	ClsPayment objPayment;
	DecimalFormat df = new DecimalFormat("0.00");
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_payment);
		
		Initialize();
		
		SetAmounts();		
	}
	
	private void Initialize(){
		//set balance total
		SharedPreferences prefs = this.getSharedPreferences(
				"com.malabon.pos", Context.MODE_PRIVATE);
		String balTotal = prefs.getString("balTotal", "0.00");
		
		objPayment = new ClsPayment();
		objPayment.balance = Double.parseDouble(balTotal);
	}
	
	private void SetAmounts(){
		TextView txtBalTotal = (TextView)findViewById(R.id.balTotal);
		txtBalTotal.setText(df.format(objPayment.balance));		
		
		TextView txtCash = (TextView)findViewById(R.id.paymentCash);
		txtCash.setText(df.format(objPayment.cash));
		
		TextView txtChange = (TextView)findViewById(R.id.paymentChange);
		txtChange.setText(df.format(objPayment.getChange()));
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
