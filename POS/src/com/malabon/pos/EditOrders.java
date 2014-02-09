package com.malabon.pos;

import java.io.Serializable;
import java.text.DecimalFormat;

import com.malabon.object.ClsItem;
import com.malabon.object.ClsSale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class EditOrders extends Activity {
	ClsSale sale = null;
	DecimalFormat df = new DecimalFormat("0.00");
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_edit_orders);
		Bundle extras = getIntent().getExtras();
		if (extras == null) {
			setResult(Activity.RESULT_CANCELED);
	        finish();
	    }
		// Get data via the key
		sale = (ClsSale) extras.get("items");
		if (sale != null) {
		  // do something with the data
		  // display
		  loadDisplay();
		} 
	}
	
	public void loadDisplay() {
		int count = 0;
		TableLayout table = (TableLayout)findViewById(R.id.tableLayout);
		table.removeAllViews();
		for (ClsItem item : sale.items) {
			LayoutInflater vi = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
	    	TableRow newRow = (TableRow) vi.inflate(R.layout.product_edit_orders_row, null);
	    	
	    	TextView temp = (TextView)newRow.findViewById(R.id.cartItemName);	    	
	    	temp.setText(item.productName);	    	
	    	temp = (TextView)newRow.findViewById(R.id.cartItemCode);
	    	temp.setText(item.code); //"XXXX");
	    	temp = (TextView)newRow.findViewById(R.id.cartItemPrice);
	    	temp.setText(df.format(item.price));
	    	temp = (TextView)newRow.findViewById(R.id.cartItemQty);
	    	temp.setText(String.valueOf(item.quantity)); //"1");
	    	
	    	ImageButton deleteButton = (ImageButton)newRow.findViewById(R.id.cartBtnDelete);
	    	deleteButton.setTag(count);
	    	deleteButton.setOnClickListener(deleteClick);
	    	ImageButton editButton = (ImageButton)newRow.findViewById(R.id.cartBtnEdit);
	    	editButton.setTag(count);
	    	editButton.setOnClickListener(editClick);

	    	count++;
	    	
	    	table.addView(newRow, 0);
		}
		
		sale.computeTotal();
		
		TextView temp = (TextView)findViewById(R.id.netTotal);
		temp.setText(df.format(sale.total));
		temp = (TextView)findViewById(R.id.taxTotal);
		temp.setText(df.format(sale.taxTotal));
		temp = (TextView)findViewById(R.id.total);
		temp.setText(df.format(sale.netTotal));
		return;
	}
	
	public OnClickListener deleteClick = new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			ImageButton deleteButton = (ImageButton) arg0;
			int index = (Integer) deleteButton.getTag();
			sale.items.remove(index);
			loadDisplay();
		}
	};
	
	public OnClickListener editClick = new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			ImageButton editButton = (ImageButton) arg0;
			TableRow newRow = (TableRow)arg0.getParent().getParent();
			EditText editText = (EditText)newRow.findViewById(R.id.cartItemQty);
			
			int index = (Integer) editButton.getTag();
			ClsItem item = sale.items.get(index);
			item.quantity = Integer.parseInt(editText.getText().toString());
			
			sale.computeTotal();
			
			TextView temp = (TextView)findViewById(R.id.total);
			temp.setText(df.format(sale.total));
			temp = (TextView)findViewById(R.id.netTotal);
			temp.setText(df.format(sale.netTotal));
			temp = (TextView)findViewById(R.id.taxTotal);
			temp.setText(df.format(sale.taxTotal));
			
		}
	};
	
	public void pay(View view) {
		sale.computeTotal();
		
		SharedPreferences prefs = this.getSharedPreferences(
				"com.malabon.pos", Context.MODE_PRIVATE);
		prefs.edit().putString("balTotal", df.format(sale.total)).commit();
		
		Intent intent = new Intent(this, Payment.class);
        startActivity(intent);
    }
	
	public void close(View view) {
		Intent resultIntent = new Intent();
		// TODO Add extras or a data URI to this intent as appropriate.
		Bundle b = new Bundle();
		b.putSerializable("item", (Serializable) sale);
		resultIntent.putExtras(b);
		setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
	
	
}
