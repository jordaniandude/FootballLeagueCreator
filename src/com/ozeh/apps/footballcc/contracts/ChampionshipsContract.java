package com.ozeh.apps.footballcc.contracts;

import com.ozeh.apps.footballcc.providers.DataProvider;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

public class ChampionshipsContract {

	public final static String ID =  "_id";
	public final static String CHAMPIONSHIP_NAME = "championship_name";
	
	public static String AUTHORITY = "com.ozeh.apps.footballcc";
	public static String PATH = "/"+DataProvider.TABLE_CHAMPIONSHIPS ; 
	public static String URL = "content://"+AUTHORITY+PATH;
	public static Uri CONTENT_URI = Uri.parse(URL);
	
	public static String getChampionshipName(Cursor cursor) {
		int colIndex = cursor.getColumnIndexOrThrow(CHAMPIONSHIP_NAME);
		return cursor.getString(colIndex);
	}

	public static void putProfit(ContentValues values, String champ_name) {
		values.put(CHAMPIONSHIP_NAME, champ_name);
	}
	public static String getId(Cursor cursor) {
		int colIndex = cursor.getColumnIndexOrThrow(ID);
		return cursor.getString(colIndex);
	}

	public static void putId(ContentValues values, String id) {
		values.put(ID, id);
	}
}

