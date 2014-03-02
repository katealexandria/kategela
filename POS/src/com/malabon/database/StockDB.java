package com.malabon.database;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.malabon.object.Stock;

public class StockDB {
	public static final String TABLE_STOCK = "stock";

	public static final String KEY_STOCK_ID = "stock_id";
	public static final String KEY_STOCK_TYPE_ID = "stock_type_id";
	public static final String KEY_ID = "id";
	public static final String KEY_QUANTITY = "quantity";
	public static final String KEY_LAST_UPDATED_DATE = "last_updated_date";
	public static final String KEY_LAST_UPDATED_USER_ID = "last_updated_user_id";

	private final ArrayList<Stock> product_stock_list = new ArrayList<Stock>();
	private final ArrayList<Stock> ingredient_stock_list = new ArrayList<Stock>();

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

	public StockDB(Context ctx) {
		this.context = ctx;
	}

	public StockDB open() throws SQLException {
		this.DbHelper = new DatabaseHelper(this.context);
		this.db = this.DbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		this.DbHelper.close();
	}

	public ArrayList<Stock> getAllProductStocks() {
		try {
			product_stock_list.clear();
			String selectQuery = "SELECT * FROM " + TABLE_STOCK;

			SQLiteDatabase db = this.DbHelper.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			if (cursor.moveToFirst()) {
				do {
					Stock stock = new Stock();
					stock.stock_id = Integer.parseInt(cursor.getString(0));
					//stock.stock_type_id = Integer.parseInt(cursor.getString(1));
					stock.id = Integer.parseInt(cursor.getString(2));
					stock.quantity = Double.parseDouble(cursor.getString(3));
					stock.last_updated_date = (java.sql.Date) new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(cursor
							.getString(4));
					stock.last_updated_user_id = Integer.parseInt(cursor
							.getString(5));

					product_stock_list.add(stock);
				} while (cursor.moveToNext());
			}
			cursor.close();
			db.close();
		} catch (Exception e) {
			Log.e("get_allproductstocks", "" + e);
		}
		return product_stock_list;
	}
	
	public ArrayList<Stock> getAllIngredientStocks() {
		try {
			
		} catch (Exception e) {
			Log.e("get_allingredientstocks", "" + e);
		}
		return ingredient_stock_list;
	}
}
