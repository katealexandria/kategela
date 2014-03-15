package com.malabon.database;

import java.util.UUID;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.malabon.object.UserSalesSummary;

public class UserSalesSummaryDB {
	public static final String TABLE_SALES_SUMMARY_PER_USER = "sales_summary_per_user";

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

	public String addUserSalesSummary(UserSalesSummary userSalesSummary) {
		String salessummaryid = null;
		try {
			SQLiteDatabase db = this.DbHelper.getWritableDatabase();
			ContentValues values = new ContentValues();
			// TODO: naming convention of KEY_SALES_SUMMARY_ID
			salessummaryid = UUID.randomUUID().toString();
			values.put(KEY_SALES_SUMMARY_ID, salessummaryid);
			values.put(KEY_CASH_TOTAL, userSalesSummary.cash_total);
			values.put(KEY_CASH_EXPECTED, userSalesSummary.cash_expected);

			db.insert(TABLE_SALES_SUMMARY_PER_USER, null, values);
			db.close();
		} catch (Exception e) {
			Log.e("add_usersalessummary", "" + e);
		}
		return salessummaryid;
	}
}
