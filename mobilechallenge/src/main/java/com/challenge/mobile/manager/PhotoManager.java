package com.challenge.mobile.manager;

import com.challenge.mobile.model.Photo;
import com.challenge.mobile.model.network.request.PhotosRequest;
import com.challenge.mobile.model.network.response.PhotosResponse;
import com.challenge.mobile.network.Api;

import java.util.List;

import retrofit2.Retrofit;
import rx.Single;
import rx.functions.Func1;

/**
 * Created by robertzzy on 31/07/17.
 */

public class PhotoManager {

	private static final String GALLERY_PHOTO_SIZE = "4";
	private static final String GALLERY_PHOTO_FEATURE = "popular";
	private static final String GALLERY_PHOTO_SORT = "rating";
	private static final String EXCLUDE_NUDE = "Nude";

	protected int currentPage = 1;

	protected boolean hasMorePage = true;

	protected Api api;


	public PhotoManager(Retrofit retrofit) {
		api = retrofit.create(Api.class);
	}

	public Single<List<Photo>> getPhotos(boolean nextPage, boolean excludeNude) {
		PhotosRequest request = new PhotosRequest(GALLERY_PHOTO_FEATURE, GALLERY_PHOTO_SORT, GALLERY_PHOTO_SIZE, String.valueOf(nextPage ? currentPage + 1 : currentPage), excludeNude ? EXCLUDE_NUDE : "");
		return api.getPhotos(request.toQueryMap()).map(new Func1<PhotosResponse, List<Photo>>() {
			@Override
			public List<Photo> call(PhotosResponse photosResponse) {
				currentPage = photosResponse.getCurrentPage();
				hasMorePage = photosResponse.getTotalPages() > currentPage;
				return photosResponse.getPhotos();
			}
		});
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public boolean hasMorePage() {
		return hasMorePage;
	}
}
