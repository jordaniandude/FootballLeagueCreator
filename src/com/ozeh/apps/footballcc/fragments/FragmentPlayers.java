package com.ozeh.apps.footballcc.fragments;

import java.util.ArrayList;
import java.util.List;

import com.ozeh.apps.footballcc.R;
import com.ozeh.apps.footballcc.contracts.ChampionshipsContract;
import com.ozeh.apps.footballcc.contracts.TeamsContract;
import com.ozeh.apps.footballcc.database.DatabaseLayer;
import com.ozeh.apps.footballcc.entities.Player;
import com.ozeh.apps.footballcc.extras.Tools;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * 
 */
public class FragmentPlayers extends Fragment {

	String teamName;
	String champName;
	Spinner spinnerPosition;
	EditText etName;
	EditText etNumber;
	EditText etPhone;
	EditText etEmail;
	Button btnAddPlayer;
	TableLayout tablePlayers;

	public FragmentPlayers() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_players, container,
				false);

		return view;

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		teamName = getArguments().getString(TeamsContract.TEAM_NAME);
		champName = getArguments().getString(
				ChampionshipsContract.CHAMPIONSHIP_NAME);
		initializeViews();

		fillSpinnerPositions();

		fillPlayersTable();
		addSpinnerOnItemSelectedListener();

		addAddPlayerOnClickListener();

	}

	private void fillPlayersTable() {
		ArrayList<Player> listPlayers = DatabaseLayer.getPlayersPerTeamName(
				getActivity().getApplicationContext(), teamName, champName);

		if (listPlayers != null) {
			if (listPlayers.size() > 0) {
				createTable(listPlayers);
			} else {
				tablePlayers.setVisibility(View.GONE);
			}
		}

	}

	private void fillSpinnerPositions() {
		List<String> list = new ArrayList<String>();
		list.add("Goalkeeper");
		list.add("Defender");
		list.add("Midfielder");
		list.add("Forward");

		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
				getActivity(), android.R.layout.simple_spinner_item, list);

		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerPosition.setAdapter(dataAdapter);

	}

	private void addSpinnerOnItemSelectedListener() {
		spinnerPosition.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				//((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
				//((TextView) parent.getChildAt(0)).setTextSize(15);

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});
	}

	private void addAddPlayerOnClickListener() {
		btnAddPlayer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					if (!etName.getText().toString().equals("")) {

						tablePlayers.setVisibility(View.VISIBLE);

						Player player = new Player(etName.getText().toString(),
								spinnerPosition.getSelectedItem().toString(),
								etNumber.getText().toString(), etEmail
										.getText().toString(), etPhone
										.getText().toString(), "");

						DatabaseLayer.insertPlayer(getActivity(), player,
								teamName,champName);
						fillPlayersTable();

						emtpyFields();
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

	private void emtpyFields() {
		etName.setText("");
		etPhone.setText("");
		etEmail.setText("");
		etNumber.setText("");

	}

	private void createTable(ArrayList<Player> listPlayers) {

		try {

			tablePlayers.removeAllViews();
			createTableHeader();

			for (int i = 0; i < listPlayers.size(); i++) {
				TableRow tr = new TableRow(getActivity()
						.getApplicationContext());
				tr.setLayoutParams(new TableRow.LayoutParams(
						TableRow.LayoutParams.MATCH_PARENT,
						TableRow.LayoutParams.MATCH_PARENT));

				tr.addView(Tools.createField(Integer.toString(i + 1),
						getActivity().getApplicationContext()));
				tr.addView(Tools.createField(listPlayers.get(i).player_name,
						getActivity().getApplicationContext()));
				tr.addView(Tools.createField(listPlayers.get(i).number,
						getActivity().getApplicationContext()));

				tablePlayers.addView(tr, new TableLayout.LayoutParams(
						TableLayout.LayoutParams.MATCH_PARENT,
						TableRow.LayoutParams.MATCH_PARENT));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	private void createTableHeader() {
		TableRow tr = new TableRow(getActivity().getApplicationContext());
		tr.setLayoutParams(new TableRow.LayoutParams(
				TableRow.LayoutParams.MATCH_PARENT,
				TableRow.LayoutParams.MATCH_PARENT));
		tr.addView(Tools.createFieldHeader("No", getActivity()
				.getApplicationContext()));
		tr.addView(Tools.createFieldHeader("Player Name", getActivity()
				.getApplicationContext()));
		tr.addView(Tools.createFieldHeader("Player Number", getActivity()
				.getApplicationContext()));

		tablePlayers.addView(tr, new TableLayout.LayoutParams(
				TableLayout.LayoutParams.MATCH_PARENT,
				TableRow.LayoutParams.MATCH_PARENT));

	}

	private void initializeViews() {

		spinnerPosition = (Spinner) getActivity().findViewById(
				R.id.spinner_position);
		etName = (EditText) getActivity().findViewById(R.id.et_player_name);
		etNumber = (EditText) getActivity().findViewById(R.id.et_player_number);
		etPhone = (EditText) getActivity().findViewById(R.id.et_phone);
		etEmail = (EditText) getActivity().findViewById(R.id.et_email);
		btnAddPlayer = (Button) getActivity().findViewById(R.id.btn_addPlayer);
		tablePlayers = (TableLayout) getActivity().findViewById(
				R.id.table_players);
	}

}
