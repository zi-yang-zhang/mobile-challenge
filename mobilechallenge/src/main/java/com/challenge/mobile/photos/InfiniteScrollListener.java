package com.challenge.mobile.photos;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

/**
 * Created by robertzzy on 31/07/17.
 */

public class InfiniteScrollListener extends RecyclerView.OnScrollListener {
	private onUpdateListListener updateListListener;

	public InfiniteScrollListener(onUpdateListListener updateListListener) {
		this.updateListListener = updateListListener;
	}

	@Override
	public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
		super.onScrolled(recyclerView, dx, dy);
		StaggeredGridLayoutManager manager =
				(StaggeredGridLayoutManager) recyclerView.getLayoutManager();

		int visibleItemCount = manager.getChildCount();
		int totalItemCount = manager.getItemCount();
		int[] firstVisibleItems = manager.findFirstVisibleItemPositions(null);
		if (visibleItemCount + firstVisibleItems[0] >= totalItemCount && ! updateListListener.loadingInProgress()) {
			if (updateListListener.hasMorePage()) {
				updateListListener.updateList();

			}
		}
	}

	public interface onUpdateListListener {
		void updateList();

		boolean hasMorePage();

		boolean loadingInProgress();
	}
}
