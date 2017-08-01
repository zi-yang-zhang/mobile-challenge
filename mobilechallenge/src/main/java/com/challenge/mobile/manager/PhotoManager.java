package com.challenge.mobile.manager;

import com.challenge.mobile.core.SharedPrefHelper;
import com.challenge.mobile.model.Photo;
import com.challenge.mobile.model.network.request.PhotosRequest;
import com.challenge.mobile.model.network.response.PhotosResponse;
import com.challenge.mobile.network.Api;
import com.jakewharton.rxrelay.BehaviorRelay;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;
import rx.Single;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Photo Manager controls memory caching of the result coming from the api.
 * It provides action to fetch photo data.
 */

public class PhotoManager {

	private static final String GALLERY_PHOTO_SIZE = "5";
	private static final String GALLERY_PHOTO_FEATURE = "popular";
	private static final String GALLERY_PHOTO_SORT = "rating";
	private static final String EXCLUDE_NUDE = "Nude";

	protected int currentPage = 1;

	protected boolean hasMorePage = true;

	protected Api api;

	protected List<Photo> photos;

	protected BehaviorRelay<Integer> updateIndexPublisher;

	protected SharedPrefHelper sharedPrefHelper;

	protected volatile boolean isLoading = false;

	public PhotoManager(Retrofit retrofit, SharedPrefHelper sharedPrefHelper) {
		api = retrofit.create(Api.class);
		photos = new ArrayList<>();
		updateIndexPublisher = BehaviorRelay.create();
		this.sharedPrefHelper = sharedPrefHelper;
	}

	public Single<List<Photo>> getPhotos(boolean nextPage) {
		PhotosRequest request = new PhotosRequest(GALLERY_PHOTO_FEATURE, GALLERY_PHOTO_SORT, GALLERY_PHOTO_SIZE, String.valueOf(nextPage ? currentPage + 1 : currentPage), sharedPrefHelper.isNudeExcluded() ? EXCLUDE_NUDE : "");
		return api.getPhotos(request.toQueryMap()).map(new Func1<PhotosResponse, List<Photo>>() {
			@Override
			public List<Photo> call(PhotosResponse photosResponse) {
				currentPage = photosResponse.getCurrentPage();
				hasMorePage = photosResponse.getTotalPages() > currentPage;
				return photosResponse.getPhotos();
			}
		}).doOnSubscribe(new Action0() {
			@Override
			public void call() {
				isLoading = true;
			}
		}).doOnSuccess(new Action1<List<Photo>>() {
			@Override
			public void call(List<Photo> photos) {
				isLoading = false;
			}
		});
	}

	public Single<Integer> getMorePhotos() {
		return getPhotos(true).observeOn(Schedulers.newThread()).map(new Func1<List<Photo>, Integer>() {
			@Override
			public Integer call(List<Photo> updates) {
				int lastUpdatedIndex = photos.size();
				photos.addAll(updates);
				return lastUpdatedIndex;
			}
		});
	}

	public Single<Integer> refreshPhotos() {
		currentPage = 1;
		return getPhotos(false).observeOn(Schedulers.newThread()).map(new Func1<List<Photo>, Integer>() {
			@Override
			public Integer call(List<Photo> updates) {
				photos = updates;
				return -1;
			}
		});
	}

	public BehaviorRelay<Integer> getUpdateIndexPublisher() {
		return updateIndexPublisher;
	}


	public int getCurrentPage() {
		return currentPage;
	}

	public boolean hasMorePage() {
		return hasMorePage;
	}

	public boolean loadingInProgress() {
		return isLoading;
	}

	public List<Photo> getCachedPhotos() {
		return photos;
	}
}
