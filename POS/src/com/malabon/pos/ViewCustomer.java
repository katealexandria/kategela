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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.malabon.database.CustomerDB;
import com.malabon.database.DBAdapter;
import com.malabon.object.Customer;

public class ViewCustomer extends Activity {

	ListView listviewCustomer;
	ArrayList<Customer> arrayCustomer = new ArrayList<Customer>();
	Customer_Adapter adapter;
	DBAdapter db;
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

			db = new DBAdapter(this);
			db.open();

			Set_Referash_Data();

		} catch (Exception e) {
			Log.e("some error", "" + e);
		}
	}

	public void addCustomer(View view) {
		Intent add_customer = new Intent(ViewCustomer.this,
				AddCustomer.class);
		add_customer.putExtra("called", "add");
		add_customer.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(add_customer);
		finish();
	}

	public void Set_Referash_Data() {
		arrayCustomer.clear();

		customerDB = new CustomerDB(this);
		customerDB.open();

		ArrayList<Customer> customer_array_from_db = customerDB
				.GetAllCustomers();

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
		adapter = new Customer_Adapter(ViewCustomer.this,
				R.layout.customer_listview_row, arrayCustomer);
		listviewCustomer.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}

	public void Show_Toast(String msg) {
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Set_Referash_Data();
	}

	public class Customer_Adapter extends ArrayAdapter<Customer> {
		Activity activity;
		int layoutResourceId;
		Customer customer;
		ArrayList<Customer> data = new ArrayList<Customer>();

		public Customer_Adapter(Activity act, int layoutResourceId,
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
				holder.first_name = (TextView) row
						.findViewById(R.id.tvFirstName);
				holder.last_name = (TextView) row.findViewById(R.id.tvLastName);
				holder.address = (TextView) row.findViewById(R.id.tvAddress);
				holder.address_landmark = (TextView) row
						.findViewById(R.id.tvAddressLandmark);
				holder.tel_no = (TextView) row.findViewById(R.id.tvTelNo);
				holder.mobile_no = (TextView) row.findViewById(R.id.tvMobileNo);
				holder.edit = (Button) row.findViewById(R.id.btnUpdateCustomer);

				row.setTag(holder);
			} else {
				holder = (CustomerHolder) row.getTag();
			}
			customer = data.get(position);
			holder.edit.setTag(customer.getCustomerId());
			holder.first_name.setText(customer.getFirstName());
			holder.last_name.setText(customer.getLastName());
			holder.address.setText(customer.getAddress());
			holder.address_landmark.setText(customer.getAddressLandmark());
			holder.tel_no.setText(customer.getTelNo());
			holder.mobile_no.setText(customer.getMobileNo());

			holder.edit.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Log.i("Edit Button Clicked", "**********");

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
			TextView first_name;
			TextView last_name;
			TextView address;
			TextView address_landmark;
			TextView tel_no;
			TextView mobile_no;
			Button edit;
		}
	}
}
