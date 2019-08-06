package com.ozeh.apps.footballcc.contracts;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.ozeh.apps.footballcc.providers.DataProvider;

public class TeamsContract {

	public final static String ID =  "_id";
	public final static String TEAM_NAME = "team_name";
	public final static String GROUP_FK  = "group_fk";
	public final static String EXTRA = "extra";
	
	public static String AUTHORITY = "com.ozeh.apps.footballcc";
	public static String PATH = "/"+DataProvider.TABLE_TEAMS ; 
	public static String URL = "content://"+AUTHORITY+PATH;
	public static Uri CONTENT_URI = Uri.parse(URL);
	
	public static String getTeamName(Cursor cursor) {
		int colIndex = cursor.getColumnIndexOrThrow(TEAM_NAME);
		return cursor.getString(colIndex);
	}

	public static void putTeamName(ContentValues values, String team_name) {
		values.put(TEAM_NAME, team_name);
	}
	public static String getId(Cursor cursor) {
		int colIndex = cursor.getColumnIndexOrThrow(ID);
		return cursor.getString(colIndex);
	}

	public static void putId(ContentValues values, String id) {
		values.put(ID, id);
	}
	public static String getExtra(Cursor cursor) {
		int colIndex = cursor.getColumnIndexOrThrow(EXTRA);
		return cursor.getString(colIndex);
	}

	public static void putExtra(ContentValues values, String extra) {
		values.put(EXTRA, extra);
	}
}

