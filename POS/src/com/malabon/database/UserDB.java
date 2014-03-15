package com.malabon.database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.malabon.object.User;

public class UserDB {
	public static final String TABLE_USER = "user";

	public static final String KEY_USER_ID = "user_id";
	public static final String KEY_USERNAME = "username";
	public static final String KEY_PASSWORD = "password";
	public static final String KEY_IS_ADMIN = "is_admin";

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

	public UserDB(Context ctx) {
		this.context = ctx;
	}

	public UserDB open() throws SQLException {
		this.DbHelper = new DatabaseHelper(this.context);
		this.db = this.DbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		this.DbHelper.close();
	}

	public User validateLogin(String username, String password) {
		User user = null;
		try {
			SQLiteDatabase db = this.DbHelper.getReadableDatabase();

			Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USER
					+ " WHERE " + KEY_USERNAME + " = '" + username + "'"
					+ " AND " + KEY_PASSWORD + " = '" + password + "'", null);

			if (cursor != null) {
				cursor.moveToFirst();
				user = new User();
				user.user_id = cursor.getInt(0);
				user.username = cursor.getString(1);
			}
			cursor.close();
			db.close();
		} catch (Exception e) {
			Log.e("get_user", "" + e);
		}
		return user;
	}

	public boolean validateAdmin(String username, String password) {
		boolean isadmin = false;
		try {
			SQLiteDatabase db = this.DbHelper.getReadableDatabase();

			Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USER
					+ " WHERE " + KEY_USERNAME + " = '" + username + "'"
					+ " AND " + KEY_PASSWORD + " = '" + password + "'"
					+ " AND " + KEY_IS_ADMIN + "= 1", null);

			if (cursor != null) {
				cursor.moveToFirst();
				isadmin = true;
			}
			cursor.close();
			db.close();
		} catch (Exception e) {
			Log.e("get_isadmin", "" + e);
		}
		return isadmin;
	}

	/*
	 * public void tempAdd() { try { DBTable table = new DBTable();
	 * db.execSQL(table.get_TABLE_USER()); } catch (Exception e) {
	 * Log.e("create_table", "" + e); }
	 * 
	 * try { SQLiteDatabase db = this.DbHelper.getWritableDatabase();
	 * ContentValues values = null;
	 * 
	 * values = new ContentValues(); values.put(KEY_USER_ID, 1);
	 * values.put(KEY_USERNAME, "admin"); values.put(KEY_PASSWORD, "pass");
	 * values.put(KEY_IS_ADMIN, 1); db.insert(TABLE_USER, null, values);
	 * 
	 * values = new ContentValues(); values.put(KEY_USER_ID, 2);
	 * values.put(KEY_USERNAME, "gela"); values.put(KEY_PASSWORD, "pass");
	 * values.put(KEY_IS_ADMIN, 0); db.insert(TABLE_USER, null, values);
	 * 
	 * values = new ContentValues(); values.put(KEY_USER_ID, 3);
	 * values.put(KEY_USERNAME, "kate"); values.put(KEY_PASSWORD, "pass");
	 * values.put(KEY_IS_ADMIN, 0); db.insert(TABLE_USER, null, values);
	 * 
	 * db.close(); } catch (Exception e) { Log.e("add_user", "" + e); } }
	 */
}
