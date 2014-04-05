package com.malabon.database;

import java.text.Format;
import java.text.SimpleDateFormat;

import com.malabon.object.ClearCacheHistory;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class HistoryClearCacheDB {
	public static final String TABLE_HISTORY_CLEAR_CACHE = "history_clear_cache";
	
	public static final String KEY_ID = "ID";
	public static final String KEY_DATE = "DATE";
	public static final String KEY_USER_ID = "USER_ID";

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

	public HistoryClearCacheDB(Context ctx) {
		this.context = ctx;
	}
	
	public HistoryClearCacheDB open() throws SQLException {
		this.DbHelper = new DatabaseHelper(this.context);
		this.db = this.DbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		this.DbHelper.close();
	}
	
	public int addHistoryClearCache(ClearCacheHistory clearCacheHistory) {
		int num = 0;
		try {
			SQLiteDatabase db = this.DbHelper.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put(KEY_DATE, formatter.format(clearCacheHistory.ClearDate));
			values.put(KEY_USER_ID, clearCacheHistory.UserId);

			db.insert(TABLE_HISTORY_CLEAR_CACHE, null, values);
			db.close();
			num = 1;
		} catch (Exception e) {
			Log.e("add_addHistoryClearCache", "" + e);
		}
		return num;
	}
}
