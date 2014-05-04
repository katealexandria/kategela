package com.malabon.database;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.malabon.function.NewID;
import com.malabon.object.Customer;

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

	public String addCustomer(Customer customer) {
		String id = null;
		try {
			SQLiteDatabase db = this.DbHelper.getWritableDatabase();
			ContentValues values = new ContentValues();

			id = new NewID().GetCustomerID(context);
			values.put(KEY_CUSTOMER_ID, id);
			values.put(KEY_FIRST_NAME, customer.first_name);
			values.put(KEY_LAST_NAME, customer.last_name);
			values.put(KEY_ADDRESS, customer.address);
			values.put(KEY_ADDRESS_LANDMARK, customer.address_landmark);
			values.put(KEY_TEL_NO, customer.tel_no);
			values.put(KEY_MOBILE_NO, customer.mobile_no);

			db.insert(TABLE_CUSTOMER, null, values);
			db.close();
			Log.d("pos", "addCustomer - success");
		} catch (Exception e) {
			Log.e("pos_error", "addCustomer" + e);
		}
		return id;
	}

	/*
	 * public Customer getCustomer(String customerid) { Customer customer =
	 * null; try { SQLiteDatabase db = this.DbHelper.getReadableDatabase();
	 * Cursor cursor = db.query(TABLE_CUSTOMER, new String[] { KEY_CUSTOMER_ID,
	 * KEY_FIRST_NAME, KEY_LAST_NAME, KEY_ADDRESS, KEY_ADDRESS_LANDMARK,
	 * KEY_TEL_NO, KEY_MOBILE_NO }, KEY_CUSTOMER_ID + "=?", new String[] {
	 * customerid }, null, null, null, null);
	 * 
	 * if (cursor != null){ cursor.moveToFirst();
	 * 
	 * customer = new Customer(); customer.customer_id = cursor.getString(0);
	 * customer.first_name = cursor.getString(1); customer.last_name =
	 * cursor.getString(2); customer.address = cursor.getString(3);
	 * customer.address_landmark = cursor.getString(4); customer.tel_no =
	 * cursor.getString(5); customer.mobile_no = cursor.getString(6); }
	 * 
	 * cursor.close(); db.close(); Log.d("pos", "getCustomer - success"); }
	 * catch (Exception e) { Log.e("pos_error", "getCustomer" + e); } return
	 * customer; }
	 */

	public ArrayList<Customer> getAllCustomers() {
		try {
			customer_list.clear();
			String selectQuery = "SELECT * FROM " + TABLE_CUSTOMER
					+ " ORDER BY " + KEY_LAST_NAME;

			SQLiteDatabase db = this.DbHelper.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			if (cursor.moveToFirst()) {
				do {
					Customer customer = new Customer();
					customer.customer_id = cursor.getString(0);
					customer.first_name = cursor.getString(1);
					customer.last_name = cursor.getString(2);
					customer.address = cursor.getString(3);
					customer.address_landmark = cursor.getString(4);
					customer.tel_no = cursor.getString(5);
					customer.mobile_no = cursor.getString(6);
					
					customer_list.add(customer);
				} while (cursor.moveToNext());
			}
			cursor.close();
			db.close();
			Log.d("pos", "getAllCustomers - success");
		} catch (Exception e) {
			Log.e("pos_error", "getAllCustomers" + e);
		}
		return customer_list;
	}
	
	public int updateCustomer(Customer customer) {
		int num = 0;
		try {
			SQLiteDatabase db = this.DbHelper.getWritableDatabase();

			ContentValues values = new ContentValues();
			values.put(KEY_FIRST_NAME, customer.first_name);
			values.put(KEY_LAST_NAME, customer.last_name);
			values.put(KEY_ADDRESS, customer.address);
			values.put(KEY_ADDRESS_LANDMARK, customer.address_landmark);
			values.put(KEY_TEL_NO, customer.tel_no);
			values.put(KEY_MOBILE_NO, customer.mobile_no);

			num = db.update(TABLE_CUSTOMER, values, KEY_CUSTOMER_ID + " = ?",
					new String[] { String.valueOf(customer.customer_id) });
			db.close();
			Log.d("pos", "updateCustomer - success");
		} catch (Exception e) {
			Log.e("pos_error", "updateCustomer" + e);
		}
		return num;
	}
	
	public int ifExistsTelNo(String telno) {
		int num = 0;
		try {
			String countQuery = "SELECT " + KEY_CUSTOMER_ID + " FROM "
					+ TABLE_CUSTOMER + " WHERE " + KEY_TEL_NO + " = " + telno;
			SQLiteDatabase db = this.DbHelper.getReadableDatabase();
			Cursor cursor = db.rawQuery(countQuery, null);
			num = cursor.getCount();

			cursor.close();
			db.close();
			Log.d("pos", "ifExistsTelNo: " + String.valueOf(num));
		} catch (Exception e) {
			Log.e("pos_error", "ifExistsTelNo" + e);
		}
		return num;
	}

	// TODO: delete after testing
	// --------------------------------------------------------------------------

	public int getCustomerCount() {
		int num = 0;
		try {
			String countQuery = "SELECT " + KEY_CUSTOMER_ID + " FROM "
					+ TABLE_CUSTOMER;
			SQLiteDatabase db = this.DbHelper.getReadableDatabase();
			Cursor cursor = db.rawQuery(countQuery, null);
			num = cursor.getCount();

			cursor.close();
			db.close();
			Log.d("pos", "getCustomerCount: " + String.valueOf(num));
		} catch (Exception e) {
			Log.e("pos_error", "getCustomerCount" + e);
		}
		return num;
	}

	public void tempAddCustomers() {
		try {
			SQLiteDatabase db = this.DbHelper.getWritableDatabase();
			ContentValues values = null;

			values = new ContentValues();
			values.put(KEY_CUSTOMER_ID, "1_1");
			values.put(KEY_FIRST_NAME, "Noble");
			values.put(KEY_LAST_NAME, "Hodge");
			values.put(KEY_ADDRESS, "Ap #708-5317 Arcu. St.");
			values.put(KEY_ADDRESS_LANDMARK, "Enim Mauris Quis LLC");
			values.put(KEY_TEL_NO, "09751856044");
			values.put(KEY_MOBILE_NO, "9229519");
			db.insert(TABLE_CUSTOMER, null, values);

			values = new ContentValues();
			values.put(KEY_CUSTOMER_ID, "1_2");
			values.put(KEY_FIRST_NAME, "Jeremy");
			values.put(KEY_LAST_NAME, "Gibson");
			values.put(KEY_ADDRESS, "P.O. Box 402, 8731 Vitae, Street");
			values.put(KEY_ADDRESS_LANDMARK, "Ornare Tortor Institute");
			values.put(KEY_TEL_NO, "7469708");
			values.put(KEY_MOBILE_NO, "09786189950");
			db.insert(TABLE_CUSTOMER, null, values);

			values = new ContentValues();
			values.put(KEY_CUSTOMER_ID, "2_1");
			values.put(KEY_FIRST_NAME, "Evan");
			values.put(KEY_LAST_NAME, "Mcdowell");
			values.put(KEY_ADDRESS, "487-7041 Neque St.");
			values.put(KEY_ADDRESS_LANDMARK, "In Faucibus Orci Industries");
			values.put(KEY_TEL_NO, "5225500");
			values.put(KEY_MOBILE_NO, "09096149581");
			db.insert(TABLE_CUSTOMER, null, values);

			values = new ContentValues();
			values.put(KEY_CUSTOMER_ID, "1_3");
			values.put(KEY_FIRST_NAME, "Magee");
			values.put(KEY_LAST_NAME, "Merrill");
			values.put(KEY_ADDRESS, "2722 Diam Ave");
			values.put(KEY_ADDRESS_LANDMARK,
					"Sagittis Placerat Cras Associates");
			values.put(KEY_TEL_NO, "6484307");
			values.put(KEY_MOBILE_NO, "09456244540");
			db.insert(TABLE_CUSTOMER, null, values);

			values = new ContentValues();
			values.put(KEY_CUSTOMER_ID, "1_4");
			values.put(KEY_FIRST_NAME, "Hilel");
			values.put(KEY_LAST_NAME, "Christensen");
			values.put(KEY_ADDRESS, "P.O. Box 325, 160 Et Rd.");
			values.put(KEY_ADDRESS_LANDMARK, "Adipiscing Elit Etiam Corp.");
			values.put(KEY_TEL_NO, "3499793");
			values.put(KEY_MOBILE_NO, "09740522018");
			db.insert(TABLE_CUSTOMER, null, values);

			values = new ContentValues();
			values.put(KEY_CUSTOMER_ID, "2_2");
			values.put(KEY_FIRST_NAME, "Kaye");
			values.put(KEY_LAST_NAME, "Palmer");
			values.put(KEY_ADDRESS, "P.O. Box 365, 4958 Orci, Road");
			values.put(KEY_ADDRESS_LANDMARK, "Erat Eget Company");
			values.put(KEY_TEL_NO, "7812327");
			values.put(KEY_MOBILE_NO, "09274836213");
			db.insert(TABLE_CUSTOMER, null, values);

			values = new ContentValues();
			values.put(KEY_CUSTOMER_ID, "1_5");
			values.put(KEY_FIRST_NAME, "Mark");
			values.put(KEY_LAST_NAME, "Foster");
			values.put(KEY_ADDRESS, "Ante Maecenas Mi Corporation");
			values.put(KEY_ADDRESS_LANDMARK, "P.O. Box 234, 6597 Mi Street");
			values.put(KEY_TEL_NO, "6267504");
			values.put(KEY_MOBILE_NO, "09619654610");
			db.insert(TABLE_CUSTOMER, null, values);

			values = new ContentValues();
			values.put(KEY_CUSTOMER_ID, "2_3");
			values.put(KEY_FIRST_NAME, "Germaine");
			values.put(KEY_LAST_NAME, "Lynch");
			values.put(KEY_ADDRESS, "P.O. Box 559, 5084 Praesent Avenue");
			values.put(KEY_ADDRESS_LANDMARK, "Phasellus Corporation");
			values.put(KEY_TEL_NO, "4546833");
			values.put(KEY_MOBILE_NO, "09231631996");
			db.insert(TABLE_CUSTOMER, null, values);

			values = new ContentValues();
			values.put(KEY_CUSTOMER_ID, "1_6");
			values.put(KEY_FIRST_NAME, "Clare");
			values.put(KEY_LAST_NAME, "Mitchell");
			values.put(KEY_ADDRESS, "3890 Dui. Road");
			values.put(KEY_ADDRESS_LANDMARK, "Aliquet Corp.");
			values.put(KEY_TEL_NO, "7207638");
			values.put(KEY_MOBILE_NO, "09921536448");
			db.insert(TABLE_CUSTOMER, null, values);

			db.close();
			Log.d("pos", "tempAddCustomers - success");
		} catch (Exception e) {
			Log.e("pos_error", "tempAddCustomers" + e);
		}
	}
}
