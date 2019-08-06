package com.ozeh.apps.footballcc.entities;

import android.os.Parcel;
import android.os.Parcelable;

public class Team implements Parcelable {

	public long id;
	public String team_name;
	public String extra;

	public Team(long id,String team_name, String extra) {
		
		this.team_name = team_name;
		this.extra = extra;
		this.id = id;

	}
	public Team(String team_name, String extra) {
		
		this.team_name = team_name;
		this.extra = extra;

	}

	public Team(Parcel source) {
		// TODO Auto-generated constructor stub
		this.id = source.readLong();
		this.team_name = source.readString();
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
		dest.writeString(team_name);
		dest.writeString(extra);
	}

	public static final Parcelable.Creator<Team> CREATOR = new Parcelable.Creator<Team>() {
		public Team createFromParcel(Parcel source) {
			return new Team(source);
		}

		public Team[] newArray(int size) {
			return new Team[size];
		}
	};

}
