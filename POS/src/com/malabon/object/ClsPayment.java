package com.malabon.object;

public class ClsPayment {
	public double balance;
	public double cash;
	private double change;
	
	public double getChange(){
		change = cash - balance;
		return change;
	}
}
