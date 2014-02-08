package com.example.object;

import java.io.Serializable;

public class Customer {	
	public String customer_id;
	public String first_name;
	public String last_name;
	public String address;
	public String address_landmark;
	public String tel_no;
	public String mobile_no;

	public Customer() {
	}

	public Customer(String customer_id, String first_name, String last_name, String address, String address_landmark,
			String tel_no, String mobile_no) {
		this.customer_id = customer_id;
		this.first_name = first_name;
		this.last_name = last_name;
		this.address = address;
		this.address_landmark = address_landmark;
		this.tel_no = tel_no;
		this.mobile_no = mobile_no;
	}

	public Customer(String first_name, String last_name, String address, String address_landmark,
			String tel_no, String mobile_no) {
		this.first_name = first_name;
		this.last_name = last_name;
		this.address = address;
		this.address_landmark = address_landmark;
		this.tel_no = tel_no;
		this.mobile_no = mobile_no;
	}
	
	public String getCustomerId(){
		return this.customer_id;
	}
	
	public void setCustomerId(String customer_id){
		this.customer_id = customer_id;
	}
	
	public String getFirstName(){
		return this.first_name;
	}
	
	public void setFirstName(String first_name){
		this.first_name = first_name;
	}
	
	public String getLastName(){
		return this.last_name;
	}
	
	public void setLastName(String last_name){
		this.last_name = last_name;
	}
	
	public String getAddress(){
		return this.address;
	}
	
	public void setAddress(String address){
		this.address = address;
	}
	
	public String getAddressLandmark(){
		return this.address_landmark;
	}
	
	public void setAddressLandmark(String address_landmark){
		this.address_landmark = address_landmark;
	}
	
	public String getTelNo(){
		return this.tel_no;
	}
	
	public void setTelNo(String tel_no){
		this.tel_no = tel_no;
	}
	
	public String getMobileNo(){
		return this.mobile_no;
	}

	public void setMobileNo(String mobile_no){
		this.mobile_no = mobile_no;
	}
}