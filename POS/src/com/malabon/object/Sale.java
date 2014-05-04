package com.malabon.object;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.malabon.function.NewID;

@SuppressWarnings("serial")
public class Sale implements Serializable {

	public List<Item> items = new ArrayList<Item>();
	public List<Item> deletedItems = new ArrayList<Item>();
	public double total;
	public double taxTotal;
	public double netTotal;
	public double cash;
	public Customer customer;
	public int user;

	public int orderType;
	public Discount discount;

	public void computeTotal() {
		total = getSubtotal();		

		double totalDiscount = 0;
		if (discount != null) {
			totalDiscount = getTotalDiscount();
			discount.amountPhp = totalDiscount;
		}

		total = total - totalDiscount;
		netTotal = total * 0.88;
		taxTotal = total * 0.12;
	}
	
	public double getTotalDiscount(){
		if(discount == null)
			return 0;
		
		return total * discount.percentage;
	}
	
	public double getSubtotal(){
		double subtotal = 0;
		for (Item item : items) {
			subtotal += (item.price * item.quantity);
		}
		return subtotal;
	}
	
	public double getChange(){
		return cash - total;
	}

	public void setDefaultCustomer() {
		this.customer = new Customer();
		// customer.customer_id = -1;
		customer.customer_id = new NewID().GetDefaultCustomerID();
		customer.first_name = "Default";
		customer.last_name = "Customer";
	}
}
