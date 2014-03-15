package com.malabon.object;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class Sale implements Serializable {

	public List<Item> items = new ArrayList<Item>();
	public List<Item> deletedItems = new ArrayList<Item>();
	public double total;
	public double taxTotal;
	public double netTotal;
	public double receiptDiscountPercent;
	public double receiptDiscountPhp;
	public Customer customer;
	public int user;
	
	public void computeTotal() {
		total = 0;
		for (Item item : items) {
			total += (item.price * item.quantity);
		}
		
		double totalDiscount = (total * receiptDiscountPercent) + receiptDiscountPhp;
		
		total = total - totalDiscount;
		netTotal = total * 0.93;
		taxTotal = total * 0.07;
	}
	
	public void setDefaultCustomer(){
		this.customer = new Customer();
		customer.customer_id = -1;
		customer.first_name = "Default";
		customer.last_name = "Customer";
	}
}
