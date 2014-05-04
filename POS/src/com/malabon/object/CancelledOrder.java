package com.malabon.object;

import java.io.Serializable;

@SuppressWarnings("serial")
public class CancelledOrder implements Serializable {
	//public Date CancelledDate;
	public int UserId;
	public Item CancelledItem;
}
