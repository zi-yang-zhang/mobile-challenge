package com.challenge.mobile.photos;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.challenge.mobile.model.Photo;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by robertzzy on 31/07/17.
 */

public class GalleryThumbnailViewHolder extends RecyclerView.ViewHolder {
	public GalleryThumbnailViewHolder(View itemView) {
		super(itemView);
	}

	public void loadImage(Photo photo) {
		float aspectRatio = ((float) photo.getWidth()) / ((float) photo.getHeight());
		SimpleDraweeView thumbnail = ((SimpleDraweeView) itemView);
		thumbnail.setAspectRatio(aspectRatio);
		thumbnail.setImageURI(Uri.parse(photo.getImageUrl()));

	}
}
