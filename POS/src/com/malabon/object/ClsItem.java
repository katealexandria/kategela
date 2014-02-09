package com.malabon.object;

import java.io.Serializable;

public class ClsItem implements Serializable {

	public String productName;
	public String code;
	public double price;
	public int quantity;
	public int id;
	
	public Category category;
}