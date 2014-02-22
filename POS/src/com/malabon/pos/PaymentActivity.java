package com.malabon.pos;

import java.text.DecimalFormat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.malabon.object.Payment;

public class PaymentActivity extends Activity {

	TextView tvPaymentTotal, tvPaymentCash, tvPaymentChange;
	String orderType;
	double cash = 0.00;
	Payment objPayment;
	DecimalFormat df = new DecimalFormat("0.00");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_payment);

		Initialize();
		setAmounts(cash);
	}

	private void Initialize() {
		try{
			cash = Double.parseDouble(getIntent().getStringExtra("cash"));
		}catch(Exception e){			
		}
		
		SharedPreferences prefs = this.getSharedPreferences("com.malabon.pos",
				Context.MODE_PRIVATE);
		String balTotal = prefs.getString("balTotal", "0.00");

		objPayment = new Payment();
		objPayment.balance = Double.parseDouble(balTotal);
		//objPayment.cash = objPayment.getCash(cash);
	}

	private void setAmounts(double amount) {
		TextView txtBalTotal = (TextView) findViewById(R.id.balTotal);
		txtBalTotal.setText(df.format(objPayment.balance));

		TextView txtCash = (TextView) findViewById(R.id.paymentCash);
		txtCash.setText(df.format(objPayment.getCash(amount)));

		TextView txtChange = (TextView) findViewById(R.id.paymentChange);
		txtChange.setText(df.format(objPayment.getChange()));
	}

	public void clickNumber(View view) {
		switch (view.getId()) {
		case R.id.btn1000:
			setAmounts(1000);
			break;
		case R.id.btn500:
			setAmounts(500);
			break;
		case R.id.btn100:
			setAmounts(100);
			break;
		case R.id.btn50:
			setAmounts(50);
			break;
		case R.id.btn20:
			setAmounts(20);
			break;
		case R.id.btn10:
			setAmounts(10);
			break;
		case R.id.btn5:
			setAmounts(5);
			break;
		case R.id.btn1:
			setAmounts(1);
			break;
		}
	}

	public void enterCash(View view) {
		Intent intent = new Intent(this, AddPayment.class);
		startActivity(intent);
	}

	public void exactCash(View view) {
		if (IsOrderTypeSelected()){
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
		}
		else{
			showToast("Select order type");
		}
	}

	public void cancel(View view) {
		finish();
	}

	public void confirm(View view) {
		confirmPayment();
	}

	private void confirmPayment(){
		if (objPayment.confirmPayment() && IsOrderTypeSelected()) {
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
		}
		else if (!objPayment.confirmPayment() && IsOrderTypeSelected()){
			showToast("Settle balance");
		}
		else if (objPayment.confirmPayment() && !IsOrderTypeSelected()){
			showToast("Select order type");
		}
		else{
			showToast("Settle balance and select order type");
		}
	}
	
	private boolean IsOrderTypeSelected() {
		boolean isSelected = false;
		RadioGroup rgOrderType = (RadioGroup) findViewById(R.id.rgOrderType);
		int selectedId = rgOrderType.getCheckedRadioButtonId();
		if (selectedId > 0) {
			isSelected = true;
			orderType = (String) ((RadioButton) findViewById(selectedId))
					.getText();
		}
		return isSelected;
	}
	
	private void showToast(String message) {
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT)
				.show();
	}
}
