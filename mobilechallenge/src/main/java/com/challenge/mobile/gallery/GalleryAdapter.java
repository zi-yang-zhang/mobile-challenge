package com.challenge.mobile.gallery;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.challenge.mobile.R;
import com.challenge.mobile.manager.PhotoManager;
import com.challenge.mobile.model.Photo;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by robertzzy on 31/07/17.
 */

public class GalleryAdapter extends RecyclerView.Adapter<GalleryThumbnailViewHolder> {


	protected OnPhotoClickedListener onPhotoClickedListener;
	protected PhotoManager manager;

	public GalleryAdapter(OnPhotoClickedListener listener, PhotoManager manager) {
		this.onPhotoClickedListener = listener;
		this.manager = manager;
	}

	public void updatePhotos(int lastIndex) {
		if (lastIndex == -1) {
			notifyDataSetChanged();
		} else {
			List<Photo> photos = manager.getCachedPhotos();
			notifyItemRangeInserted(lastIndex, photos.size());
		}

	}

	@Override
	public GalleryThumbnailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View content = LayoutInflater.from(parent.getContext()).inflate(R.layout.component_image_thumbnail, parent, false);
		return new GalleryThumbnailViewHolder(content);
	}

	@Override
	public void onBindViewHolder(final GalleryThumbnailViewHolder holder, int position) {
		((SimpleDraweeView) holder.itemView).setImageURI(null);
		List<Photo> photos = manager.getCachedPhotos();
		holder.loadImage(photos.get(position));
		holder.itemView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				onPhotoClickedListener.photoClicked(holder.getAdapterPosition());
			}
		});
	}

	@Override
	public int getItemCount() {
		return manager.getCachedPhotos().size();
	}

	interface OnPhotoClickedListener {
		void photoClicked(int photoIndex);
	}
}
