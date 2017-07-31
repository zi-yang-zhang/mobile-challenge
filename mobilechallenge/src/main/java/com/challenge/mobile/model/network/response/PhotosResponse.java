package com.challenge.mobile.model.network.response;

import com.challenge.mobile.model.Photo;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by robertzzy on 30/07/17.
 */

public class PhotosResponse {

	@Expose
	@SerializedName("feature")
	private String feature;
	@Expose
	@SerializedName("current_page")
	private Integer currentPage;
	@Expose
	@SerializedName("total_pages")
	private Long totalPages;
	@Expose
	@SerializedName("total_items")
	private Long totalItems;
	@Expose
	@SerializedName("photos")
	private List<Photo> photos;

	public String getFeature() {
		return feature;
	}

	public Integer getCurrentPage() {
		return currentPage;
	}

	public Long getTotalPages() {
		return totalPages;
	}

	public Long getTotalItems() {
		return totalItems;
	}

	public List<Photo> getPhotos() {
		return photos;
	}
}
