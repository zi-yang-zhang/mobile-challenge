package com.challenge.mobile.network;

import com.challenge.mobile.model.network.response.PhotosResponse;

import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Single;

/**
 * Created by robertzzy on 31/07/17.
 */

public interface Api {

	@GET("/v1/photos")
	Single<PhotosResponse> getPhotos(@QueryMap Map<String, String> queries);

}
