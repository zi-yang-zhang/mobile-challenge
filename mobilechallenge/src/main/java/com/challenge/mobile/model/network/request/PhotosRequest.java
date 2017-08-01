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
	private static final String EXCLUDE_KEY = "exclude";

	private String feature;
	private String sort;
	private String imageSize;
	private String page;
	private String[] excludes;

	public PhotosRequest(String feature, String sort, String imageSize, String page, String... exclusion) {
		this.feature = feature;
		this.sort = sort;
		this.imageSize = imageSize;
		this.page = page;
		this.excludes = exclusion;
	}

	public Map<String, String> toQueryMap() {
		Map<String, String> queryMap = super.toQueryMap();

		queryMap.put(FEATURE_KEY, feature);
		queryMap.put(SORT_KEY, sort);
		queryMap.put(IMAGE_SIZE_KEY, imageSize);
		queryMap.put(PAGE_KEY, page);
		String excludes = excludeToCVS();
		if (excludes.length() > 0) {
			queryMap.put(EXCLUDE_KEY, excludeToCVS());
		}

		return queryMap;
	}

	private String excludeToCVS() {
		StringBuilder exclude = new StringBuilder();
		for (String exclusion : excludes) {
			exclude.append(exclusion);
			exclude.append(",");
		}
		exclude.deleteCharAt(exclude.lastIndexOf(","));
		return exclude.toString();
	}

}
