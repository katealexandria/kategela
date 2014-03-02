package com.malabon.database;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.malabon.object.Product;

public class ProductDB {
	public static final String TABLE_PRODUCT = "product";

	public static final String KEY_PRODUCT_ID = "product_id";
	public static final String KEY_NAME = "name";
	public static final String KEY_PRICE = "price";
	public static final String KEY_UNIT = "unit";
	public static final String KEY_CATEGORY_ID = "category_id";
	public static final String KEY_SORTORDER = "sortorder";
	public static final String KEY_CAN_BE_TAKEN_OUT = "can_be_taken_out";

	private final ArrayList<Product> product_list = new ArrayList<Product>();

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

	public ProductDB(Context ctx) {
		this.context = ctx;
	}

	public ProductDB open() throws SQLException {
		this.DbHelper = new DatabaseHelper(this.context);
		this.db = this.DbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		this.DbHelper.close();
	}

	public ArrayList<Product> getAllProducts() {
		try {
			product_list.clear();
			String selectQuery = "SELECT * FROM " + TABLE_PRODUCT;

			SQLiteDatabase db = this.DbHelper.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			if (cursor.moveToFirst()) {
				do {
					Product product = new Product();
					product.product_id = Integer.parseInt(cursor.getString(0));
					product.name = cursor.getString(1);
					product.price = Double.parseDouble(cursor.getString(2));
					product.unit = cursor.getString(3);
					product.category_id = Integer.parseInt(cursor.getString(4));
					product.sortorder = Integer.parseInt(cursor.getString(5));
					product.can_be_taken_out = Boolean.parseBoolean(cursor
							.getString(6));

					product_list.add(product);
				} while (cursor.moveToNext());
			}
			cursor.close();
			db.close();
		} catch (Exception e) {
			Log.e("get_allproducts", "" + e);
		}
		return product_list;
	}
}
