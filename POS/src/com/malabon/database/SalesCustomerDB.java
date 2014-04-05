package com.malabon.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.malabon.object.Sale;

public class SalesCustomerDB {
	public static final String TABLE_SALES_CUSTOMER = "sales_customer";

	public static final String KEY_ID = "id";
	public static final String KEY_SALES_ID = "sales_id";
	public static final String KEY_CUSTOMER_ID = "customer_id";

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

	public SalesCustomerDB(Context ctx) {
		this.context = ctx;
	}

	public SalesCustomerDB open() throws SQLException {
		this.DbHelper = new DatabaseHelper(this.context);
		this.db = this.DbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		this.DbHelper.close();
	}

	public void addSaleCustomer(int id, String customerid) {
		try {
			SQLiteDatabase db = this.DbHelper.getWritableDatabase();

			ContentValues values = new ContentValues();
			values.put(KEY_SALES_ID, id);
			values.put(KEY_CUSTOMER_ID, customerid);
			db.insert(TABLE_SALES_CUSTOMER, null, values);

			db.close();
			Log.d("pos", "addSaleCustomer - success");
		} catch (Exception e) {
			Log.e("pos_error", "addSaleCustomer" + e);
		}
	}
}
