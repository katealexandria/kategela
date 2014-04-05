package com.malabon.database;

import java.text.Format;
import java.text.SimpleDateFormat;

import com.malabon.object.SyncHistory;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.format.Time;
import android.util.Log;

public class HistorySyncDB {
	public static final String TABLE_HISTORY_SYNC = "history_sync";
	
	public static final String KEY_ID = "id";
	public static final String KEY_DATE = "date";
	public static final String KEY_USER_ID = "user_id";
	public static final String KEY_IS_MANUAL = "is_manual";

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

	public HistorySyncDB(Context ctx) {
		this.context = ctx;
	}
	
	public HistorySyncDB open() throws SQLException {
		this.DbHelper = new DatabaseHelper(this.context);
		this.db = this.DbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		this.DbHelper.close();
	}
	
	public int addHistorySync(SyncHistory syncHistory) {
		int num = 0;
		try {
			SQLiteDatabase db = this.DbHelper.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put(KEY_DATE, formatter.format(syncHistory.SyncDate));
			values.put(KEY_USER_ID, syncHistory.UserId);
			values.put(KEY_IS_MANUAL, syncHistory.IsManual);

			db.insert(TABLE_HISTORY_SYNC, null, values);
			db.close();
			num = 1;
		} catch (Exception e) {
			Log.e("add_addHistorySync", "" + e);
		}
		return num;
	}
}
