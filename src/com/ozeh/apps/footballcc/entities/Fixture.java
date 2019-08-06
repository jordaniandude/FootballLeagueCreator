package com.ozeh.apps.footballcc.entities;

import android.os.Parcel;
import android.os.Parcelable;

public class Fixture implements Parcelable {

	public int rank;
	
	public int games_played;
	public int lose ;
	public int draw ;
	public int win;
	public int points;
	public int plus_minus;
	public String teamName;

	public Fixture()
	{
		this.games_played = 0;
		this.win=0;
		this.lose = 0;
		this.draw = 0;
		this.points = 0;
		this.plus_minus = 0;
		this.teamName = "";
		this.rank = 0;
	}
	public Fixture(int rank,int games_played, int win, int lose,int draw, int points,int plus_minus,String teamName) {
		this.games_played = games_played;
		this.win=win;
		this.lose = lose;
		this.draw = draw;
		this.points = points;
		this.teamName = teamName;
		this.rank = rank;
		

	}

	public Fixture(Parcel source) {
		// TODO Auto-generated constructor stub
		this.rank = source.readInt();
		this.games_played = source.readInt();
		this.win = source.readInt();
		this.lose = source.readInt();
		this.draw = source.readInt();
		this.points = source.readInt();
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
		dest.writeLong(rank);
		dest.writeInt(games_played);
		dest.writeInt(win);
		dest.writeInt(lose);
		dest.writeInt(draw);
		dest.writeInt(points);
		dest.writeString(teamName);
	}

	public static final Parcelable.Creator<Fixture> CREATOR = new Parcelable.Creator<Fixture>() {
		public Fixture createFromParcel(Parcel source) {
			return new Fixture(source);
		}

		public Fixture[] newArray(int size) {
			return new Fixture[size];
		}
	};

}
