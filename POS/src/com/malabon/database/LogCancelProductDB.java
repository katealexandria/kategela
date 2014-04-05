package com.malabon.database;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.malabon.object.CancelledOrder;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class LogCancelProductDB {
	public static final String TABLE_LOG_CANCEL_PRODUCT = "log_cancel_product";
	
	public static final String KEY_ID = "id";
	public static final String KEY_DATE = "date";
	public static final String KEY_USER_ID = "user_id";
	public static final String KEY_PRODUCT_ID = "product_id";

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

	public LogCancelProductDB(Context ctx) {
		this.context = ctx;
	}
	
	public LogCancelProductDB open() throws SQLException {
		this.DbHelper = new DatabaseHelper(this.context);
		this.db = this.DbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		this.DbHelper.close();
	}
	
	public int addLogCancelProduct(List<CancelledOrder> cancelledOrders) {
		int num = 0;
		try {
			SQLiteDatabase db = this.DbHelper.getWritableDatabase();
			
			for (CancelledOrder order : cancelledOrders){
				ContentValues values = new ContentValues();
				values.put(KEY_DATE, formatter.format(new Date()));
				values.put(KEY_USER_ID, order.UserId);
				values.put(KEY_PRODUCT_ID, String.valueOf(order.CancelledItem.id));
				db.insert(TABLE_LOG_CANCEL_PRODUCT, null, values);
			}
			db.close();
			num = 1;
			Log.d("pos", "addLogCancelProduct - success");
		} catch (Exception e) {
			Log.e("pos_error", "addLogCancelProduct" + e);
		}
		return num;
	}
}
