package com.malabon.pos;

import java.io.Serializable;
import java.text.DecimalFormat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.malabon.object.Item;
import com.malabon.object.Sale;

public class EditOrders extends Activity {
	Sale sale = null;
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
		sale = (Sale) extras.get("items");
		if (sale != null) {
			// do something with the data
			// display
			loadDisplay();
		}
	}

	public void loadDisplay() {
		int count = 0;
		TableLayout table = (TableLayout) findViewById(R.id.tableLayout);
		table.removeAllViews();
		for (Item item : sale.items) {
			LayoutInflater vi = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
			TableRow newRow = (TableRow) vi.inflate(
					R.layout.product_edit_orders_row, null);

			TextView temp = (TextView) newRow.findViewById(R.id.cartItemName);
			temp.setText(item.name);
			temp = (TextView) newRow.findViewById(R.id.cartItemPrice);
			temp.setText(df.format(item.price));
			temp = (TextView) newRow.findViewById(R.id.cartItemQty);
			temp.setText(String.valueOf(item.quantity)); // "1");

			ImageButton deleteButton = (ImageButton) newRow
					.findViewById(R.id.cartBtnDelete);
			deleteButton.setTag(count);
			deleteButton.setOnClickListener(deleteClick);
			ImageButton moreQtyButton = (ImageButton) newRow
					.findViewById(R.id.cartMoreQty);
			moreQtyButton.setTag(count);
			moreQtyButton.setOnClickListener(moreQtyClick);
			ImageButton lessQtyButton = (ImageButton) newRow
					.findViewById(R.id.cartLessQty);
			lessQtyButton.setTag(count);
			lessQtyButton.setOnClickListener(lessQtyClick);

			count++;

			table.addView(newRow, 0);
		}

		sale.computeTotal();

		TextView temp = (TextView) findViewById(R.id.netTotal);
		temp.setText(df.format(sale.total));
		temp = (TextView) findViewById(R.id.taxTotal);
		temp.setText(df.format(sale.taxTotal));
		temp = (TextView) findViewById(R.id.total);
		temp.setText(df.format(sale.netTotal));
		return;
	}

	public OnClickListener deleteClick = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			ImageButton deleteButton = (ImageButton) arg0;
			int index = (Integer) deleteButton.getTag();
			Item item = sale.items.get(index);
			sale.items.remove(index);
			sale.deletedItems.add(item);
			loadDisplay();
		}
	};

	public OnClickListener moreQtyClick = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			addMinusQty(arg0, 1);
		}
	};
	
	public OnClickListener lessQtyClick = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			addMinusQty(arg0, -1);
		}
	};

	private void addMinusQty(View arg0, int number) {
		
		ImageButton qtyButton = (ImageButton) arg0;
		int index = (Integer) qtyButton.getTag();
		Item item = sale.items.get(index);
		
		if(item.availableQty == 0 && number > 0){
			showToast("Out of stock!");
			return;
		}
				
		TableRow newRow = (TableRow) arg0.getParent().getParent();
		EditText editText = (EditText) newRow.findViewById(R.id.cartItemQty);
		int num = Integer.parseInt(editText.getText().toString()) + number;
		if (num > 0)
			editText.setText(String.valueOf(num));
				
		item.quantity = Integer.parseInt(editText.getText().toString());
		item.availableQty -= number;

		sale.computeTotal();

		TextView temp = (TextView) findViewById(R.id.total);
		temp.setText(df.format(sale.total));
		temp = (TextView) findViewById(R.id.netTotal);
		temp.setText(df.format(sale.netTotal));
		temp = (TextView) findViewById(R.id.taxTotal);
		temp.setText(df.format(sale.taxTotal));
	}

	public void pay(View view) {
		sale.computeTotal();

		SharedPreferences prefs = this.getSharedPreferences("com.malabon.pos",
				Context.MODE_PRIVATE);
		prefs.edit().putString("balTotal", df.format(sale.total)).commit();

		Intent intent = new Intent(this, PaymentActivity.class);
		startActivity(intent);
	}

	public void close(View view) {
		Intent resultIntent = new Intent();
		Bundle b = new Bundle();
		b.putSerializable("item", (Serializable) sale);
		resultIntent.putExtras(b);
		setResult(Activity.RESULT_OK, resultIntent);
		finish();
	}
	
	private void showToast(String message) {
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT)
				.show();
	}

}
