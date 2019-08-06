package com.ozeh.apps.footballcc.entities;

import android.os.Parcel;
import android.os.Parcelable;

public class GroupTeam implements Parcelable {

	public long id;
	public int games_played;
	public int win;
	public int lose ;
	public int draw ;
	public int goals_for;
	public int goals_against;
	public String teamName;

	public GroupTeam()
	{
		this.games_played = 0;
		this.win=0;
		this.lose = 0;
		this.draw = 0;
		this.goals_against = 0;
		this.goals_for = 0;
	}
	public GroupTeam(int games_played, int win, int lose,int draw, int goals_for,int goals_against,String teamName) {
		this.games_played = games_played;
		this.win=win;
		this.lose = lose;
		this.draw = draw;
		this.goals_against = goals_against;
		this.goals_for = goals_for;
		this.teamName = teamName;

	}
	public GroupTeam(long id,int games_played, int win, int lose,int draw, int goals_for,int goals_against) {
		this.games_played = games_played;
		this.win=win;
		this.lose = lose;
		this.draw = draw;
		this.goals_against = goals_against;
		this.goals_for = goals_for;
		this.id = id;

	}

	public GroupTeam(Parcel source) {
		// TODO Auto-generated constructor stub
		this.id = source.readLong();
		this.games_played = source.readInt();
		this.win = source.readInt();
		this.lose = source.readInt();
		this.draw = source.readInt();
		this.goals_against = source.readInt();
		this.goals_for = source.readInt();
		this.teamName = source.readString();
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeLong(id);
		dest.writeInt(games_played);
		dest.writeInt(win);
		dest.writeInt(lose);
		dest.writeInt(draw);
		dest.writeInt(goals_against);
		dest.writeInt(goals_for);
		dest.writeString(teamName);
	}

	public static final Parcelable.Creator<GroupTeam> CREATOR = new Parcelable.Creator<GroupTeam>() {
		public GroupTeam createFromParcel(Parcel source) {
			return new GroupTeam(source);
		}

		public GroupTeam[] newArray(int size) {
			return new GroupTeam[size];
		}
	};

}
