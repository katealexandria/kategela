package com.malabon.pos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.malabon.object.Customer;
import com.malabon.object.Item;
import com.malabon.object.Sync;

public class ViewCustomer extends Activity {

	static final int ADD_CUSTOMER_REQUEST = 20;
	static final int EDIT_CUSTOMER_REQUEST = 21;
	
	TableLayout tableCustomer;
	List<Customer> arrayCustomer = new ArrayList<Customer>();
	//CustomerDB customerDB;
	String toastMsg;
	int currentCustomerId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_view_customer);

		try {
			SharedPreferences prefs = this.getSharedPreferences(
					"com.malabon.pos", Context.MODE_PRIVATE);
			currentCustomerId = prefs.getInt("CurrentCustomer", -1);
			
			tableCustomer = (TableLayout) findViewById(R.id.listCustomer);
			refreshData("");

		} catch (Exception e) {
			Log.e("view_customer", "" + e);
		}
	}
	
	public void doSearchCustomer(View view) {
		EditText txtSearchCustomer = (EditText)findViewById(R.id.txtSearchCustomer);
		refreshData(txtSearchCustomer.getText().toString());
	}
	
	public void showAllCustomers(View view){
		EditText txtSearchCustomer = (EditText)findViewById(R.id.txtSearchCustomer);
		txtSearchCustomer.setText("");
		refreshData("");
	}
	
	public void cancel(View view) {
		if(currentCustomerId != -1)
			SelectCustomer(currentCustomerId);
		else{
			Intent resultIntent = new Intent();
			setResult(Activity.RESULT_CANCELED, resultIntent);
			finish();
		}
	}

	public void addCustomer(View view) {
		Intent add_customer = new Intent(ViewCustomer.this, AddCustomer.class);
		add_customer.putExtra("called", "add");
		add_customer.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivityForResult(add_customer, ADD_CUSTOMER_REQUEST);
	}

	public void refreshData(String filter) {
		tableCustomer.removeAllViews();
		arrayCustomer = Sync.GetCustomers();
		
		for(Customer c : arrayCustomer){
			if(filter != null && !filter.isEmpty()){
				String sFilter = filter.toLowerCase();
				if(!c.first_name.toLowerCase().contains(sFilter) &&
						!c.last_name.toLowerCase().contains(sFilter))
					continue;
			}
				
			
			LayoutInflater vi = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
			TableRow row = (TableRow) vi.inflate(R.layout.customer_row, null);
			
			ImageButton select = (ImageButton) row
					.findViewById(R.id.btnSelectCustomer);
			TextView full_name = (TextView) row.findViewById(R.id.tvFullName);
			TextView tel_no = (TextView) row.findViewById(R.id.tvTelNo);
			TextView mobile_no = (TextView) row.findViewById(R.id.tvMobileNo);
			ImageButton edit = (ImageButton) row
					.findViewById(R.id.btnUpdateCustomer);
			
			select.setId(c.customer_id);
			edit.setId(c.customer_id);
			
			select.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					int id = v.getId();
					SelectCustomer(id);
				}
			});
			
			edit.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					int id = v.getId();
					UpdateCustomer(id);
				}
			});
			
			full_name.setText(c.last_name + ", "
					+ c.first_name);
			tel_no.setText(c.tel_no);
			mobile_no.setText(c.mobile_no);

			tableCustomer.addView(row);
		}
	}
	private void UpdateCustomer(int customerId){
		Customer c = GetCustomerById(customerId);	
		Intent update_customer = new Intent(this,
				AddCustomer.class);
		update_customer.putExtra("called", "update");
		update_customer.putExtra("CUSTOMER_ID", c.customer_id);
		startActivityForResult(update_customer, EDIT_CUSTOMER_REQUEST);
	}
	
	private void SelectCustomer(int customerId){
		Customer c = GetCustomerById(customerId);					
		Intent resultIntent = new Intent();
		Bundle b = new Bundle();
		b.putSerializable("SelectedCustomer", (Serializable) c);
		resultIntent.putExtras(b);
		setResult(Activity.RESULT_OK, resultIntent);
		finish();
	}
	
	private Customer GetCustomerById(int id){
		Customer cust = new Customer();
		for(Customer c : arrayCustomer){
			if(c.customer_id == id){
				cust = c;
				break;
			}
		}
		return cust;
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		refreshData("");
	}

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if (resultCode == Activity.RESULT_OK)
			refreshData("");
	}
}
