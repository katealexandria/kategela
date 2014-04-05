package com.malabon.database;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.malabon.object.Item;

public class ProductDB {
	public static final String TABLE_PRODUCT = "product";

	public static final String KEY_PRODUCT_ID = "product_id";
	public static final String KEY_NAME = "name";
	public static final String KEY_PRICE = "price";
	public static final String KEY_UNIT = "unit";
	public static final String KEY_CATEGORY_ID = "category_id";
	public static final String KEY_SORTORDER = "sortorder";
	public static final String KEY_CAN_BE_TAKEN_OUT = "can_be_taken_out";

	private final ArrayList<Item> product_list = new ArrayList<Item>();

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

	public ArrayList<Item> getAllProducts() {
		try {
			product_list.clear();
			String selectQuery = "SELECT * FROM " + TABLE_PRODUCT;

			SQLiteDatabase db = this.DbHelper.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			if (cursor.moveToFirst()) {
				do {
					Item item = new Item();
					item.id = cursor.getInt(0);
					item.name = cursor.getString(1);
					item.price = cursor.getDouble(2);
					item.unit = cursor.getString(3);
					item.category_id = cursor.getInt(4);
					item.sortorder = cursor.getInt(5);
					item.can_be_taken_out = item.can_be_taken_out = cursor.getInt(6)>0;
					
					//"***" will mark items that canNOT be taken out 
					if(!item.can_be_taken_out)
						item.name = item.name + "***";

					product_list.add(item);
				} while (cursor.moveToNext());
			}
			cursor.close();
			db.close();
			Log.d("pos", "getAllProducts - success");
		} catch (Exception e) {
			Log.e("pos_error", "getAllProducts" + e);
		}
		return product_list;
	}
	
	// TODO: delete after testing
	// --------------------------------------------------------------------------
	
	public int getProductCount() {
		int num = 0;
		try {
			String countQuery = "SELECT " + KEY_PRODUCT_ID + " FROM " + TABLE_PRODUCT;
			SQLiteDatabase db = this.DbHelper.getReadableDatabase();
			Cursor cursor = db.rawQuery(countQuery, null);
			num = cursor.getCount();
			
			cursor.close();
			Log.d("pos", "getProductCount: " + String.valueOf(num));
		} catch (Exception e) {
			Log.e("pos_error", "getProductCount" + e);
		}
		return num;
	}
	
	public void tempAddProduct(){
		try{
			SQLiteDatabase db = this.DbHelper.getWritableDatabase();
			ContentValues values = null;
			
			values = new ContentValues();
			values.put(KEY_PRODUCT_ID, 1);
			values.put(KEY_NAME, "Pizza");
			values.put(KEY_PRICE, 15.50);
			values.put(KEY_UNIT, "kilo");
			values.put(KEY_CATEGORY_ID, 1);
			values.put(KEY_SORTORDER, 1);
			values.put(KEY_CAN_BE_TAKEN_OUT, 1);
			db.insert(TABLE_PRODUCT, null, values);
			
			values = new ContentValues();
			values.put(KEY_PRODUCT_ID, 2);
			values.put(KEY_NAME, "Coke");
			values.put(KEY_PRICE, 17.50);
			values.put(KEY_UNIT, "kilo");
			values.put(KEY_CATEGORY_ID, 2);
			values.put(KEY_SORTORDER, 2);
			values.put(KEY_CAN_BE_TAKEN_OUT, 1);
			db.insert(TABLE_PRODUCT, null, values);
			
			values = new ContentValues();
			values.put(KEY_PRODUCT_ID, 3);
			values.put(KEY_NAME, "Spaghetti");
			values.put(KEY_PRICE, 5.00);
			values.put(KEY_UNIT, "kilo");
			values.put(KEY_CATEGORY_ID, 1);
			values.put(KEY_SORTORDER, 3);
			values.put(KEY_CAN_BE_TAKEN_OUT, 1);
			db.insert(TABLE_PRODUCT, null, values);
			
			values = new ContentValues();
			values.put(KEY_PRODUCT_ID, 4);
			values.put(KEY_NAME, "French Fries");
			values.put(KEY_PRICE, 9.00);
			values.put(KEY_UNIT, "kilo");
			values.put(KEY_CATEGORY_ID, 1);
			values.put(KEY_SORTORDER, 4);
			values.put(KEY_CAN_BE_TAKEN_OUT, 0);
			db.insert(TABLE_PRODUCT, null, values);
			
			values = new ContentValues();
			values.put(KEY_PRODUCT_ID, 5);
			values.put(KEY_NAME, "Sprite");
			values.put(KEY_PRICE, 21.50);
			values.put(KEY_UNIT, "kilo");
			values.put(KEY_CATEGORY_ID, 2);
			values.put(KEY_SORTORDER, 5);
			values.put(KEY_CAN_BE_TAKEN_OUT, 1);
			db.insert(TABLE_PRODUCT, null, values);
			
			values = new ContentValues();
			values.put(KEY_PRODUCT_ID, 6);
			values.put(KEY_NAME, "Milkshake");
			values.put(KEY_PRICE, 11.50);
			values.put(KEY_UNIT, "kilo");
			values.put(KEY_CATEGORY_ID, 2);
			values.put(KEY_SORTORDER, 6);
			values.put(KEY_CAN_BE_TAKEN_OUT, 0);
			db.insert(TABLE_PRODUCT, null, values);
			
			db.close();
			Log.d("pos", "tempAddProduct - success");
		}catch (Exception e) {
			Log.e("pos_error", "tempAddProduct" + e);
		}
		
	}
}
