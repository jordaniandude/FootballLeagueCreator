package com.ozeh.apps.footballcc;

import java.util.ArrayList;

import com.ozeh.apps.footballcc.contracts.ChampionshipsContract;
import com.ozeh.apps.footballcc.database.DatabaseLayer;
import com.ozeh.apps.footballcc.dialogs.NewGroupsDialog;
import com.ozeh.apps.footballcc.dialogs.NewTeamsDialog;
import com.ozeh.apps.footballcc.entities.Group;
import com.ozeh.apps.footballcc.entities.GroupTeam;
import com.ozeh.apps.footballcc.entities.Team;
import com.ozeh.apps.footballcc.fragments.FragmentGroups;
import com.ozeh.apps.footballcc.fragments.FragmentTeams;
import com.ozeh.apps.footballcc.interfaces.IDialogListener;

import android.app.ActionBar;
import android.app.DialogFragment;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class DashboardActivity extends FragmentActivity implements TabListener,
		IDialogListener {

	ActionBar actionBar;
	ViewPager viewPager;
	String champName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dashboard);
		champName = getIntent().getStringExtra(
				ChampionshipsContract.CHAMPIONSHIP_NAME);

		this.setTitle(champName);

		viewPager = (ViewPager) findViewById(R.id.pager);
		viewPager.setAdapter(new MyAdapter(getSupportFragmentManager(),
				champName));

		viewPager.setPageTransformer(false, new ViewPager.PageTransformer() {

			@Override
			public void transformPage(View page, float position) {
				final float normalizedposition = Math.abs(Math.abs(position) - 1);
				page.setAlpha(normalizedposition);
				// page.animate();
			}
		});

		setupOnPageChangeListener();

		actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		ActionBar.Tab tabGroups = actionBar.newTab();
		tabGroups.setText("Groups");
		tabGroups.setTabListener(this);

		ActionBar.Tab tabTeams = actionBar.newTab();
		tabTeams.setText("Teams");
		tabTeams.setTabListener(this);

		actionBar.addTab(tabGroups);
		actionBar.addTab(tabTeams);

	}

	private void setupOnPageChangeListener() {
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				actionBar.setSelectedNavigationItem(position);

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.dashboard, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.add_team) {

			DialogFragment d = new NewTeamsDialog();
			Bundle b = new Bundle();
			b.putString(ChampionshipsContract.CHAMPIONSHIP_NAME, champName);
			d.setArguments(b);
			d.show(getFragmentManager(), "AddTeamsDialog");

			return true;
		} else if (id == R.id.add_group) {
			DialogFragment d = new NewGroupsDialog();
			Bundle b = new Bundle();
			b.putString(ChampionshipsContract.CHAMPIONSHIP_NAME, champName);
			d.setArguments(b);
			d.show(getFragmentManager(), "NewGroupsDialog");

			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		viewPager.setCurrentItem(tab.getPosition());

	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDialogPositiveClick(DialogFragment dialog, String champName,
			ArrayList<Team> listTeams) {
		try {
			if (dialog.getTag().equals("AddTeamsDialog")) {
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

				for (int i = 0; i < listTeams.size(); i++) {
					DatabaseLayer.insertMatchesPerTeam(getApplicationContext(),
							listTeams.get(i).team_name, listTeams.get(i).extra,
							champName);
				}
				finish();
				startActivity(getIntent());

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onDialogPositiveClick(DialogFragment dialog, String newChamp) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDialogPositiveClick(DialogFragment dialog,
			ArrayList<Group> listGroups, String champName) {

		try {
			if (dialog.getTag().equals("NewGroupsDialog")) {

				for (int i = 0; i < listGroups.size(); i++) {
					DatabaseLayer.insertGroup(getApplicationContext(),
							listGroups.get(i), champName);
				}

				DialogFragment d = new NewTeamsDialog();
				Bundle b = new Bundle();
				b.putString(ChampionshipsContract.CHAMPIONSHIP_NAME, champName);
				d.setArguments(b);
				d.show(getFragmentManager(), "AddTeamsDialog");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	@Override
	public void onDialogNegativeClick(DialogFragment dialog) {
		dialog.getDialog().cancel();

	}

}

class MyAdapter extends FragmentPagerAdapter {

	String champ_name;

	public MyAdapter(FragmentManager fm, String champName) {
		super(fm);
		champ_name = champName;
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int position) {
		Fragment fragment = null;
		if (position == 0) {
			fragment = new FragmentGroups();
			Bundle bundle = new Bundle();
			bundle.putString(ChampionshipsContract.CHAMPIONSHIP_NAME,
					champ_name);
			fragment.setArguments(bundle);
		} else if (position == 1) {
			fragment = new FragmentTeams();
			Bundle bundle = new Bundle();
			bundle.putString(ChampionshipsContract.CHAMPIONSHIP_NAME,
					champ_name);
			fragment.setArguments(bundle);
		}
		return fragment;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 2;
	}
}
