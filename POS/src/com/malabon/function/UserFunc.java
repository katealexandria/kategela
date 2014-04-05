package com.malabon.function;

import java.sql.Blob;

import android.content.Context;

import com.malabon.database.DBAdapter;
import com.malabon.database.LogUserTimeSheetDB;
import com.malabon.database.UserDB;

public class UserFunc {
	public boolean isValidUser(Context context, String user_name, String user_password){
		Boolean isvalid = false;
		
		UserDB userDB = new UserDB(context);
		userDB.open();
		isvalid = userDB.validateLogin(user_name, user_password);
		
		return isvalid;
	}
	
	public void addTimein(Context context, int user_id, byte[] timein_image){
		LogUserTimeSheetDB logUserTimeSheetDB = new LogUserTimeSheetDB(context);
		logUserTimeSheetDB.open();
		logUserTimeSheetDB.addTimein(user_id, null);	//TODO: image
	}
	
	public boolean isAdmin(Context context, String user_name, String user_password){
		Boolean isvalid = false;
		
		UserDB userDB = new UserDB(context);
		userDB.open();
		isvalid = userDB.validateAdmin(user_name, user_password);
		
		return isvalid;
	}
}
