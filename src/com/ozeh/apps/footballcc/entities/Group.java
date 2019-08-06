package com.ozeh.apps.footballcc.entities;

import android.os.Parcel;
import android.os.Parcelable;

public class Group implements Parcelable {

	public long id;
	public String group_name;
	public String extra;

	public Group(String group_name, String extra) {

		this.group_name = group_name;
		this.extra = extra;

	}

	public Group(long id, String group_name, String extra) {

		this.group_name = group_name;
		this.extra = extra;

	}

	public Group(Parcel source) {
		// TODO Auto-generated constructor stub
		this.id = source.readLong();
		this.group_name = source.readString();
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
		dest.writeString(group_name);
		dest.writeString(extra);
	}

	public static final Parcelable.Creator<Group> CREATOR = new Parcelable.Creator<Group>() {
		public Group createFromParcel(Parcel source) {
			return new Group(source);
		}

		public Group[] newArray(int size) {
			return new Group[size];
		}
	};

}
