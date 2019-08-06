package com.ozeh.apps.footballcc;

import java.util.ArrayList;

import com.ozeh.apps.footballcc.contracts.ChampionshipsContract;
import com.ozeh.apps.footballcc.database.DatabaseLayer;
import com.ozeh.apps.footballcc.dialogs.DeleteChampDialog;
import com.ozeh.apps.footballcc.dialogs.NewChampDialog;
import com.ozeh.apps.footballcc.dialogs.NewGroupsDialog;
import com.ozeh.apps.footballcc.dialogs.NewTeamsDialog;
import com.ozeh.apps.footballcc.entities.Group;
import com.ozeh.apps.footballcc.entities.GroupTeam;
import com.ozeh.apps.footballcc.entities.Team;
import com.ozeh.apps.footballcc.extras.Tools;
import com.ozeh.apps.footballcc.interfaces.IDialogListener;

import android.support.v7.app.ActionBarActivity;
import android.app.DialogFragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity implements IDialogListener,
		LoaderManager.LoaderCallbacks<Cursor> {

	private static final int LOADER_ID = 1;
	private SimpleCursorAdapter currentChampsAdapter;
	private ListView listCurrentChamps = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		listCurrentChamps = (ListView) findViewById(R.id.lst_current_champ);
		fillCurrentChampsList();

		setListItemClickListener();

		setListLongItemClickListener();

		setMultiChoiceListener();

	}

	private void setMultiChoiceListener() {

	}

	private void setListLongItemClickListener() {
		listCurrentChamps
				.setOnItemLongClickListener(new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> parent,
							View view, int position, long id) {
						TextView textView = (TextView) view
								.findViewById(android.R.id.text1);

						DialogFragment dialog = new DeleteChampDialog();
						Bundle bundle = new Bundle();
						bundle.putString(
								ChampionshipsContract.CHAMPIONSHIP_NAME,
								textView.getText().toString());
						dialog.setArguments(bundle);
						dialog.show(getFragmentManager(), "DeleteChampDialog");
						return false;
					}
				});
	}

	private void setListItemClickListener() {
		listCurrentChamps.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				TextView textView = (TextView) view
						.findViewById(android.R.id.text1);

				Intent intent = new Intent(getApplicationContext(),
						DashboardActivity.class);

				intent.putExtra(ChampionshipsContract.CHAMPIONSHIP_NAME,
						textView.getText().toString());
				startActivity(intent);

			}
		});

	}

	private void fillCurrentChampsList() {

		String[] from = new String[] { ChampionshipsContract.CHAMPIONSHIP_NAME };
		// Fields on the UI to which we map
		int[] to = new int[] { android.R.id.text1 };

		getLoaderManager().initLoader(LOADER_ID, null, this);
		currentChampsAdapter = new SimpleCursorAdapter(getApplicationContext(),
				android.R.layout.simple_list_item_2, null, from, to, 0);

		listCurrentChamps.setAdapter(currentChampsAdapter);
		// setListAdapter(chatroomsAdapter);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void createChampionship(View view) {
		// Intent intent = new Intent(this, NewChampActivity.class);
		// startActivity(intent);

		DialogFragment dialog = new NewChampDialog();
		dialog.show(getFragmentManager(), "NewChampDialog");
	}

	@Override
	public void onDialogNegativeClick(DialogFragment dialog) {
		dialog.getDialog().cancel();

	}

	@Override
	public void onDialogPositiveClick(DialogFragment dialog,
			ArrayList<Group> listGroups, String champName) {

		try
		{
		if (dialog.getTag().equals("NewGroupsDialog")) {

			for (int i = 0; i < listGroups.size(); i++) {
				DatabaseLayer.insertGroup(getApplicationContext(),
						listGroups.get(i), champName);
			}

			DialogFragment d = new NewTeamsDialog();
			Bundle b = new Bundle();
			b.putString(ChampionshipsContract.CHAMPIONSHIP_NAME, champName);
			d.setArguments(b);
			d.show(getFragmentManager(), "NewTeamsDialog");
		}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}

	}

	@Override
	public void onDialogPositiveClick(DialogFragment dialog, String newChamp) {

		try {
			if (dialog.getTag().equals("NewChampDialog")) {

				String result = DatabaseLayer.insertChampionship(
						getApplicationContext(), newChamp);
				if (result.equals("SUCCESS")) {

					DialogFragment d = new NewGroupsDialog();
					Bundle b = new Bundle();
					b.putString(ChampionshipsContract.CHAMPIONSHIP_NAME,
							newChamp);
					d.setArguments(b);
					d.show(getFragmentManager(), "NewGroupsDialog");
				} else {
					Tools.showToast(result, getApplicationContext());
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	@Override
	public void onDialogPositiveClick(DialogFragment dialog, String champName,
			ArrayList<Team> listTeams) {
		try {
			if (dialog.getTag().equals("NewTeamsDialog")) {
				// add teams to Teams table
				// add teams to GroupTeams table

				for (int i = 0; i < listTeams.size(); i++) {
					DatabaseLayer
							.insertTeam(getApplicationContext(),
									listTeams.get(i), listTeams.get(i).extra,
									champName);

					GroupTeam groupTeam = new GroupTeam();
					DatabaseLayer.insertGroupTeam(getApplicationContext(),
							groupTeam, listTeams.get(i).extra,
							listTeams.get(i).team_name, champName);
				}

				// create matches for all groups
				DatabaseLayer.insertMatchesPerChampionship(
						getApplicationContext(), champName);

			} else if (dialog.getTag().equals("AddTeamsDialog")) {
				for (int i = 0; i < listTeams.size(); i++) {
					DatabaseLayer
							.insertTeam(getApplicationContext(),
									listTeams.get(i), listTeams.get(i).extra,
									champName);

					GroupTeam groupTeam = new GroupTeam();
					DatabaseLayer.insertGroupTeam(getApplicationContext(),
							groupTeam, listTeams.get(i).extra,
							listTeams.get(i).team_name, champName);
				}

				// insertMatchesPerTeam
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {
		// TODO Auto-generated method stub
		CursorLoader loader;
		switch (loaderId) {
		case LOADER_ID:
			String[] projection = { ChampionshipsContract.ID,
					ChampionshipsContract.CHAMPIONSHIP_NAME };
			loader = new CursorLoader(getApplicationContext(),
					ChampionshipsContract.CONTENT_URI, projection, null, null,
					null);
			break;
		default:
			loader = null;
			break;

		}
		return loader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		switch (loader.getId()) {
		case LOADER_ID:
			currentChampsAdapter.swapCursor(data);
			break;

		}

	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		currentChampsAdapter.swapCursor(null);

	}

}
