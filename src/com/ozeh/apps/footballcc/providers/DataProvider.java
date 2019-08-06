package com.ozeh.apps.footballcc.providers;

import java.util.HashMap;

import com.ozeh.apps.footballcc.contracts.ChampionshipsContract;
import com.ozeh.apps.footballcc.contracts.GoalsContract;
import com.ozeh.apps.footballcc.contracts.GroupTeamsContract;
import com.ozeh.apps.footballcc.contracts.GroupsContract;
import com.ozeh.apps.footballcc.contracts.MatchesContract;
import com.ozeh.apps.footballcc.contracts.PlayersContract;
import com.ozeh.apps.footballcc.contracts.TeamsContract;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class DataProvider extends ContentProvider {

	public static String DATABASE_NAME = "footballcc6.db";
	public static String TABLE_CHAMPIONSHIPS = "Championships";
	public static String TABLE_GROUPS = "Groups";
	public static String TABLE_TEAMS = "Teams";
	public static String TABLE_PLAYERS = "Players";
	public static String TABLE_MATCHES = "Matches";
	public static String TABLE_GOALS = "Goals";
	public static String TABLE_GROUP_TEAMS = "Group_Teams";
	public static String INDEX_GROUPS_CHAMPIONSHIP_FK = "ChampionshipFkIndex";

	public static String PARAMS_TABLE = "Params";
	public static int DATABASE_VERSION = 1;

	public static String DATABASE_CREATE_CHAMPIONSHIPS = "create table "
			+ TABLE_CHAMPIONSHIPS + "( " + ChampionshipsContract.ID
			+ " integer primary key autoincrement, "
			+ ChampionshipsContract.CHAMPIONSHIP_NAME + " text);";

	public static String DATABASE_CREATE_GROUPS = "create table "
			+ TABLE_GROUPS + "( " + GroupsContract.ID
			+ " integer primary key autoincrement, "
			+ GroupsContract.GROUP_NAME + " text not null,"
			+ GroupsContract.EXTRA + " text," + GroupsContract.CHAMPIONSHIP_FK
			+ " integer," + "FOREIGN KEY (" + GroupsContract.CHAMPIONSHIP_FK
			+ ") REFERENCES " + TABLE_CHAMPIONSHIPS
			+ "(_id) ON DELETE CASCADE);";

	public static String DATABASE_CREATE_TEAMS = "create table " + TABLE_TEAMS
			+ "( " + TeamsContract.ID + " integer primary key autoincrement, "
			+ TeamsContract.TEAM_NAME + " text not null," + TeamsContract.EXTRA
			+ " text," + TeamsContract.GROUP_FK + " integer," + "FOREIGN KEY ("
			+ TeamsContract.GROUP_FK + ") REFERENCES " + TABLE_GROUPS
			+ "(_id) ON DELETE CASCADE);";

	public static String DATABASE_CREATE_PLAYERS = "create table "
			+ TABLE_PLAYERS + "( " + PlayersContract.ID
			+ " integer primary key autoincrement, "
			+ PlayersContract.PLAYER_NAME + " text not null,"
			+ PlayersContract.EXTRA + " text," + PlayersContract.POSITION
			+ " text," + PlayersContract.NUMBER + " text,"
			+ PlayersContract.EMAIL + " text," + PlayersContract.PHONE
			+ " text," + PlayersContract.TEAM_FK + " integer,"
			+ "FOREIGN KEY (" + PlayersContract.TEAM_FK + ") REFERENCES "
			+ TABLE_TEAMS + "(_id) ON DELETE CASCADE);";

	public static String DATABASE_CREATE_GROUP_TEAMS = "create table "
			+ TABLE_GROUP_TEAMS + "( " + GroupTeamsContract.ID
			+ " integer primary key autoincrement, " + GroupTeamsContract.WIN
			+ " integer not null," + GroupTeamsContract.LOSE
			+ " integer not null," + GroupTeamsContract.DRAW
			+ " integer not null," + GroupTeamsContract.GAMES_PLAYED
			+ " integer not null," + GroupTeamsContract.GOALS_FOR
			+ " integer not null," + GroupTeamsContract.GOALS_AGAINST
			+ " integer not null," + GroupTeamsContract.TEAM_FK + " integer,"
			+ GroupTeamsContract.GROUP_FK + " integer," + "FOREIGN KEY ("
			+ GroupTeamsContract.GROUP_FK + ") REFERENCES " + TABLE_GROUPS
			+ "(_id) ON DELETE CASCADE," + "FOREIGN KEY ("
			+ GroupTeamsContract.TEAM_FK + ") REFERENCES " + TABLE_TEAMS
			+ "(_id) ON DELETE CASCADE);";

	public static String DATABASE_CREATE_MATCHES = "create table "
			+ TABLE_MATCHES + "( " + MatchesContract.ID
			+ " integer primary key autoincrement, "
			+ MatchesContract.TEAM1_RESULT + " integer not null,"
			+ MatchesContract.TEAM2_RESULT + " integer not null,"
			+ MatchesContract.EXTRA + " text," + MatchesContract.TEAM1_FK
			+ " integer," + MatchesContract.TEAM2_FK + " integer,"
			+ "FOREIGN KEY (" + MatchesContract.TEAM1_FK + ") REFERENCES "
			+ TABLE_TEAMS + "(_id) ON DELETE CASCADE," + "FOREIGN KEY ("
			+ MatchesContract.TEAM2_FK + ") REFERENCES " + TABLE_TEAMS
			+ "(_id) ON DELETE CASCADE);";

	public static String DATABASE_CREATE_GOALS = "create table " + TABLE_GOALS
			+ "( " + GoalsContract.ID + " integer primary key autoincrement, "
			+ GoalsContract.MINUTE + " integer not null,"
			+ GoalsContract.GOAL_TYPE + " text not null,"
			+ GoalsContract.PLAYER_FK + " integer," + GoalsContract.MATCH_FK
			+ " integer," + "FOREIGN KEY (" + GoalsContract.PLAYER_FK
			+ ") REFERENCES " + TABLE_PLAYERS + "(_id) ON DELETE CASCADE,"
			+ "FOREIGN KEY (" + GoalsContract.MATCH_FK + ") REFERENCES "
			+ TABLE_MATCHES + "(_id) ON DELETE CASCADE);";

	public static String INDEX_GROUPS_CHAMPIONSHIP_FK_SCRIPT = "CREATE INDEX "
			+ INDEX_GROUPS_CHAMPIONSHIP_FK + " ON " + TABLE_GROUPS + "("
			+ GroupsContract.CHAMPIONSHIP_FK + ");";

	public static String DATABASE_UPGRADE = "DROP TABLE IF EXISTS";
	private SQLiteDatabase db;

	private static final int ALL_ROWS = 1;
	private static final int SINGLE_ROW = 2;

	private static HashMap<String, String> PROJECTION_MAP;

	private static final UriMatcher uriMatcher;
	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(ChampionshipsContract.AUTHORITY, TABLE_CHAMPIONSHIPS,
				ALL_ROWS);
		uriMatcher.addURI(ChampionshipsContract.AUTHORITY, TABLE_CHAMPIONSHIPS
				+ "/#", SINGLE_ROW);

		uriMatcher.addURI(GroupsContract.AUTHORITY, TABLE_GROUPS, ALL_ROWS);
		uriMatcher.addURI(GroupsContract.AUTHORITY, TABLE_GROUPS + "/#",
				SINGLE_ROW);

		uriMatcher.addURI(TeamsContract.AUTHORITY, TABLE_TEAMS, ALL_ROWS);
		uriMatcher.addURI(TeamsContract.AUTHORITY, TABLE_TEAMS + "/#",
				SINGLE_ROW);

		uriMatcher.addURI(GroupTeamsContract.AUTHORITY, TABLE_GROUP_TEAMS,
				ALL_ROWS);
		uriMatcher.addURI(GroupTeamsContract.AUTHORITY, TABLE_GROUP_TEAMS
				+ "/#", SINGLE_ROW);

		uriMatcher.addURI(PlayersContract.AUTHORITY, TABLE_PLAYERS, ALL_ROWS);
		uriMatcher.addURI(PlayersContract.AUTHORITY, TABLE_PLAYERS + "/#",
				SINGLE_ROW);

		uriMatcher.addURI(MatchesContract.AUTHORITY, TABLE_MATCHES, ALL_ROWS);
		uriMatcher.addURI(MatchesContract.AUTHORITY, TABLE_MATCHES + "/#",
				SINGLE_ROW);

		uriMatcher.addURI(GoalsContract.AUTHORITY, TABLE_GOALS, ALL_ROWS);
		uriMatcher.addURI(GoalsContract.AUTHORITY, TABLE_GOALS + "/#",
				SINGLE_ROW);

	}

	private static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_CREATE_CHAMPIONSHIPS);
			db.execSQL(DATABASE_CREATE_GROUPS);
			db.execSQL(DATABASE_CREATE_TEAMS);
			db.execSQL(DATABASE_CREATE_GROUP_TEAMS);
			db.execSQL(DATABASE_CREATE_PLAYERS);
			db.execSQL(DATABASE_CREATE_MATCHES);
			db.execSQL(DATABASE_CREATE_GOALS);
			db.execSQL(INDEX_GROUPS_CHAMPIONSHIP_FK_SCRIPT);
			// createDefaultParams(db);

		}

		/*
		 * private void createDefaultParams(SQLiteDatabase db) { ContentValues
		 * values = new ContentValues(); values.put(ParamsContract.PARAM_NAME,
		 * Params.PARAM_PAYMENT_RATE); values.put(ParamsContract.PARAM_VALUE,
		 * Params.PAYMENT_RATE); values.put(ParamsContract.ENABLED, true);
		 * db.insert(PARAMS_TABLE, null, values); }
		 */

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL(DATABASE_UPGRADE + " " + TABLE_CHAMPIONSHIPS);
			db.execSQL(DATABASE_UPGRADE + " " + TABLE_GROUPS);
			db.execSQL(DATABASE_UPGRADE + " " + TABLE_TEAMS);
			db.execSQL(DATABASE_UPGRADE + " " + TABLE_GROUP_TEAMS);
			db.execSQL(DATABASE_UPGRADE + " " + TABLE_PLAYERS);
			db.execSQL(DATABASE_UPGRADE + " " + TABLE_MATCHES);
			db.execSQL(DATABASE_UPGRADE + " " + TABLE_GOALS);

			onCreate(db);
		}
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int count = 0;

		switch (uriMatcher.match(uri)) {
		case ALL_ROWS:
			if (uri == ChampionshipsContract.CONTENT_URI) {
				// db.delete(TABLE_GOALS, null, null);
				// db.delete(TABLE_MATCHES, null, null);
				db.delete(TABLE_PLAYERS, PlayersContract.TEAM_FK
						+ " in (select _id from Teams where "
						+ TeamsContract.GROUP_FK
						+ " in (select _id from Groups where " + selection
						+ "))", selectionArgs);
				db.delete(TABLE_GROUP_TEAMS, GroupTeamsContract.GROUP_FK
						+ " in (select _id from Groups where " + selection
						+ ")", selectionArgs);
				db.delete(TABLE_TEAMS, TeamsContract.GROUP_FK
						+ " in (select _id from Groups where " + selection
						+ ")", selectionArgs);
				db.delete(TABLE_GROUPS, selection, selectionArgs);
				count = db
						.delete(TABLE_CHAMPIONSHIPS, selection, selectionArgs);
			} else if (uri == GroupsContract.CONTENT_URI) {
				count = db.delete(TABLE_GROUPS, selection, selectionArgs);
			} else if (uri == TeamsContract.CONTENT_URI) {
				count = db.delete(TABLE_TEAMS, selection, selectionArgs);
			} else if (uri == GroupTeamsContract.CONTENT_URI) {
				count = db.delete(TABLE_GROUP_TEAMS, selection, selectionArgs);
			} else if (uri == PlayersContract.CONTENT_URI) {
				count = db.delete(TABLE_PLAYERS, selection, selectionArgs);
			} else if (uri == MatchesContract.CONTENT_URI) {
				count = db.delete(TABLE_MATCHES, selection, selectionArgs);
			} else if (uri == GoalsContract.CONTENT_URI) {
				count = db.delete(TABLE_GOALS, selection, selectionArgs);
			}

			break;
		case SINGLE_ROW:
			// String id = uri.getPathSegments().get(1);

			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	@Override
	public String getType(Uri uri) {
		switch (uriMatcher.match(uri)) {

		case ALL_ROWS:
		case SINGLE_ROW:

			if (uri == ChampionshipsContract.CONTENT_URI) {
				return "vnd.android.cursor/vnd."
						+ ChampionshipsContract.AUTHORITY + "."
						+ TABLE_CHAMPIONSHIPS;
			} else if (uri == GroupsContract.CONTENT_URI) {
				return "vnd.android.cursor/vnd." + GroupsContract.AUTHORITY
						+ "." + TABLE_GROUPS;
			} else if (uri == TeamsContract.CONTENT_URI) {
				return "vnd.android.cursor/vnd." + TeamsContract.AUTHORITY
						+ "." + TABLE_TEAMS;
			} else if (uri == GroupTeamsContract.CONTENT_URI) {
				return "vnd.android.cursor/vnd." + GroupTeamsContract.AUTHORITY
						+ "." + TABLE_GROUP_TEAMS;
			} else if (uri == PlayersContract.CONTENT_URI) {
				return "vnd.android.cursor/vnd." + PlayersContract.AUTHORITY
						+ "." + TABLE_PLAYERS;
			} else if (uri == MatchesContract.CONTENT_URI) {
				return "vnd.android.cursor/vnd." + MatchesContract.AUTHORITY
						+ "." + TABLE_MATCHES;
			} else if (uri == GoalsContract.CONTENT_URI) {
				return "vnd.android.cursor/vnd." + GoalsContract.AUTHORITY
						+ "." + TABLE_GOALS;
			}

		default:
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		long rowID = 0;

		if (uri == ChampionshipsContract.CONTENT_URI) {
			rowID = db.insert(TABLE_CHAMPIONSHIPS, "", values);
		}

		else if (uri == GroupsContract.CONTENT_URI) {
			rowID = db.insert(TABLE_GROUPS, "", values);
		}

		else if (uri == TeamsContract.CONTENT_URI) {
			rowID = db.insert(TABLE_TEAMS, "", values);
		}

		else if (uri == GroupTeamsContract.CONTENT_URI) {
			rowID = db.insert(TABLE_GROUP_TEAMS, "", values);
		}

		else if (uri == PlayersContract.CONTENT_URI) {
			rowID = db.insert(TABLE_PLAYERS, "", values);
		}

		else if (uri == MatchesContract.CONTENT_URI) {
			rowID = db.insert(TABLE_MATCHES, "", values);
		}

		else if (uri == GoalsContract.CONTENT_URI) {
			rowID = db.insert(TABLE_GOALS, "", values);
		}

		if (rowID > 0) {
			Uri _uri = null;

			if (uri == ChampionshipsContract.CONTENT_URI) {
				_uri = ContentUris.withAppendedId(
						ChampionshipsContract.CONTENT_URI, rowID);
			}

			else if (uri == GroupsContract.CONTENT_URI) {
				_uri = ContentUris.withAppendedId(GroupsContract.CONTENT_URI,
						rowID);
			}

			else if (uri == TeamsContract.CONTENT_URI) {
				_uri = ContentUris.withAppendedId(TeamsContract.CONTENT_URI,
						rowID);
			}

			else if (uri == GroupTeamsContract.CONTENT_URI) {
				_uri = ContentUris.withAppendedId(
						GroupTeamsContract.CONTENT_URI, rowID);
			}

			else if (uri == PlayersContract.CONTENT_URI) {
				_uri = ContentUris.withAppendedId(PlayersContract.CONTENT_URI,
						rowID);
			} else if (uri == MatchesContract.CONTENT_URI) {
				_uri = ContentUris.withAppendedId(MatchesContract.CONTENT_URI,
						rowID);
			} else if (uri == GoalsContract.CONTENT_URI) {
				_uri = ContentUris.withAppendedId(GoalsContract.CONTENT_URI,
						rowID);
			}

			getContext().getContentResolver().notifyChange(_uri, null);
			return _uri;
		}
		throw new SQLException("Failed to add a record into " + uri);
	}

	@Override
	public boolean onCreate() {
		Context context = getContext();
		DatabaseHelper dbHelper = new DatabaseHelper(context);
		/**
		 * Create a write able database which will trigger its creation if it
		 * doesn't already exist.
		 */
		db = dbHelper.getWritableDatabase();
		return (db == null) ? false : true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

		if (uri == ChampionshipsContract.CONTENT_URI) {
			qb.setTables(TABLE_CHAMPIONSHIPS);
		}

		else if (uri == GroupsContract.CONTENT_URI) {
			qb.setTables(TABLE_GROUPS);
		}

		else if (uri == TeamsContract.CONTENT_URI) {
			qb.setTables(TABLE_TEAMS);
		}

		else if (uri == GroupTeamsContract.CONTENT_URI) {
			qb.setTables(TABLE_GROUP_TEAMS);
		}

		else if (uri == PlayersContract.CONTENT_URI) {
			qb.setTables(TABLE_PLAYERS);
		}

		else if (uri == MatchesContract.CONTENT_URI) {
			qb.setTables(TABLE_MATCHES);
		}

		else if (uri == GoalsContract.CONTENT_URI) {
			qb.setTables(TABLE_GOALS);
		}

		switch (uriMatcher.match(uri)) {
		case ALL_ROWS:
			qb.setProjectionMap(PROJECTION_MAP);
			break;
		case SINGLE_ROW:
			qb.appendWhere(ChampionshipsContract.ID + "="
					+ uri.getPathSegments().get(1));
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		if (sortOrder == null || sortOrder == "") {
			sortOrder = ChampionshipsContract.ID;
		}

		Cursor c = qb.query(db, projection, selection, selectionArgs, null,
				null, sortOrder);
		/**
		 * register to watch a content URI for changes
		 */
		c.setNotificationUri(getContext().getContentResolver(), uri);

		return c;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		int count = 0;

		switch (uriMatcher.match(uri)) {
		case ALL_ROWS:
			if (uri == ChampionshipsContract.CONTENT_URI) {
				count = db.update(TABLE_CHAMPIONSHIPS, values, selection,
						selectionArgs);
			}

			else if (uri == GroupsContract.CONTENT_URI) {
				count = db.update(TABLE_GROUPS, values, selection,
						selectionArgs);
			}

			else if (uri == TeamsContract.CONTENT_URI) {
				count = db
						.update(TABLE_TEAMS, values, selection, selectionArgs);
			}

			else if (uri == GroupTeamsContract.CONTENT_URI) {
				count = db.update(TABLE_GROUP_TEAMS, values, selection,
						selectionArgs);
			}

			else if (uri == PlayersContract.CONTENT_URI) {
				count = db.update(TABLE_PLAYERS, values, selection,
						selectionArgs);
			} else if (uri == MatchesContract.CONTENT_URI) {
				count = db.update(TABLE_MATCHES, values, selection,
						selectionArgs);
			} else if (uri == GoalsContract.CONTENT_URI) {
				count = db
						.update(TABLE_GOALS, values, selection, selectionArgs);
			}

			break;
		case SINGLE_ROW:
			if (uri == ChampionshipsContract.CONTENT_URI) {
				count = db.update(
						TABLE_CHAMPIONSHIPS,
						values,
						ChampionshipsContract.ID
								+ " = "
								+ uri.getPathSegments().get(1)
								+ (!TextUtils.isEmpty(selection) ? " AND ("
										+ selection + ')' : ""), selectionArgs);
			}

			else if (uri == GroupsContract.CONTENT_URI) {
				count = db.update(
						TABLE_GROUPS,
						values,
						GroupsContract.ID
								+ " = "
								+ uri.getPathSegments().get(1)
								+ (!TextUtils.isEmpty(selection) ? " AND ("
										+ selection + ')' : ""), selectionArgs);

			}

			else if (uri == TeamsContract.CONTENT_URI) {
				count = db.update(TABLE_TEAMS, values,
						TeamsContract.ID
								+ " = "
								+ uri.getPathSegments().get(1)
								+ (!TextUtils.isEmpty(selection) ? " AND ("
										+ selection + ')' : ""), selectionArgs);

			}

			else if (uri == GroupTeamsContract.CONTENT_URI) {
				count = db.update(
						TABLE_GROUP_TEAMS,
						values,
						GroupTeamsContract.ID
								+ " = "
								+ uri.getPathSegments().get(1)
								+ (!TextUtils.isEmpty(selection) ? " AND ("
										+ selection + ')' : ""), selectionArgs);
			}

			else if (uri == PlayersContract.CONTENT_URI) {
				count = db.update(
						TABLE_PLAYERS,
						values,
						PlayersContract.ID
								+ " = "
								+ uri.getPathSegments().get(1)
								+ (!TextUtils.isEmpty(selection) ? " AND ("
										+ selection + ')' : ""), selectionArgs);
			}

			else if (uri == MatchesContract.CONTENT_URI) {
				count = db.update(
						TABLE_MATCHES,
						values,
						MatchesContract.ID
								+ " = "
								+ uri.getPathSegments().get(1)
								+ (!TextUtils.isEmpty(selection) ? " AND ("
										+ selection + ')' : ""), selectionArgs);
			}

			else if (uri == GoalsContract.CONTENT_URI) {
				count = db.update(TABLE_GOALS, values,
						GoalsContract.ID
								+ " = "
								+ uri.getPathSegments().get(1)
								+ (!TextUtils.isEmpty(selection) ? " AND ("
										+ selection + ')' : ""), selectionArgs);
			}

			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

}
