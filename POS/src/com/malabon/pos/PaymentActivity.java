package com.malabon.pos;

import java.text.DecimalFormat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.malabon.object.Payment;
import com.malabon.object.Sale;
import com.malabon.object.Sync;

public class PaymentActivity extends Activity {

	static final int ENTER_CASH = 15;
	
	TextView tvPaymentTotal, tvPaymentCash, tvPaymentChange;
	String orderType;
	double cash = 0.00;
	Payment objPayment;
	DecimalFormat df = new DecimalFormat("0.00");
	Sale sale;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_payment);

		Initialize();
		setAmounts(cash);
	}

	private void Initialize() {
		
		Bundle extras = getIntent().getExtras();
		if (extras == null) {
			setResult(Activity.RESULT_CANCELED);
			finish();
		}
		// Get data via the key
		sale = (Sale) extras.get("Sale_Payment");
		if (sale != null) {
			TextView txtCustomerName = (TextView) findViewById(R.id.paymentCustomerName);
			txtCustomerName.setText(sale.customer.first_name + " " + sale.customer.last_name);
			
			sale.computeTotal();
			objPayment = new Payment();
			objPayment.balance = Double.parseDouble(df.format(sale.total));
		}	
		
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
		startActivityForResult(intent, ENTER_CASH);
	}

	public void exactCash(View view) {
		objPayment.cash = 0;
		cash = sale.total;
		setAmounts(cash);
	}

	public void cancel(View view) {
		Intent resultIntent = new Intent();
		setResult(Activity.RESULT_CANCELED, resultIntent);
		finish();
	}

	public void confirm(View view) {
		confirmPayment();
	}

	private void confirmPayment(){
		if (objPayment.confirmPayment() && IsOrderTypeSelected()) {
			
			commitSale();
			
			Intent resultIntent = new Intent();
			setResult(Activity.RESULT_FIRST_USER, resultIntent);
			finish();
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
	
	private void commitSale(){
		Sync.AddSale(sale);
		Sync.RefreshInventory();
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
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		switch (requestCode) {
		case(ENTER_CASH): {
			if (resultCode == Activity.RESULT_OK) {
				String amt = intent.getStringExtra("cash");
				cash = Double.parseDouble(amt);
				setAmounts(cash);
			}
		}
		}
	}
}
