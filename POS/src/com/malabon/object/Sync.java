package com.malabon.object;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.graphics.Bitmap;

import com.malabon.pos.OrderType;

public class Sync {
	
	static DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
	
	public static Bitmap CurrentUserBitmap;
	
	public static List<Sale> Sales;
	public static List<CancelledOrder> CancelledOrders;
	public static List<Item> OutOfStockItems;
	public static List<UserSalesSummary> UserSalesSummaries;

	public static List<Ingredient> Ingredients;
	public static List<Recipe> Recipes;
	public static List<Item> Items;
	public static List<Category> Categories;
	public static List<Customer> Customers;
	public static List<User> Users;

	public static List<Discount> Discounts;
	public static List<OrderType> OrderTypes;
	public static PosSettings posSettings;
	public static List<UserQuestion> UserQuestions;
	public static List<StockType> StockTypes;
	
	public static List<ClearCacheHistory> LstClearCacheHistory;
	public static List<SyncHistory> LstSyncHistory;
	
	
	public static void DoSync(Boolean isManual, String userId){
		if(LstSyncHistory == null)
			GetSyncHistory();
		
		// For automatic sync, if already synced for current date, return.
		if(!LstSyncHistory.isEmpty()){
			SyncHistory lastSh = LstSyncHistory.get(LstSyncHistory.size() - 1);
			if(dateFormat.format(new Date()) == dateFormat.format(lastSh.SyncDate)
					&& !isManual)
				return;
		}
		
		////////////////////
		//TODO: perform sync
		////////////////////
		
		//Add to sync history		
		SyncHistory sh = new SyncHistory();
		sh.IsManual = isManual;
		sh.SyncDate = new Date();
		sh.UserId = userId;
		LstSyncHistory.add(sh);
	}
	
	// Clear image cache for user log-in
	public static void ClearCache(String cacheFolder, String userId){
		if(LstClearCacheHistory == null)
			GetClearCacheHistory();
		
		// If already cleared for current date, return.
		ClearCacheHistory lastCch = LstClearCacheHistory.get(LstClearCacheHistory.size() - 1);
		if(dateFormat.format(new Date()) == dateFormat.format(lastCch.ClearDate))
			return;
		
		File cf = new File(cacheFolder);
		if(!cf.exists())
			return;
		
		for(File f : cf.listFiles()){
			if(f.getName().contains("LoginUserMug_"))
				f.delete();
		}
		
		//Add to clear cache history
		ClearCacheHistory cch = new ClearCacheHistory();
		cch.ClearDate = new Date();
		cch.UserId = userId;
		LstClearCacheHistory.add(cch);
	}
	
	public static Date GetNextSyncDate(){
		//TODO: get actual sync frequency from DB
		int syncFrequencyInDays = 30;
		
		if(LstSyncHistory == null)
			GetSyncHistory();
		
		if(LstSyncHistory.isEmpty())
			return new Date(); // sync now
		
		SyncHistory sh = LstSyncHistory.get(LstSyncHistory.size() - 1);
		Calendar c = Calendar.getInstance();
		c.setTime(sh.SyncDate);
		c.add(Calendar.DATE, syncFrequencyInDays);
				
		return c.getTime();			
	}
	
	public static Date GetNextClearCacheDate(){
		//TODO: get actual frequency from DB
		int clearFrequencyInDays = 30;
		
		if(LstClearCacheHistory == null)
			GetClearCacheHistory();
		
		if(LstClearCacheHistory.isEmpty())
			return new Date(); // clear now
		
		ClearCacheHistory cch = LstClearCacheHistory.get(LstClearCacheHistory.size() - 1);
		Calendar c = Calendar.getInstance();
		c.setTime(cch.ClearDate);
		c.add(Calendar.DATE, clearFrequencyInDays);		
		
		return c.getTime();			
	}
	
	public static List<SyncHistory> GetSyncHistory(){
		if(LstSyncHistory == null)
			LstSyncHistory = new ArrayList<SyncHistory>();
		//TODO: populate with actual history, order by date ASC
		return LstSyncHistory;
	}
	
	public static List<ClearCacheHistory> GetClearCacheHistory(){
		if(LstClearCacheHistory == null)
			LstClearCacheHistory = new ArrayList<ClearCacheHistory>();
		//TODO: populate with actual history, order by date ASC
		return LstClearCacheHistory;
	}
	
	// --- INVENTORY --- //

	// Use this method to get latest stock quantity from DB, then subtract sold
	// items.
	public static void RefreshInventory() {

		// reset items to null to force get from DB
		Items = null;
		GetItems();

		// update available quantity
		for (Sale sale : Sales) {
			for (Item item : sale.items) {
				Sync.UpdateProductQuantity(item.id, item.availableQty);
				Sync.UpdateIngredientsQuantity(item.id, item.quantity);
			}
		}
	}

	// ---SALES--- //
	public static void AddSale(Sale sale) {
		if (Sales == null)
			Sales = new ArrayList<Sale>();
		Sales.add(sale);
		
		//Log cancelled items
		LogCancelledOrders(sale.deletedItems, GetUserById(sale.user).username);
	}
	
	public static void LogCancelledOrders(List<Item> cancelledOrders, String user){
		if(CancelledOrders == null)
			CancelledOrders = new ArrayList<CancelledOrder>();
		Date date = new Date();
		for(Item i : cancelledOrders){
			CancelledOrder co = new CancelledOrder();
			co.CancelledDate = date;
			co.UserId = user;
			co.CancelledItem = i;
			CancelledOrders.add(co);
		}
	}

	public static void AddUserSalesSummary(UserSalesSummary summary) {
		if (UserSalesSummaries == null)
			UserSalesSummaries = new ArrayList<UserSalesSummary>();
		UserSalesSummaries.add(summary);
		ResetSalesForUser(summary.user);
	}

	public static double GetUserExpectedCash(int user_id) {
		double expectedCash = 0;

		if (Sales != null) {
			for (Sale s : Sales) {
				if (s.user == user_id)
					expectedCash += s.total;
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
	}

	// ---INGREDIENTS---//

	public static List<Ingredient> GetIngredients() {
		if (Ingredients == null) {
			Ingredients = new ArrayList<Ingredient>();

			Ingredient ing = new Ingredient();
			ing.id = 1;
			ing.name = "Tomato";
			ing.availableQty = 20;
			Ingredients.add(ing);

			ing = new Ingredient();
			ing.id = 2;
			ing.name = "Bread";
			ing.availableQty = 10;
			Ingredients.add(ing);

			ing = new Ingredient();
			ing.id = 3;
			ing.name = "Noodles";
			ing.availableQty = 10;
			Ingredients.add(ing);

			ing = new Ingredient();
			ing.id = 4;
			ing.name = "Potato";
			ing.availableQty = 10;
			Ingredients.add(ing);

			ing = new Ingredient();
			ing.id = 5;
			ing.name = "Coke";
			ing.availableQty = 10;
			Ingredients.add(ing);

			ing = new Ingredient();
			ing.id = 6;
			ing.name = "Sprite";
			ing.availableQty = 10;
			Ingredients.add(ing);

			ing = new Ingredient();
			ing.id = 7;
			ing.name = "Milk";
			ing.availableQty = 10;
			Ingredients.add(ing);
		}

		return Ingredients;
	}

	public static Ingredient GetIngredientById(int id) {
		for (Ingredient ing : GetIngredients()) {
			if (ing.id == id)
				return ing;
		}
		return null;
	}

	public static void UpdateIngredientsQuantity(int productId, int soldQty) {
		if (Recipes == null)
			GetRecipes();

		for (Recipe rec : Recipes) {
			if (rec.product_id == productId)
				UpdateIngredientQuantity(rec.ingredient_id, soldQty
						* rec.ingredient_qty);
		}
	}

	public static void UpdateIngredientQuantity(int ingredientId, int soldQty) {
		if (Ingredients == null)
			GetIngredients();

		for (Ingredient ing : Ingredients) {
			if (ing.id == ingredientId) {
				ing.availableQty -= soldQty;
				return;
			}
		}
	}

	// ---RECIPES---//

	public static List<Recipe> GetRecipes() {
		if (Recipes == null) {
			Recipes = new ArrayList<Recipe>();

			Recipe rec = new Recipe();
			rec.recipe_id = 1;
			rec.product_id = 1; // pizza
			rec.ingredient_id = 1; // tomato
			rec.ingredient_qty = 2;
			Recipes.add(rec);

			rec = new Recipe();
			rec.recipe_id = 2;
			rec.product_id = 1; // pizza
			rec.ingredient_id = 2; // bread
			rec.ingredient_qty = 2;
			Recipes.add(rec);

			rec = new Recipe();
			rec.recipe_id = 3;
			rec.product_id = 2; // coke
			rec.ingredient_id = 5; // coke
			rec.ingredient_qty = 1;
			Recipes.add(rec);

			rec = new Recipe();
			rec.recipe_id = 4;
			rec.product_id = 3; // spaghetti
			rec.ingredient_id = 3; // tomato
			rec.ingredient_qty = 2;
			Recipes.add(rec);

			rec = new Recipe();
			rec.recipe_id = 5;
			rec.product_id = 3; // spaghetti
			rec.ingredient_id = 3; // noodles
			rec.ingredient_qty = 1;
			Recipes.add(rec);

			rec = new Recipe();
			rec.recipe_id = 6;
			rec.product_id = 4; // fries
			rec.ingredient_id = 4; // potato
			rec.ingredient_qty = 4;
			Recipes.add(rec);

			rec = new Recipe();
			rec.recipe_id = 7;
			rec.product_id = 5; // sprite
			rec.ingredient_id = 6; // sprite
			rec.ingredient_qty = 1;
			Recipes.add(rec);

			rec = new Recipe();
			rec.recipe_id = 8;
			rec.product_id = 6; // milk shake
			rec.ingredient_id = 7; // milk
			rec.ingredient_qty = 1;
			Recipes.add(rec);
		}

		return Recipes;
	}

	// ---ITEMS---//

	public static List<Item> GetItems() {
		if (Items == null) {
			Items = new ArrayList<Item>();

			Item item = new Item();
			item.name = "Pizza";
			item.price = 15.50;
			item.quantity = 1;
			item.category_id = 1;
			item.availableQty = GetItemAvailableQty(1);
			item.id = 1;
			Items.add(item);

			item = new Item();
			item.name = "Coke";
			item.price = 17.50;
			item.quantity = 1;
			item.category_id = 2;
			item.availableQty = GetItemAvailableQty(2);
			item.id = 2;
			Items.add(item);

			item = new Item();
			item.name = "Spaghetti";
			item.price = 5.00;
			item.quantity = 1;
			item.category_id = 1;
			item.availableQty = GetItemAvailableQty(3);
			item.id = 3;
			Items.add(item);

			item = new Item();
			item.name = "French Fries";
			item.price = 9.00;
			item.quantity = 1;
			item.category_id = 1;
			item.availableQty = GetItemAvailableQty(4);
			item.id = 4;
			Items.add(item);

			item = new Item();
			item.name = "Sprite";
			item.price = 21.50;
			item.quantity = 1;
			item.category_id = 2;
			item.availableQty = GetItemAvailableQty(5);
			item.id = 5;
			Items.add(item);

			item = new Item();
			item.name = "Milkshake";
			item.price = 11.50;
			item.quantity = 1;
			item.category_id = 2;
			item.availableQty = GetItemAvailableQty(6);
			item.id = 6;
			Items.add(item);

		}

		return Items;

	}

	public static int GetItemAvailableQty(int productId) {
		int availableQty = -1;
		for (Recipe rec : GetRecipes()) {
			if (rec.product_id == productId) {
				Ingredient ing = GetIngredientById(rec.ingredient_id);
				if (ing.availableQty >= rec.ingredient_qty) {
					int tmpQty = ing.availableQty / rec.ingredient_qty;
					if (tmpQty >= 1
							&& (availableQty == -1 || availableQty > tmpQty))
						availableQty = tmpQty;
				}
			}
		}
		return availableQty;
	}

	public static void UpdateProductQuantity(int productId, int newQty) {
		if (Items == null)
			GetItems();

		Item outOfStockItem = null;
		for (Item item : Items) {
			if (item.id == productId) {
				if (newQty == 0)
					outOfStockItem = item;
				item.availableQty = newQty;
				item.quantity = 1;
				break;
			}
		}

		if (outOfStockItem != null)
			ItemOutOfStock(outOfStockItem);
	}

	public static void ItemOutOfStock(Item item) {
		if (OutOfStockItems == null)
			OutOfStockItems = new ArrayList<Item>();
		OutOfStockItems.add(item);
		Items.remove(item);
	}

	// ---CATEGORIES---//

	public static List<Category> GetCategories() {
		if (Categories == null) {
			Categories = new ArrayList<Category>();

			Category catSolid = new Category();
			catSolid.name = "Solid";
			catSolid.id = 1;
			// catSolid.sortorder = 1;

			Category catLiquid = new Category();
			catLiquid.name = "Liquid";
			catLiquid.id = 2;
			// catLiquid.sortorder = 2;

			/*
			 * Category catSolid3 = new Category(); catSolid3.name = "Solid3";
			 * catSolid3.description = "Solid3"; catSolid3.id = 3;
			 * 
			 * Category catLiquid4 = new Category(); catLiquid4.name =
			 * "Liquid4"; catLiquid4.description = "Liquid4"; catLiquid4.id = 4;
			 * 
			 * Category catSolid5 = new Category(); catSolid5.name = "Solid5";
			 * catSolid5.description = "Solid5"; catSolid5.id = 5;
			 * 
			 * Category catLiquid6 = new Category(); catLiquid6.name =
			 * "Liquid6"; catLiquid6.description = "Liquid6"; catLiquid6.id = 6;
			 * 
			 * Categories.add(catSolid); Categories.add(catLiquid);
			 * Categories.add(catSolid3); Categories.add(catLiquid4);
			 * Categories.add(catSolid5); Categories.add(catLiquid6);
			 */

			Categories.add(catSolid);
			Categories.add(catLiquid);
		}

		return Categories;
	}

	// ---CUSTOMERS---//

	public static List<Customer> GetCustomers() {
		if (Customers == null) {
			Customers = new ArrayList<Customer>();
			Customer c;

			c = new Customer();
			c.customer_id = 1;
			c.first_name = "Noble";
			c.last_name = "Hodge";
			c.address = "Ap #708-5317 Arcu. St.";
			c.address_landmark = "Enim Mauris Quis LLC";
			c.tel_no = "09751856044";
			c.mobile_no = "9229519";
			Customers.add(c);

			c = new Customer();
			c.customer_id = 2;
			c.first_name = "Jeremy";
			c.last_name = "Gibson";
			c.address = "P.O. Box 402, 8731 Vitae, Street";
			c.address_landmark = "Ornare Tortor Institute";
			c.tel_no = "7469708";
			c.mobile_no = "09786189950";
			Customers.add(c);

			c = new Customer();
			c.customer_id = 3;
			c.first_name = "Evan";
			c.last_name = "Mcdowell";
			c.address = "487-7041 Neque St.";
			c.address_landmark = "In Faucibus Orci Industries";
			c.tel_no = "5225500";
			c.mobile_no = "09096149581";
			Customers.add(c);

			c = new Customer();
			c.customer_id = 4;
			c.first_name = "Magee";
			c.last_name = "Merrill";
			c.address = "2722 Diam Ave";
			c.address_landmark = "Sagittis Placerat Cras Associates";
			c.tel_no = "6484307";
			c.mobile_no = "09456244540";
			Customers.add(c);

			c = new Customer();
			c.customer_id = 5;
			c.first_name = "Hilel";
			c.last_name = "Christensen";
			c.address = "P.O. Box 325, 160 Et Rd.";
			c.address_landmark = "Adipiscing Elit Etiam Corp.";
			c.tel_no = "3499793";
			c.mobile_no = "09740522018";
			Customers.add(c);

			c = new Customer();
			c.customer_id = 6;
			c.first_name = "Kaye";
			c.last_name = "Palmer";
			c.address = "P.O. Box 365, 4958 Orci, Road";
			c.address_landmark = "Erat Eget Company";
			c.tel_no = "7812327";
			c.mobile_no = "09274836213";
			Customers.add(c);

			c = new Customer();
			c.customer_id = 7;
			c.first_name = "Mark";
			c.last_name = "Foster";
			c.address = "Ante Maecenas Mi Corporation";
			c.address_landmark = "P.O. Box 234, 6597 Mi Street";
			c.tel_no = "6267504";
			c.mobile_no = "09619654610";
			Customers.add(c);

			c = new Customer();
			c.customer_id = 8;
			c.first_name = "Germaine";
			c.last_name = "Lynch";
			c.address = "P.O. Box 559, 5084 Praesent Avenue";
			c.address_landmark = "Phasellus Corporation";
			c.tel_no = "4546833";
			c.mobile_no = "09231631996";
			Customers.add(c);

			c = new Customer();
			c.customer_id = 9;
			c.first_name = "Clare";
			c.last_name = "Mitchell";
			c.address = "3890 Dui. Road";
			c.address_landmark = "Aliquet Corp.";
			c.tel_no = "7207638";
			c.mobile_no = "09921536448";
			Customers.add(c);
		}
		return Customers;
	}

	// ---USERS---//
	// TODO: get only one user in DB, not all

	public static List<User> GetUsers() {
		if (Users == null) {
			Users = new ArrayList<User>();

			User u = new User();
			u.user_id = 1;
			u.username = "admin";
			Users.add(u);

			u = new User();
			u.user_id = 2;
			u.username = "kate";
			Users.add(u);

			u = new User();
			u.user_id = 3;
			u.username = "gela";
			Users.add(u);
		}
		return Users;
	}

	public static User GetUserById(int id) {
		for (User u : GetUsers()) {
			if (u.user_id == id)
				return u;
		}
		return null;
	}

	public static User GetUserByUsername(String username) {
		for (User u : GetUsers()) {
			if (u.username.equalsIgnoreCase(username))
				return u;
		}
		return null;
	}

	// ---DISCOUNTS---//

	public static List<Discount> GetDiscounts() {
		if (Discounts == null) {
			Discounts = new ArrayList<Discount>();

			Discount d = new Discount();
			d.discount_id = 1;
			d.name = "Senior Citizen";
			d.percentage = .05;
			Discounts.add(d);

			d = new Discount();
			d.discount_id = 2;
			d.name = "Student";
			d.percentage = .1;
			Discounts.add(d);

			d = new Discount();
			d.discount_id = 3;
			d.name = "PWD";
			d.percentage = .15;
			Discounts.add(d);
		}
		return Discounts;
	}

	// ---ORDER TYPES---//

	public static List<OrderType> GetOrderTypes() {
		if (OrderTypes == null) {
			OrderTypes = new ArrayList<OrderType>();

			OrderType o = new OrderType();
			o.order_type_id = 1;
			o.name = "Dine in";
			OrderTypes.add(o);

			o = new OrderType();
			o.order_type_id = 2;
			o.name = "Take out";
			OrderTypes.add(o);

			o = new OrderType();
			o.order_type_id = 3;
			o.name = "Delivery";
			OrderTypes.add(o);
		}
		return OrderTypes;
	}

	// ---POS SETTINGS---//

	public static PosSettings GetPosSettings() {
		if (posSettings == null) {

			PosSettings p = new PosSettings();
			p.branch_id = 1;
			p.branch_name = "Cubao";
			p.is_automatic = 0;
			p.sync_frequency = null;
			p.sync_time = null;
		}
		return posSettings;
	}

	// ---USER QUESTIONS---//
	//TODO: get only questions for one userid

	public static List<UserQuestion> GetUserQuestions() {
		if (UserQuestions == null) {
			UserQuestions = new ArrayList<UserQuestion>();

			UserQuestion u = new UserQuestion();
			u.id = 1;
			u.question = "What was your childhood nickname";
			u.answer = "secret";
			UserQuestions.add(u);

			u = new UserQuestion();
			u.id = 2;
			u.question = "In what city did you meet your spouse/significant other?";
			u.answer = "secret";
			UserQuestions.add(u);

			u = new UserQuestion();
			u.id = 3;
			u.question = "What is the name of your favorite childhood friend?";
			u.answer = "secret";
			UserQuestions.add(u);

			u = new UserQuestion();
			u.id = 4;
			u.question = "What street did you live on in third grade?";
			u.answer = "secret";
			UserQuestions.add(u);

			u = new UserQuestion();
			u.id = 5;
			u.question = "What is the middle name of your oldest child?";
			u.answer = "secret";
			UserQuestions.add(u);

			u = new UserQuestion();
			u.id = 6;
			u.question = "What is your oldest sibling's middle name?";
			u.answer = "secret";
			UserQuestions.add(u);
		}
		return UserQuestions;
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
