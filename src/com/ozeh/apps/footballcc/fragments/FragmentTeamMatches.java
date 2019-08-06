package com.ozeh.apps.footballcc.fragments;

import java.util.ArrayList;

import com.ozeh.apps.footballcc.R;
import com.ozeh.apps.footballcc.contracts.ChampionshipsContract;
import com.ozeh.apps.footballcc.contracts.TeamsContract;
import com.ozeh.apps.footballcc.database.DatabaseLayer;
import com.ozeh.apps.footballcc.entities.GroupTeam;
import com.ozeh.apps.footballcc.entities.Match;
import com.ozeh.apps.footballcc.entities.Team;
import com.ozeh.apps.footballcc.extras.Tools;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * 
 */
public class FragmentTeamMatches extends Fragment {

	String teamName;
	String champName;
	LinearLayout mainLayout;
	EditText etTeam1Result;
	EditText etTeam2Result;
	TextView tvTeam1;
	TextView tvTeam2;
	ArrayList<Match> matchesList;
	int matchesCount;
	int resultFieldsCount;
	int editTextInitialId = 110;
	int editTextIdCounter = editTextInitialId;

	int textViewInitialId = 10;
	int textViewIdCounter = textViewInitialId;

	public FragmentTeamMatches() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_team_matches, container,
				false);

		setHasOptionsMenu(true);

		return view;

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		teamName = getArguments().getString(TeamsContract.TEAM_NAME);
		champName = getArguments().getString(
				ChampionshipsContract.CHAMPIONSHIP_NAME);

		initializeViews();

		viewMatches();

	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Add your menu entries here
		inflater.inflate(R.menu.fragment_team_matches_menu, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.save_button) {
			int count = 0;
			// save matches results...
			for (int i = textViewInitialId; i < textViewInitialId
					+ resultFieldsCount; i += 2) {
				TextView tvT1 = (TextView) getActivity().findViewById(i);
				EditText etR1 = (EditText) getActivity().findViewById(i + 100);

				TextView tvT2 = (TextView) getActivity().findViewById(i + 1);
				EditText etR2 = (EditText) getActivity().findViewById(
						i + 1 + 100);

				String t1 = tvT1.getText().toString();
				String t2 = tvT2.getText().toString();
				String r1 = etR1.getText().toString();
				String r2 = etR2.getText().toString();

				String team1 = matchesList.get(count).team1;
				String team2 = matchesList.get(count).team2;

				if (t1.equals(team1) && t2.equals(team2)) {
					if (!r1.equals("") && !r2.equals(""))
						DatabaseLayer.updateMatchesPerTeamsNames(getActivity(),
								champName, t1, t2, r1, r2);
				}
				count++;

			}

			updateGroupTeams();

			enableResultFields(false);

			return true;
		}
		if (id == R.id.edit_button) {

			enableResultFields(true);

			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void updateGroupTeams() {

		String teamId = DatabaseLayer.getTeamIdByTeamName(getActivity()
				.getApplicationContext(), teamName, champName);
		String groupId = DatabaseLayer.getGroupIdByTeamId(getActivity()
				.getApplicationContext(), teamId);
		ArrayList<Team> teamsList = DatabaseLayer.getTeamsPerGroupId(
				getActivity().getApplicationContext(), groupId);

		resetGroupTeams(teamsList);

		ArrayList<Match> groupMatchesList = new ArrayList<Match>();
		for (int i = 0; i < teamsList.size(); i++) {
			ArrayList<Match> teamMatchesList = DatabaseLayer
					.getMatchesPerTeamName(getActivity()
							.getApplicationContext(),
							teamsList.get(i).team_name, champName);

			for (int j = 0; j < teamMatchesList.size(); j++) {
				if (newMatch(teamMatchesList.get(j).id, groupMatchesList)) {
					groupMatchesList.add(teamMatchesList.get(j));
				}

				// updateGroupTeamsPerMatch(currentTeam, team1, team2, result1,
				// result2);

			}

		}

		for (int x = 0; x < groupMatchesList.size(); x++) {
			String team1 = groupMatchesList.get(x).team1;
			String team2 = groupMatchesList.get(x).team2;
			int result1 = groupMatchesList.get(x).team1_result;
			int result2 = groupMatchesList.get(x).team2_result;
			updateGroupTeamsPerMatch(team1, team2, result1, result2);
		}

	}

	private boolean newMatch(long id, ArrayList<Match> groupMatchesList) {

		boolean result = true;
		for (int i = 0; i < groupMatchesList.size(); i++) {
			if (id == groupMatchesList.get(i).id) {
				result = false;
			}
		}

		return result;
	}

	private void resetGroupTeams(ArrayList<Team> teamsList) {

		for (int i = 0; i < teamsList.size(); i++) {
			GroupTeam groupTeam = new GroupTeam();
			groupTeam.teamName = teamsList.get(i).team_name;
			DatabaseLayer
					.updateGroupTeam(getActivity().getApplicationContext(),
							groupTeam, champName);
		}

	}

	private void updateGroupTeamsPerMatch(String t1, String t2, int r1, int r2) {

		if (r1 != -1 && r2 != -1) {

			/*
			 * String currentTeam, otherTeam; if (team.equals(t1)) { currentTeam
			 * = t1; otherTeam = t2; } else { currentTeam = t2; otherTeam = t1;
			 * }
			 */
			GroupTeam groupTeamCurrent = DatabaseLayer.getGroupTeamPerTeamName(
					getActivity().getApplicationContext(), t1, champName);
			updateGroupTeamPerTeam(groupTeamCurrent, t1, t2, r1, r2);

			GroupTeam groupTeamOther = DatabaseLayer.getGroupTeamPerTeamName(
					getActivity().getApplicationContext(), t2, champName);
			updateGroupTeamPerTeam(groupTeamOther, t1, t2, r1, r2);

		}

	}

	private void updateGroupTeamPerTeam(GroupTeam groupTeam, String t1,
			String t2, int r1, int r2) {

		int win = groupTeam.win;
		int lose = groupTeam.lose;
		int draw = groupTeam.draw;
		int gp = groupTeam.games_played;
		int goals_for = groupTeam.goals_for;
		int goals_against = groupTeam.goals_against;

		gp += 1;

		if (t1.equals(groupTeam.teamName)) {
			goals_for += r1;
			goals_against += r2;
			if (r1 > r2) // win
			{
				win++;

			} else if (r1 == r2) // draw
			{
				draw++;

			} else // intR1 < intR2 loss
			{
				lose++;
			}
		} else if (t2.equals(groupTeam.teamName)) {
			goals_for += r2;
			goals_against += r1;

			if (r2 > r1) // win
			{
				win++;

			} else if (r2 == r1) // draw
			{
				draw++;

			} else // intR1 < intR2 loss
			{
				lose++;
			}

		}
		GroupTeam updatedTeam = new GroupTeam(gp, win, lose, draw, goals_for,
				goals_against, groupTeam.teamName);
		DatabaseLayer.updateGroupTeam(getActivity().getApplicationContext(),
				updatedTeam, champName);

	}

	private void enableResultFields(boolean enable) {

		for (int i = editTextInitialId; i < editTextInitialId
				+ resultFieldsCount; i++) {
			EditText et = (EditText) getActivity().findViewById(i);
			et.setEnabled(enable);
		}
	}

	private void viewMatches() {
		matchesList = DatabaseLayer.getMatchesPerTeamName(getActivity()
				.getApplicationContext(), teamName, champName);
		matchesCount = matchesList.size();
		resultFieldsCount = matchesCount * 2;
		for (int i = 0; i < matchesList.size(); i++) {
			createSubLayout(matchesList.get(i));
		}

	}

	private void initializeViews() {
		mainLayout = (LinearLayout) getActivity().findViewById(
				R.id.layout_fragment_team_matches);

	}

	private void createSubLayout(Match match) {

		LinearLayout layout = new LinearLayout(getActivity()
				.getApplicationContext());

		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);

		layout.setWeightSum(6);
		layout.setOrientation(LinearLayout.HORIZONTAL);
		layoutParams.setMargins(5, 5, 5, 5);

		createMatchViews(layout, match);
		mainLayout.addView(layout, layoutParams);

	}

	private void createMatchViews(LinearLayout layout, Match match) {
		tvTeam1 = new TextView(getActivity().getApplicationContext());
		tvTeam1.setText(match.team1);
		tvTeam1.setId(textViewIdCounter);
		tvTeam1.setTextSize(15);
		tvTeam1.setTextColor(Color.BLACK);
		tvTeam1.setGravity(Gravity.CENTER);
		tvTeam1.setBackgroundResource(R.drawable.border2);
		tvTeam1.setLayoutParams(new LayoutParams(0, LayoutParams.WRAP_CONTENT,
				2));
		layout.addView(tvTeam1);

		etTeam1Result = new EditText(getActivity().getApplicationContext());
		etTeam1Result.setTextSize(15);
		etTeam1Result.setId(editTextIdCounter);
		etTeam1Result.setText(Tools.verifyResult(match.team1_result));
		etTeam1Result.setTextColor(Color.BLACK);
		etTeam1Result.setGravity(Gravity.CENTER);
		etTeam1Result.setBackgroundResource(R.drawable.border2);
		etTeam1Result.setEnabled(false);

		etTeam1Result.setInputType(InputType.TYPE_CLASS_TEXT
				| InputType.TYPE_CLASS_NUMBER);
		LayoutParams params = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1);
		params.setMargins(2, 0, 2, 0);
		etTeam1Result.setLayoutParams(params);
		layout.addView(etTeam1Result);

		editTextIdCounter++;

		etTeam2Result = new EditText(getActivity().getApplicationContext());
		etTeam2Result.setTextSize(15);

		etTeam2Result.setText(Tools.verifyResult(match.team2_result));
		etTeam2Result.setTextColor(Color.BLACK);
		etTeam2Result.setId(editTextIdCounter);
		etTeam2Result.setBackgroundResource(R.drawable.border2);
		etTeam2Result.setGravity(Gravity.CENTER);
		etTeam2Result.setEnabled(false);
		etTeam2Result.setInputType(InputType.TYPE_CLASS_TEXT
				| InputType.TYPE_CLASS_NUMBER);

		etTeam2Result.setLayoutParams(params);
		layout.addView(etTeam2Result);

		textViewIdCounter++;

		tvTeam2 = new TextView(getActivity().getApplicationContext());
		tvTeam2.setText(match.team2);
		tvTeam2.setId(textViewIdCounter);
		tvTeam2.setTextSize(15);
		tvTeam2.setTextColor(Color.BLACK);
		tvTeam2.setGravity(Gravity.CENTER);
		tvTeam2.setBackgroundResource(R.drawable.border2);
		tvTeam2.setLayoutParams(new LayoutParams(0, LayoutParams.WRAP_CONTENT,
				2));
		layout.addView(tvTeam2);

		editTextIdCounter++;
		textViewIdCounter++;

	}

}
