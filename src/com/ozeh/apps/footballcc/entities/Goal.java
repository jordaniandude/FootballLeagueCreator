package com.ozeh.apps.footballcc.entities;

import android.os.Parcel;
import android.os.Parcelable;

public class Goal implements Parcelable {

	public long id;
	public String player;
	public int match_fk;
	public int minute;
	// Normal, Own Goal, Penalty
	public String goal_type;
	public String extra;

	public Goal(String player,int match_fk,int minute,String goalType, String extra) {

		this.player = player;
		this.match_fk = match_fk;
		this.minute = minute;
		this.goal_type = goalType;
		this.extra = extra;

	}

	public Goal(Parcel source) {
		// TODO Auto-generated constructor stub
		this.id = source.readLong();
		this.player = source.readString();
		this.match_fk = source.readInt();
		this.minute = source.readInt();
		this.goal_type = source.readString();
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
		dest.writeString(player);
		dest.writeInt(minute);
		dest.writeInt(match_fk);
		dest.writeString(goal_type);
		dest.writeString(extra);
	}

	public static final Parcelable.Creator<Goal> CREATOR = new Parcelable.Creator<Goal>() {
		public Goal createFromParcel(Parcel source) {
			return new Goal(source);
		}

		public Goal[] newArray(int size) {
			return new Goal[size];
		}
	};

}
