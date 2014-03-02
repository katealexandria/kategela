package com.malabon.database;

import java.text.SimpleDateFormat;
import java.util.Locale;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.malabon.object.PosSettings;

public class PosSettingsDB {
	public static final String TABLE_POS_SETTINGS = "pos_settings";

	public static final String KEY_ID = "id";
	public static final String KEY_BRANCH_ID = "branch_id";
	public static final String KEY_BRANCH_NAME = "branch_name";
	public static final String KEY_IS_AUTOMATIC = "is_automatic";
	public static final String KEY_SYNC_FREQUENCY = "sync_frequency";
	public static final String KEY_SYNC_TIME = "sync_time";

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

	public PosSettingsDB(Context ctx) {
		this.context = ctx;
	}

	public PosSettingsDB open() throws SQLException {
		this.DbHelper = new DatabaseHelper(this.context);
		this.db = this.DbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		this.DbHelper.close();
	}

	public PosSettings getAllPosSettings() {
		PosSettings posSettings = null;
		try {
			String selectQuery = "SELECT * FROM " + TABLE_POS_SETTINGS
					+ " LIMIT 1";
			SQLiteDatabase db = this.DbHelper.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			if (cursor != null) {
				cursor.moveToFirst();
				
				posSettings = new PosSettings();
				posSettings.branch_id = Integer.parseInt(cursor.getString(1));
				posSettings.branch_name = cursor.getString(2);
				posSettings.is_automatic = Short
						.parseShort(cursor.getString(3));
				posSettings.sync_frequency = cursor.getString(4);
				posSettings.sync_time = (java.sql.Date) new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(cursor
						.getString(5));
			}
			cursor.close();
			db.close();
		} catch (Exception e) {
			Log.e("get_allPosSettings", "" + e);
		}
		return posSettings;
	}
}
