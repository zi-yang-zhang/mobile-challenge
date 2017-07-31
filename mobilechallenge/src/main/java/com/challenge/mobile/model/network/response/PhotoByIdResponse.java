package com.challenge.mobile.model.network.response;

import com.challenge.mobile.model.Photo;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by robertzzy on 31/07/17.
 */

public class PhotoByIdResponse {
	@Expose
	@SerializedName("photo")
	private Photo photo;

	public Photo getPhoto() {
		return photo;
	}
}
