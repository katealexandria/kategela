package com.malabon.database;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.malabon.object.Discount;

public class DiscountDB {
	public static final String TABLE_DISCOUNT = "discount";

	public static final String KEY_DISCOUNT_ID = "discount_id";
	public static final String KEY_NAME = "name";
	public static final String KEY_PERCENTAGE = "percentage";

	private final ArrayList<Discount> discount_list = new ArrayList<Discount>();

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

	public DiscountDB(Context ctx) {
		this.context = ctx;
	}

	public DiscountDB open() throws SQLException {
		this.DbHelper = new DatabaseHelper(this.context);
		this.db = this.DbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		this.DbHelper.close();
	}

	public ArrayList<Discount> getAllDiscounts() {
		try {
			discount_list.clear();
			String selectQuery = "SELECT * FROM " + TABLE_DISCOUNT;

			SQLiteDatabase db = this.DbHelper.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			if (cursor.moveToFirst()) {
				do {
					Discount discount = new Discount();
					discount.discount_id = cursor.getInt(0);
					discount.name = cursor.getString(1);
					discount.percentage = cursor.getDouble(2);

					discount_list.add(discount);
				} while (cursor.moveToNext());
			}
			cursor.close();
			db.close();
		} catch (Exception e) {
			Log.e("get_alldiscount", "" + e);
		}
		return discount_list;
	}
}
