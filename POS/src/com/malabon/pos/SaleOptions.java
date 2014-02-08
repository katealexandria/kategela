package com.malabon.pos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class SaleOptions extends Activity {

	static final int RECEIPT_REQUEST = 10;  // The request code
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_sale_options);
	}
	
	public void discount(View view) {
		Intent intent = new Intent(this, ReceiptDiscount.class);
        startActivityForResult(intent, RECEIPT_REQUEST);
    }
	
	public void printReceipt(View view) {
        finish();
    }
	
	public void cancel(View view) {
        finish();
    }
	
	public void newSale(View view) {
        finish();
    }
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
	  super.onActivityResult(requestCode, resultCode, intent);
	  switch(requestCode) {
	    case (RECEIPT_REQUEST) : {
	      if (resultCode == Activity.RESULT_OK) {
	        // TODO Extract the data returned from the child Activity.
	    	setResult(Activity.RESULT_OK);
	    	finish();
	      }
	      break;
	    } 
	  }
	}
}
