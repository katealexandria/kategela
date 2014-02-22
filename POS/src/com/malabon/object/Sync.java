package com.malabon.object;

import java.util.ArrayList;
import java.util.List;

public class Sync {
	
	public static List<Ingredient> Ingredients;
	public static List<Recipe> Recipes;
	public static List<Item> Items;
	public static List<Category> Categories;
	public static List<Customer> Customers;
	public static List<User> Users;
	
	//---INGREDIENTS---//
	
	public static List<Ingredient> GetIngredients(){
		if(Ingredients == null){		
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
	
	public static Ingredient GetIngredientById(int id){
		for(Ingredient ing : GetIngredients()){
			if(ing.id == id)
				return ing;
		}
		return null;
	}
	
	public static void UpdateIngredientsQuantity(int productId, int soldQty){
		if(Recipes == null)
			return;
		
		for(Recipe rec : Recipes){
			if(rec.product_id == productId)
				UpdateIngredientQuantity(rec.ingredient_id, soldQty * rec.ingredient_qty);
		}
	}
	
	public static void UpdateIngredientQuantity(int ingredientId, int soldQty){
		if(Ingredients == null)
			return;
		
		for(Ingredient ing : Ingredients){
			if(ing.id == ingredientId){
				ing.availableQty -= soldQty;
				return;
			}
		}
	}
	
	//---RECIPES---//
	
	public static List<Recipe> GetRecipes(){
		if(Recipes == null){		
			Recipes = new ArrayList<Recipe>();
			
			Recipe rec = new Recipe();
			rec.recipe_id = 1;
			rec.product_id = 1;	//pizza
			rec.ingredient_id = 1;	//tomato
			rec.ingredient_qty = 2;
			Recipes.add(rec);
			
			rec = new Recipe();
			rec.recipe_id = 2;
			rec.product_id = 1;	//pizza
			rec.ingredient_id = 2;	//bread
			rec.ingredient_qty = 2;
			Recipes.add(rec);
			
			rec = new Recipe();
			rec.recipe_id = 3;
			rec.product_id = 2;	//coke
			rec.ingredient_id = 5;	//coke
			rec.ingredient_qty = 1;
			Recipes.add(rec);
			
			rec = new Recipe();
			rec.recipe_id = 4;
			rec.product_id = 3;	//spaghetti
			rec.ingredient_id = 3;	//tomato
			rec.ingredient_qty = 2;
			Recipes.add(rec);
			
			rec = new Recipe();
			rec.recipe_id = 5;
			rec.product_id = 3;	//spaghetti
			rec.ingredient_id = 3;	//noodles
			rec.ingredient_qty = 1;
			Recipes.add(rec);
			
			rec = new Recipe();
			rec.recipe_id = 6;
			rec.product_id = 4;	//fries
			rec.ingredient_id = 4;	//potato
			rec.ingredient_qty = 4;
			Recipes.add(rec);
			
			rec = new Recipe();
			rec.recipe_id = 7;
			rec.product_id = 5;	//sprite
			rec.ingredient_id = 6;	//sprite
			rec.ingredient_qty = 1;
			Recipes.add(rec);
			
			rec = new Recipe();
			rec.recipe_id = 8;
			rec.product_id = 6;	//milk shake
			rec.ingredient_id = 7;	//milk
			rec.ingredient_qty = 1;
			Recipes.add(rec);
		}
		
		return Recipes;
	}
		
	//---ITEMS---//
	
	public static List<Item> GetItems() {
		if(Items == null){
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
		
	public static int GetItemAvailableQty(int productId){
		int availableQty = -1;
		for(Recipe rec : GetRecipes()){
			if(rec.product_id == productId){
				Ingredient ing = GetIngredientById(rec.ingredient_id);
				if(ing.availableQty >= rec.ingredient_qty){
					int tmpQty = ing.availableQty / rec.ingredient_qty;
				if(tmpQty >= 1 && (availableQty == -1 || availableQty > tmpQty))
						availableQty = tmpQty;
				}
			}
		}
		return availableQty;
	}
	
	public static void UpdateProductQuantity(int productId, int newQty){
		if(Items == null)
			return;
		
		for(Item item : Items){
			if(item.id == productId){
				item.availableQty = newQty;
				item.quantity = 1;
				return;
			}
		}
	}
	
	//---CATEGORIES---//
	
	public static List<Category> GetCategories(){		
		if(Categories == null){
			Categories = new ArrayList<Category>();
			
			Category catSolid = new Category();
			catSolid.name = "Solid";
			catSolid.description = "Solid";
			catSolid.id = 1;
			
			Category catLiquid = new Category();
			catLiquid.name = "Liquid";
			catLiquid.description = "Liquid";
			catLiquid.id = 2;
			
			Categories.add(catSolid);
			Categories.add(catLiquid);
		}
		
		return Categories;
	}
	
	//---CUSTOMERS---//
	
	public static List<Customer> GetCustomers(){
		if(Customers == null){
			Customers = new ArrayList<Customer>();
			Customer c;
			
			c = new Customer();
			c.first_name = "Noble";
			c.last_name = "Hodge";
			c.address = "Ap #708-5317 Arcu. St.";
			c.address_landmark = "Enim Mauris Quis LLC";
			c.tel_no = "09751856044";
			c.mobile_no = "9229519";
			Customers.add(c);
			
			c = new Customer();
			c.first_name = "Jeremy";
			c.last_name = "Gibson";
			c.address = "P.O. Box 402, 8731 Vitae, Street";
			c.address_landmark = "Ornare Tortor Institute";
			c.tel_no = "7469708";
			c.mobile_no = "09786189950";
			Customers.add(c);
			
			c = new Customer();
			c.first_name = "Evan";
			c.last_name = "Mcdowell";
			c.address = "487-7041 Neque St.";
			c.address_landmark = "In Faucibus Orci Industries";
			c.tel_no = "5225500";
			c.mobile_no = "09096149581";
			Customers.add(c);
			
			c = new Customer();
			c.first_name = "Magee";
			c.last_name = "Merrill";
			c.address = "2722 Diam Ave";
			c.address_landmark = "Sagittis Placerat Cras Associates";
			c.tel_no = "6484307";
			c.mobile_no = "09456244540";
			Customers.add(c);
			
			c = new Customer();
			c.first_name = "Hilel";
			c.last_name = "Christensen";
			c.address = "P.O. Box 325, 160 Et Rd.";
			c.address_landmark = "Adipiscing Elit Etiam Corp.";
			c.tel_no = "3499793";
			c.mobile_no = "09740522018";
			Customers.add(c);
			
			c = new Customer();
			c.first_name = "Kaye";
			c.last_name = "Palmer";
			c.address = "P.O. Box 365, 4958 Orci, Road";
			c.address_landmark = "Erat Eget Company";
			c.tel_no = "7812327";
			c.mobile_no = "09274836213";
			Customers.add(c);
			
			c = new Customer();
			c.first_name = "Mark";
			c.last_name = "Foster";
			c.address = "Ante Maecenas Mi Corporation";
			c.address_landmark = "P.O. Box 234, 6597 Mi Street";
			c.tel_no = "6267504";
			c.mobile_no = "09619654610";
			Customers.add(c);
			
			c = new Customer();
			c.first_name = "Germaine";
			c.last_name = "Lynch";
			c.address = "P.O. Box 559, 5084 Praesent Avenue";
			c.address_landmark = "Phasellus Corporation";
			c.tel_no = "4546833";
			c.mobile_no = "09231631996";
			Customers.add(c);
			
			c = new Customer();
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

	//---USERS---//
	
	public static List<User> GetUsers(){
		if(Users == null){
			Users = new ArrayList<User>();
			
			User u = new User();
			u.user_id = 1;
			u.username = "admin";
			u.is_admin = 1;
			Users.add(u);
			
			u = new User();
			u.user_id = 2;
			u.username = "kate";
			u.is_admin = 0;
			Users.add(u);
			
			u = new User();
			u.user_id = 3;
			u.username = "gela";
			u.is_admin = 0;
			Users.add(u);
		}
		return Users;
	}

}
