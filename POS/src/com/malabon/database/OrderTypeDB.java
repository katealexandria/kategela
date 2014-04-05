package com.malabon.database;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.malabon.object.OrderType;

public class OrderTypeDB {
	public static final String TABLE_ORDER_TYPE = "order_type";

	public static final String KEY_ORDER_TYPE_ID = "order_type_id";
	public static final String KEY_NAME = "name";

	private final ArrayList<OrderType> ordertype_list = new ArrayList<OrderType>();

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

	public OrderTypeDB(Context ctx) {
		this.context = ctx;
	}

	public OrderTypeDB open() throws SQLException {
		this.DbHelper = new DatabaseHelper(this.context);
		this.db = this.DbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		this.DbHelper.close();
	}

	public ArrayList<OrderType> getAllOrderTypes() {
		try {
			ordertype_list.clear();
			String selectQuery = "SELECT * FROM " + TABLE_ORDER_TYPE;

			SQLiteDatabase db = this.DbHelper.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			if (cursor.moveToFirst()) {
				do {
					OrderType ordertype = new OrderType();
					ordertype.order_type_id = cursor.getInt(0);
					ordertype.name = cursor.getString(1);

					ordertype_list.add(ordertype);
				} while (cursor.moveToNext());
			}
			cursor.close();
			db.close();
			Log.d("pos", "getAllOrderTypes - success");
		} catch (Exception e) {
			Log.e("pos_error", "getAllOrderTypes" + e);
		}
		return ordertype_list;
	}
	
	// TODO: delete after testing
	// --------------------------------------------------------------------------

	public int getOrderTypeCount() {
		int num = 0;
		try {
			String countQuery = "SELECT " + KEY_ORDER_TYPE_ID + " FROM "
					+ TABLE_ORDER_TYPE;
			SQLiteDatabase db = this.DbHelper.getReadableDatabase();
			Cursor cursor = db.rawQuery(countQuery, null);
			num = cursor.getCount();

			cursor.close();
			Log.d("pos", "getOrderTypeCount: " + String.valueOf(num));
		} catch (Exception e) {
			Log.e("pos_error", "getOrderTypeCount" + e);
		}
		return num;
	}
	
	public void tempAddOrderTypes(){
		try {
			SQLiteDatabase db = this.DbHelper.getWritableDatabase();
			ContentValues values = null;
			
			values = new ContentValues();
			values.put(KEY_ORDER_TYPE_ID, 1);
			values.put(KEY_NAME, "Dine In");
			db.insert(TABLE_ORDER_TYPE, null, values);
			
			values = new ContentValues();
			values.put(KEY_ORDER_TYPE_ID, 2);
			values.put(KEY_NAME, "Take Out");
			db.insert(TABLE_ORDER_TYPE, null, values);
			
			values = new ContentValues();
			values.put(KEY_ORDER_TYPE_ID, 3);
			values.put(KEY_NAME, "Delivery");
			db.insert(TABLE_ORDER_TYPE, null, values);
			
			db.close();
			Log.d("pos", "tempAddCustomers - success");
		}catch (Exception e) {
			Log.e("pos_error", "tempAddCustomers" + e);
		}
	}
}
