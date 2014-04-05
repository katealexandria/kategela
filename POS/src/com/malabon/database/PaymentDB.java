package com.malabon.database;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class PaymentDB {
	public static final String TABLE_PAYMENT = "payment";
	
	public static final String KEY_PAYMENT_ID = "payment_id";
	public static final String KEY_SALES_ID = "sales_id";
	public static final String KEY_TOTAL_AMOUNT = "total_amount";
	public static final String KEY_TOTAL_NET = "total_net";
	public static final String KEY_TOTAL_TAX = "total_tax";
	public static final String KEY_TOTAL_DISCOUNT = "total_discount";
	public static final String KEY_RECEIPT_ID = "receipt_id";
	public static final String KEY_DATE = "date";

	private DatabaseHelper DbHelper;
	private SQLiteDatabase db;
	private final Context context;
	
	Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
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
	
	public PaymentDB(Context ctx) {
		this.context = ctx;
	}

	public PaymentDB open() throws SQLException {
		this.DbHelper = new DatabaseHelper(this.context);
		this.db = this.DbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		this.DbHelper.close();
	}
	
	public void addPayment(int salesid, double totalamount, double totalnet, double totaltax, double totaldiscount){
		try {
			SQLiteDatabase db = this.DbHelper.getWritableDatabase();

			ContentValues values = new ContentValues();
			values.put(KEY_SALES_ID, salesid);
			values.put(KEY_TOTAL_AMOUNT, totalamount);
			values.put(KEY_TOTAL_NET, totalnet);
			values.put(KEY_TOTAL_TAX, totaltax);
			values.put(KEY_TOTAL_DISCOUNT, totaldiscount);
			values.put(KEY_DATE, formatter.format(new Date()));
			db.insert(TABLE_PAYMENT, null, values);

			db.close();
			Log.d("pos", "addPayment - success");
		} catch (Exception e) {
			Log.e("pos_error", "addPayment" + e);
		}
	}
}
