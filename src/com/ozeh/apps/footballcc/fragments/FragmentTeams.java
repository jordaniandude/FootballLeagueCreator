package com.ozeh.apps.footballcc.fragments;

import java.util.ArrayList;
import com.ozeh.apps.footballcc.R;
import com.ozeh.apps.footballcc.TeamDashboard;
import com.ozeh.apps.footballcc.contracts.ChampionshipsContract;
import com.ozeh.apps.footballcc.contracts.TeamsContract;
import com.ozeh.apps.footballcc.database.DatabaseLayer;
import com.ozeh.apps.footballcc.entities.Team;
import com.ozeh.apps.footballcc.extras.Tools;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * 
 */
public class FragmentTeams extends Fragment {

	String champName;
	GridView gridView;

	public FragmentTeams() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_teams, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		champName = getArguments().getString(
				ChampionshipsContract.CHAMPIONSHIP_NAME);

		try {
			ArrayList<Team> teamsList = DatabaseLayer
					.getTeamsPerChampionshipName(getActivity()
							.getApplicationContext(), champName);
			gridView = (GridView) getActivity().findViewById(R.id.gv_teams);
			gridView.setAdapter(new ImageAdapter(getActivity()
					.getApplicationContext(), teamsList));

			gridView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {

					TextView tvTeam = (TextView) view
							.findViewById(R.id.grid_item_label);
					Intent intent = new Intent(view.getContext(),
							TeamDashboard.class);
					intent.putExtra(TeamsContract.TEAM_NAME, tvTeam.getText()
							.toString());
					intent.putExtra(ChampionshipsContract.CHAMPIONSHIP_NAME,
							champName);
					startActivity(intent);

				}
			});
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public class ImageAdapter extends BaseAdapter {
		private Context context;
		private ArrayList<Team> teamsList;

		public ImageAdapter(Context context, ArrayList<Team> teamsList) {
			this.context = context;
			this.teamsList = teamsList;
		}

		public View getView(int position, View convertView, ViewGroup parent) {

			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			View gridView;

			if (convertView == null) {

				gridView = new View(context);

				// get layout from mobile.xml
				gridView = inflater.inflate(R.layout.grid_teams, null);

				// set value into textview
				TextView textView = (TextView) gridView
						.findViewById(R.id.grid_item_label);
				textView.setText(teamsList.get(position).team_name);
				// textView.setTextColor(R.color.color_edit_1);
				// set image based on selected text
				ImageView imageView = (ImageView) gridView
						.findViewById(R.id.grid_item_image);

				// String team = teamsList[position];

				imageView.setImageResource(R.drawable.ic_launcher);

			} else {
				gridView = (View) convertView;
			}

			return gridView;
		}

		@Override
		public int getCount() {
			return teamsList.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

	}

}
