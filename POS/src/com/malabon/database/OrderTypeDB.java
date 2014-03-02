package com.malabon.database;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.malabon.pos.OrderType;

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
					ordertype.order_type_id = Integer
							.parseInt(cursor.getString(0));
					ordertype.name = cursor.getString(1);
					
					ordertype_list.add(ordertype);
				} while (cursor.moveToNext());
			}
			cursor.close();
			db.close();
		} catch (Exception e) {
			Log.e("get_allordertype", "" + e);
		}
		return ordertype_list;
	}
}
