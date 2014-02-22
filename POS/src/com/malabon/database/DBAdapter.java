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
				db.execSQL(table.get_TABLE_USER());
			} catch (Exception e) {
				Log.e("create_table", "" + e);
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + CustomerDB.TABLE_CUSTOMER);
			db.execSQL("DROP TABLE IF EXISTS " + UserDB.TABLE_USER);
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