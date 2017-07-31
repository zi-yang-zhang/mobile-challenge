package com.challenge.mobile.model.network.request;

import java.util.Map;

/**
 * Created by robertzzy on 31/07/17.
 */

public class PhotoByIdRequest extends AbstractRequest {
	private static final String IMAGE_SIZE_KEY = "image_size";

	private String imageSize;
	private long id;

	public PhotoByIdRequest(String imageSize, long id) {
		this.imageSize = imageSize;
		this.id = id;
	}

	public Map<String, String> toQueryMap() {
		Map<String, String> queryMap = super.toQueryMap();
		queryMap.put(IMAGE_SIZE_KEY, imageSize);
		return queryMap;
	}

	public long getId() {
		return id;
	}
}
