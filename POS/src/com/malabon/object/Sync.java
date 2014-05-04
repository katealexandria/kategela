package com.malabon.object;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.malabon.database.CustomerDB;
import com.malabon.database.DiscountDB;
import com.malabon.database.HistoryClearCacheDB;
import com.malabon.database.HistorySyncDB;
import com.malabon.database.IngredientDB;
import com.malabon.database.LogCashDB;
import com.malabon.database.PosSettingsDB;
import com.malabon.database.ProductCategoryDB;
import com.malabon.database.ProductDB;
import com.malabon.database.ReceiptDetailDB;
import com.malabon.database.RecipeDB;
import com.malabon.database.SalesDB;
import com.malabon.database.StockDB;
import com.malabon.database.UserSalesSummaryDB;

public class Sync {

	static DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

	public static User user;
	public static Bitmap CurrentUserBitmap;
	public static PosSettings posSettings;
	public static ReceiptDetail receiptDetail;
	public static String userSalesSummary;
	public static Sale lastSale;

	public static List<Sale> Sales;
	public static List<CancelledOrder> CancelledOrders;
	public static List<Item> OutOfStockItems;
	//public static List<UserSalesSummary> UserSalesSummaries;

	public static List<Ingredient> Ingredients;
	public static List<Recipe> Recipes;
	public static List<Item> Items;
	public static List<Category> Categories;
	public static List<Customer> Customers;
	// public static List<User> Users;

	public static List<Discount> Discounts;
	public static List<StockType> StockTypes;

	public static List<ClearCacheHistory> LstClearCacheHistory;
	public static List<SyncHistory> LstSyncHistory;

	public static List<Stock> Stocks;
	public static List<CashInOut> CashInOuts;

	public static void SetUser(int userid, String username) {
		user = new User();
		user.user_id = userid;
		user.username = username;
	}

	public static void SetSettings(Context context) {
		if (posSettings == null) {
			PosSettingsDB posSettingsDB = new PosSettingsDB(context);
			posSettingsDB.open();

			posSettings = posSettingsDB.getAllPosSettings();
		}
	}
	
	public static void SetReceiptDetail(Context context){
		if (receiptDetail == null){
			ReceiptDetailDB receiptDetailDB = new ReceiptDetailDB(context);
			receiptDetailDB.open();
			
			receiptDetail = receiptDetailDB.getAllReceiptDetails();
		}
	}
	
	public static String GetUserSalesSummaryID(){
		String tempID = userSalesSummary;
		userSalesSummary = null;
		return tempID;
	}

	public static void DoSync(Context context, Boolean isManual, int userId) {
		if (LstSyncHistory == null)
			GetSyncHistory(context);

		// For automatic sync, if already synced for current date, return.
		if (!LstSyncHistory.isEmpty()) {
			SyncHistory lastSh = LstSyncHistory.get(LstSyncHistory.size() - 1);
			if (dateFormat.format(new Date()) == dateFormat
					.format(lastSh.SyncDate) && !isManual)
				return;
		}

		// //////////////////
		// TODO: perform sync
		// //////////////////

		// Add to sync history
		SyncHistory sh = new SyncHistory();
		sh.IsManual = isManual;
		sh.SyncDate = new Date();
		sh.UserId = userId;
		LstSyncHistory.add(sh);
		
		HistorySyncDB historySyncDB = new HistorySyncDB(context);
		historySyncDB.open();
		historySyncDB.addHistorySync(sh);
	}

	// Clear image cache for user log-in
	public static void ClearCache(Context context, String cacheFolder, String userId) {
		if (LstClearCacheHistory == null)
			GetClearCacheHistory(context);

		// If already cleared for current date, return.
		ClearCacheHistory lastCch = LstClearCacheHistory
				.get(LstClearCacheHistory.size() - 1);
		if (dateFormat.format(new Date()) == dateFormat
				.format(lastCch.ClearDate))
			return;

		File cf = new File(cacheFolder);
		if (!cf.exists())
			return;

		for (File f : cf.listFiles()) {
			if (f.getName().contains("LoginUserMug_"))
				f.delete();
		}

		// Add to clear cache history
		ClearCacheHistory cch = new ClearCacheHistory();
		cch.ClearDate = new Date();
		cch.UserId = Integer.parseInt(userId);
		LstClearCacheHistory.add(cch);
		
		HistoryClearCacheDB historyClearCacheDB = new HistoryClearCacheDB(context);
		historyClearCacheDB.open();
		// TODO: handle error
		int num = historyClearCacheDB.addHistoryClearCache(cch);
	}

	public static Date GetNextSyncDate(Context context) {
		//int syncFrequencyInDays = 30;
		int syncFrequencyInDays = posSettings.sync_frequency;
		
		if (LstSyncHistory == null)
			GetSyncHistory(context);

		if (LstSyncHistory.isEmpty())
			return new Date(); // sync now

		SyncHistory sh = LstSyncHistory.get(LstSyncHistory.size() - 1);
		Calendar c = Calendar.getInstance();
		c.setTime(sh.SyncDate);
		c.add(Calendar.DATE, syncFrequencyInDays);

		return c.getTime();
	}

	public static Date GetNextClearCacheDate(Context context) {
		//int clearFrequencyInDays = 30;
		int clearFrequencyInDays = posSettings.clear_frequency;

		if (LstClearCacheHistory == null)
			GetClearCacheHistory(context);

		if (LstClearCacheHistory.isEmpty())
			return new Date(); // clear now

		ClearCacheHistory cch = LstClearCacheHistory.get(LstClearCacheHistory
				.size() - 1);
		Calendar c = Calendar.getInstance();
		c.setTime(cch.ClearDate);
		c.add(Calendar.DATE, clearFrequencyInDays);

		return c.getTime();
	}

	public static List<SyncHistory> GetSyncHistory(Context context) {
		if (LstSyncHistory == null){
			//LstSyncHistory = new ArrayList<SyncHistory>();
			
			HistorySyncDB historySyncDB = new HistorySyncDB(context);
			historySyncDB.open();
			LstSyncHistory = historySyncDB.getSyncHistory();
		}
		return LstSyncHistory;
	}

	public static List<ClearCacheHistory> GetClearCacheHistory(Context context) {
		if (LstClearCacheHistory == null){
			//LstClearCacheHistory = new ArrayList<ClearCacheHistory>();
			
			HistoryClearCacheDB historyClearCacheDB = new HistoryClearCacheDB(context);
			historyClearCacheDB.open();
			LstClearCacheHistory = historyClearCacheDB.getClearCacheHistory();
		}
		return LstClearCacheHistory;
	}

	// --- INVENTORY --- //

	// Use this method to get latest stock quantity from DB, then subtract sold
	// items.
	public static void RefreshInventory(Context context) {

		// reset items to null to force get from DB
		Items = null;
		GetItems(context);

		// update available quantity
		for (Sale sale : Sales) {
			for (Item item : sale.items) {
				Sync.UpdateProductQuantity(context, item.id, item.availableQty);
				Sync.UpdateIngredientsQuantity(context, item.id, item.quantity);
			}
		}
	}

	// ---SALES--- //
	public static void AddSale(Context context, Sale sale) {
		if (Sales == null)
			Sales = new ArrayList<Sale>();

		Sales.add(sale);

		SalesDB salesDB = new SalesDB(context);
		salesDB.open();
		int num = salesDB.newSale(sale);

		// Log cancelled items
		// LogCancelledOrders(sale.deletedItems,
		// GetUserById(sale.user).username);
		LogCancelledOrders(context, sale.deletedItems, user.user_id);
	}

	public static void LogCancelledOrders(Context context,
			List<Item> cancelledOrders, int user) {
		if (CancelledOrders == null)
			CancelledOrders = new ArrayList<CancelledOrder>();
		// Date date = new Date();
		for (Item i : cancelledOrders) {
			CancelledOrder co = new CancelledOrder();
			// co.CancelledDate = date;
			co.UserId = user;
			co.CancelledItem = i;
			CancelledOrders.add(co);
		}
	}

	public static void AddUserSalesSummary(Context context, int user, double cash_total, double cash_expected) {
		UserSalesSummaryDB userSalesSummaryDB = new UserSalesSummaryDB(context);
		userSalesSummaryDB.open();
		userSalesSummary = userSalesSummaryDB.addUserSalesSummary(user, cash_total, cash_expected);
		
		Log.d("temp_time", "AddUserSalesSummary..");
		Log.d("temp_time", userSalesSummary);
		
		ResetSalesForUser(user);
	}

	public static double GetUserExpectedCash(int user_id) {
		double expectedCash = 0;

		if (Sales != null) {
			for (Sale s : Sales) {
				if (s.user == user_id)
					expectedCash += s.total;
			}
		}
		
		if (CashInOuts != null){
			for (CashInOut c : CashInOuts){
				if (c.is_cash_in)
					expectedCash += c.amount;
				else
					expectedCash -= c.amount;
			}
		}

		return expectedCash;
	}

	// Use this method when all sales for the user has been committed to the
	// database.
	public static void ResetSalesForUser(int user_id) {

		// Get current sales for user
		List<Sale> userSales = new ArrayList<Sale>();
		for (Sale s : Sales) {
			if (s.user == user_id)
				userSales.add(s);
		}

		// Assuming the sales are already in DB, delete them from memory.
		for (Sale s : userSales)
			Sales.remove(s);
		
		// clear after close day
		if (CashInOuts != null)
			CashInOuts.clear();
	}

	// ---INGREDIENTS---//

	public static List<Ingredient> GetIngredients(Context context) {
		if (Ingredients == null) {
			IngredientDB ingredientDB = new IngredientDB(context);
			ingredientDB.open();
			Ingredients = ingredientDB.getAllIngredients();

			Stocks = new ArrayList<Stock>();
			StockDB stockDB = new StockDB(context);
			stockDB.open();
			Stocks = stockDB.getAllPerishableStocks("ingredient");

			for (Ingredient ingredient : Ingredients) {
				for (Stock stock : Stocks) {
					if (stock.id == ingredient.id) {
						ingredient.availableQty = stock.quantity;
						break;
					}
				}
			}
		}

		// TODO: delete when done testing
		Log.d("object_count", "counting ingredients");
		for (Ingredient ingredient : Ingredients) {
			Log.d("object_count", ingredient.name + ": "
					+ ingredient.availableQty);
		}

		return Ingredients;
	}

	public static Ingredient GetIngredientById(Context context, int id) {
		for (Ingredient ing : GetIngredients(context)) {
			if (ing.id == id)
				return ing;
		}
		return null;
	}

	public static void UpdateIngredientsQuantity(Context context,
			int productId, int soldQty) {
		if (Recipes == null)
			GetRecipes(context);

		for (Recipe rec : Recipes) {
			if (rec.product_id == productId)
				UpdateIngredientQuantity(context, rec.ingredient_id, soldQty
						* rec.ingredient_qty);
		}

		// TODO: delete when done testing
		Log.d("object_count", "counting ingredients after update");
		for (Ingredient ingredient : Ingredients) {
			Log.d("object_count", ingredient.name + ": "
					+ ingredient.availableQty);
		}
	}

	public static void UpdateIngredientQuantity(Context context,
			int ingredientId, double soldQty) {
		if (Ingredients == null)
			GetIngredients(context);

		for (Ingredient ing : Ingredients) {
			if (ing.id == ingredientId) {
				ing.availableQty -= soldQty;
				if (ing.availableQty < 0)
					ing.availableQty = 0;

				StockDB stockDB = new StockDB(context);
				stockDB.open();

				// TODO: handle error
				int num = stockDB.updateStock("ingredient", ing.availableQty,
						ing.id, user.user_id);

				return;
			}
		}
	}

	// ---RECIPES---//

	public static List<Recipe> GetRecipes(Context context) {
		if (Recipes == null) {
			RecipeDB recipeDB = new RecipeDB(context);
			recipeDB.open();

			Recipes = recipeDB.getAllRecipes();
		}

		return Recipes;
	}

	// ---ITEMS---//

	public static List<Item> GetItems(Context context) {
		if (Items == null) {
			ProductDB productDB = new ProductDB(context);
			productDB.open();

			Items = productDB.getAllProducts();
			for (Item item : Items) {
				item.quantity = 1;
				item.availableQty = GetItemAvailableQty(context, item.id);
			}
		}

		// TODO: delete when done testing
		Log.d("object_count", "counting items");
		for (Item item : Items) {
			Log.d("object_count", item.name + ": " + item.availableQty);
		}

		return Items;

	}

	public static int GetItemAvailableQty(Context context, int productId) {
		int availableQty = -1;
		for (Recipe rec : GetRecipes(context)) {
			if (rec.product_id == productId) {
				Ingredient ing = GetIngredientById(context, rec.ingredient_id);
				if (ing.availableQty >= rec.ingredient_qty) {
					double tempNum = ing.availableQty / rec.ingredient_qty;
					int tmpQty = (int) tempNum;
					if (tmpQty >= 1 && (availableQty == -1 || availableQty > tmpQty))
						availableQty = tmpQty;
				}
			}
		}
		
		if(availableQty < 0)
			availableQty = 0;
		
		return availableQty;
	}

	public static void UpdateProductQuantity(Context context, int productId,
			int newQty) {
		if (Items == null)
			GetItems(context);

		Item outOfStockItem = null;
		for (Item item : Items) {
			if (item.id == productId) {
				if (newQty == 0)
					outOfStockItem = item;
				item.availableQty = newQty;
				item.quantity = 1;

				StockDB stockDB = new StockDB(context);
				stockDB.open();
				// TODO: handle error
				int num = stockDB.updateStock("product", item.availableQty,
						item.id, user.user_id);

				break;
			}
		}

		if (outOfStockItem != null)
			ItemOutOfStock(outOfStockItem);

		// TODO: delete when done testing
		Log.d("object_count", "counting items after update");
		for (Item item : Items) {
			Log.d("object_count", item.name + ": " + item.availableQty);
		}
	}

	public static void ItemOutOfStock(Item item) {
		if (OutOfStockItems == null)
			OutOfStockItems = new ArrayList<Item>();
		OutOfStockItems.add(item);
		Items.remove(item);
	}

	// ---CATEGORIES---//

	public static List<Category> GetCategories(Context context) {
		if (Categories == null) {
			// Categories = new ArrayList<Category>();

			ProductCategoryDB productCategoryDB = new ProductCategoryDB(context);
			productCategoryDB.open();

			Categories = productCategoryDB.getAllProductCategories();
		}

		return Categories;
	}

	// ---CUSTOMERS---//

	public static List<Customer> GetCustomers(Context context) {
		if (Customers == null) {
			// Customers = new ArrayList<Customer>();

			CustomerDB customerDB = new CustomerDB(context);
			customerDB.open();

			Customers = customerDB.getAllCustomers();
		}
		return Customers;
	}

	public static int AddCustomer(Context context, Customer customer) {
		int num = 0;
		if (customer != null) {
			CustomerDB customerDB = new CustomerDB(context);
			customerDB.open();

			num = customerDB.ifExistsTelNo(customer.tel_no);

			if (num == 0) {
				customer.customer_id = customerDB.addCustomer(customer);

				if (customer.customer_id != null) {
					Customers.add(customer);
					num = 1;
				}
			} else
				num = 2;
		}
		return num;
	}

	public static int UpdateCustomer(Context context, Customer customer) {
		int num = 0;
		if (customer != null) {
			for (Customer c : Customers) {
				if (c.customer_id.equals(customer.customer_id)) {
					c.first_name = customer.first_name;
					c.last_name = customer.last_name;
					c.address = customer.address;
					c.address_landmark = customer.address_landmark;
					c.tel_no = customer.tel_no;
					c.mobile_no = customer.mobile_no;

					CustomerDB customerDB = new CustomerDB(context);
					customerDB.open();
					num = customerDB.updateCustomer(customer);

					break;
				}
			}
		}
		return num;
	}

	// ---DISCOUNTS---//

	public static List<Discount> GetDiscounts(Context context) {
		if (Discounts == null) {

			DiscountDB discountDB = new DiscountDB(context);
			discountDB.open();

			Discounts = discountDB.getAllDiscounts();
		}
		return Discounts;
	}

	public static boolean AddCashInOut(Context context, int iscashin, double amount, String description) {
		if (CashInOuts == null)
			CashInOuts = new ArrayList<CashInOut>();

		CashInOut cash = new CashInOut();
		cash.is_cash_in = iscashin > 0;
		cash.amount = amount;

		CashInOuts.add(cash);
		
		LogCashDB logCashDB = new LogCashDB(context);
		logCashDB.open();

		if (logCashDB.addLogCash(iscashin, amount, description) > 0)
			return true;
		else
			return false;
	}

	// ---STOCK TYPE---//

	public static List<StockType> GetStockTypes() {
		if (StockTypes == null) {
			StockTypes = new ArrayList<StockType>();

			StockType s = new StockType();
			s.stock_type_id = 1;
			s.name = "product";
			s.description = null;
			StockTypes.add(s);

			s = new StockType();
			s.stock_type_id = 2;
			s.name = "ingredient";
			s.description = null;
			StockTypes.add(s);

			s = new StockType();
			s.stock_type_id = 3;
			s.name = "clean";
			s.description = "xonrox";
			StockTypes.add(s);

			s = new StockType();
			s.stock_type_id = 4;
			s.name = "takeout";
			s.description = "spork";
			StockTypes.add(s);
		}
		return StockTypes;
	}
}
