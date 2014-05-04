package com.malabon.database;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.malabon.function.NewID;

public class UserSalesSummaryDB {
	public static final String TABLE_USER_SALES_SUMMARY = "user_sales_summary";

	public static final String KEY_SALES_SUMMARY_ID = "sales_summary_id";
	public static final String KEY_CASH_TOTAL = "cash_total";
	public static final String KEY_CASH_EXPECTED = "cash_expected";

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

	public UserSalesSummaryDB(Context ctx) {
		this.context = ctx;
	}

	public UserSalesSummaryDB open() throws SQLException {
		this.DbHelper = new DatabaseHelper(this.context);
		this.db = this.DbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		this.DbHelper.close();
	}

	public String addUserSalesSummary(int user, double cash_total,
			double cash_expected) {
		String id = "";

		try {
			SQLiteDatabase db = this.DbHelper.getWritableDatabase();
			ContentValues values = new ContentValues();

			id = new NewID().GetSalesSummaryID(String.valueOf(user),
					new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
			values.put(KEY_SALES_SUMMARY_ID, id);
			values.put(KEY_CASH_TOTAL, cash_total);
			values.put(KEY_CASH_EXPECTED, cash_expected);

			db.insert(TABLE_USER_SALES_SUMMARY, null, values);
			db.close();
			Log.d("pos", "addUserSalesSummary - success");
		} catch (Exception e) {
			Log.e("pos_error", "" + e);
		}
		return id;
	}
}
