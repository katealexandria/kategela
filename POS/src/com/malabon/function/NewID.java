package com.malabon.function;

import android.content.Context;

import com.malabon.database.CustomerDB;
import com.malabon.object.Customer;
import com.malabon.object.Sync;

public class NewID {

	private int count;
	private static int branchCustomerIDcount = 0;
	// TODO: real branchid
	// private String branchID = String.valueOf(Sync.posSettings.branch_id);
	private String branchID = "1";

	public String GetDefaultCustomerID() {
		return AppendBranchID(0);
	}

	private int getBranchCustomerCount() {
		if (branchCustomerIDcount == 0) {
			for (Customer c : Sync.Customers) {
				if (c.customer_id.charAt(0) == branchID.charAt(0)) {
					branchCustomerIDcount++;
				}
			}
		} else {
			branchCustomerIDcount++;
		}
		return branchCustomerIDcount;
	}

	public String GetCustomerID(Context context) {
		CustomerDB customerDB = new CustomerDB(context);
		customerDB.open();

		count = getBranchCustomerCount() + 1;

		return AppendBranchID(count);
	}

	public String GetSalesSummaryID(Context context) {
		count = 1; // get++

		return AppendBranchID(count);
	}

	/*public String GetPaymentID(Context context) {
		count = 1; // get++

		return AppendBranchID(count);
	}

	public String GetSalesID(Context context) {
		count = 1; // get++

		return AppendBranchID(count);
	}*/

	public String AppendBranchID(int count) {
		return branchID + "_" + count;
	}
}
