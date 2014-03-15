package com.malabon.database;

public class DBTable {
	public String get_TABLE_CUSTOMER() {
		return "CREATE TABLE "+ CustomerDB.TABLE_CUSTOMER +"(" 
				+ CustomerDB.KEY_CUSTOMER_ID + " nvarchar(25) NOT NULL primary key, "
				+ CustomerDB.KEY_FIRST_NAME + " nvarchar(50) NULL, "
				+ CustomerDB.KEY_LAST_NAME + " nvarchar(50) NULL, "
				+ CustomerDB.KEY_LAST_NAME + " nvarchar(200) NULL, "
				+ CustomerDB.KEY_ADDRESS + " nvarchar(200) NULL, "
				+ CustomerDB.KEY_TEL_NO + " nvarchar(15) NULL, "
				+ CustomerDB.KEY_MOBILE_NO + " nvarchar(15) NULL);";
	}
	
	public String get_TABLE_DISCOUNT(){
		return "CREATE TABLE "+ DiscountDB.TABLE_DISCOUNT +"(" 
				+ DiscountDB.KEY_DISCOUNT_ID + " int NOT NULL PRIMARY KEY, " 
				+ DiscountDB.KEY_NAME + " nvarchar(20) NOT NULL, " 
				+ DiscountDB.KEY_PERCENTAGE + " double NOT NULL);";
	}
	
	public String get_TABLE_INGREDIENT(){
		return "CREATE TABLE "+ IngredientDB.TABLE_INGREDIENT +"(" 
				+ IngredientDB.KEY_INGREDIENT_ID + " int NOT NULL PRIMARY KEY, " 
				+ IngredientDB.KEY_NAME + " nvarchar(100) NOT NULL, " 
				+ IngredientDB.KEY_UNIT + " nvarchar(10) NOT NULL);";
	}

	
	public String get_TABLE_LOG_CASH(){
		return "CREATE TABLE "+ LogCashDB.TABLE_LOG_CASH +"(" 
				+ LogCashDB.KEY_ID + " int NOT NULL PRIMARY KEY autoincrement, " 
				+ LogCashDB.KEY_IS_CASH_IN + " boolean NOT NULL, " 
				+ LogCashDB.KEY_AMOUNT +" double NOT NULL, " 
				+ LogCashDB.KEY_USER_ID +" int NOT NULL, " 
				+ LogCashDB.KEY_DATE + " date NOT NULL, "
				+ "foreign key ("+LogCashDB.KEY_USER_ID+") references "+ UserDB.TABLE_USER +"("+UserDB.KEY_USER_ID+"));";
	}
	
	public String get_TABLE_LOG_USER_TIME_SHEET(){
		return "CREATE TABLE "+ LogUserTimeSheetDB.TABLE_LOG_USER_TIME_SHEET +"(" 
				+ LogUserTimeSheetDB.KEY_ID + " int NOT NULL PRIMARY KEY autoincrement, " 
				+ LogUserTimeSheetDB.KEY_USER_ID + " int NOT NULL, " 
				+ LogUserTimeSheetDB.KEY_TIMEIN +" date, " 
				+ LogUserTimeSheetDB.KEY_TIMEIN_IMAGE +" blob, " 
				+ LogUserTimeSheetDB.KEY_TIMEOUT +" date, " 
				+ LogUserTimeSheetDB.KEY_TIMEOUT_IMAGE +" blob, " 
				+ LogUserTimeSheetDB.KEY_SALES_SUMMARY_ID + " int, "
				+ "foreign key ("+LogUserTimeSheetDB.KEY_USER_ID+") references "+UserDB.TABLE_USER+"("+UserDB.KEY_USER_ID+"), " 
				+ "foreign key ("+LogUserTimeSheetDB.KEY_SALES_SUMMARY_ID+") references "+UserSalesSummaryDB.TABLE_SALES_SUMMARY_PER_USER+"("+UserSalesSummaryDB.KEY_SALES_SUMMARY_ID+"));";
	}
	
	public String get_TABLE_ORDER_TYPE(){
		return "CREATE TABLE "+ OrderTypeDB.TABLE_ORDER_TYPE +"(" 
				+ OrderTypeDB.KEY_ORDER_TYPE_ID + " int NOT NULL PRIMARY KEY, " 
				+ OrderTypeDB.KEY_NAME + " nvarchar(15) NOT NULL);";
	}
	
	public String get_TABLE_POS_SETTINGS(){
		return "CREATE TABLE "+ PosSettingsDB.TABLE_POS_SETTINGS +"(" 
				+ PosSettingsDB.KEY_ID + " int NOT NULL PRIMARY KEY, " 
				+ PosSettingsDB.KEY_BRANCH_ID + " int NOT NULL, " 
				+ PosSettingsDB.KEY_BRANCH_NAME +" nvarchar(100) NOT NULL, " 
				+ PosSettingsDB.KEY_IS_AUTOMATIC +" boolean NOT NULL, " 
				+ PosSettingsDB.KEY_SYNC_FREQUENCY +" nvarchar(20) NOT NULL, " 
				+ PosSettingsDB.KEY_SYNC_TIME + " date NOT NULL);";
	}
	
	public String get_TABLE_PRODUCT(){
		return "CREATE TABLE "+ ProductDB.TABLE_PRODUCT +"(" 
				+ ProductDB.KEY_PRODUCT_ID + " int NOT NULL PRIMARY KEY, " 
				+ ProductDB.KEY_NAME + " nvarchar(100) NOT NULL, " 
				+ ProductDB.KEY_PRICE +" double NOT NULL, " 
				+ ProductDB.KEY_UNIT +" nvarchar(10) NOT NULL, " 
				+ ProductDB.KEY_CATEGORY_ID +" int NOT NULL, " 
				+ ProductDB.KEY_SORTORDER +" int NOT NULL, " 
				+ ProductDB.KEY_CAN_BE_TAKEN_OUT + " boolean NOT NULL, "
				+ "foreign key ("+ProductDB.KEY_CATEGORY_ID+") references "+ProductCategoryDB.TABLE_PRODUCT_CATEGORY+"("+ProductCategoryDB.KEY_CATEGORY_ID+"));";
	}
	
	public String get_TABLE_PRODUCT_CATEGORY(){
		return "CREATE TABLE "+ ProductCategoryDB.TABLE_PRODUCT_CATEGORY +"(" 
				+ ProductCategoryDB.KEY_CATEGORY_ID + " int NOT NULL PRIMARY KEY, " 
				+ ProductCategoryDB.KEY_NAME + " nvarchar(100) NOT NULL, " 
				+ ProductCategoryDB.KEY_SORTORDER + " int NOT NULL);";
	}
	
	public String get_TABLE_RECIPE(){
		return "CREATE TABLE "+ RecipeDB.TABLE_RECIPE +"(" 
				+ RecipeDB.KEY_RECIPE_ID + " int NOT NULL PRIMARY KEY, " 
				+ RecipeDB.KEY_PRODUCT_ID + " int NOT NULL, " 
				+ RecipeDB.KEY_INGREDIENT_ID +" int NOT NULL, " 
				+ RecipeDB.KEY_INGREDIENT_QUANTITY + " double NOT NULL, " 
				+ "foreign key ("+RecipeDB.KEY_PRODUCT_ID+") references "+ProductDB.TABLE_PRODUCT+"("+ProductDB.KEY_PRODUCT_ID+"));";
	}
	
	public String get_TABLE_SALES_SUMMARY_PER_USER(){
		return "CREATE TABLE "+ UserSalesSummaryDB.TABLE_SALES_SUMMARY_PER_USER +"(" 
				+ UserSalesSummaryDB.KEY_SALES_SUMMARY_ID + " nvarchar(25) NOT NULL PRIMARY KEY, " 
				+ UserSalesSummaryDB.KEY_CASH_TOTAL + " double NOT NULL, " 
				+ UserSalesSummaryDB.KEY_CASH_EXPECTED + " double NOT NULL);"; 
	}
	
	public String get_TABLE_STOCK_TYPE(){
		return "CREATE TABLE "+ StockTypeDB.TABLE_STOCK_TYPE +"(" 
				+ StockTypeDB.KEY_STOCK_TYPE_ID + " int NOT NULL PRIMARY KEY, " 
				+ StockTypeDB.KEY_NAME + " nvarchar(100) NOT NULL, " 
				+ StockTypeDB.KEY_DESCRIPTION + " nvarchar(100));";
	}
	
	public String get_TABLE_USER(){
		return "CREATE TABLE "+ UserDB.TABLE_USER +"(" 
				+ UserDB.KEY_USER_ID + " int NOT NULL PRIMARY KEY, " 
				+ UserDB.KEY_USERNAME + " nvarchar(20) NOT NULL, " 
				+ UserDB.KEY_PASSWORD +" nvarchar(20) NOT NULL, " 
				+ UserDB.KEY_IS_ADMIN + " boolean NOT NULL);";
	}
	
	public String get_TABLE_USER_QUESTION(){
		return "CREATE TABLE "+ UserQuestionDB.TABLE_USER_QUESTION +"(" 
				+ UserQuestionDB.KEY_ID + " int NOT NULL PRIMARY KEY, " 
				+ UserQuestionDB.KEY_USER_ID + " int NOT NULL, " 
				+ UserQuestionDB.KEY_QUESTION +" nvarchar(200) NOT NULL, " 
				+ UserQuestionDB.KEY_ANSWER + " nvarchar(200) NOT NULL, " 
				+ "foreign key ("+UserQuestionDB.KEY_USER_ID+") references "+UserDB.TABLE_USER+"("+UserDB.KEY_USER_ID+"));";
	}
}
