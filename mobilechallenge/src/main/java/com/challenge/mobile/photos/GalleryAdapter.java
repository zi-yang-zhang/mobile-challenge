package com.challenge.mobile.photos;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.challenge.mobile.R;
import com.challenge.mobile.model.Photo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by robertzzy on 31/07/17.
 */

public class GalleryAdapter extends RecyclerView.Adapter<GalleryThumbnailViewHolder> {

	protected List<Photo> photos;
	protected OnPhotoClickedListener onPhotoClickedListener;

	public GalleryAdapter(OnPhotoClickedListener listener) {
		this.photos = new ArrayList<>();
		this.onPhotoClickedListener = listener;
	}

	public void addPhotos(List<Photo> photos) {
		int lastIndex = this.photos.size();
		this.photos.addAll(photos);
		notifyItemRangeInserted(lastIndex, photos.size());
	}

	@Override
	public GalleryThumbnailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View content = LayoutInflater.from(parent.getContext()).inflate(R.layout.component_image_thumbnail, parent, false);
		return new GalleryThumbnailViewHolder(content);
	}

	@Override
	public void onBindViewHolder(final GalleryThumbnailViewHolder holder, int position) {
		holder.loadImage(photos.get(position));
		holder.itemView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				onPhotoClickedListener.photoClicked(photos.get(holder.getAdapterPosition()));
			}
		});
	}

	@Override
	public int getItemCount() {
		return photos.size();
	}

	interface OnPhotoClickedListener {
		void photoClicked(Photo photo);
	}
}
