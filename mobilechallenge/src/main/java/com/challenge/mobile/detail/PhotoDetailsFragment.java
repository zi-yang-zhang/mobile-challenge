package com.challenge.mobile.detail;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.challenge.mobile.R;
import com.challenge.mobile.gallery.PhotoGalleryActivity;
import com.challenge.mobile.manager.PhotoManager;
import com.challenge.mobile.model.Photo;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import timber.log.Timber;

/**
 * Created by zhangziy on 2017-07-31.
 */

public class PhotoDetailsFragment extends DialogFragment implements ViewPager.OnPageChangeListener {
	private static final String ARG_PHOTO_INDEX = "arg_photo_index";
	private Toolbar toolbar;
	private TextView userName;
	private TextView rating;
	private TextView votes;
	private PhotoManager photoManager;
	private ViewPager viewPager;
	private int currentIndex = 0;
	private Subscription updateSubscription;
	private Subscription loadingSubscription;

	public static PhotoDetailsFragment create(int index) {
		Bundle arg = new Bundle();
		arg.putInt(ARG_PHOTO_INDEX, index);
		PhotoDetailsFragment fragment = new PhotoDetailsFragment();
		fragment.setArguments(arg);
		return fragment;
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		photoManager = ((PhotoGalleryActivity) getActivity()).getManager();
	}

	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		setStyle(STYLE_NO_TITLE, R.style.AppTheme_Dialog_Full);
		return super.onCreateDialog(savedInstanceState);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.activity_photo_detail, container, false);
	}

	@Override
	public void onActivityCreated(Bundle arg0) {
		super.onActivityCreated(arg0);
		Window window = getDialog().getWindow();
		if (window != null) {
			window.getAttributes().windowAnimations = R.style.DialogAnimation;
		}
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		toolbar = view.findViewById(R.id.tool_bar);
		userName = view.findViewById(R.id.user_name);
		rating = view.findViewById(R.id.rating);
		votes = view.findViewById(R.id.votes);
		viewPager = view.findViewById(R.id.photo);
		toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
		toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				dismiss();
			}
		});
		Bundle arg = getArguments();
		if (arg != null && arg.containsKey(ARG_PHOTO_INDEX)) {
			currentIndex = arg.getInt(ARG_PHOTO_INDEX);
			Photo photo = photoManager.getCachedPhotos().get(currentIndex);
			viewPager.addOnPageChangeListener(this);
			viewPager.setAdapter(new PhotoDetailAdapter(photoManager));
			viewPager.setCurrentItem(currentIndex);
			updatePhotoDescription(photo);
		} else {
			dismiss();
		}

	}


	@Override
	public void onResume() {
		super.onResume();
		Timber.d("Subscribe to update");
	}

	@Override
	public void onPause() {
		super.onPause();
		Timber.d("UnSubscribe to update");
		if (updateSubscription != null && !updateSubscription.isUnsubscribed())
			updateSubscription.unsubscribe();
		if (loadingSubscription != null && !loadingSubscription.isUnsubscribed())
			loadingSubscription.unsubscribe();

	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		if (position == photoManager.getCachedPhotos().size() - 1) {
			loadingSubscription = photoManager.getMorePhotos().observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Integer>() {
				@Override
				public void call(Integer lastIndex) {
					Timber.d("Data updated, notify adapter");
					PagerAdapter adapter = viewPager.getAdapter();
					adapter.notifyDataSetChanged();
				}
			}, new Action1<Throwable>() {
				@Override
				public void call(Throwable throwable) {
					Timber.e(throwable, "Error Loading data");
				}
			});
		} else {
			currentIndex = position;
			updatePhotoDescription(photoManager.getCachedPhotos().get(position));
		}
	}

	private void updatePhotoDescription(Photo photo) {
		toolbar.setTitle(photo.getName());
		userName.setText(photo.getUser().getFullname());
		rating.setText(String.valueOf(photo.getRating()));
		votes.setText(String.valueOf(photo.getVotesCount()));
	}

	@Override
	public void onPageSelected(int position) {

	}

	@Override
	public void onPageScrollStateChanged(int state) {

	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		super.onDismiss(dialog);
		photoManager.getUpdateIndexPublisher().call(currentIndex);
	}
}
