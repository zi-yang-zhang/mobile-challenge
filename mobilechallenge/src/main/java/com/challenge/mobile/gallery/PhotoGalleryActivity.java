package com.challenge.mobile.gallery;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.challenge.mobile.R;
import com.challenge.mobile.core.PhotoApplication;
import com.challenge.mobile.core.SharedPrefHelper;
import com.challenge.mobile.detail.PhotoDetailsFragment;
import com.challenge.mobile.manager.PhotoManager;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

public class PhotoGalleryActivity extends AppCompatActivity implements GalleryAdapter.OnPhotoClickedListener, InfiniteScrollListener.onUpdateListListener, CompoundButton.OnCheckedChangeListener {


	private static final int PORTRAIT_SPAN = 2;
	private static final int LANDSCAPE_SPAN = 3;
	@Inject
	protected PhotoManager manager;
	@Inject
	protected SharedPrefHelper sharedPrefHelper;
	private View loading;
	private RecyclerView gallery;
	private CheckBox excludeNude;
	private CompositeSubscription subscriptions;
	private Subscription loadingSubscription;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		((PhotoApplication) getApplication()).getObjectGraph().inject(this);
		subscriptions = new CompositeSubscription();
		setContentView(R.layout.activity_photo_gallery);
		loading = findViewById(R.id.loading);
		gallery = findViewById(R.id.gallery);
		excludeNude = findViewById(R.id.nudeOption);
		excludeNude.setOnCheckedChangeListener(this);
		GalleryAdapter adapter = new GalleryAdapter(this, manager);
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
		excludeNude.setChecked(sharedPrefHelper.isNudeExcluded());
		loading.setVisibility(View.VISIBLE);
		loadingSubscription = manager.refreshPhotos().observeOn(AndroidSchedulers.mainThread()).subscribe(loadingSubscriber());
	}

	@Override
	protected void onResume() {
		super.onResume();
		Timber.d("Subscribe to update");
		subscriptions.add(manager.getUpdateIndexPublisher().observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Integer>() {
			@Override
			public void call(Integer lastIndex) {
				gallery.smoothScrollToPosition(lastIndex);
			}
		}));
	}

	@Override
	protected void onPause() {
		super.onPause();
		Timber.d("UnSubscribe to update");
		subscriptions.clear();
		if (loadingSubscription != null && !loadingSubscription.isUnsubscribed())
			loadingSubscription.unsubscribe();
	}

	@Override
	public void photoClicked(int photoIndex) {
		FragmentManager fragmentManager = getSupportFragmentManager();
		PhotoDetailsFragment.create(photoIndex).show(fragmentManager, null);
	}

	@Override
	public void updateList() {
		Timber.d("Update List");
		loadingSubscription = manager.getMorePhotos().observeOn(AndroidSchedulers.mainThread()).subscribe(loadingSubscriber());
	}

	@Override
	public boolean hasMorePage() {
		return manager.hasMorePage();
	}

	@Override
	public boolean loadingInProgress() {
		return manager.loadingInProgress();
	}

	@Override
	public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
		sharedPrefHelper.setNudeExcluded(checked);
		loadingSubscription = manager.refreshPhotos().observeOn(AndroidSchedulers.mainThread()).subscribe(loadingSubscriber());
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

	private Subscriber<Integer> loadingSubscriber() {
		return new Subscriber<Integer>() {
			@Override
			public void onCompleted() {

			}

			@Override
			public void onError(Throwable e) {
				Timber.e(e, "Error loading data");
			}

			@Override
			public void onNext(Integer lastIndex) {
				loading.setVisibility(View.GONE);
				GalleryAdapter adapter = (GalleryAdapter) gallery.getAdapter();
				adapter.updatePhotos(lastIndex);
			}
		};
	}

	public PhotoManager getManager() {
		return manager;
	}
}
