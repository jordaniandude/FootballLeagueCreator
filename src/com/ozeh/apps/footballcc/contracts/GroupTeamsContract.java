package com.ozeh.apps.footballcc.contracts;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.ozeh.apps.footballcc.providers.DataProvider;

public class GroupTeamsContract {

	public final static String ID =  "_id";
	public final static String GAMES_PLAYED = "games_played";
	public final static String WIN = "win";
	public final static String LOSE = "lose";
	public final static String DRAW = "draw";
	public final static String GOALS_FOR = "goals_for";
	public final static String GOALS_AGAINST = "goals_against";
	
	public final static String GROUP_FK  = "group_fk";
	public final static String TEAM_FK  = "team_fk";
	
	public static String AUTHORITY = "com.ozeh.apps.footballcc";
	public static String PATH = "/"+DataProvider.TABLE_GROUP_TEAMS ; 
	public static String URL = "content://"+AUTHORITY+PATH;
	public static Uri CONTENT_URI = Uri.parse(URL);
	
	public static String getGamesPlayed(Cursor cursor) {
		int colIndex = cursor.getColumnIndexOrThrow(GAMES_PLAYED);
		return cursor.getString(colIndex);
	}

	public static void putGamesPlayed(ContentValues values, String value) {
		values.put(GAMES_PLAYED, value);
	}
	public static String getWin(Cursor cursor) {
		int colIndex = cursor.getColumnIndexOrThrow(WIN);
		return cursor.getString(colIndex);
	}

	public static void putWin(ContentValues values, String value) {
		values.put(WIN, value);
	}

	public static String getLose(Cursor cursor) {
		int colIndex = cursor.getColumnIndexOrThrow(LOSE);
		return cursor.getString(colIndex);
	}

	public static void putLose(ContentValues values, String value) {
		values.put(LOSE, value);
	}
	public static String getDraw(Cursor cursor) {
		int colIndex = cursor.getColumnIndexOrThrow(DRAW);
		return cursor.getString(colIndex);
	}

	public static void putDraw(ContentValues values, String value) {
		values.put(DRAW, value);
	}
	public static String getGoalsFor(Cursor cursor) {
		int colIndex = cursor.getColumnIndexOrThrow(GOALS_FOR);
		return cursor.getString(colIndex);
	}

	public static void putGoalsFor(ContentValues values, String value) {
		values.put(GOALS_FOR, value);
	}
	public static String getGoalsAgainst(Cursor cursor) {
		int colIndex = cursor.getColumnIndexOrThrow(GOALS_AGAINST);
		return cursor.getString(colIndex);
	}

	public static void putGoalsAgainst(ContentValues values, String value) {
		values.put(GOALS_AGAINST, value);
	}
	


	public static String getId(Cursor cursor) {
		int colIndex = cursor.getColumnIndexOrThrow(ID);
		return cursor.getString(colIndex);
	}

	public static void putId(ContentValues values, String id) {
		values.put(ID, id);
	}
}
