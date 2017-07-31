package com.challenge.mobile.model.network.request;

import com.challenge.mobile.BuildConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by robertzzy on 31/07/17.
 */

public abstract class AbstractRequest {
	private static final String CONSUMER_KEY = "consumer_key";

	public Map<String, String> toQueryMap() {
		Map<String, String> queryMap = new HashMap<>();
		queryMap.put(CONSUMER_KEY, BuildConfig.CONSUMER_KEY);
		return queryMap;
	}
}
