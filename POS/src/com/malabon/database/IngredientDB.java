package com.malabon.database;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.malabon.object.Ingredient;

public class IngredientDB {
	public static final String TABLE_INGREDIENT = "ingredient";

	public static final String KEY_INGREDIENT_ID = "ingredient_id";
	public static final String KEY_NAME = "name";
	public static final String KEY_UNIT = "unit";

	private final ArrayList<Ingredient> ingredient_list = new ArrayList<Ingredient>();

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

	public IngredientDB(Context ctx) {
		this.context = ctx;
	}

	public IngredientDB open() throws SQLException {
		this.DbHelper = new DatabaseHelper(this.context);
		this.db = this.DbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		this.DbHelper.close();
	}

	public ArrayList<Ingredient> getAllIngredients() {
		try {
			ingredient_list.clear();
			String selectQuery = "SELECT * FROM " + TABLE_INGREDIENT;

			SQLiteDatabase db = this.DbHelper.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			if (cursor.moveToFirst()) {
				do {
					Ingredient ingredient = new Ingredient();
					ingredient.id = cursor.getInt(0);
					ingredient.name = cursor.getString(1);
					ingredient.unit = cursor.getString(2);

					ingredient_list.add(ingredient);
				} while (cursor.moveToNext());
			}
			cursor.close();
			db.close();
		} catch (Exception e) {
			Log.e("get_allingredients", "" + e);
		}
		return ingredient_list;
	}
}
