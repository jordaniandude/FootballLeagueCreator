package com.ozeh.apps.footballcc.contracts;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.ozeh.apps.footballcc.providers.DataProvider;

public class MatchesContract {

	public final static String ID =  "_id";
	public final static String TEAM1_FK = "team1_fk";
	public final static String TEAM2_FK = "team2_fk";
	//public final static String GROUP_FK  = "group_fk";
	public final static String TEAM1_RESULT = "team1_result";
	public final static String TEAM2_RESULT = "team2_result";
	public final static String EXTRA = "extra";
	
	public static String AUTHORITY = "com.ozeh.apps.footballcc";
	public static String PATH = "/"+DataProvider.TABLE_MATCHES ; 
	public static String URL = "content://"+AUTHORITY+PATH;
	public static Uri CONTENT_URI = Uri.parse(URL);
	
	public static String getTeam2Result(Cursor cursor) {
		int colIndex = cursor.getColumnIndexOrThrow(TEAM2_RESULT);
		return cursor.getString(colIndex);
	}

	public static void putTeam2Result(ContentValues values, String teamResult) {
		values.put(TEAM2_RESULT, teamResult);
	}
	
	public static String getTeam1Result(Cursor cursor) {
		int colIndex = cursor.getColumnIndexOrThrow(TEAM1_RESULT);
		return cursor.getString(colIndex);
	}

	public static void putTeam1Result(ContentValues values, String teamResult) {
		values.put(TEAM1_RESULT, teamResult);
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

