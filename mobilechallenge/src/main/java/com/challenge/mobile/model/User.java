package com.challenge.mobile.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by robertzzy on 30/07/17.
 */

public class User implements Parcelable {
	public static final Creator<User> CREATOR = new Creator<User>() {
		@Override
		public User createFromParcel(Parcel in) {
			return new User(in);
		}

		@Override
		public User[] newArray(int size) {
			return new User[size];
		}
	};
	@Expose
	@SerializedName("id")
	private Integer id;
	@Expose
	@SerializedName("username")
	private String username;
	@Expose
	@SerializedName("fullname")
	private String fullname;

	protected User(Parcel in) {
		id = in.readInt();
		username = in.readString();
		fullname = in.readString();
	}

	public Integer getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public String getFullname() {
		return fullname;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int i) {
		parcel.writeInt(this.id);
		parcel.writeString(this.username);
		parcel.writeString(this.fullname);
	}
}
