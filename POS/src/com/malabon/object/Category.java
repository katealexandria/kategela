package com.malabon.object;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Category implements Serializable {
	public int id;
	public String name;
	public String description;		//TODO: whats this?
	public int sortorder;
}
