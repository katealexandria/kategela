package com.malabon.pos;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.malabon.database.CustomerDB;
import com.malabon.object.Customer;

public class ViewCustomer extends Activity {

	ListView listviewCustomer;
	ArrayList<Customer> arrayCustomer = new ArrayList<Customer>();
	customerAdapter adapter;
	CustomerDB customerDB;
	String toastMsg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_view_customer);

		try {
			listviewCustomer = (ListView) findViewById(R.id.listCustomer);
			listviewCustomer.setItemsCanFocus(false);

			refreshData();

		} catch (Exception e) {
			Log.e("view_customer", "" + e);
		}
	}

	public void addCustomer(View view) {
		Intent add_customer = new Intent(ViewCustomer.this, AddCustomer.class);
		add_customer.putExtra("called", "add");
		add_customer.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(add_customer);
		finish();
	}

	public void refreshData() {
		arrayCustomer.clear();

		customerDB = new CustomerDB(this);
		customerDB.open();

		ArrayList<Customer> customer_array_from_db = customerDB
				.getAllCustomers();

		for (int i = 0; i < customer_array_from_db.size(); i++) {
			String customer_id = customer_array_from_db.get(i).getCustomerId();
			String first_name = customer_array_from_db.get(i).getFirstName();
			String last_name = customer_array_from_db.get(i).getLastName();
			String address = customer_array_from_db.get(i).getAddress();
			String address_landmark = customer_array_from_db.get(i)
					.getAddressLandmark();
			String tel_no = customer_array_from_db.get(i).getTelNo();
			String mobile_no = customer_array_from_db.get(i).getMobileNo();
			Customer cust = new Customer();
			cust.setCustomerId(customer_id);
			cust.setFirstName(first_name);
			cust.setLastName(last_name);
			cust.setAddress(address);
			cust.setAddressLandmark(address_landmark);
			cust.setTelNo(tel_no);
			cust.setMobileNo(mobile_no);

			arrayCustomer.add(cust);
		}
		adapter = new customerAdapter(ViewCustomer.this,
				R.layout.customer_listview_row, arrayCustomer);
		listviewCustomer.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		refreshData();
	}

	public class customerAdapter extends ArrayAdapter<Customer> {
		Activity activity;
		int layoutResourceId;
		Customer customer;
		ArrayList<Customer> data = new ArrayList<Customer>();

		public customerAdapter(Activity act, int layoutResourceId,
				ArrayList<Customer> data) {
			super(act, layoutResourceId, data);
			this.layoutResourceId = layoutResourceId;
			this.activity = act;
			this.data = data;
			notifyDataSetChanged();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = convertView;
			CustomerHolder holder = null;

			if (row == null) {
				LayoutInflater inflater = LayoutInflater.from(activity);

				row = inflater.inflate(layoutResourceId, parent, false);
				holder = new CustomerHolder();
				holder.full_name = (TextView) row.findViewById(R.id.tvFullName);
				holder.tel_no = (TextView) row.findViewById(R.id.tvTelNo);
				holder.mobile_no = (TextView) row.findViewById(R.id.tvMobileNo);
				holder.edit = (ImageButton) row
						.findViewById(R.id.btnUpdateCustomer);

				row.setTag(holder);
			} else {
				holder = (CustomerHolder) row.getTag();
			}
			customer = data.get(position);
			holder.edit.setTag(customer.getCustomerId());
			holder.full_name.setText(customer.getLastName() + ", "
					+ customer.getFirstName());
			holder.tel_no.setText(customer.getTelNo());
			holder.mobile_no.setText(customer.getMobileNo());

			holder.edit.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent update_customer = new Intent(activity,
							AddCustomer.class);
					update_customer.putExtra("called", "update");
					update_customer.putExtra("CUSTOMER_ID", v.getTag()
							.toString());
					activity.startActivity(update_customer);
				}
			});
			return row;
		}

		class CustomerHolder {
			TextView full_name;
			TextView tel_no;
			TextView mobile_no;
			ImageButton edit;
		}
	}
}
