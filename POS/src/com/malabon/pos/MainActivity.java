package com.malabon.pos;

import java.text.DecimalFormat;
import java.util.List;

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
import com.malabon.object.Customer;
import com.malabon.object.Item;
import com.malabon.object.Sale;
import com.malabon.object.Sync;
import com.malabon.object.User;

public class MainActivity extends Activity {

	// REQUEST CODES
	static final int EDIT_ORDERS_REQUEST = 10;
	static final int SALE_OPTIONS_REQUEST = 11;
	static final int SELECT_CUSTOMER_REQUEST = 12;
	static final int LOGIN_REQUEST = 13;
	static final int PAYMENT_REQUEST = 14;

	Sale sale;
	List<Item> allItems;
	List<Category> allCats;
	DecimalFormat df = new DecimalFormat("0.00");
	Category currentCat;
	User currentUser;
	int firstCatIndex, lastCatIndex;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);	
		
		if(currentUser == null)
			Login(false, null);
		else
			Initialize();
	}
	
	private void Initialize(){
		Customer c = null;
		if(sale != null)
			c = sale.customer;
		
		sale = new Sale();
		sale.customer = c;
		sale.user = currentUser.user_id; 
		allItems = Sync.GetItems();
		allCats = Sync.GetCategories();
		
		firstCatIndex = 0;		
		lastCatIndex = 4;
		if(allCats.size() < 5)
			lastCatIndex = allCats.size() - 1;
		
		// DBAdapter db = new DBAdapter(this).open();
		
		InitializeUser();
		InitializeCategories();
		InitializeProducts(-1);
		InitializeCustomer();
	}

	private void InitializeUser(){		
		TextView username = (TextView) findViewById(R.id.currentUserName);
		username.setText(currentUser.username);
	}
	
	private void InitializeCustomer(){
		if(sale.customer == null)
			sale.setDefaultCustomer();
		
		TextView tvCustomerName = (TextView) findViewById(R.id.tvCustomerName);
		tvCustomerName.setText(sale.customer.first_name + " " + sale.customer.last_name);
	}

	private void InitializeCategories() {
		findViewById(R.id.btnPrevCat).setEnabled(true);
		findViewById(R.id.btnNextCat).setEnabled(true);
		
		if(firstCatIndex == 0 || allCats.size() < 5)
			findViewById(R.id.btnPrevCat).setEnabled(false);
		if(lastCatIndex == allCats.size() - 1  || allCats.size() < 5)
			findViewById(R.id.btnNextCat).setEnabled(false);
		
		LinearLayout ll = (LinearLayout) findViewById(R.id.catButtonsContainer);
		ll.removeAllViews();

		for (int i = firstCatIndex; i <= lastCatIndex; i++) {
			Category cat = allCats.get(i);
			LayoutInflater vi = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

			Button newButton = (Button) vi.inflate(R.layout.cat_button, null);
			newButton.setId(cat.id);
			newButton.setOnClickListener(catClicked);
			newButton.setText(cat.name);

			ll.addView(newButton);
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
	
	public void prevCat(View view) {
		if(firstCatIndex == 0 || allCats.size() < 5)
			return;
		firstCatIndex--;
		lastCatIndex--;
		InitializeCategories();
	}
	
	public void nextCat(View view) {
		if(lastCatIndex == allCats.size() - 1 || allCats.size() < 5)
			return;
		firstCatIndex++;
		lastCatIndex++;
		InitializeCategories();
	}

	public void showAllCats(View view) {
		InitializeProducts(-1);
	}

	public void editOrders(View view) {
		Intent intent = new Intent(this, EditOrders.class);
		intent.putExtra("Sale_EditOrders", sale);
		startActivityForResult(intent, EDIT_ORDERS_REQUEST);
	}

	public void addCustomer(View view) {
		SharedPreferences prefs = this.getSharedPreferences(
				"com.malabon.pos", Context.MODE_PRIVATE);
		prefs.edit().putInt("CurrentCustomer", sale.customer.customer_id).commit();
		Intent intent = new Intent(this, ViewCustomer.class);
		startActivityForResult(intent, SELECT_CUSTOMER_REQUEST);
	}

	public void saleOptions(View view) {
		Intent intent = new Intent(this, SaleOptions.class);		
		startActivityForResult(intent, SALE_OPTIONS_REQUEST);
	}

	public void functions(View view) {
		SharedPreferences prefs = this.getSharedPreferences(
				"com.malabon.pos", Context.MODE_PRIVATE);
		prefs.edit().putInt("user_id", currentUser.user_id).commit();
		Intent intent = new Intent(this, Functions.class);
		startActivity(intent);
	}

	public void switchUser(View view) {
		Login(false, currentUser.username);
	}
	
	public void lockRegister(View view) {
		Login(true, currentUser.username);
	}
	
	private void Login(Boolean lockRegister, String username){
		SharedPreferences prefs = this.getSharedPreferences(
				"com.malabon.pos", Context.MODE_PRIVATE);
		prefs.edit().putString("CurrentUser", username).commit();
		prefs.edit().putBoolean("lockRegister", lockRegister).commit();
		Intent intent = new Intent(this, Login.class);
		startActivityForResult(intent, LOGIN_REQUEST);
	}

	public void pay(View view) {
		Intent intent = new Intent(this, PaymentActivity.class);
		intent.putExtra("Sale_Payment", sale);
		startActivityForResult(intent, PAYMENT_REQUEST);
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
		SharedPreferences prefs = this.getSharedPreferences(
				"com.malabon.pos", Context.MODE_PRIVATE);
		switch (requestCode) {
		case (EDIT_ORDERS_REQUEST): {
			if (resultCode == Activity.RESULT_OK) {
				Bundle data = intent.getExtras();
				sale = (Sale) data.get("Sale_EditOrders");
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
			if (resultCode == Activity.RESULT_FIRST_USER) {
				Initialize();
				bindOrderData();
			}
			break;
		}
		case (SALE_OPTIONS_REQUEST): {
			if (resultCode == Activity.RESULT_OK) {
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
					InitializeCustomer();
				}
			}
			break;
		}
		case (SELECT_CUSTOMER_REQUEST): {
			if (resultCode == Activity.RESULT_OK) {
				Bundle data = intent.getExtras();
				Customer selectedCustomer = (Customer) data.get("SelectedCustomer");
				if(selectedCustomer != null){
					sale.customer = selectedCustomer;
					InitializeCustomer();
				}
			}
			break;
		}
		case (LOGIN_REQUEST): {
			if (resultCode == Activity.RESULT_OK) {
				String u = prefs.getString("CurrentUser", null);
				if(u != null){
					currentUser = Sync.GetUserByUsername(u);
					Initialize();
				}
			}
			break;
		}
		case(PAYMENT_REQUEST): {
			if (resultCode == Activity.RESULT_FIRST_USER) {
				Initialize();
				bindOrderData();
			}
		}
		}
	}
	
	private void showToast(String message) {
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT)
				.show();
	}
}
