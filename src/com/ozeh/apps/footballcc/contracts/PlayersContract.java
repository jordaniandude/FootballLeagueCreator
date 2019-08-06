package com.ozeh.apps.footballcc.contracts;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.ozeh.apps.footballcc.providers.DataProvider;

public class PlayersContract {

	public final static String ID =  "_id";
	public final static String PLAYER_NAME = "player_name";
	public final static String POSITION = "position";
	public final static String NUMBER = "number";
	public final static String EMAIL = "email";
	public final static String PHONE = "phone";
	
	public final static String TEAM_FK  = "team_fk";
	public final static String EXTRA = "extra";
	
	public static String AUTHORITY = "com.ozeh.apps.footballcc";
	public static String PATH = "/"+DataProvider.TABLE_PLAYERS ; 
	public static String URL = "content://"+AUTHORITY+PATH;
	public static Uri CONTENT_URI = Uri.parse(URL);
	
	public static String getPlayerName(Cursor cursor) {
		int colIndex = cursor.getColumnIndexOrThrow(PLAYER_NAME);
		return cursor.getString(colIndex);
	}

	public static void putPlayerName(ContentValues values, String player_name) {
		values.put(PLAYER_NAME, player_name);
	}
	public static String getPosition(Cursor cursor) {
		int colIndex = cursor.getColumnIndexOrThrow(POSITION);
		return cursor.getString(colIndex);
	}

	public static void putPosition(ContentValues values, String position) {
		values.put(POSITION, position);
	}
	public static String getNumber(Cursor cursor) {
		int colIndex = cursor.getColumnIndexOrThrow(NUMBER);
		return cursor.getString(colIndex);
	}

	public static void putNumber(ContentValues values, String number) {
		values.put(NUMBER, number);
	}
	public static String getEmail(Cursor cursor) {
		int colIndex = cursor.getColumnIndexOrThrow(EMAIL);
		return cursor.getString(colIndex);
	}

	public static void putEmail(ContentValues values, String email) {
		values.put(EMAIL, email);
	}
	public static String getPhone(Cursor cursor) {
		int colIndex = cursor.getColumnIndexOrThrow(PHONE);
		return cursor.getString(colIndex);
	}

	public static void putPhone(ContentValues values, String phone) {
		values.put(PHONE, phone);
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


