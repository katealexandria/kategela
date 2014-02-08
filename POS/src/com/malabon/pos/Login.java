package com.malabon.pos;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       /* //dummy user_id data
        DatabaseHandler dbh = new DatabaseHandler(Login.this);  
        SQLiteDatabase db = dbh.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("user_id", 1);
		values.put("user_name", "admin");
		values.put("user_password", "pass");	
		db.insert(DatabaseHandler.TABLE_SYS_USERS, null, values);
		
		values.put("user_id", 2);
		values.put("user_name", "gela");
		values.put("user_password", "pass");	
		db.insert(DatabaseHandler.TABLE_SYS_USERS, null, values);
        
        setContentView(R.layout.activity_login);*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }
    
    public void validateLogin(View view) {
    	/*String user_name = ((EditText)findViewById(R.id.txtUsername)).getText().toString();
    	String user_password = ((EditText)findViewById(R.id.txtPassword)).getText().toString();
    	
    	DatabaseHandler dbh = new DatabaseHandler(Login.this);  	
    	SQLiteDatabase db = dbh.getReadableDatabase();
    	 	
    	//SELECT
		String[] columns = {"user_id"};
		//WHERE clause
		String selection = "user_name=? AND user_password=?";
		//WHERE clause arguments
		String[] selectionArgs = {user_name, user_password};
		
		Cursor cursor = null;
		try{
		//SELECT query login
		cursor = db.query(DatabaseHandler.TABLE_SYS_USERS, columns, selection, selectionArgs, null, null, null);
		
		startManagingCursor(cursor);
		}catch(Exception e){
			e.printStackTrace();
		}
		int numberOfRows = cursor.getCount();
		
		if(numberOfRows <= 0){
		
			Toast.makeText(getApplicationContext(), "Login Failed..\nTry Again", Toast.LENGTH_SHORT).show();
		}
		else{
	    	Intent intent = new Intent(this, MainActivity.class);
	        startActivity(intent);
		}*/
    }
    
}
