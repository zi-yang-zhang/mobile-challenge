package com.challenge.mobile.detail;

import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.challenge.mobile.R;
import com.challenge.mobile.manager.PhotoManager;
import com.challenge.mobile.model.Photo;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by zhangziy on 2017-07-31.
 */

public class PhotoDetailAdapter extends PagerAdapter {

	private PhotoManager photoManager;


	public PhotoDetailAdapter(PhotoManager photoManager) {
		this.photoManager = photoManager;
	}

	@Override
	public int getCount() {
		return photoManager.getCachedPhotos().size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		LayoutInflater inflater = LayoutInflater.from(container.getContext());
		Photo photo = photoManager.getCachedPhotos().get(position);
		SimpleDraweeView photoView = (SimpleDraweeView) inflater.inflate(R.layout.component_image_detail, container, false);
		float aspectRatio = ((float) photo.getWidth()) / ((float) photo.getHeight());
		photoView.setAspectRatio(aspectRatio);
		photoView.setImageURI(Uri.parse(photo.getImageUrl()));
		container.addView(photoView);
		return photoView;
	}


	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}
}
