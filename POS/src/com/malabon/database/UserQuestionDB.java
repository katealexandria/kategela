package com.malabon.database;

import com.malabon.object.UserQuestion;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class UserQuestionDB {
	public static final String TABLE_USER_QUESTION = "user_question";
	
	public static final String KEY_ID = "id";
	public static final String KEY_USER_ID = "user_id";
	public static final String KEY_QUESTION = "question";
	public static final String KEY_ANSWER = "answer";

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
	
	public UserQuestionDB(Context ctx) {
		this.context = ctx;
	}

	public UserQuestionDB open() throws SQLException {
		this.DbHelper = new DatabaseHelper(this.context);
		this.db = this.DbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		this.DbHelper.close();
	}
	
	public int validateQuestion(int id, int user_id, String answer) {
		int num = 0;
		try {
			SQLiteDatabase db = this.DbHelper.getReadableDatabase();

			Cursor cursor = db.rawQuery("SELECT "+ KEY_ID +" FROM " + TABLE_USER_QUESTION
					+ " WHERE " + KEY_ID + " = '" + id + "'"
					+ " AND " + KEY_USER_ID + " = '" + user_id + "'"
					+ " AND " + KEY_ANSWER + " = '" + answer + "'", null);

			if (cursor != null) {
				cursor.moveToFirst();
				num = 1;
			}
			cursor.close();
			db.close();
		} catch (Exception e) {
			Log.e("get_userQuestion", "" + e);
		}
		return num;
	}
}
