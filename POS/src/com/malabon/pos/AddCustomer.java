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

import com.malabon.object.Customer;
import com.malabon.object.Sync;

public class AddCustomer extends Activity {
	
	// TO DO: Move all DB commit logic to Sync class, after adding/updating in object.
	
	EditText textFirstName, textLastName, textAddress, textAddressLandMark,
			textTelNo, textMobileNo;
	LinearLayout add_view, update_view;
	String valid_number = null, valid_name = null;
	int CUSTOMER_ID;
	//CustomerDB customerDB;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_add_customer);
		
		if(Sync.Customers == null)
			Sync.GetCustomers();
			
		try {
			//customerDB = new CustomerDB(this);
			//customerDB.open();

			set_Add_Update_Screen();

			String called_from = getIntent().getStringExtra("called");

			if (called_from.equalsIgnoreCase("add")) {
				add_view.setVisibility(View.VISIBLE);
				update_view.setVisibility(View.GONE);
			} else {
				update_view.setVisibility(View.VISIBLE);
				add_view.setVisibility(View.GONE);
				CUSTOMER_ID = getIntent().getIntExtra("CUSTOMER_ID", 0);

				Customer c = new Customer(); //customerDB.getCustomer(CUSTOMER_ID);
				
				for(Customer tmp : Sync.Customers){
					if(tmp.customer_id == CUSTOMER_ID){
						c = tmp;
						break;
					}
				}

				textFirstName.setText(c.first_name);
				textLastName.setText(c.last_name);
				textAddress.setText(c.address);
				textAddressLandMark.setText(c.address_landmark);
				textTelNo.setText(c.tel_no);
				textMobileNo.setText(c.mobile_no);
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
			
			Customer c = new Customer();
			c.first_name = textFirstName.getText().toString();
			c.last_name = textLastName.getText().toString();
			c.address = textAddress.getText().toString();
			c.address_landmark = textAddressLandMark.getText().toString();
			c.tel_no = textTelNo.getText().toString();
			c.mobile_no = textMobileNo.getText().toString();
			
			Sync.Customers.add(c);
			//if (customerDB.addCustomer(c) > 0)
				showToast("Data inserted successfully");
			//else
			//	showToast("Data insertion unsuccessful");
			cancel(null);
		}
	}

	public void updateCustomer(View view) {
		if ((Is_Valid_Name(textFirstName) == true)
				&& (Is_Valid_Name(textLastName) == true)
				&& (Is_Valid__Number(7, 10, textTelNo) == true)
				&& (Is_Valid__Number(11, 12, textMobileNo) == true)) {
			
			for(Customer c : Sync.Customers){
				if(c.customer_id == CUSTOMER_ID){
					c.first_name = textFirstName.getText().toString();
					c.last_name = textLastName.getText().toString();
					c.address = textAddress.getText().toString();
					c.address_landmark = textAddressLandMark.getText().toString();
					c.tel_no = textTelNo.getText().toString();
					c.mobile_no = textMobileNo.getText().toString();
					break;
				}
			}
			
			showToast("Data updated successfully");
			
			/*
			Customer c = new Customer();
			c.customer_id = CUSTOMER_ID;
			c.first_name = textFirstName.getText().toString();
			c.last_name = textLastName.getText().toString();
			c.address = textAddress.getText().toString();
			c.address_landmark = textAddressLandMark.getText().toString();
			c.tel_no = textTelNo.getText().toString();
			c.mobile_no = textMobileNo.getText().toString();
			
			
			if (customerDB.updateCustomer(c) > 0)
				showToast("Data updated successfully");
			else
				showToast("Data update unsuccessful");*/
			cancel(null);
		}
	}

	public void cancel(View view) {
		Intent view_user = new Intent();
		//view_user.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
		//		| Intent.FLAG_ACTIVITY_NEW_TASK);
		setResult(Activity.RESULT_OK, view_user);
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
			edt.setError("Numbers Only");
			isValid = false;
		} else if (edt.getText().toString().length() < MinLen) {
			edt.setError("Minimum length: " + MinLen);
			isValid = false;
		} else if (edt.getText().toString().length() > MaxLen) {
			edt.setError("Maximum length: " + MaxLen);
			isValid = false;
		} else {
			isValid = true;
		}
		return isValid;
	}

	public Boolean Is_Valid_Name(EditText edt) throws NumberFormatException {
		Boolean isValid = true;
		if (edt.getText().toString().length() <= 0) {
			edt.setError("Alphabets Only");
			isValid = false;
		} else if (!edt.getText().toString().matches("[a-zA-Z ]+")) {
			edt.setError("Alphabets Only");
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
