package com.malabon.database;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.malabon.object.Recipe_;

public class RecipeDB {
	public static final String TABLE_RECIPE = "recipe";

	public static final String KEY_RECIPE_ID = "recipe_id";
	public static final String KEY_PRODUCT_ID = "product_id";
	public static final String KEY_INGREDIENT_ID = "ingredient_id";
	public static final String KEY_INGREDIENT_QUANTITY = "ingredient_quantity";

	private final ArrayList<Recipe_> recipe_list = new ArrayList<Recipe_>();

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

	public RecipeDB(Context ctx) {
		this.context = ctx;
	}

	public RecipeDB open() throws SQLException {
		this.DbHelper = new DatabaseHelper(this.context);
		this.db = this.DbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		this.DbHelper.close();
	}

	public ArrayList<Recipe_> getAllRecipes() {
		try {
			recipe_list.clear();
			String selectQuery = "SELECT * FROM " + TABLE_RECIPE;

			SQLiteDatabase db = this.DbHelper.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			if (cursor.moveToFirst()) {
				do {
					Recipe_ recipe = new Recipe_();
					recipe.recipe_id = Integer.parseInt(cursor.getString(0));
					recipe.product_id = Integer.parseInt(cursor.getString(1));
					recipe.ingredient_id = Integer
							.parseInt(cursor.getString(2));
					recipe.ingredient_quantity = Double.parseDouble(cursor
							.getString(3));

					recipe_list.add(recipe);
				} while (cursor.moveToNext());
			}
			cursor.close();
			db.close();
		} catch (Exception e) {
			Log.e("get_allrecipes", "" + e);
		}
		return recipe_list;
	}
}
