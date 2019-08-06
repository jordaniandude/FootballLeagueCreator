package com.ozeh.apps.footballcc.entities;

import android.os.Parcel;
import android.os.Parcelable;

public class Player implements Parcelable {

	public long id;
	public String player_name;
	public String position;
	public String number;
	public String email;
	public String phone;
	public String extra;

	public Player(String player_name, String position,String number, String email, String phone, String extra) {
		this.player_name = player_name;
		this.extra = extra;
		this.position = position;
		this.number = number;
		this.email = email;
		this.phone = phone;

	}
	public Player(long id,String player_name, String position,String number, String email, String phone, String extra) {
		this.player_name = player_name;
		this.extra = extra;
		this.position = position;
		this.number = number;
		this.email = email;
		this.phone = phone;
		this.id = id;

	}

	public Player(Parcel source) {
		// TODO Auto-generated constructor stub
		this.id = source.readLong();
		this.player_name = source.readString();
		this.extra = source.readString();
		this.position = source.readString();
		this.phone = source.readString();
		this.email = source.readString();
		this.number = source.readString();
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
		dest.writeString(player_name);
		dest.writeString(extra);
		dest.writeString(position);
		dest.writeString(number);
		dest.writeString(email);
		dest.writeString(phone);
	}

	public static final Parcelable.Creator<Player> CREATOR = new Parcelable.Creator<Player>() {
		public Player createFromParcel(Parcel source) {
			return new Player(source);
		}

		public Player[] newArray(int size) {
			return new Player[size];
		}
	};

}
