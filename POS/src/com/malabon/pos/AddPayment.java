package com.malabon.pos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class AddPayment extends Activity {

	TextView txtAmount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_add_payment);

		Initialize();
	}

	private void Initialize() {
		txtAmount = (TextView) findViewById(R.id.txtAmount);
		txtAmount.setKeyListener(null);
	}

	public void clickNumber(View view) {
		switch (view.getId()) {
		case R.id.btn0n:
			concatAmount("0");
			break;
		case R.id.btn1n:
			concatAmount("1");
			break;
		case R.id.btn2n:
			concatAmount("2");
			break;
		case R.id.btn3n:
			concatAmount("3");
			break;
		case R.id.btn4n:
			concatAmount("4");
			break;
		case R.id.btn5n:
			concatAmount("5");
			break;
		case R.id.btn6n:
			concatAmount("6");
			break;
		case R.id.btn7n:
			concatAmount("7");
			break;
		case R.id.btn8n:
			concatAmount("8");
			break;
		case R.id.btn9n:
			concatAmount("9");
			break;
		case R.id.btnClr:
			txtAmount.setText("");
			break;
		case R.id.bntBackspace:
			removeLastChar();
			break;
		}
	}

	public void confirm(View view) {
		Intent payment = new Intent(AddPayment.this, PaymentActivity.class);
		payment.putExtra("cash", txtAmount.getText().toString());
		payment.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(payment);
		finish();
	}

	public void cancel(View view) {
		finish();
	}

	private void concatAmount(String amount) {
		txtAmount.setText(txtAmount.getText().toString().concat(amount));
	}

	private void removeLastChar() {
		String str = txtAmount.getText().toString();
		if (str != null && str.length() != 0)
			txtAmount.setText(str.substring(0, str.length() - 1));
	}
}
