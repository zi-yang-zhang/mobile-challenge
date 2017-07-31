package com.challenge.mobile.photos;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

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

public class PhotoGalleryActivity extends AppCompatActivity implements GalleryAdapter.OnPhotoClickedListener, InfiniteScrollListener.onUpdateListListener {


	@Inject
	protected PhotoManager manager;
	protected Subscription photosSubscription;
	private View loading;
	private RecyclerView gallery;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ObjectGraph.create(new AndroidModule(), new ManagerModule(), new NetworkModule()).inject(this);
		setContentView(R.layout.activity_photo_gallery);
		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		loading = findViewById(R.id.loading);
		gallery = findViewById(R.id.gallery);
		GalleryAdapter adapter = new GalleryAdapter(this);
		gallery.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
		gallery.setAdapter(adapter);
		gallery.addOnScrollListener(new InfiniteScrollListener(this));
		loading.setVisibility(View.VISIBLE);
		photosSubscription = manager.getPhotos(false).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<List<Photo>>() {
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
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void photoClicked(Photo photo) {
		//TODO: Handle photo clicked
	}

	@Override
	public void updateList() {
		if (gallery.getAdapter().getItemCount() > 0) {
			Timber.d("Update List");
			photosSubscription = manager.getPhotos(true).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<List<Photo>>() {
				@Override
				public void call(List<Photo> photos) {
					GalleryAdapter adapter = (GalleryAdapter) gallery.getAdapter();
					adapter.addPhotos(photos);
					photosSubscription.unsubscribe();
				}
			});
		}

	}

	@Override
	public boolean hasMorePage() {
		return manager.hasMorePage();
	}

	@Override
	public boolean loadingInProgress() {
		return ! photosSubscription.isUnsubscribed();
	}
}
