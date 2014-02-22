package com.malabon.pos;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.GridLayout.LayoutParams;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.malabon.object.Category;
import com.malabon.object.Item;
import com.malabon.object.Sale;
import com.malabon.object.Sync;

public class MainActivity extends Activity {

	// REQUEST CODES
	static final int EDIT_ORDERS_REQUEST = 10;
	static final int SALE_OPTIONS_REQUEST = 11;

	Sale sale;
	List<Item> allItems;
	List<Category> allCats;
	DecimalFormat df = new DecimalFormat("0.00");
	Category currentCat;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Initialize();
	}
	
	private void Initialize(){
		sale = new Sale();
		allItems = Sync.GetItems();
		allCats = Sync.GetCategories();
		
		// DBAdapter db = new DBAdapter(this).open();
		InitializeCategories();
		InitializeProducts(-1);
		InitializeCustomer();
	}

	private void InitializeCustomer(){
		String customername = getIntent().getStringExtra("CUSTOMER_NAME");
		if (customername != null){
			TextView tvCustomerName = (TextView) findViewById(R.id.tvCustomerName);
			tvCustomerName.setText(customername);
		}
	}

	private void InitializeCategories() {
		LinearLayout ll = (LinearLayout) findViewById(R.id.catButtonsContainer);

		for (Category cat : allCats) {
			LayoutInflater vi = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

			Button newButton = (Button) vi.inflate(R.layout.cat_button, null);
			newButton.setId(cat.id);
			newButton.setOnClickListener(catClicked);
			newButton.setText(cat.name);

			ll.addView(newButton, 3);
		}

		return;
	}

	private void InitializeProducts(int catId) {
		TextView temp = (TextView) findViewById(R.id.cartItemName);

		sale.computeTotal();

		temp = (TextView) findViewById(R.id.txtTotal);
		temp.setText(df.format(sale.total));

		temp = (TextView) findViewById(R.id.txtTaxTotal);
		temp.setText(df.format(sale.taxTotal));

		temp = (TextView) findViewById(R.id.txtNetTotal);
		temp.setText(df.format(sale.netTotal));

		// add buttons to grid layout
		LinearLayout grid = (LinearLayout) this.findViewById(R.id.productsGrid);
		grid.removeAllViews();

		int count = 0;

		LayoutInflater vi = (LayoutInflater) this
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		LinearLayout rowHandle = null;

		for (Item item : allItems) {

			// don't paint items from different category
			if (catId != -1 && catId != item.category_id)
				continue;

			LinearLayout layout = null;

			// check if odd or even style
			if (count % 2 == 1) {
				layout = (LinearLayout) vi.inflate(R.layout.product_button_odd,
						null);
			} else {
				layout = (LinearLayout) vi.inflate(
						R.layout.product_button_even, null);
			}

			layout.setOnClickListener(productClicked);
			layout.setId(item.id);

			// set product attributes
			LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(
					0, LayoutParams.MATCH_PARENT);
			btnParams.weight = 1;
			btnParams.setMargins(5, 5, 5, 5);
			layout.setLayoutParams(btnParams);

			TextView itemName = (TextView) layout
					.findViewById(R.id.prodBtnItemName);
			TextView itemPrice = (TextView) layout
					.findViewById(R.id.prodBtnItemPrice);

			itemName.setText(item.name); // entry.getKey());
			itemPrice.setText(df.format(item.price)); // entry.getValue());

			if (count == 0) {
				// create a new row and set row attributes
				rowHandle = (LinearLayout) vi.inflate(R.layout.product_row,
						null);
				LinearLayout.LayoutParams newParam = new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, 0);
				rowHandle.setOrientation(LinearLayout.HORIZONTAL);
				newParam.weight = 1;
				rowHandle.setLayoutParams(newParam);
				rowHandle.addView(layout);
				grid.addView(rowHandle);
			} else {
				// add to existing row
				rowHandle.addView(layout);
			}
			count++;
			count = count % 4;
		}
	}

	// {{ others code

	// --------- BUTTON ACTIONS --------- //

	public void addCategory(View view) {
		Intent intent = new Intent(this, AddCategory.class);
		startActivity(intent);
	}

	public void showAllCats(View view) {
		InitializeProducts(-1);
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
		sale.computeTotal();

		SharedPreferences prefs = this.getSharedPreferences("com.malabon.pos",
				Context.MODE_PRIVATE);
		prefs.edit().putString("balTotal", df.format(sale.total)).commit();

		Intent intent = new Intent(this, PaymentActivity.class);
		startActivity(intent);
	}

	// Listeners
	public OnClickListener productClicked = new OnClickListener() {

		@Override
		public void onClick(View v) {

			int id = v.getId();
			Item currentItem = new Item();
			int currentItemIndex = -1;

			// check if item was already added
			for (Item item : sale.items) {
				if (item.id == id) {
					currentItem = item;					
					if(currentItem.availableQty > 0){
						currentItemIndex = (sale.items.size() - sale.items
								.indexOf(currentItem)) + 1;
						sale.items.remove(currentItem);
						currentItem.quantity += 1;
					}
					break;
				}
			}			
			// if not, set item from allItems
			if (currentItemIndex == -1) {
				for (Item item : allItems) {
					if (item.id == id) {
						currentItem = item;
						break;
					}
				}
			}			
			
			if(currentItem.availableQty == 0){
				showToast("Out of stock!");
				return;
			}
			
			allItems.remove(currentItem);
			currentItem.availableQty -= 1;
			allItems.add(currentItem);			
			sale.items.add(currentItem);

			String name = currentItem.name;
			String price = df.format(currentItem.price);
			
			LayoutInflater vi = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
			TableRow newRow = (TableRow) vi.inflate(R.layout.product_info_row,
					null);
			TableLayout tableLayout = (TableLayout) findViewById(R.id.productCart);

			TextView temp = (TextView) newRow.findViewById(R.id.cartItemName);
			temp.setText(name);
			temp = (TextView) newRow.findViewById(R.id.cartItemPrice);
			temp.setText(price);
			temp = (TextView) newRow.findViewById(R.id.cartItemQty);
			temp.setText(String.valueOf(currentItem.quantity)); // "1");

			if (currentItemIndex != -1)
				tableLayout.removeViewAt(currentItemIndex);

			tableLayout.addView(newRow, 2);

			// UPDATE TOTALS
			sale.computeTotal();

			temp = (TextView) findViewById(R.id.txtTotal);
			temp.setText(df.format(sale.total));

			temp = (TextView) findViewById(R.id.txtTaxTotal);
			temp.setText(df.format(sale.taxTotal));

			temp = (TextView) findViewById(R.id.txtNetTotal);
			temp.setText(df.format(sale.netTotal));
		}
	};

	public OnClickListener catClicked = new OnClickListener() {
		@Override
		public void onClick(View v) {
			InitializeProducts(v.getId());
		}
	};

	private void bindOrderData() {
		TableLayout tableLayout = (TableLayout) findViewById(R.id.productCart);
		tableLayout.removeViews(2, tableLayout.getChildCount() - 7);
		for (Item item : sale.items) {
			LayoutInflater vi = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
			TableRow newRow = (TableRow) vi.inflate(R.layout.product_info_row,
					null);

			TextView temp = (TextView) newRow.findViewById(R.id.cartItemName);
			temp.setText(item.name);
			temp = (TextView) newRow.findViewById(R.id.cartItemPrice);
			temp.setText(df.format(item.price));
			temp = (TextView) newRow.findViewById(R.id.cartItemQty);
			temp.setText(String.valueOf(item.quantity));

			tableLayout.addView(newRow, 2);
		}

		sale.computeTotal();

		TextView temp = (TextView) findViewById(R.id.txtTotal);
		temp.setText(df.format(sale.total));
		temp = (TextView) findViewById(R.id.txtTaxTotal);
		temp.setText(df.format(sale.taxTotal));
		temp = (TextView) findViewById(R.id.txtNetTotal);
		temp.setText(df.format(sale.netTotal));
	}

	// }}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		switch (requestCode) {
		case (EDIT_ORDERS_REQUEST): {
			if (resultCode == Activity.RESULT_OK) {
				Bundle data = intent.getExtras();
				sale = (Sale) data.get("item");
				bindOrderData();
				
				// Reset qty. of deleted items
				for(Item d : sale.deletedItems){
					for(Item item : allItems){
						if(item.id == d.id){
							item.availableQty = Sync.GetItemAvailableQty(item.id);
							item.quantity = 1;
						}
					}
				}
			}
			break;
		}
		case (SALE_OPTIONS_REQUEST): {
			if (resultCode == Activity.RESULT_OK) {
				SharedPreferences prefs = this.getSharedPreferences(
						"com.malabon.pos", Context.MODE_PRIVATE);

				// Deduct discounts
				sale.receiptDiscountPercent = prefs.getFloat("discountPercent",
						0);
				sale.receiptDiscountPhp = prefs.getFloat("discountPhp", 0);
				sale.computeTotal();

				TextView temp = (TextView) findViewById(R.id.txtTotal);
				temp.setText(df.format(sale.total));
				temp = (TextView) findViewById(R.id.txtTaxTotal);
				temp.setText(df.format(sale.taxTotal));
				temp = (TextView) findViewById(R.id.txtNetTotal);
				temp.setText(df.format(sale.netTotal));

				// If new sale...
				boolean doNewSale = prefs.getBoolean("doNewSale", false);
				if (doNewSale) {
					sale = new Sale();
					bindOrderData();
				}
			}
			break;
		}
		}
	}
	
	private void showToast(String message) {
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT)
				.show();
	}
}
