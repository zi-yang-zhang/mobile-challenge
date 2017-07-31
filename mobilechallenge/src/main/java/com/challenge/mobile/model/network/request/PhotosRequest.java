package com.challenge.mobile.model.network.request;

import java.util.Map;

/**
 * Created by robertzzy on 31/07/17.
 */

public class PhotosRequest extends AbstractRequest {
	private static final String FEATURE_KEY = "feature";
	private static final String SORT_KEY = "sort";
	private static final String IMAGE_SIZE_KEY = "image_size";
	private static final String PAGE_KEY = "page";

	private String feature;
	private String sort;
	private String imageSize;
	private String page;

	public PhotosRequest(String feature, String sort, String imageSize, String page) {
		this.feature = feature;
		this.sort = sort;
		this.imageSize = imageSize;
		this.page = page;
	}

	public Map<String, String> toQueryMap() {
		Map<String, String> queryMap = super.toQueryMap();

		queryMap.put(FEATURE_KEY, feature);
		queryMap.put(SORT_KEY, sort);
		queryMap.put(IMAGE_SIZE_KEY, imageSize);
		queryMap.put(PAGE_KEY, page);

		return queryMap;
	}

}
