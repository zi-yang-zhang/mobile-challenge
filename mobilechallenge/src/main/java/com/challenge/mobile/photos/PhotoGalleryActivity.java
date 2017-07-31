package com.challenge.mobile.photos;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.challenge.mobile.R;
import com.challenge.mobile.manager.PhotoManager;
import com.challenge.mobile.model.Photo;
import com.challenge.mobile.modules.AndroidModule;
import com.challenge.mobile.modules.ManagerModule;
import com.challenge.mobile.modules.NetworkModule;

import java.util.List;

import javax.inject.Inject;

import dagger.ObjectGraph;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import timber.log.Timber;

public class PhotoGalleryActivity extends AppCompatActivity implements GalleryAdapter.OnPhotoClickedListener, InfiniteScrollListener.onUpdateListListener, CompoundButton.OnCheckedChangeListener {


	private static final int PORTRAIT_SPAN = 2;
	private static final int LANDSCAPE_SPAN = 3;
	@Inject
	protected PhotoManager manager;
	protected Subscription photosSubscription;
	private View loading;
	private RecyclerView gallery;
	private CheckBox excludeNude;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ObjectGraph.create(new AndroidModule(), new ManagerModule(), new NetworkModule()).inject(this);
		setContentView(R.layout.activity_photo_gallery);
		loading = findViewById(R.id.loading);
		gallery = findViewById(R.id.gallery);
		excludeNude = findViewById(R.id.nudeOption);
		excludeNude.setOnCheckedChangeListener(this);
		GalleryAdapter adapter = new GalleryAdapter(this);
		RecyclerView.LayoutManager layoutManager;
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			layoutManager = new StaggeredGridLayoutManager(LANDSCAPE_SPAN, StaggeredGridLayoutManager.VERTICAL);
		} else {
			layoutManager = new StaggeredGridLayoutManager(PORTRAIT_SPAN, StaggeredGridLayoutManager.VERTICAL);
		}
		gallery.setLayoutManager(layoutManager);
		gallery.setAdapter(adapter);
		gallery.addOnScrollListener(new InfiniteScrollListener(this));
		gallery.addOnScrollListener(new RecyclerView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
				super.onScrollStateChanged(recyclerView, newState);
				if (newState == RecyclerView.SCROLL_STATE_IDLE) {
					Animation down = AnimationUtils.loadAnimation(PhotoGalleryActivity.this, R.anim.shift_down);
					down.setAnimationListener(new Animation.AnimationListener() {
						@Override
						public void onAnimationStart(Animation animation) {

						}

						@Override
						public void onAnimationEnd(Animation animation) {
							excludeNude.setVisibility(View.VISIBLE);

						}

						@Override
						public void onAnimationRepeat(Animation animation) {

						}
					});
					excludeNude.startAnimation(down);
				} else {
					Animation up = AnimationUtils.loadAnimation(PhotoGalleryActivity.this, R.anim.shift_up);
					up.setAnimationListener(new Animation.AnimationListener() {
						@Override
						public void onAnimationStart(Animation animation) {

						}

						@Override
						public void onAnimationEnd(Animation animation) {
							excludeNude.setVisibility(View.GONE);
						}

						@Override
						public void onAnimationRepeat(Animation animation) {

						}
					});
					excludeNude.startAnimation(up);
				}
			}
		});
		loading.setVisibility(View.VISIBLE);
		photosSubscription = manager.getPhotos(false, excludeNude.isChecked()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<List<Photo>>() {
			@Override
			public void call(List<Photo> photos) {
				loading.setVisibility(View.GONE);
				GalleryAdapter adapter = (GalleryAdapter) gallery.getAdapter();
				adapter.addPhotos(photos);
				photosSubscription.unsubscribe();
			}
		});
	}

	@Override
	public void photoClicked(Photo photo) {
		//TODO: Handle photo clicked
	}

	@Override
	public void updateList() {
		Timber.d("Update List");
		photosSubscription = manager.getPhotos(true, excludeNude.isChecked()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<List<Photo>>() {
			@Override
			public void call(List<Photo> photos) {
				loading.setVisibility(View.GONE);
				GalleryAdapter adapter = (GalleryAdapter) gallery.getAdapter();
				adapter.addPhotos(photos);
				photosSubscription.unsubscribe();
			}
		});
	}

	@Override
	public boolean hasMorePage() {
		return manager.hasMorePage();
	}

	@Override
	public boolean loadingInProgress() {
		return photosSubscription != null && !photosSubscription.isUnsubscribed();
	}

	@Override
	public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
		photosSubscription = manager.getPhotos(false, checked).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<List<Photo>>() {
			@Override
			public void call(List<Photo> photos) {
				loading.setVisibility(View.GONE);
				GalleryAdapter adapter = (GalleryAdapter) gallery.getAdapter();
				adapter.refreshData(photos);
				photosSubscription.unsubscribe();
			}
		});
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		setupLayoutManager(newConfig.orientation);
	}

	private void setupLayoutManager(int orientation) {
		StaggeredGridLayoutManager manager = (StaggeredGridLayoutManager) gallery.getLayoutManager();
		if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
			manager.setSpanCount(LANDSCAPE_SPAN);
		} else {
			manager.setSpanCount(PORTRAIT_SPAN);
		}
	}
}
