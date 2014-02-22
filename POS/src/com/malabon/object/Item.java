package com.malabon.object;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Item implements Serializable {
	
	public int id;
	public String name;
	public double price;
	public int quantity;	
	public int category_id;
	public int availableQty;
}