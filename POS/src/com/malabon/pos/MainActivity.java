package com.malabon.pos;

import java.text.DecimalFormat;
import java.util.List;

import com.malabon.object.ClsItem;
import com.malabon.object.ClsSale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.GridLayout.LayoutParams;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	static final int EDIT_ORDERS_REQUEST = 10;  // The request code
	static final int SALE_OPTIONS_REQUEST = 11;  // The request code
	
	ClsSale sale = new ClsSale();
	public List<ClsItem> allItems = sale.GetAllItems();
	DecimalFormat df = new DecimalFormat("0.00");
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
				
		Initialize();
	}
	
	private void Initialize()
	{	
		TextView temp = (TextView)findViewById(R.id.cartItemName);
		
		temp = (TextView)findViewById(R.id.txtTotal);
    	temp.setText(df.format(sale.getTotal()));
		
    	temp = (TextView)findViewById(R.id.txtTaxTotal);
    	temp.setText(df.format(sale.getTaxTotal())); 
    	
		temp = (TextView)findViewById(R.id.txtNetTotal);
    	temp.setText(df.format(sale.getNetTotal()));

		//add buttons to grid layout
		LinearLayout grid = (LinearLayout)this.findViewById(R.id.productsGrid); 
		
		int count = 0;
		
		LayoutInflater vi = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
		LinearLayout rowHandle = null;
		
		//for (Map.Entry<String,String> entry : testMap.entrySet()) {
		
		for(ClsItem item : allItems) {
			LinearLayout layout = null;
			
			//check if odd or even style
			if(count % 2 == 1)
			{
				layout = (LinearLayout) vi.inflate(R.layout.product_button_odd,null);
			}
			else
			{
				layout = (LinearLayout) vi.inflate(R.layout.product_button_even,null);
			}
			
			layout.setOnClickListener(productClicked);
			layout.setId(item.code.hashCode());
			
			//set product attributes
			LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT);
			btnParams.weight = 1;
			btnParams.setMargins(5, 5, 5, 5);
			layout.setLayoutParams(btnParams);
			
			TextView itemName = (TextView)layout.findViewById(R.id.prodBtnItemName);
			TextView itemPrice = (TextView)layout.findViewById(R.id.prodBtnItemPrice);

			itemName.setText(item.productName); //entry.getKey());			
			itemPrice.setText(df.format(item.price)); //entry.getValue());
			
			if(count == 0)
			{
				//create a new row and set row attributes
				rowHandle = (LinearLayout) vi.inflate(R.layout.product_row,null);
				LinearLayout.LayoutParams newParam = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0);
				rowHandle.setOrientation(LinearLayout.HORIZONTAL);
				newParam.weight = 1;
				rowHandle.setLayoutParams(newParam);
				rowHandle.addView(layout);
				grid.addView(rowHandle);
			}
			else
			{
				//add to existing row
				rowHandle.addView(layout);
			}
			count++;
			count = count %4;
		}
	}

	// {{ others code
	
	
	// --------- BUTTON ACTIONS --------- //
		
	public void addCategory(View view) {
		Intent intent = new Intent(this, AddCategory.class);
        startActivity(intent);
    }
	
	public void editOrders(View view) {
		Intent intent = new Intent(this, EditOrders.class);
		intent.putExtra("items", sale);
        startActivityForResult(intent, EDIT_ORDERS_REQUEST);
    }
	
	public void addCustomer(View view) {
		Intent intent = new Intent(this, ViewCustomer.class);
        startActivity(intent);
    }
	
	public void saleOptions(View view) {
		Intent intent = new Intent(this, SaleOptions.class);
		startActivityForResult(intent, SALE_OPTIONS_REQUEST);
    }
	
	public void functions(View view) {
		Intent intent = new Intent(this, Functions.class);
        startActivity(intent);
    }
	
	public void login(View view) {
    	Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }
	
	public void pay(View view) {
		Intent intent = new Intent(this, Payment.class);
        startActivity(intent);
    }
	
	//Listeners
	public OnClickListener productClicked = new OnClickListener() {

	    @Override
	    public void onClick(View v) {
	    	
	    	int id = v.getId();
	    	ClsItem currentItem = new ClsItem();
	    	int currentItemIndex = -1;

	    	for(ClsItem item : allItems){
	    		if(item.code.hashCode() == id){
	    			currentItem = item;
	    			
	    			if(sale.items.contains(currentItem)) {
	    				currentItemIndex = 
	    						(sale.items.size() - sale.items.indexOf(currentItem))
	    						+ 1;
	    				sale.items.remove(currentItem);
	    				currentItem.quantity += 1;
	    			}
	    			
	    			sale.items.add(currentItem);
	    		}
	    	}
	    	
	        //
	    	String name = currentItem.productName;
	    			//((TextView)v.findViewById(R.id.prodBtnItemName)).getText().toString();
	    	String price = df.format(currentItem.price);
	    			//((TextView)v.findViewById(R.id.prodBtnItemPrice)).getText().toString();
	    	
	    	//create your object here
	    	//temporarily use strings to display
	    	LayoutInflater vi = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
	    	TableRow newRow = (TableRow) vi.inflate(R.layout.product_info_row, null);
	    	TableLayout tableLayout = (TableLayout)findViewById(R.id.productCart);
	    	
	    	TextView temp = (TextView)newRow.findViewById(R.id.cartItemName);
	    	temp.setText(name);
	    	temp = (TextView)newRow.findViewById(R.id.cartItemCode);
	    	temp.setText(currentItem.code); //"XXXX");
	    	temp = (TextView)newRow.findViewById(R.id.cartItemPrice);
	    	temp.setText(price);
	    	temp = (TextView)newRow.findViewById(R.id.cartItemDiscount);
	    	temp.setText(df.format(currentItem.discount)); //"0");
	    	temp = (TextView)newRow.findViewById(R.id.cartItemQty);
	    	temp.setText(String.valueOf(currentItem.quantity)); //"1");
	    	
	    	if(currentItemIndex != -1)
	    		tableLayout.removeViewAt(currentItemIndex);
	    	
	    	tableLayout.addView(newRow, 2);	    	
	    	
	    	//UPDATE TOTALS
	    	
	    	temp = (TextView)findViewById(R.id.txtTotal);
	    	temp.setText(df.format(sale.getTotal()));
			
	    	temp = (TextView)findViewById(R.id.txtTaxTotal);
	    	temp.setText(df.format(sale.getTaxTotal())); 
	    	
			temp = (TextView)findViewById(R.id.txtNetTotal);
	    	temp.setText(df.format(sale.getNetTotal()));
	    }
	};	
	
	private void bindOrderData()
	{
		TableLayout tableLayout = (TableLayout)findViewById(R.id.productCart);
		tableLayout.removeViews(2, tableLayout.getChildCount() - 7);
		for (ClsItem item : sale.items) {
			LayoutInflater vi = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
	    	TableRow newRow = (TableRow) vi.inflate(R.layout.product_info_row, null);
	    	
	    	TextView temp = (TextView)newRow.findViewById(R.id.cartItemName);
	    	temp.setText(item.productName);
	    	temp = (TextView)newRow.findViewById(R.id.cartItemCode);
	    	temp.setText(item.code); //"XXXX");
	    	temp = (TextView)newRow.findViewById(R.id.cartItemPrice);
	    	temp.setText(df.format(item.price));
	    	temp = (TextView)newRow.findViewById(R.id.cartItemDiscount);
	    	temp.setText(df.format(item.discount)); //"0");
	    	temp = (TextView)newRow.findViewById(R.id.cartItemQty);
	    	temp.setText(String.valueOf(item.quantity)); //"1");
	    	
	    	tableLayout.addView(newRow, 2);
		}

		TextView temp = (TextView)findViewById(R.id.txtTotal);
		temp.setText(df.format(sale.getTotal()));
		temp = (TextView)findViewById(R.id.txtTaxTotal);
		temp.setText(df.format(sale.getTaxTotal()));
		temp = (TextView)findViewById(R.id.txtNetTotal);
		temp.setText(df.format(sale.getNetTotal()));
	}
	// }}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
	  super.onActivityResult(requestCode, resultCode, intent);
	  switch(requestCode) {
	    case (EDIT_ORDERS_REQUEST) : {
		      if (resultCode == Activity.RESULT_OK) {
		        // TODO Extract the data returned from the child Activity.
		    	  Bundle data = intent.getExtras();
		    	  sale = (ClsSale) data.get("item");
		    	  bindOrderData();
		      }
		      break;
	    }
    	case(SALE_OPTIONS_REQUEST) : {
	    	  if (resultCode == Activity.RESULT_OK) {
	    		  SharedPreferences prefs = this.getSharedPreferences("com.malabon.pos", Context.MODE_PRIVATE);
	    		  sale.receiptDiscountPercent = prefs.getFloat("discountPercent", 0);
	    		  sale.receiptDiscountPhp = prefs.getFloat("discountPhp", 0);
	    		  TextView temp = (TextView)findViewById(R.id.txtTotal);
	    			temp.setText(df.format(sale.getTotal()));
	    			temp = (TextView)findViewById(R.id.txtTaxTotal);
	    			temp.setText(df.format(sale.getTaxTotal()));
	    			temp = (TextView)findViewById(R.id.txtNetTotal);
	    			temp.setText(df.format(sale.getNetTotal()));
	    	  }
	    	  break;
    	}
	  }		 
	}
}
