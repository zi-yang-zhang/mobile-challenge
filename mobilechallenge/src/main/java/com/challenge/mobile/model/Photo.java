package com.challenge.mobile.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by robertzzy on 30/07/17.
 */

public class Photo implements Parcelable {
	public static final Creator<Photo> CREATOR = new Creator<Photo>() {
		@Override
		public Photo createFromParcel(Parcel in) {
			return new Photo(in);
		}

		@Override
		public Photo[] newArray(int size) {
			return new Photo[size];
		}
	};
	@Expose
	@SerializedName("id")
	private Integer id;
	@Expose
	@SerializedName("name")
	private String name;
	@Expose
	@SerializedName("description")
	private String description;
	@Expose
	@SerializedName("rating")
	private Double rating;
	@Expose
	@SerializedName("votes_count")
	private Integer votesCount;
	@Expose
	@SerializedName("image_url")
	private String imageUrl;
	@Expose
	@SerializedName("user")
	private User user;
	@Expose
	@SerializedName("width")
	private Long width;
	@Expose
	@SerializedName("height")
	private Long height;

	protected Photo(Parcel in) {
		id = in.readInt();
		name = in.readString();
		description = in.readString();
		rating = in.readDouble();
		votesCount = in.readInt();
		imageUrl = in.readString();
		user = in.readParcelable(User.class.getClassLoader());
		width = in.readLong();
		height = in.readLong();
	}

	public Integer getId() {
		return id;
	}


	public Long getWidth() {
		return width;
	}

	public Long getHeight() {

		return height;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public Double getRating() {
		return rating;
	}

	public Integer getVotesCount() {
		return votesCount;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public User getUser() {
		return user;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int i) {
		parcel.writeInt(this.id);
		parcel.writeString(this.name);
		parcel.writeString(this.description);
		parcel.writeDouble(this.rating);
		parcel.writeInt(this.votesCount);
		parcel.writeString(this.imageUrl);
		parcel.writeParcelable(this.user, i);
		parcel.writeLong(this.width);
		parcel.writeLong(this.height);
	}
}
