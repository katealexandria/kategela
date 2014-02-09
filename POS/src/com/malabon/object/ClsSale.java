package com.malabon.object;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class ClsSale implements Serializable {

	public List<ClsItem> items = new ArrayList<ClsItem>();
	public double total;
	public double taxTotal;
	public double netTotal;
	public double receiptDiscountPercent;
	public double receiptDiscountPhp;
	
	public List<ClsItem> GetAllItems() {

		List<ClsItem> allItems = new ArrayList<ClsItem>();
		
		Category catSolid = new Category();
		catSolid.name = "Solid";
		catSolid.description = "Solid";
		catSolid.id = 1;
		
		Category catLiquid = new Category();
		catLiquid.name = "Liquid";
		catLiquid.description = "Liquid";
		catSolid.id = 2;

		ClsItem item = new ClsItem();
		item.productName = "Pizza";
		item.code = "PIZ";
		item.price = 15.50;
		item.quantity = 1;
		item.category = catSolid;
		item.id = 1;
		allItems.add(item);

		item = new ClsItem();
		item.productName = "Coke";
		item.code = "COK";
		item.price = 17.50;
		item.quantity = 1;
		item.category = catLiquid;
		item.id = 2;
		allItems.add(item);

		item = new ClsItem();
		item.productName = "Spaghetti";
		item.code = "SPA";
		item.price = 5.00;
		item.quantity = 1;
		item.category = catSolid;
		item.id = 3;
		allItems.add(item);

		item = new ClsItem();
		item.productName = "French Fries";
		item.code = "FRE";
		item.price = 9.00;
		item.quantity = 1;
		item.category = catSolid;
		item.id = 4;
		allItems.add(item);
		
		item = new ClsItem();
		item.productName = "Sprite";
		item.code = "SPR";
		item.price = 21.50;
		item.quantity = 1;
		item.category = catLiquid;
		item.id = 5;
		allItems.add(item);
		
		item = new ClsItem();
		item.productName = "Milkshake";
		item.code = "MIL";
		item.price = 11.50;
		item.quantity = 1;
		item.category = catLiquid;
		item.id = 6;
		allItems.add(item);

		return allItems;

	}
	
	public List<Category> GetAllCategories(){
		
		List<Category> allCats = new ArrayList<Category>();
		
		Category catSolid = new Category();
		catSolid.name = "Solid";
		catSolid.description = "Solid";
		catSolid.id = 1;
		
		Category catLiquid = new Category();
		catLiquid.name = "Liquid";
		catLiquid.description = "Liquid";
		catSolid.id = 2;
		
		allCats.add(catSolid);
		allCats.add(catLiquid);
		
		return allCats;
	}
	
	public void computeTotal() {
		total = 0;
		for (ClsItem item : items) {
			total += (item.price * item.quantity);
		}
		
		double totalDiscount = (total * receiptDiscountPercent) + receiptDiscountPhp;
		
		total = total - totalDiscount;
		netTotal = total * 0.93;
		taxTotal = total * 0.07;
	}
}
