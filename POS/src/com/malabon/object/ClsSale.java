package com.malabon.object;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ClsSale implements Serializable{

	public List<ClsItem> items = new ArrayList<ClsItem>();
	public double total;
	public double taxTotal;
	public double netTotal;
	public double receiptDiscountPercent;
	public double receiptDiscountPhp;
	
	public List<ClsItem> GetAllItems(){
		
		List<ClsItem> allItems = new ArrayList<ClsItem>();
		
		ClsItem item1 = new ClsItem();		
		item1.productName = "Pizza";
		item1.code = "PIZ";
		item1.price = 15.50;
		item1.discount = 0;
		item1.quantity = 1;
		allItems.add(item1);
		
		ClsItem item2 = new ClsItem();	
		item2.productName = "Coke";
		item2.code = "COK";
		item2.price = 17.50;
		item2.discount = 0;
		item2.quantity = 1;
		allItems.add(item2);
		
		ClsItem item3 = new ClsItem();	
		item3.productName = "Spaghetti";
		item3.code = "SPA";
		item3.price = 5.00;
		item3.discount = 0;
		item3.quantity = 1;
		allItems.add(item3);
		
		ClsItem item4 = new ClsItem();	
		item4.productName = "French Fries";
		item4.code = "FRE";
		item4.price = 9.00;
		item4.discount = 0;
		item4.quantity = 1;
		allItems.add(item4);
		
		return allItems;
		
	}

	public double getTotal(){
		total = 0;
		for(ClsItem item : items){
			total += ((item.price * item.quantity) - item.discount);
		}
		total = (total * (1 - receiptDiscountPercent))- receiptDiscountPhp;
		return total;
	}
	
	public double getNetTotal(){
		netTotal = total * 0.93;
		return netTotal;
	}
	
	public double getTaxTotal(){
		taxTotal = total * 0.07;
		return taxTotal;
	}
}
