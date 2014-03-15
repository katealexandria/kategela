package com.malabon.database;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.malabon.object.ProductCategory;

public class ProductCategoryDB {
	public static final String TABLE_PRODUCT_CATEGORY = "product_category";

	public static final String KEY_CATEGORY_ID = "category_id";
	public static final String KEY_NAME = "name";
	public static final String KEY_SORTORDER = "sortorder";

	private final ArrayList<ProductCategory> productcategory_list = new ArrayList<ProductCategory>();

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

	public ProductCategoryDB(Context ctx) {
		this.context = ctx;
	}

	public ProductCategoryDB open() throws SQLException {
		this.DbHelper = new DatabaseHelper(this.context);
		this.db = this.DbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		this.DbHelper.close();
	}

	public ArrayList<ProductCategory> getAllProductCategories() {
		try {
			productcategory_list.clear();
			String selectQuery = "SELECT * FROM " + TABLE_PRODUCT_CATEGORY;

			SQLiteDatabase db = this.DbHelper.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			if (cursor.moveToFirst()) {
				do {
					ProductCategory productCategory = new ProductCategory();
					productCategory.category_id = cursor.getInt(0);
					productCategory.name = cursor.getString(1);
					productCategory.sortorder = cursor.getInt(2); 

					productcategory_list.add(productCategory);
				} while (cursor.moveToNext());
			}
			cursor.close();
			db.close();
		} catch (Exception e) {
			Log.e("get_allproductcategories", "" + e);
		}
		return productcategory_list;
	}
}
