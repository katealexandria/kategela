package com.malabon.object;

import java.util.ArrayList;
import java.util.List;

public class Sync {
	
	public static List<Ingredient> Ingredients;
	public static List<Recipe> Recipes;
	public static List<Item> Items;
	public static List<Category> Categories;
	
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
}
