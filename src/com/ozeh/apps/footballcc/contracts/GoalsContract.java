package com.ozeh.apps.footballcc.contracts;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.ozeh.apps.footballcc.providers.DataProvider;

public class GoalsContract {

	public final static String ID =  "_id";
	public final static String PLAYER_FK = "player_fk";
	public final static String MATCH_FK = "match_fk";
	public final static String MINUTE = "minute";
	// Normal, Own Goal, Penalty
	public final static String GOAL_TYPE = "goal_type";
	
	public static String AUTHORITY = "com.ozeh.apps.footballcc";
	public static String PATH = "/"+DataProvider.TABLE_GOALS ; 
	public static String URL = "content://"+AUTHORITY+PATH;
	public static Uri CONTENT_URI = Uri.parse(URL);
	
	public static String getMinute(Cursor cursor) {
		int colIndex = cursor.getColumnIndexOrThrow(MINUTE);
		return cursor.getString(colIndex);
	}

	public static void putMinute(ContentValues values, String value) {
		values.put(MINUTE, value);
	}
	
	public static String getGoalType(Cursor cursor) {
		int colIndex = cursor.getColumnIndexOrThrow(GOAL_TYPE);
		return cursor.getString(colIndex);
	}

	public static void putGoalType(ContentValues values, String value) {
		values.put(GOAL_TYPE, value);
	}
	public static String getId(Cursor cursor) {
		int colIndex = cursor.getColumnIndexOrThrow(ID);
		return cursor.getString(colIndex);
	}

	public static void putId(ContentValues values, String id) {
		values.put(ID, id);
	}
}

