package com.challenge.mobile.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by robertzzy on 30/07/17.
 */

public class Photo {
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
}
