package com.malabon.database;

public class DBTable {
	public String get_TABLE_CUSTOMER() {
		return "CREATE TABLE "+ CustomerDB.TABLE_CUSTOMER +"(" 
				+ CustomerDB.KEY_CUSTOMER_ID + " nvarchar(25) NOT NULL primary key, "
				+ CustomerDB.KEY_FIRST_NAME + " nvarchar(50), "
				+ CustomerDB.KEY_LAST_NAME + " nvarchar(50), "
				+ CustomerDB.KEY_ADDRESS + " nvarchar(200), "
				+ CustomerDB.KEY_ADDRESS_LANDMARK + " nvarchar(200), "
				+ CustomerDB.KEY_TEL_NO + " nvarchar(15), "
				+ CustomerDB.KEY_MOBILE_NO + " nvarchar(15));";
	}
	
	public String get_TABLE_DISCOUNT(){
		return "CREATE TABLE "+ DiscountDB.TABLE_DISCOUNT +"(" 
				+ DiscountDB.KEY_DISCOUNT_ID + " INTEGER NOT NULL PRIMARY KEY, " 
				+ DiscountDB.KEY_NAME + " nvarchar(20) NOT NULL, " 
				+ DiscountDB.KEY_PERCENTAGE + " double NOT NULL);";
	}
	
	public String get_TABLE_HISTORY_CLEAR_CACHE(){
		return "CREATE TABLE "+ HistoryClearCacheDB.TABLE_HISTORY_CLEAR_CACHE +"(" 
				+ HistoryClearCacheDB.KEY_ID + " INTEGER NOT NULL PRIMARY KEY autoincrement, " 
				+ HistoryClearCacheDB.KEY_DATE + " date NOT NULL, " 
				+ HistoryClearCacheDB.KEY_USER_ID +" INTEGER NOT NULL, " 
				+ "foreign key ("+HistoryClearCacheDB.KEY_USER_ID+") references "+ UserDB.TABLE_USER +"("+UserDB.KEY_USER_ID+"));";
	}
	
	public String get_TABLE_HISTORY_SYNC(){
		return "CREATE TABLE "+ HistorySyncDB.TABLE_HISTORY_SYNC +"(" 
				+ HistorySyncDB.KEY_ID + " INTEGER NOT NULL PRIMARY KEY autoincrement, " 
				+ HistorySyncDB.KEY_DATE + " date NOT NULL, " 
				+ HistorySyncDB.KEY_USER_ID +" INTEGER NOT NULL, " 
				+ HistorySyncDB.KEY_IS_MANUAL + " boolean NOT NULL, "
				+ "foreign key ("+HistorySyncDB.KEY_USER_ID+") references "+ UserDB.TABLE_USER +"("+UserDB.KEY_USER_ID+"));";
	}
	
	public String get_TABLE_INGREDIENT(){
		return "CREATE TABLE "+ IngredientDB.TABLE_INGREDIENT +"(" 
				+ IngredientDB.KEY_INGREDIENT_ID + " INTEGER NOT NULL PRIMARY KEY, " 
				+ IngredientDB.KEY_NAME + " nvarchar(100) NOT NULL, " 
				+ IngredientDB.KEY_UNIT + " nvarchar(10) NOT NULL);";
	}

	public String get_TABLE_LOG_CANCEL_PRODUCT(){
		return "CREATE TABLE "+ LogCancelProductDB.TABLE_LOG_CANCEL_PRODUCT +"(" 
				+ LogCancelProductDB.KEY_ID + " INTEGER NOT NULL PRIMARY KEY autoincrement, " 
				+ LogCancelProductDB.KEY_DATE + " date NOT NULL, " 
				+ LogCancelProductDB.KEY_USER_ID +" INTEGER NOT NULL, " 
				+ LogCancelProductDB.KEY_PRODUCT_ID +" INTEGER NOT NULL, " 
								+ LogUserTimeSheetDB.KEY_SALES_SUMMARY_ID + " nvarchar(25), "
				+ "foreign key ("+LogCancelProductDB.KEY_USER_ID+") references "+UserDB.TABLE_USER+"("+UserDB.KEY_USER_ID+"), " 
				+ "foreign key ("+LogCancelProductDB.KEY_PRODUCT_ID+") references "+ProductDB.TABLE_PRODUCT+"("+ProductDB.KEY_PRODUCT_ID+"));";
	}
	
	public String get_TABLE_LOG_CASH(){
		return "CREATE TABLE "+ LogCashDB.TABLE_LOG_CASH +"( " 
				+ LogCashDB.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
				+ LogCashDB.KEY_IS_CASH_IN + " boolean NOT NULL, " 
				+ LogCashDB.KEY_AMOUNT +" double NOT NULL, " 
				+ LogCashDB.KEY_USER_ID +" INTEGER NOT NULL, " 
				+ LogCashDB.KEY_DESCRIPTION +" nvarchar(500), " 
				+ LogCashDB.KEY_DATE + " date NOT NULL, "
				+ "foreign key ("+LogCashDB.KEY_USER_ID+") references "+ UserDB.TABLE_USER +"("+UserDB.KEY_USER_ID+"));";
	}
	
	public String get_TABLE_LOG_USER_TIME_SHEET(){
		return "CREATE TABLE "+ LogUserTimeSheetDB.TABLE_LOG_USER_TIME_SHEET +"(" 
				+ LogUserTimeSheetDB.KEY_ID + " INTEGER NOT NULL PRIMARY KEY autoincrement, " 
				+ LogUserTimeSheetDB.KEY_USER_ID + " INTEGER NOT NULL, " 
				+ LogUserTimeSheetDB.KEY_TIMEIN +" date, " 
				+ LogUserTimeSheetDB.KEY_TIMEIN_IMAGE +" blob, " 
				+ LogUserTimeSheetDB.KEY_TIMEOUT +" date, " 
				+ LogUserTimeSheetDB.KEY_TIMEOUT_IMAGE +" blob, " 
				+ LogUserTimeSheetDB.KEY_SALES_SUMMARY_ID + " nvarchar(25), "
				+ "foreign key ("+LogUserTimeSheetDB.KEY_USER_ID+") references "+UserDB.TABLE_USER+"("+UserDB.KEY_USER_ID+"), " 
				+ "foreign key ("+LogUserTimeSheetDB.KEY_SALES_SUMMARY_ID+") references "+UserSalesSummaryDB.TABLE_SALES_SUMMARY_PER_USER+"("+UserSalesSummaryDB.KEY_SALES_SUMMARY_ID+"));";
	}
	
	public String get_TABLE_ORDER_TYPE(){
		return "CREATE TABLE "+ OrderTypeDB.TABLE_ORDER_TYPE +"(" 
				+ OrderTypeDB.KEY_ORDER_TYPE_ID + " INTEGER NOT NULL PRIMARY KEY, " 
				+ OrderTypeDB.KEY_NAME + " nvarchar(15) NOT NULL);";
	}
	
	public String get_TABLE_POS_SETTINGS(){
		return "CREATE TABLE "+ PosSettingsDB.TABLE_POS_SETTINGS +"(" 
				+ PosSettingsDB.KEY_ID + " INTEGER NOT NULL PRIMARY KEY, " 
				+ PosSettingsDB.KEY_BRANCH_ID + " INTEGER NOT NULL, " 
				+ PosSettingsDB.KEY_BRANCH_NAME +" nvarchar(100) NOT NULL, " 
				+ PosSettingsDB.KEY_IS_MANUAL +" boolean NOT NULL, " 
				+ PosSettingsDB.KEY_SYNC_FREQUENCY +" INTEGER NULL, " 
				+ PosSettingsDB.KEY_SYNC_TIME +" nvarchar(10) NULL, " 
				+ PosSettingsDB.KEY_CLEAR_FREQUENCY + " INTEGER NOT NULL);";
	}
	
	public String get_TABLE_PRODUCT(){
		return "CREATE TABLE "+ ProductDB.TABLE_PRODUCT +"(" 
				+ ProductDB.KEY_PRODUCT_ID + " INTEGER NOT NULL PRIMARY KEY, " 
				+ ProductDB.KEY_NAME + " nvarchar(100) NOT NULL, " 
				+ ProductDB.KEY_PRICE +" double NOT NULL, " 
				+ ProductDB.KEY_UNIT +" nvarchar(10) NOT NULL, " 
				+ ProductDB.KEY_CATEGORY_ID +" INTEGER NOT NULL, " 
				+ ProductDB.KEY_SORTORDER +" INTEGER NOT NULL, " 
				+ ProductDB.KEY_CAN_BE_TAKEN_OUT + " boolean NOT NULL, "
				+ "foreign key ("+ProductDB.KEY_CATEGORY_ID+") references "+ProductCategoryDB.TABLE_PRODUCT_CATEGORY+"("+ProductCategoryDB.KEY_CATEGORY_ID+"));";
	}
	
	public String get_TABLE_PRODUCT_CATEGORY(){
		return "CREATE TABLE "+ ProductCategoryDB.TABLE_PRODUCT_CATEGORY +"(" 
				+ ProductCategoryDB.KEY_CATEGORY_ID + " INTEGER NOT NULL PRIMARY KEY, " 
				+ ProductCategoryDB.KEY_NAME + " nvarchar(100) NOT NULL, " 
				+ ProductCategoryDB.KEY_SORTORDER + " INTEGER NOT NULL);";
	}
	
	public String get_TABLE_TABLE_RECEIPT_DETAIL(){
		return "CREATE TABLE "+ ReceiptDetailDB.TABLE_RECEIPT_DETAIL +"(" 
				+ ReceiptDetailDB.KEY_RECEIPT_ID + " INTEGER NOT NULL PRIMARY KEY, " 
				+ ReceiptDetailDB.KEY_STORE_NAME + " nvarchar(200) NOT NULL, " 
				+ ReceiptDetailDB.KEY_OPERATED_BY + " nvarchar(200) NOT NULL, " 
				+ ReceiptDetailDB.KEY_ADDRESS + " nvarchar(200) NOT NULL, " 
				+ ReceiptDetailDB.KEY_PERMIT_NO + " nvarchar(50), " 
				+ ReceiptDetailDB.KEY_TIN_NO + " nvarchar(50), " 
				+ ReceiptDetailDB.KEY_SERIAL_NO + " nvarchar(50), " 
				+ ReceiptDetailDB.KEY_ACCR_NO + " nvarchar(50), " 
				+ ReceiptDetailDB.KEY_MIN_NO + " nvarchar(50), " 
				+ ReceiptDetailDB.KEY_MESSAGE + " nvarchar(500));";
	}	
	
	public String get_TABLE_RECIPE(){
		return "CREATE TABLE "+ RecipeDB.TABLE_RECIPE +"(" 
				+ RecipeDB.KEY_RECIPE_ID + " INTEGER NOT NULL PRIMARY KEY, " 
				+ RecipeDB.KEY_PRODUCT_ID + " INTEGER NOT NULL, " 
				+ RecipeDB.KEY_INGREDIENT_ID +" INTEGER NOT NULL, " 
				+ RecipeDB.KEY_INGREDIENT_QUANTITY + " double NOT NULL, " 
				+ "foreign key ("+RecipeDB.KEY_PRODUCT_ID+") references "+ProductDB.TABLE_PRODUCT+"("+ProductDB.KEY_PRODUCT_ID+"));";
	}
	
	public String get_TABLE_SALES_SUMMARY_PER_USER(){
		return "CREATE TABLE "+ UserSalesSummaryDB.TABLE_SALES_SUMMARY_PER_USER +"(" 
				+ UserSalesSummaryDB.KEY_SALES_SUMMARY_ID + " nvarchar(25) NOT NULL PRIMARY KEY, " 
				+ UserSalesSummaryDB.KEY_CASH_TOTAL + " double NOT NULL, " 
				+ UserSalesSummaryDB.KEY_CASH_EXPECTED + " double NOT NULL);"; 
	}
	
	public String get_TABLE_STOCK(){
		return "CREATE TABLE "+ StockDB.TABLE_STOCK +"(" 
				+ StockDB.KEY_STOCK_ID + " INTEGER NOT NULL PRIMARY KEY, " 
				+ StockDB.KEY_STOCK_TYPE_ID + " INTEGER NOT NULL, " 
				+ StockDB.KEY_ID +" INTEGER NOT NULL, " 
				+ StockDB.KEY_QUANTITY + " double NOT NULL, " 
				+ StockDB.KEY_LAST_UPDATED_DATE + " date NOT NULL, " 
				+ StockDB.KEY_LAST_UPDATED_USER_ID + " INTEGER NOT NULL, " 
				+ "foreign key ("+StockDB.KEY_STOCK_TYPE_ID+") references "+UserDB.TABLE_USER+"("+StockTypeDB.KEY_STOCK_TYPE_ID+"), " 
				+ "foreign key ("+StockDB.KEY_LAST_UPDATED_USER_ID+") references "+UserSalesSummaryDB.TABLE_SALES_SUMMARY_PER_USER+"("+UserDB.KEY_USER_ID+"));";
	}
	
	public String get_TABLE_STOCK_TYPE(){
		return "CREATE TABLE "+ StockTypeDB.TABLE_STOCK_TYPE +"(" 
				+ StockTypeDB.KEY_STOCK_TYPE_ID + " INTEGER NOT NULL PRIMARY KEY, " 
				+ StockTypeDB.KEY_NAME + " nvarchar(100) NOT NULL, " 
				+ StockTypeDB.KEY_DESCRIPTION + " nvarchar(100));";
	}
	
	public String get_TABLE_USER(){
		return "CREATE TABLE "+ UserDB.TABLE_USER +"(" 
				+ UserDB.KEY_USER_ID + " INTEGER NOT NULL PRIMARY KEY, " 
				+ UserDB.KEY_USERNAME + " nvarchar(20) NOT NULL, " 
				+ UserDB.KEY_PASSWORD +" nvarchar(20) NOT NULL, " 
				+ UserDB.KEY_IS_ADMIN + " boolean NOT NULL);";
	}
	
	public String get_TABLE_USER_QUESTION(){
		return "CREATE TABLE "+ UserQuestionDB.TABLE_USER_QUESTION +"(" 
				+ UserQuestionDB.KEY_ID + " INTEGER NOT NULL PRIMARY KEY, " 
				+ UserQuestionDB.KEY_USER_ID + " INTEGER NOT NULL, " 
				+ UserQuestionDB.KEY_QUESTION +" nvarchar(200) NOT NULL, " 
				+ UserQuestionDB.KEY_ANSWER + " nvarchar(200) NOT NULL, " 
				+ "foreign key ("+UserQuestionDB.KEY_USER_ID+") references "+UserDB.TABLE_USER+"("+UserDB.KEY_USER_ID+"));";
	}
}
