package com.ozeh.apps.footballcc;

import com.ozeh.apps.footballcc.contracts.ChampionshipsContract;
import com.ozeh.apps.footballcc.contracts.TeamsContract;
import com.ozeh.apps.footballcc.fragments.FragmentGroups;
import com.ozeh.apps.footballcc.fragments.FragmentPlayers;
import com.ozeh.apps.footballcc.fragments.FragmentTeamMatches;
import com.ozeh.apps.footballcc.fragments.FragmentTeams;

import android.app.ActionBar.Tab;
import android.app.ActionBar;
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

public class TeamDashboard extends FragmentActivity implements TabListener {

	ActionBar actionBar;
	ViewPager viewPager;
	String teamName, champName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_team_dashboard);

		teamName = getIntent().getStringExtra(TeamsContract.TEAM_NAME);
		champName = getIntent().getStringExtra(
				ChampionshipsContract.CHAMPIONSHIP_NAME);

		this.setTitle(teamName);

		viewPager = (ViewPager) findViewById(R.id.pager_team);
		viewPager.setAdapter(new AdapterTeams(getSupportFragmentManager(),
				teamName, champName));

		setupOnPageChangeListener();

		actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		ActionBar.Tab tabMatches = actionBar.newTab();
		tabMatches.setText("Matches");
		tabMatches.setTabListener(this);

		ActionBar.Tab tabPlayers = actionBar.newTab();
		tabPlayers.setText("Players");
		tabPlayers.setTabListener(this);

		actionBar.addTab(tabMatches);
		actionBar.addTab(tabPlayers);

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
		getMenuInflater().inflate(R.menu.team_dashboard, menu);
		return true;
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

}

class AdapterTeams extends FragmentPagerAdapter {

	String team_name;
	String champ_name;

	public AdapterTeams(FragmentManager fm, String teamName, String champName) {
		super(fm);
		team_name = teamName;
		champ_name = champName;
	}

	@Override
	public Fragment getItem(int position) {
		Fragment fragment = null;
		if (position == 0) {
			fragment = new FragmentTeamMatches();
			Bundle bundle = new Bundle();
			bundle.putString(TeamsContract.TEAM_NAME, team_name);
			bundle.putString(ChampionshipsContract.CHAMPIONSHIP_NAME,
					champ_name);
			fragment.setArguments(bundle);
		} else if (position == 1) {
			fragment = new FragmentPlayers();
			Bundle bundle = new Bundle();
			bundle.putString(TeamsContract.TEAM_NAME, team_name);
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
