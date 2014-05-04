package com.malabon.database;

import java.util.Date;

import com.malabon.object.Item;
import com.malabon.object.Sale;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SalesProductDB {
	public static final String TABLE_SALES_PRODUCT = "sales_product";

	public static final String KEY_ID = "id";
	public static final String KEY_SALES_ID = "sales_id";
	public static final String KEY_PRODUCT_ID = "product_id";
	public static final String KEY_QUANTITY = "quantity";

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

	public SalesProductDB(Context ctx) {
		this.context = ctx;
	}

	public SalesProductDB open() throws SQLException {
		this.DbHelper = new DatabaseHelper(this.context);
		this.db = this.DbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		this.DbHelper.close();
	}
	
	public void addSaleProduct(Sale sale, int id){
		Log.d("temp_debug", "addSaleProduct...");
		try{
			SQLiteDatabase db = this.DbHelper.getWritableDatabase();
			
			for (Item item : sale.items){
				
				Log.d("temp_debug", String.valueOf(id));
				Log.d("temp_debug", String.valueOf(item.id));
				Log.d("temp_debug", String.valueOf(item.quantity));
				
				ContentValues values = new ContentValues();
				values.put(KEY_SALES_ID, id);
				values.put(KEY_PRODUCT_ID, item.id);
				values.put(KEY_QUANTITY, item.quantity);
				db.insert(TABLE_SALES_PRODUCT, null, values);
			}
			db.close();
			Log.d("pos", "addSaleProduct - success");
		}catch (Exception e) {
			Log.e("pos_error", "addSaleProduct" + e);
		}
		Log.d("temp_debug", "addSaleProduct end...");
	}
}
