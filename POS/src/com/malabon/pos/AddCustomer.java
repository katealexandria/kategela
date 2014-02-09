package com.malabon.pos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.malabon.database.CustomerDB;
import com.malabon.object.Customer;

public class AddCustomer extends Activity {

	EditText textFirstName, textLastName, textAddress, textAddressLandMark,
			textTelNo, textMobileNo;
	LinearLayout add_view, update_view;
	String valid_number = null, valid_name = null;
	String CUSTOMER_ID;
	CustomerDB customerDB;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_add_customer);
		
		try {
			customerDB = new CustomerDB(this);
			customerDB.open();

			set_Add_Update_Screen();

			String called_from = getIntent().getStringExtra("called");

			if (called_from.equalsIgnoreCase("add")) {
				add_view.setVisibility(View.VISIBLE);
				update_view.setVisibility(View.GONE);
			} else {
				update_view.setVisibility(View.VISIBLE);
				add_view.setVisibility(View.GONE);
				CUSTOMER_ID = getIntent().getStringExtra("CUSTOMER_ID");

				Customer c = customerDB.getCustomer(CUSTOMER_ID);

				textFirstName.setText(c.getFirstName());
				textLastName.setText(c.getLastName());
				textAddress.setText(c.getAddress());
				textAddressLandMark.setText(c.getAddressLandmark());
				textTelNo.setText(c.getTelNo());
				textMobileNo.setText(c.getMobileNo());
			}
		} catch (Exception e) {
			Log.e("add_customer", "" + e);
		}
	}

	public void addCustomer(View view) {
		if ((Is_Valid_Name(textFirstName) == true)
				&& (Is_Valid_Name(textLastName) == true)
				&& (Is_Valid__Number(7, 10, textTelNo) == true)
				&& (Is_Valid__Number(11, 12, textMobileNo) == true)) {
			
			if (customerDB.addCustomer(new Customer(textFirstName.getText()
					.toString(), textLastName.getText().toString(), textAddress
					.getText().toString(), textAddressLandMark.getText()
					.toString(), textTelNo.getText().toString(), textMobileNo
					.getText().toString())) > 0)
				showToast("Data inserted successfully");
			else
				showToast("Data insertion unsuccessful");
			viewAllCustomer(null);
		}
	}

	public void updateCustomer(View view) {
		if ((Is_Valid_Name(textFirstName) == true)
				&& (Is_Valid_Name(textLastName) == true)
				&& (Is_Valid__Number(7, 10, textTelNo) == true)
				&& (Is_Valid__Number(11, 12, textMobileNo) == true)) {
			if (customerDB.updateCustomer(new Customer(CUSTOMER_ID, textFirstName
					.getText().toString(), textLastName.getText().toString(),
					textAddress.getText().toString(), textAddressLandMark
							.getText().toString(), textTelNo.getText()
							.toString(), textMobileNo.getText().toString())) > 0)
				showToast("Data updated successfully");
			else
				showToast("Data update unsuccessful");
			viewAllCustomer(null);
		}
	}

	public void viewAllCustomer(View view) {
		Intent view_user = new Intent(AddCustomer.this, ViewCustomer.class);
		view_user.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(view_user);
		finish();
	}

	public void set_Add_Update_Screen() {
		textFirstName = (EditText) findViewById(R.id.textFirstName);
		textLastName = (EditText) findViewById(R.id.textLastName);
		textAddress = (EditText) findViewById(R.id.textAddress);
		textAddressLandMark = (EditText) findViewById(R.id.textAddressLandMark);
		textTelNo = (EditText) findViewById(R.id.textTelNo);
		textMobileNo = (EditText) findViewById(R.id.textMobileNo);

		add_view = (LinearLayout) findViewById(R.id.add_view);
		update_view = (LinearLayout) findViewById(R.id.update_view);

		add_view.setVisibility(View.GONE);
		update_view.setVisibility(View.GONE);
	}

	public Boolean Is_Valid__Number(int MinLen, int MaxLen, EditText edt)
			throws NumberFormatException {
		Boolean isValid = true;
		if (edt.getText().toString().length() <= 0) {
			edt.setError("Number Only");
			isValid = false;
		} else if (edt.getText().toString().length() < MinLen) {
			edt.setError("Minimum length " + MinLen);
			isValid = false;
		} else if (edt.getText().toString().length() > MaxLen) {
			edt.setError("Maximum length " + MaxLen);
			isValid = false;
		} else {
			isValid = true;
		}
		return isValid;
	}

	public Boolean Is_Valid_Name(EditText edt) throws NumberFormatException {
		Boolean isValid = true;
		if (edt.getText().toString().length() <= 0) {
			edt.setError("Accept Alphabets Only.");
			isValid = false;
		} else if (!edt.getText().toString().matches("[a-zA-Z ]+")) {
			edt.setError("Accept Alphabets Only.");
			isValid = false;
		} else {
			isValid = true;
		}
		return isValid;
	}

	private void showToast(String message) {
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT)
				.show();
	}
}
