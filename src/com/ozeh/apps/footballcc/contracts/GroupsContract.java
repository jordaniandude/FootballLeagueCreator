package com.ozeh.apps.footballcc.contracts;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.ozeh.apps.footballcc.providers.DataProvider;

public class GroupsContract {

	public final static String ID =  "_id";
	public final static String GROUP_NAME = "group_name";
	public final static String EXTRA = "extra";
	
	public final static String CHAMPIONSHIP_FK  = "championship_fk";
	
	public static String AUTHORITY = "com.ozeh.apps.footballcc";
	public static String PATH = "/"+DataProvider.TABLE_GROUPS ; 
	public static String URL = "content://"+AUTHORITY+PATH;
	public static Uri CONTENT_URI = Uri.parse(URL);
	
	public static String getGroupName(Cursor cursor) {
		int colIndex = cursor.getColumnIndexOrThrow(GROUP_NAME);
		return cursor.getString(colIndex);
	}

	public static void putGroupName(ContentValues values, String group_name) {
		values.put(GROUP_NAME, group_name);
	}
	public static String getExtra(Cursor cursor) {
		int colIndex = cursor.getColumnIndexOrThrow(EXTRA);
		return cursor.getString(colIndex);
	}

	public static void putExtra(ContentValues values, String extra) {
		values.put(EXTRA, extra);
	}
	
	public static String getId(Cursor cursor) {
		int colIndex = cursor.getColumnIndexOrThrow(ID);
		return cursor.getString(colIndex);
	}

	public static void putId(ContentValues values, String id) {
		values.put(ID, id);
	}
}

