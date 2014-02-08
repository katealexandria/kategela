package com.example.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBAdapter {

	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "Android_POS";

	private static CustomerDB customerDB;

	private DatabaseHelper DBHelper;
	private SQLiteDatabase db;
	private final Context context;
	
	private static final String CREATE_TABLE_CUSTOMER = "CREATE TABLE customer(customer_id nvarchar(25) NOT NULL primary key, first_name nvarchar(50) NULL, last_name nvarchar(50) NULL, address nvarchar(200) NULL, address_landmark nvarchar(200) NULL, tel_no nvarchar(15) NULL, mobile_no nvarchar(15) NULL);";
		
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
			db.execSQL(CREATE_TABLE_CUSTOMER);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + customerDB.TABLE_CUSTOMER);
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
