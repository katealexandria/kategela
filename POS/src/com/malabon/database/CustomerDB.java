package com.malabon.database;

import java.util.ArrayList;
import java.util.UUID;

import com.malabon.object.*;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class CustomerDB {
	public static final String TABLE_CUSTOMER = "customer";

	public static final String KEY_CUSTOMER_ID = "customer_id";
	public static final String KEY_FIRST_NAME = "first_name";
	public static final String KEY_LAST_NAME = "last_name";
	public static final String KEY_ADDRESS = "address";
	public static final String KEY_ADDRESS_LANDMARK = "address_landmark";
	public static final String KEY_TEL_NO = "tel_no";
	public static final String KEY_MOBILE_NO = "mobile_no";

	private final ArrayList<Customer> customer_list = new ArrayList<Customer>();

	private DatabaseHelper DbHelper;
	private SQLiteDatabase db;
	private final Context context;

	private static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context) {
			super(context, DBAdapter.DATABASE_NAME, null,
					DBAdapter.DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		}
	}

	public CustomerDB(Context ctx) {
		this.context = ctx;
	}

	public CustomerDB open() throws SQLException {
		this.DbHelper = new DatabaseHelper(this.context);
		this.db = this.DbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		this.DbHelper.close();
	}

	public int addCustomer(Customer customer) {
		int num = 0;
		try {
			SQLiteDatabase db = this.DbHelper.getWritableDatabase();
			ContentValues values = new ContentValues();
			// TODO: naming convention
			// values.put(KEY_CUSTOMER_ID, customer.getCustomerId());
			values.put(KEY_CUSTOMER_ID, UUID.randomUUID().toString());
			values.put(KEY_FIRST_NAME, customer.getFirstName());
			values.put(KEY_LAST_NAME, customer.getLastName());
			values.put(KEY_ADDRESS, customer.getAddress());
			values.put(KEY_ADDRESS_LANDMARK, customer.getAddressLandmark());
			values.put(KEY_TEL_NO, customer.getTelNo());
			values.put(KEY_MOBILE_NO, customer.getMobileNo());

			db.insert(TABLE_CUSTOMER, null, values);
			db.close();
			num = 1;
		} catch (Exception e) {
			Log.e("add_customer", "" + e);
		}
		return num;
	}

	public Customer getCustomer(String customerid) {
		Customer customer = null;
		try {
			SQLiteDatabase db = this.DbHelper.getReadableDatabase();
			Cursor cursor = db.query(TABLE_CUSTOMER, new String[] {
					KEY_CUSTOMER_ID, KEY_FIRST_NAME, KEY_LAST_NAME,
					KEY_ADDRESS, KEY_ADDRESS_LANDMARK, KEY_TEL_NO,
					KEY_MOBILE_NO }, KEY_CUSTOMER_ID + "=?",
					new String[] { String.valueOf(customerid) }, null, null,
					null, null);

			if (cursor != null)
				cursor.moveToFirst();

			customer = new Customer(cursor.getString(0), cursor.getString(1),
					cursor.getString(2), cursor.getString(3),
					cursor.getString(4), cursor.getString(5),
					cursor.getString(6));

			cursor.close();
			db.close();
		} catch (Exception e) {
			Log.e("get_customer", "" + e);
		}
		return customer;
	}

	public ArrayList<Customer> getAllCustomers() {
		try {
			customer_list.clear();
			String selectQuery = "SELECT  * FROM " + TABLE_CUSTOMER;

			SQLiteDatabase db = this.DbHelper.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			if (cursor.moveToFirst()) {
				do {
					Customer customer = new Customer();
					customer.setCustomerId(cursor.getString(0));
					customer.setFirstName(cursor.getString(1));
					customer.setLastName(cursor.getString(2));
					customer.setAddress(cursor.getString(3));
					customer.setAddressLandmark(cursor.getString(4));
					customer.setTelNo(cursor.getString(5));
					customer.setMobileNo(cursor.getString(6));

					customer_list.add(customer);
				} while (cursor.moveToNext());
			}
			cursor.close();
			db.close();
			return customer_list;
		} catch (Exception e) {
			Log.e("all_customer", "" + e);
		}
		return customer_list;
	}

	public int updateCustomer(Customer customer) {
		int num = 0;
		try {
			SQLiteDatabase db = this.DbHelper.getWritableDatabase();

			ContentValues values = new ContentValues();
			values.put(KEY_FIRST_NAME, customer.getFirstName());
			values.put(KEY_LAST_NAME, customer.getLastName());
			values.put(KEY_ADDRESS, customer.getAddress());
			values.put(KEY_ADDRESS_LANDMARK, customer.getAddressLandmark());
			values.put(KEY_TEL_NO, customer.getTelNo());
			values.put(KEY_MOBILE_NO, customer.getMobileNo());

			num = db.update(TABLE_CUSTOMER, values, KEY_CUSTOMER_ID + " = ?",
					new String[] { String.valueOf(customer.getCustomerId()) });
		} catch (Exception e) {
			Log.e("update_customer", "" + e);
		}
		return num;
	}

	public int getCustomerCount() {
		int num = 0;
		try {
			String countQuery = "SELECT " + KEY_CUSTOMER_ID + " FROM "
					+ TABLE_CUSTOMER;
			SQLiteDatabase db = this.DbHelper.getReadableDatabase();
			Cursor cursor = db.rawQuery(countQuery, null);
			cursor.close();

			return cursor.getCount();
		} catch (Exception e) {
			Log.e("get_customer_count", "" + e);
		}
		return num;
	}
}
