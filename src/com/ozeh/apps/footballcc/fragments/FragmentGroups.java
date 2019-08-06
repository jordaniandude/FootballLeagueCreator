package com.ozeh.apps.footballcc.fragments;

import java.util.ArrayList;

import com.ozeh.apps.footballcc.R;
import com.ozeh.apps.footballcc.contracts.ChampionshipsContract;
import com.ozeh.apps.footballcc.database.DatabaseLayer;
import com.ozeh.apps.footballcc.entities.Group;
import com.ozeh.apps.footballcc.entities.GroupTeam;
import com.ozeh.apps.footballcc.extras.Tools;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * 
 */
public class FragmentGroups extends Fragment {

	String champName;
	LinearLayout linearLayout;
	TextView tvGroupName;

	public FragmentGroups() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater
				.inflate(R.layout.fragment_groups, container, false);

		return view;

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		linearLayout = (LinearLayout) getActivity().findViewById(
				R.id.layout_groups);

		tvGroupName = (TextView) getActivity().findViewById(R.id.tv_champ_name);

		champName = getArguments().getString(
				ChampionshipsContract.CHAMPIONSHIP_NAME);

		tvGroupName.setText("Championship: " + champName);

	}

	private void createGroupTable(ArrayList<GroupTeam> groupTeams,
			LinearLayout layout, String groupName) {
		TableLayout table = createTable();
		createGroupNameHeader(table, groupName);
		createTableHeader(table);
		createTableRows(groupTeams, table);
		layout.addView(table);

	}

	private TableLayout createTable() {
		TableLayout.LayoutParams tableParams = new TableLayout.LayoutParams(
				TableLayout.LayoutParams.WRAP_CONTENT,
				TableLayout.LayoutParams.WRAP_CONTENT);
		tableParams.setMargins(10, 10, 10, 10);
		TableLayout table = new TableLayout(getActivity()
				.getApplicationContext());
		table.setLayoutParams(tableParams);

		table.setStretchAllColumns(true);
		table.setShrinkAllColumns(true);
		return table;
	}

	private void createGroupNameHeader(TableLayout table, String groupName) {
		TableRow tr = new TableRow(getActivity().getApplicationContext());
		tr.setLayoutParams(new TableRow.LayoutParams(
				TableRow.LayoutParams.MATCH_PARENT,
				TableRow.LayoutParams.MATCH_PARENT));
		tr.addView(Tools.createFieldHeader("Group", getActivity()
				.getApplicationContext()));
		tr.addView(Tools.createFieldHeader(groupName, getActivity()
				.getApplicationContext()));

		table.addView(tr, new TableLayout.LayoutParams(
				TableLayout.LayoutParams.MATCH_PARENT,
				TableLayout.LayoutParams.MATCH_PARENT));

	}

	private void createTableHeader(TableLayout table) {
		TableRow tr = new TableRow(getActivity().getApplicationContext());
		tr.setLayoutParams(new TableRow.LayoutParams(
				TableRow.LayoutParams.MATCH_PARENT,
				TableRow.LayoutParams.MATCH_PARENT));
		tr.addView(Tools.createFieldHeader("Rnk", getActivity()
				.getApplicationContext()));
		tr.addView(Tools.createFieldHeader("Team", getActivity()
				.getApplicationContext()));
		tr.addView(Tools.createFieldHeader("GP", getActivity()
				.getApplicationContext()));
		tr.addView(Tools.createFieldHeader("W", getActivity()
				.getApplicationContext()));
		tr.addView(Tools.createFieldHeader("D", getActivity()
				.getApplicationContext()));
		tr.addView(Tools.createFieldHeader("L", getActivity()
				.getApplicationContext()));
		tr.addView(Tools.createFieldHeader("+/-", getActivity()
				.getApplicationContext()));
		tr.addView(Tools.createFieldHeader("P", getActivity()
				.getApplicationContext()));

		table.addView(tr, new TableLayout.LayoutParams(
				TableLayout.LayoutParams.MATCH_PARENT,
				TableLayout.LayoutParams.MATCH_PARENT));

	}

	private void createTableRows(ArrayList<GroupTeam> listTeams,
			TableLayout table) {

		try {

			for (int i = 0; i < listTeams.size(); i++) {
				TableRow tr = new TableRow(getActivity()
						.getApplicationContext());
				tr.setLayoutParams(new TableRow.LayoutParams(
						TableRow.LayoutParams.MATCH_PARENT,
						TableRow.LayoutParams.MATCH_PARENT));

				tr.addView(Tools.createField(Integer.toString(i + 1),
						getActivity().getApplicationContext()));

				tr.addView(Tools.createField(listTeams.get(i).teamName,
						getActivity().getApplicationContext()));

				tr.addView(Tools.createField(
						Integer.toString(listTeams.get(i).games_played),
						getActivity().getApplicationContext()));

				tr.addView(Tools.createField(
						Integer.toString(listTeams.get(i).win), getActivity()
								.getApplicationContext()));

				tr.addView(Tools.createField(
						Integer.toString(listTeams.get(i).draw), getActivity()
								.getApplicationContext()));

				tr.addView(Tools.createField(
						Integer.toString(listTeams.get(i).lose), getActivity()
								.getApplicationContext()));

				int goals = Tools.calculateGoalsPlusMinus(
						listTeams.get(i).goals_for,
						listTeams.get(i).goals_against);
				tr.addView(Tools.createField(Integer.toString(goals),
						getActivity().getApplicationContext()));

				int points = Tools.calculatePoints(listTeams.get(i).win,
						listTeams.get(i).draw);
				tr.addView(Tools.createField(Integer.toString(points),
						getActivity().getApplicationContext()));

				table.addView(tr, new TableLayout.LayoutParams(
						TableLayout.LayoutParams.MATCH_PARENT,
						TableLayout.LayoutParams.MATCH_PARENT));
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	@Override
	public void onResume() {
		super.onResume(); // Always call the superclass method first
		try {
			linearLayout.removeAllViews();
			ArrayList<Group> groups = DatabaseLayer.getGroupsByChampionship(
					getActivity().getApplicationContext(), champName);
			for (int i = 0; i < groups.size(); i++) {
				ArrayList<GroupTeam> groupTeams = DatabaseLayer
						.getGroupTeamsPerGroupName(getActivity()
								.getApplicationContext(),
								groups.get(i).group_name, champName);
				createGroupTable(groupTeams, linearLayout,
						groups.get(i).group_name);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
