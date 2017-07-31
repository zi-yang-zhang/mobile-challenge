package com.challenge.mobile.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by robertzzy on 30/07/17.
 */

public class User {
	@Expose
	@SerializedName("id")
	private Integer id;
	@Expose
	@SerializedName("username")
	private String username;
	@Expose
	@SerializedName("fullname")
	private String fullname;

	public Integer getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public String getFullname() {
		return fullname;
	}
}
