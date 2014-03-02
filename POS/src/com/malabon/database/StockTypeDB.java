package com.malabon.database;

import java.util.ArrayList;

import com.malabon.object.StockType;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class StockTypeDB {
	public static final String TABLE_STOCK_TYPE = "stock_type";
	
	public static final String KEY_STOCK_TYPE_ID = "stock_type_id";
	public static final String KEY_NAME = "name";
	public static final String KEY_DESCRIPTION = "description";

	private final ArrayList<StockType> stocktype_list = new ArrayList<StockType>();

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
	
	public StockTypeDB(Context ctx) {
		this.context = ctx;
	}

	public StockTypeDB open() throws SQLException {
		this.DbHelper = new DatabaseHelper(this.context);
		this.db = this.DbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		this.DbHelper.close();
	}
	
	public ArrayList<StockType> getAllStockTypes() {
		try {
			stocktype_list.clear();
			String selectQuery = "SELECT * FROM " + TABLE_STOCK_TYPE;

			SQLiteDatabase db = this.DbHelper.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			if (cursor.moveToFirst()) {
				do {
					StockType stockType = new StockType();
					stockType.stock_type_id = Integer.parseInt(cursor.getString(0));
					stockType.name = cursor.getString(1);
					stockType.description = cursor.getString(2);
					
					stocktype_list.add(stockType);
				} while (cursor.moveToNext());
			}
			cursor.close();
			db.close();
		} catch (Exception e) {
			Log.e("get_allstocktypes", "" + e);
		}
		return stocktype_list;
	}
}
