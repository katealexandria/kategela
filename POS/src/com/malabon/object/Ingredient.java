package com.malabon.object;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Ingredient implements Serializable {
	public int id;
	public String name;
	public int availableQty;		//TODO: update in stock DB
	public String unit;
}
