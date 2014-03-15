package com.malabon.database;

import java.sql.Date;
import java.text.Format;
import java.text.SimpleDateFormat;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.format.Time;
import android.util.Log;

import com.malabon.object.LogCash;

public class LogCashDB {
	public static final String TABLE_LOG_CASH = "log_cash";

	public static final String KEY_ID = "id";
	public static final String KEY_IS_CASH_IN = "is_cash_in";
	public static final String KEY_AMOUNT = "amount";
	public static final String KEY_USER_ID = "user_id";
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

	public LogCashDB(Context ctx) {
		this.context = ctx;
	}

	public LogCashDB open() throws SQLException {
		this.DbHelper = new DatabaseHelper(this.context);
		this.db = this.DbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		this.DbHelper.close();
	}

	public double getLogCashTotal(Date dateFrom, Date DateTo) {
		double amount = 0.0;
		try {
			String selectQuery = "SELECT " + "((SELECT SUM(" + KEY_AMOUNT
					+ ") FROM " + TABLE_LOG_CASH + " where " + KEY_IS_CASH_IN
					+ " == 1 " + "and " + KEY_DATE + " between "
					+ formatter.format(dateFrom) + " and "
					+ formatter.format(dateFrom) + ") " + " - "
					+ "(SELECT SUM(" + KEY_AMOUNT + ") FROM " + TABLE_LOG_CASH
					+ " where " + KEY_IS_CASH_IN + " == 0 " + "and " + KEY_DATE
					+ " between " + formatter.format(dateFrom) + " and "
					+ formatter.format(dateFrom) + ") ";
			SQLiteDatabase db = this.DbHelper.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			if (cursor != null) {
				cursor.moveToFirst();
				amount = cursor.getDouble(0);
			}
			cursor.close();
			db.close();
		} catch (Exception e) {
			Log.e("get_logcashtotal", "" + e);
		}
		return amount;
	}

	public int addLogCash(LogCash logCash) {
		int num = 0;
		try {
			SQLiteDatabase db = this.DbHelper.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put(KEY_IS_CASH_IN, logCash.is_cash_in);
			values.put(KEY_AMOUNT, logCash.amount);
			values.put(KEY_USER_ID, logCash.user_id);
			values.put(KEY_DATE, formatter.format(new Time()));

			db.insert(TABLE_LOG_CASH, null, values);
			db.close();
			num = 1;
		} catch (Exception e) {
			Log.e("add_logcash", "" + e);
		}
		return num;
	}
}
