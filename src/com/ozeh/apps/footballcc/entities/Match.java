package com.ozeh.apps.footballcc.entities;

import android.os.Parcel;
import android.os.Parcelable;

public class Match implements Parcelable {

	public long id;
	public String team1;
	public String team2;
	public int team1_result;
	public int team2_result;
	public String extra;

	public Match(String team1, String team2, int team1_result,
			int team2_result, String extra) {

		this.team1 = team1;
		this.extra = extra;
		this.team2 = team2;
		this.team1_result = team1_result;
		this.team2_result = team2_result;

	}

	public Match(long id, String team1, String team2, int team1_result,
			int team2_result, String extra) {

		this.id = id;
		this.team1 = team1;
		this.extra = extra;
		this.team2 = team2;
		this.team1_result = team1_result;
		this.team2_result = team2_result;

	}

	public Match(Parcel source) {
		// TODO Auto-generated constructor stub
		this.id = source.readLong();
		this.team1 = source.readString();
		this.team2 = source.readString();
		this.team1_result = source.readInt();
		this.team2_result = source.readInt();
		this.extra = source.readString();
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
		dest.writeString(team1);
		dest.writeString(team2);
		dest.writeInt(team1_result);
		dest.writeInt(team2_result);
		dest.writeString(extra);
	}

	public static final Parcelable.Creator<Match> CREATOR = new Parcelable.Creator<Match>() {
		public Match createFromParcel(Parcel source) {
			return new Match(source);
		}

		public Match[] newArray(int size) {
			return new Match[size];
		}
	};

}
