package com.ozeh.apps.footballcc.dialogs;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.ozeh.apps.footballcc.R;
import com.ozeh.apps.footballcc.contracts.ChampionshipsContract;
import com.ozeh.apps.footballcc.database.DatabaseLayer;
import com.ozeh.apps.footballcc.entities.Group;
import com.ozeh.apps.footballcc.entities.Team;
import com.ozeh.apps.footballcc.extras.Tools;
import com.ozeh.apps.footballcc.interfaces.IDialogListener;

public class NewTeamsDialog extends DialogFragment {
	IDialogListener mListener;
	Button btnAddTeam;
	TableLayout table_teams;
	EditText etTeamName;
	Spinner spinnerGroups;
	String champName;
	ArrayList<Team> listTeams = new ArrayList<Team>();

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		// Verify that the host activity implements the callback interface
		try {

			mListener = (IDialogListener) activity;

		} catch (ClassCastException e) {
			// The activity doesn't implement the interface, throw exception
			throw new ClassCastException(activity.toString()
					+ " must implement IDialogListener");
		}
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		LayoutInflater factory = LayoutInflater.from(getActivity());
		final View v = factory.inflate(R.layout.dialog_new_teams, null);

		champName = getArguments().getString(ChampionshipsContract.CHAMPIONSHIP_NAME);
		
		fillGroupsList(v);

		initializeViews(v);

		addAddTeamOnClickListener();

		addSpinnerOnItemSelectedListener();

		int style = DialogFragment.STYLE_NORMAL | DialogFragment.STYLE_NO_FRAME;
		setStyle(style, R.style.ASModaListDialogStyle);

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage(R.string.teams)
				.setView(v)
				.setPositiveButton(R.string.finish, null)
				.setNegativeButton(android.R.string.cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								mListener
										.onDialogNegativeClick(NewTeamsDialog.this);
							}
						});

		return builder.create();

	}

	private void addSpinnerOnItemSelectedListener() {
		spinnerGroups.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				try {
					((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
					((TextView) parent.getChildAt(0)).setTextSize(18);

					String selectedGroup = parent.getItemAtPosition(position)
							.toString();
					ArrayList<Team> listTeamsPerGroup = new ArrayList<Team>();

					for (int i = 0; i < listTeams.size(); i++) {
						if (listTeams.get(i).extra.equals(selectedGroup)) {
							listTeamsPerGroup.add(listTeams.get(i));
						}
					}
					//table_teams = (TableLayout) view.findViewById(R.id.table_teams);
					refreshTeamsTable(listTeamsPerGroup);
					// getTeamsPerGroupName
				} catch (Exception ex) {
					ex.printStackTrace();
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

	}

	private void refreshTeamsTable(ArrayList<Team> listTeams) {

		try {
			
			table_teams.removeAllViews();
			createTableHeader();
			
			

			for (int i = 0; i < listTeams.size(); i++) {
				TableRow tr = new TableRow(getDialog().getContext());
				tr.setLayoutParams(new TableRow.LayoutParams(
						TableRow.LayoutParams.MATCH_PARENT, 50));
				tr.addView(Tools.createField(Integer.toString(i + 1), getDialog()
						.getContext()));
				tr.addView(Tools.createField(listTeams.get(i).team_name,
						getDialog().getContext()));
				tr.addView(Tools.createField(listTeams.get(i).extra,
						getDialog().getContext()));

				table_teams.addView(tr, new TableLayout.LayoutParams(
						TableLayout.LayoutParams.MATCH_PARENT, 50));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	private void refreshTeamsTable() {

		try {
			
			table_teams.removeAllViews();
			createTableHeader();
			ArrayList<Team> listTeamsPerGroup = new ArrayList<Team>();

			for (int i = 0; i < listTeams.size(); i++) {
				if (listTeams.get(i).extra.equals(spinnerGroups.getSelectedItem().toString())) {
					listTeamsPerGroup.add(listTeams.get(i));
				}
			}

			for (int i = 0; i < listTeamsPerGroup.size(); i++) {
				TableRow tr = new TableRow(getDialog().getContext());
				tr.setLayoutParams(new TableRow.LayoutParams(
						TableRow.LayoutParams.MATCH_PARENT, 50));
				tr.addView(Tools.createField(Integer.toString(i + 1), getDialog()
						.getContext()));
				tr.addView(Tools.createField(listTeamsPerGroup.get(i).team_name,
						getDialog().getContext()));
				tr.addView(Tools.createField(listTeamsPerGroup.get(i).extra,
						getDialog().getContext()));

				table_teams.addView(tr, new TableLayout.LayoutParams(
						TableLayout.LayoutParams.MATCH_PARENT, 50));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}
	
	private void createTableHeader() {
		TableRow tr = new TableRow(getDialog().getContext());
		tr.setLayoutParams(new TableRow.LayoutParams(
				TableRow.LayoutParams.MATCH_PARENT, 50));
		tr.addView(Tools.createFieldHeader("No", getDialog()
				.getContext()));
		tr.addView(Tools.createFieldHeader("Team Name",
				getDialog().getContext()));
		tr.addView(Tools.createFieldHeader("Group Name",
				getDialog().getContext()));

		table_teams.addView(tr, new TableLayout.LayoutParams(
				TableLayout.LayoutParams.MATCH_PARENT, 50));
		
	}

	private void initializeViews(View v) {
		table_teams = (TableLayout) v.findViewById(R.id.table_teams);
		table_teams.setVisibility(View.GONE);

		etTeamName = (EditText) v.findViewById(R.id.et_team_name);

		btnAddTeam = (Button) v.findViewById(R.id.btn_addteam);

	}

	private void addAddTeamOnClickListener() {
		btnAddTeam.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					if (!etTeamName.getText().toString().equals("")) {
						
						table_teams.setVisibility(View.VISIBLE);

						Team team = new Team(etTeamName.getText().toString(),
								spinnerGroups.getSelectedItem().toString());
						listTeams.add(team);
						refreshTeamsTable();

						etTeamName.setText("");
					} else {
						Tools.showToast("Please enter a Team Name",
								v.getContext());
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}

			}
		});

	}

	private void fillGroupsList(View v) {
		// ArrayList<Group> listGroups =
		// getArguments().getParcelableArrayList("ListGroups");
		String championshipName = getArguments().getString(
				ChampionshipsContract.CHAMPIONSHIP_NAME);
		ArrayList<Group> listGroups = DatabaseLayer.getGroupsByChampionship(
				v.getContext(), championshipName);

		spinnerGroups = (Spinner) v.findViewById(R.id.spinner_groups);
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < listGroups.size(); i++) {
			list.add(listGroups.get(i).group_name);
		}

		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
				v.getContext(), android.R.layout.simple_spinner_item, list);

		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerGroups.setAdapter(dataAdapter);

	}

	@Override
	public void onResume() {
		super.onResume();
		AlertDialog alertDialog = (AlertDialog) getDialog();
		// AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		Button okButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
		okButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (table_teams.getVisibility() == View.VISIBLE) {
					dismiss();
					
					mListener.onDialogPositiveClick(NewTeamsDialog.this,champName,listTeams);
				} else {
					Tools.showToast(
							"Please add at least one team to move to the next stage.",
							getDialog().getContext());
				}
			}
		});
	}
}
