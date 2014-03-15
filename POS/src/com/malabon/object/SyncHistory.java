package com.malabon.object;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class SyncHistory implements Serializable {
	public Date SyncDate;
	public String UserId;
	public Boolean IsManual;
}
