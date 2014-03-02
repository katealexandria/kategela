package com.malabon.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {

	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "Android_POS1";
	private static DBTable table;

	private DatabaseHelper DBHelper;
	private SQLiteDatabase db;
	private final Context context;

	public DBAdapter(Context ctx) {
		this.context = ctx;
		this.DBHelper = new DatabaseHelper(this.context);
	}

	private static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			try {
				db.execSQL(table.get_TABLE_CUSTOMER());
				db.execSQL(table.get_TABLE_DISCOUNT());
				db.execSQL(table.get_TABLE_INGREDIENT());
				db.execSQL(table.get_TABLE_LOG_CASH());
				db.execSQL(table.get_TABLE_ORDER_TYPE());
				db.execSQL(table.get_TABLE_POS_SETTINGS());
				db.execSQL(table.get_TABLE_PRODUCT());
				db.execSQL(table.get_TABLE_PRODUCT_CATEGORY());
				db.execSQL(table.get_TABLE_RECIPE());
				db.execSQL(table.get_TABLE_STOCK_TYPE());
				db.execSQL(table.get_TABLE_USER());
				db.execSQL(table.get_TABLE_USER_QUESTION());
				
			} catch (Exception e) {
				Log.e("create_table", "" + e);
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			//db.execSQL("DROP TABLE IF EXISTS " + UserDB.TABLE_USER);
			onCreate(db);
		}
	}

	public DBAdapter open() throws SQLException {
		this.db = this.DBHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		this.DBHelper.close();
	}
}