package com.ozeh.apps.footballcc.database;

import java.util.ArrayList;

import com.ozeh.apps.footballcc.contracts.ChampionshipsContract;
import com.ozeh.apps.footballcc.contracts.GroupTeamsContract;
import com.ozeh.apps.footballcc.contracts.GroupsContract;
import com.ozeh.apps.footballcc.contracts.MatchesContract;
import com.ozeh.apps.footballcc.contracts.PlayersContract;
import com.ozeh.apps.footballcc.contracts.TeamsContract;
import com.ozeh.apps.footballcc.entities.Group;
import com.ozeh.apps.footballcc.entities.GroupTeam;
import com.ozeh.apps.footballcc.entities.Match;
import com.ozeh.apps.footballcc.entities.Player;
import com.ozeh.apps.footballcc.entities.Team;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class DatabaseLayer {
	/*
	 * Insert Methods
	 */
	public static String insertChampionship(Context context,
			String championshipName) {
		String result = getChampionshipIdByName(context, championshipName);
		if (result.equals("NULL")) {
			ContentValues values = new ContentValues();

			values.put(ChampionshipsContract.CHAMPIONSHIP_NAME,
					championshipName);
			context.getContentResolver().insert(
					ChampionshipsContract.CONTENT_URI, values);
			return "SUCCESS";
		} else {
			return "Championship name already exists";
		}
	}

	public static void insertGroup(Context context, Group group,
			String champName) {
		String champId = getChampionshipIdByName(context, champName);
		ContentValues values = new ContentValues();
		values.put(GroupsContract.GROUP_NAME, group.group_name);
		values.put(GroupsContract.EXTRA, group.extra);
		values.put(GroupsContract.CHAMPIONSHIP_FK, champId);
		context.getContentResolver().insert(GroupsContract.CONTENT_URI, values);
	}

	public static void insertTeam(Context context, Team team, String groupName,
			String champName) {
		String groupId = getGroupIdByGroupName(context, groupName, champName);
		ContentValues values = new ContentValues();
		values.put(TeamsContract.TEAM_NAME, team.team_name);
		values.put(TeamsContract.EXTRA, team.extra);
		values.put(TeamsContract.GROUP_FK, groupId);
		context.getContentResolver().insert(TeamsContract.CONTENT_URI, values);
	}

	public static void insertPlayer(Context context, Player player,
			String teamName, String champName) {
		String teamId = getTeamIdByTeamName(context, teamName, champName);
		ContentValues values = new ContentValues();
		values.put(PlayersContract.PLAYER_NAME, player.player_name);
		values.put(PlayersContract.PHONE, player.phone);
		values.put(PlayersContract.EMAIL, player.email);
		values.put(PlayersContract.EXTRA, player.extra);
		values.put(PlayersContract.POSITION, player.position);
		values.put(PlayersContract.NUMBER, player.number);
		values.put(PlayersContract.TEAM_FK, teamId);
		context.getContentResolver()
				.insert(PlayersContract.CONTENT_URI, values);
	}

	public static void insertGroupTeam(Context context, GroupTeam groupTeam,
			String groupName, String teamName, String champName) {

		String groupId = getGroupIdByGroupName(context, groupName, champName);
		String teamId = getTeamIdByTeamName(context, teamName, champName);

		ContentValues values = new ContentValues();
		values.put(GroupTeamsContract.GAMES_PLAYED, groupTeam.games_played);
		values.put(GroupTeamsContract.WIN, groupTeam.win);
		values.put(GroupTeamsContract.LOSE, groupTeam.lose);
		values.put(GroupTeamsContract.DRAW, groupTeam.draw);
		values.put(GroupTeamsContract.GOALS_AGAINST, groupTeam.goals_against);
		values.put(GroupTeamsContract.GOALS_FOR, groupTeam.goals_for);
		values.put(GroupTeamsContract.GROUP_FK, groupId);
		values.put(GroupTeamsContract.TEAM_FK, teamId);

		context.getContentResolver().insert(GroupTeamsContract.CONTENT_URI,
				values);
	}

	public static void insertMatch(Context context, Match match,
			String champName) {

		ContentValues values = new ContentValues();
		String team1_ID = getTeamIdByTeamName(context, match.team1, champName);
		String team2_ID = getTeamIdByTeamName(context, match.team2, champName);
		values.put(MatchesContract.TEAM1_FK, team1_ID);
		values.put(MatchesContract.TEAM2_FK, team2_ID);
		values.put(MatchesContract.TEAM1_RESULT, match.team1_result);
		values.put(MatchesContract.TEAM2_RESULT, match.team2_result);
		values.put(MatchesContract.EXTRA, match.extra);

		context.getContentResolver()
				.insert(MatchesContract.CONTENT_URI, values);
	}

	public static void insertMatchesPerGroup(Context context, String groupName,
			String champName) {
		ArrayList<GroupTeam> teams = getGroupTeamsPerGroupName(context,
				groupName, champName);
		// ArrayList<Match> matches = new ArrayList<Match>();
		for (int i = 0; i < teams.size(); i++) {
			String teamName = teams.get(i).teamName;
			for (int j = 0; j < teams.size(); j++) {
				if (teamName != teams.get(j).teamName) {
					String team2 = teams.get(j).teamName;
					Match match = new Match(teamName, team2, -1, -1, "");
					if (!isMatchExists(context, match, champName)) {
						insertMatch(context, match, champName);
					}
				}
			}
		}
	}
	public static void insertMatchesPerTeam(Context context,String teamName,String groupName,String champName)
	{
		ArrayList<GroupTeam> teams = getGroupTeamsPerGroupName(context,
				groupName, champName);
		for (int j = 0; j < teams.size(); j++) {
			if (!teamName.equals(teams.get(j).teamName)) {
				String team2 = teams.get(j).teamName;
				Match match = new Match(teamName, team2, -1, -1, "");
				if (!isMatchExists(context, match, champName)) {
					insertMatch(context, match, champName);
				}
			}
		}
	}

	public static void insertMatchesPerChampionship(Context context,
			String champName) {
		ArrayList<Group> groups = getGroupsByChampionship(context, champName);
		for (int i = 0; i < groups.size(); i++) {
			insertMatchesPerGroup(context, groups.get(i).group_name, champName);
		}
	}

	/*
	 * Other Methods
	 */
	public static boolean isMatchExists(Context context, Match match,
			String champName) {

		Cursor c = null;
		boolean result = false;
		try {
			String team1_ID = getTeamIdByTeamName(context, match.team1,
					champName);
			String team2_ID = getTeamIdByTeamName(context, match.team2,
					champName);

			c = context.getContentResolver().query(
					MatchesContract.CONTENT_URI,
					null,
					MatchesContract.TEAM1_FK + "=? and "
							+ MatchesContract.TEAM2_FK + "=?",
					new String[] { team1_ID, team2_ID }, null);
			c.moveToFirst();
			if (c.getCount() > 0) {
				result = true;
			} else {
				c.close();
				c = context.getContentResolver().query(
						MatchesContract.CONTENT_URI,
						null,
						MatchesContract.TEAM1_FK + "=? and "
								+ MatchesContract.TEAM2_FK + "=?",
						new String[] { team2_ID, team1_ID }, null);
				c.moveToFirst();

				if (c.getCount() > 0) {
					result = true;
				} else {
					result = false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			c.close();
		}
		return result;
	}

	/*
	 * Edit Methods
	 */
	public static void editChampionship(Context context,
			String oldChampionshipName, String newChampionshipName) {
		ContentValues values = new ContentValues();
		values.put(ChampionshipsContract.CHAMPIONSHIP_NAME, newChampionshipName);
		context.getContentResolver().insert(ChampionshipsContract.CONTENT_URI,
				values);
		context.getContentResolver().update(ChampionshipsContract.CONTENT_URI,
				values, ChampionshipsContract.CHAMPIONSHIP_NAME + "=?",
				new String[] { oldChampionshipName });
	}

	public static void editGroup(Context context, Group group,
			String oldGroupName) {
		ContentValues values = new ContentValues();
		values.put(GroupsContract.GROUP_NAME, group.group_name);
		values.put(GroupsContract.EXTRA, group.extra);
		context.getContentResolver()
				.update(GroupsContract.CONTENT_URI, values,
						GroupsContract.GROUP_NAME + "=?",
						new String[] { oldGroupName });
	}

	public static void editTeam(Context context, Team team, String oldTeamName) {
		ContentValues values = new ContentValues();
		values.put(TeamsContract.TEAM_NAME, team.team_name);
		values.put(TeamsContract.EXTRA, team.extra);

		context.getContentResolver().update(TeamsContract.CONTENT_URI, values,
				TeamsContract.TEAM_NAME + "=?", new String[] { oldTeamName });

	}

	public static void editPlayer(Context context, Player player,
			String oldPlayerName) {
		ContentValues values = new ContentValues();
		values.put(PlayersContract.PLAYER_NAME, player.player_name);
		values.put(PlayersContract.PHONE, player.phone);
		values.put(PlayersContract.EMAIL, player.email);
		values.put(PlayersContract.EXTRA, player.extra);
		values.put(PlayersContract.POSITION, player.position);
		values.put(PlayersContract.NUMBER, player.number);

		context.getContentResolver().update(PlayersContract.CONTENT_URI,
				values, PlayersContract.PLAYER_NAME + "=?",
				new String[] { oldPlayerName });
	}

	public static void editGroupTeam(Context context, GroupTeam groupTeam,
			String teamName, String champName) {
		String teamId = getTeamIdByTeamName(context, teamName, champName);

		ContentValues values = new ContentValues();
		values.put(GroupTeamsContract.GAMES_PLAYED, groupTeam.games_played);
		values.put(GroupTeamsContract.WIN, groupTeam.win);
		values.put(GroupTeamsContract.LOSE, groupTeam.lose);
		values.put(GroupTeamsContract.DRAW, groupTeam.draw);
		values.put(GroupTeamsContract.GOALS_AGAINST, groupTeam.goals_against);
		values.put(GroupTeamsContract.GOALS_FOR, groupTeam.goals_for);

		context.getContentResolver().update(GroupTeamsContract.CONTENT_URI,
				values, GroupTeamsContract.TEAM_FK + "=?",
				new String[] { teamId });
	}

	/*
	 * Get Methods
	 */
	public static String getChampionshipIdByName(Context context,
			String champName) {
		String[] projection = new String[] { ChampionshipsContract.ID };
		Cursor c = context.getContentResolver().query(
				ChampionshipsContract.CONTENT_URI, projection,
				ChampionshipsContract.CHAMPIONSHIP_NAME + "=?",
				new String[] { champName }, null);
		c.moveToFirst();
		if (c.getCount() > 0) {
			return c.getString(c.getColumnIndex(TeamsContract.ID));
		} else {
			return "NULL";
		}
	}

	public static String getTeamIdByTeamName(Context context, String teamName,
			String champName) {
		ArrayList<Team> teamsList = getTeamsPerChampionshipName(context,
				champName);

		String teamId = null;
		for (int i = 0; i < teamsList.size(); i++) {
			if (teamName.equals(teamsList.get(i).team_name)) {
				teamId = Long.toString(teamsList.get(i).id);
			}
		}
		return teamId;
	}

	public static String getGroupIdByTeamId(Context context, String teamId) {
		String[] projection = new String[] { TeamsContract.GROUP_FK };
		Cursor c = context.getContentResolver().query(
				TeamsContract.CONTENT_URI, projection, TeamsContract.ID + "=?",
				new String[] { teamId }, null);
		c.moveToFirst();
		if (c.getCount() > 0) {
			return c.getString(c.getColumnIndex(TeamsContract.GROUP_FK));
		} else {
			return "NULL";
		}
	}

	public static String getGroupIdByTeamName(Context context, String teamName,
			String champName) {
		String teamId = getTeamIdByTeamName(context, teamName, champName);
		String[] projection = new String[] { TeamsContract.GROUP_FK };
		Cursor c = context.getContentResolver().query(
				TeamsContract.CONTENT_URI, projection, TeamsContract.ID + "=?",
				new String[] { teamId }, null);
		c.moveToFirst();
		if (c.getCount() > 0) {
			return c.getString(c.getColumnIndex(TeamsContract.GROUP_FK));
		} else {
			return "NULL";
		}
	}

	public static ArrayList<Team> getTeamsPerGroupName(Context context,
			String groupName, String champName) {

		ArrayList<Team> teamsList = null;
		try {
			String[] projection = new String[] { TeamsContract.ID,
					TeamsContract.TEAM_NAME, TeamsContract.EXTRA };
			String groupId = getGroupIdByGroupName(context, groupName,
					champName);

			Cursor c = context.getContentResolver().query(
					TeamsContract.CONTENT_URI, projection,
					TeamsContract.GROUP_FK + "=?", new String[] { groupId },
					null);

			c.moveToFirst();
			if (c.getCount() > 0) {
				teamsList = new ArrayList<Team>();
				for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
					Team team = new Team(c.getLong(c
							.getColumnIndex(TeamsContract.ID)), c.getString(c
							.getColumnIndex(TeamsContract.TEAM_NAME)),
							c.getString(c.getColumnIndex(TeamsContract.EXTRA)));

					teamsList.add(team);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return teamsList;

	}

	public static ArrayList<Team> getTeamsPerGroupId(Context context,
			String groupId) {

		ArrayList<Team> teamsList = null;
		try {
			String[] projection = new String[] { TeamsContract.ID,
					TeamsContract.TEAM_NAME, TeamsContract.EXTRA };

			Cursor c = context.getContentResolver().query(
					TeamsContract.CONTENT_URI, projection,
					TeamsContract.GROUP_FK + "=?", new String[] { groupId },
					null);

			c.moveToFirst();
			if (c.getCount() > 0) {
				teamsList = new ArrayList<Team>();
				for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
					Team team = new Team(c.getLong(c
							.getColumnIndex(TeamsContract.ID)), c.getString(c
							.getColumnIndex(TeamsContract.TEAM_NAME)),
							c.getString(c.getColumnIndex(TeamsContract.EXTRA)));

					teamsList.add(team);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return teamsList;

	}

	public static String getGroupIdByGroupName(Context context,
			String groupName, String champName) {
		String champId = getChampionshipIdByName(context, champName);
		String[] projection = new String[] { GroupsContract.ID };
		Cursor c = context.getContentResolver().query(
				GroupsContract.CONTENT_URI,
				projection,
				GroupsContract.GROUP_NAME + "=? and "
						+ GroupsContract.CHAMPIONSHIP_FK + "=?",
				new String[] { groupName, champId }, null);
		c.moveToFirst();
		if (c.getCount() > 0) {
			return c.getString(c.getColumnIndex(GroupsContract.ID));
		} else {
			return "NULL";
		}
	}

	public static ArrayList<Group> getGroupsByChampionship(Context context,
			String championshipName) {
		// String groupId = getGroupIdByGroupName(context,groupName);
		String champId = getChampionshipIdByName(context, championshipName);
		Cursor c = context.getContentResolver().query(
				GroupsContract.CONTENT_URI, null,
				GroupsContract.CHAMPIONSHIP_FK + "=?",
				new String[] { champId }, GroupsContract.GROUP_NAME + " ASC");
		ArrayList<Group> groupsList = null;
		c.moveToFirst();
		if (c.getCount() > 0) {
			groupsList = new ArrayList<Group>();
			for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
				Group group = new Group(c.getString(c
						.getColumnIndex(GroupsContract.GROUP_NAME)),
						c.getString(c.getColumnIndex(GroupsContract.EXTRA)));
				groupsList.add(group);
			}
		}
		return groupsList;
	}

	public static ArrayList<Match> getMatchesPerTeamName(Context context,
			String teamName, String champName) {

		String teamId = getTeamIdByTeamName(context, teamName, champName);

		Cursor c = context.getContentResolver().query(
				MatchesContract.CONTENT_URI,
				null,
				MatchesContract.TEAM1_FK + "=? or " + MatchesContract.TEAM2_FK
						+ "=?", new String[] { teamId, teamId }, null);
		ArrayList<Match> matchesList = null;
		if (c.getCount() > 0) {
			matchesList = new ArrayList<Match>();
			for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {

				String team1 = getTeamNameByTeamId(context,
						c.getInt(c.getColumnIndex(MatchesContract.TEAM1_FK)));
				String team2 = getTeamNameByTeamId(context,
						c.getInt(c.getColumnIndex(MatchesContract.TEAM2_FK)));

				Match match = new Match(
						c.getLong(c.getColumnIndex(MatchesContract.ID)),
						team1,
						team2,
						c.getInt(c.getColumnIndex(MatchesContract.TEAM1_RESULT)),
						c.getInt(c.getColumnIndex(MatchesContract.TEAM2_RESULT)),
						"");

				matchesList.add(match);
			}
		}
		return matchesList;
	}

	public static ArrayList<GroupTeam> getGroupTeamsPerGroupName(
			Context context, String groupName, String champName) {
		String groupId = getGroupIdByGroupName(context, groupName, champName);
		Cursor c = context.getContentResolver().query(
				GroupTeamsContract.CONTENT_URI, null,
				GroupTeamsContract.GROUP_FK + "=?", new String[] { groupId },
				GroupTeamsContract.WIN + " DESC,"+GroupTeamsContract.GOALS_FOR+" DESC");
		ArrayList<GroupTeam> teamsList = null;
		c.moveToFirst();
		if (c.getCount() > 0) {
			teamsList = new ArrayList<GroupTeam>();
			for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
				int games_played = c.getInt(c
						.getColumnIndex(GroupTeamsContract.GAMES_PLAYED));
				int win = c.getInt(c.getColumnIndex(GroupTeamsContract.WIN));
				int lose = c.getInt(c.getColumnIndex(GroupTeamsContract.LOSE));
				int draw = c.getInt(c.getColumnIndex(GroupTeamsContract.DRAW));
				int goals_for = c.getInt(c
						.getColumnIndex(GroupTeamsContract.GOALS_FOR));
				int goals_against = c.getInt(c
						.getColumnIndex(GroupTeamsContract.GOALS_AGAINST));
				int teamId = c.getInt(c
						.getColumnIndex(GroupTeamsContract.TEAM_FK));
				String teamName = getTeamNameByTeamId(context, teamId);
				GroupTeam team = new GroupTeam(games_played, win, lose, draw,
						goals_for, goals_against, teamName);
				teamsList.add(team);
			}
		}
		return teamsList;

	}

	public static GroupTeam getGroupTeamPerTeamName(Context context,
			String teamName, String champName) {

		String teamId = getTeamIdByTeamName(context, teamName, champName);

		Cursor c = context.getContentResolver().query(
				GroupTeamsContract.CONTENT_URI, null,
				GroupTeamsContract.TEAM_FK + "=?", new String[] { teamId },
				GroupTeamsContract.ID + " ASC");
		GroupTeam team = null;
		c.moveToFirst();
		if (c.getCount() > 0) {

			int games_played = c.getInt(c
					.getColumnIndex(GroupTeamsContract.GAMES_PLAYED));
			int win = c.getInt(c.getColumnIndex(GroupTeamsContract.WIN));
			int lose = c.getInt(c.getColumnIndex(GroupTeamsContract.LOSE));
			int draw = c.getInt(c.getColumnIndex(GroupTeamsContract.DRAW));
			int goals_for = c.getInt(c
					.getColumnIndex(GroupTeamsContract.GOALS_FOR));
			int goals_against = c.getInt(c
					.getColumnIndex(GroupTeamsContract.GOALS_AGAINST));

			team = new GroupTeam(games_played, win, lose, draw, goals_for,
					goals_against, teamName);

		}
		return team;

	}

	public static String getTeamNameByTeamId(Context context, int teamId) {
		String[] projection = new String[] { TeamsContract.ID,
				TeamsContract.TEAM_NAME };
		Cursor c = context.getContentResolver().query(
				TeamsContract.CONTENT_URI, projection, TeamsContract.ID + "=?",
				new String[] { Integer.toString(teamId) }, null);
		c.moveToFirst();
		if (c.getCount() > 0) {
			return c.getString(c.getColumnIndex(TeamsContract.TEAM_NAME));
		} else {
			return "NULL";
		}
	}

	public static ArrayList<Team> getTeamsPerChampionshipName(Context context,
			String champName) {
		// String[] projection = new String[] { TeamsContract.ID,TeamsContract
		// };

		ArrayList<Team> teamsList = new ArrayList<Team>();
		try {

			ArrayList<Group> groups = getGroupsByChampionship(context,
					champName);
			for (int i = 0; i < groups.size(); i++) {
				ArrayList<Team> tempTeams = getTeamsPerGroupName(context,
						groups.get(i).group_name, champName);
				for (int j = 0; j < tempTeams.size(); j++) {
					teamsList.add(tempTeams.get(j));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return teamsList;

	}

	public static ArrayList<Player> getPlayersPerTeamName(Context context,
			String teamName, String champName) {

		ArrayList<Team> teamList = getTeamsPerChampionshipName(context,
				champName);
		long teamId = -1;
		for (int i = 0; i < teamList.size(); i++) {
			if (teamList.get(i).team_name.equals(teamName)) {
				teamId = teamList.get(i).id;
			}
		}

		ArrayList<Player> playersList = null;
		if (teamId != -1) {
			Cursor c = context.getContentResolver().query(
					PlayersContract.CONTENT_URI, null,
					PlayersContract.TEAM_FK + "=?",
					new String[] { Long.toString(teamId) }, null);

			c.moveToFirst();
			if (c.getCount() > 0) {
				playersList = new ArrayList<Player>();
				for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
					String player_name = c.getString(c
							.getColumnIndex(PlayersContract.PLAYER_NAME));
					String position = c.getString(c
							.getColumnIndex(PlayersContract.POSITION));
					String number = c.getString(c
							.getColumnIndex(PlayersContract.NUMBER));
					String phone = c.getString(c
							.getColumnIndex(PlayersContract.PHONE));
					String email = c.getString(c
							.getColumnIndex(PlayersContract.EMAIL));

					Player player = new Player(player_name, position, number,
							email, phone, "");
					playersList.add(player);
				}
			}
		}
		return playersList;

	}

	/*
	 * Update Methods
	 */
	public static void updateMatchesPerTeamsNames(Context context,
			String champName, String team1, String team2, String team1Result,
			String team2Result) {
		String team1Id = getTeamIdByTeamName(context, team1, champName);
		String team2Id = getTeamIdByTeamName(context, team2, champName);

		ContentValues values = new ContentValues();
		values.put(MatchesContract.TEAM1_RESULT, team1Result);
		values.put(MatchesContract.TEAM2_RESULT, team2Result);
		context.getContentResolver().update(
				MatchesContract.CONTENT_URI,
				values,
				MatchesContract.TEAM1_FK + "=? and " + MatchesContract.TEAM2_FK
						+ "=?", new String[] { team1Id, team2Id });

	}

	public static void updateGroupTeam(Context context, GroupTeam groupTeam,
			String champName) {

		String teamId = getTeamIdByTeamName(context, groupTeam.teamName,
				champName);
		String groupId = getGroupIdByTeamId(context, teamId);

		ContentValues values = new ContentValues();
		values.put(GroupTeamsContract.GAMES_PLAYED, groupTeam.games_played);
		values.put(GroupTeamsContract.WIN, groupTeam.win);
		values.put(GroupTeamsContract.LOSE, groupTeam.lose);
		values.put(GroupTeamsContract.DRAW, groupTeam.draw);
		values.put(GroupTeamsContract.GOALS_AGAINST, groupTeam.goals_against);
		values.put(GroupTeamsContract.GOALS_FOR, groupTeam.goals_for);
		values.put(GroupTeamsContract.GROUP_FK, groupId);
		values.put(GroupTeamsContract.TEAM_FK, teamId);

		context.getContentResolver().update(GroupTeamsContract.CONTENT_URI,
				values, GroupTeamsContract.TEAM_FK + "=?",
				new String[] { teamId });
	}

	/*
	 * Delete Methods
	 */
	public static void deleteChampionship(Context context, String champName) {
		String champId = getChampionshipIdByName(context, champName);
		context.getContentResolver().delete(ChampionshipsContract.CONTENT_URI,
				ChampionshipsContract.ID + "=?", new String[] { champId });
	}
}
