package com.malabon.database;

public class DBTable {
	public String get_TABLE_CUSTOMER() {
		return "CREATE TABLE customer(" 
				+ CustomerDB.KEY_CUSTOMER_ID + " nvarchar(25) NOT NULL primary key, "
				+ CustomerDB.KEY_FIRST_NAME + " nvarchar(50) NULL, "
				+ CustomerDB.KEY_LAST_NAME + " nvarchar(50) NULL, "
				+ CustomerDB.KEY_LAST_NAME + " nvarchar(200) NULL, "
				+ CustomerDB.KEY_ADDRESS + " nvarchar(200) NULL, "
				+ CustomerDB.KEY_TEL_NO + " nvarchar(15) NULL, "
				+ CustomerDB.KEY_MOBILE_NO + " nvarchar(15) NULL);";
	}
	
	public String get_TABLE_USER(){
		return "CREATE TABLE user(" 
				+ UserDB.KEY_USER_ID + " integer NOT NULL PRIMARY KEY, " 
				+ UserDB.KEY_USERNAME + " nvarchar(20) NOT NULL, " 
				+ UserDB.KEY_PASSWORD +" nvarchar(20) NOT NULL, " 
				+ UserDB.KEY_IS_ADMIN + " tinyint NOT NULL);";
	}
}
