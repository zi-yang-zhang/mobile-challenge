package com.challenge.mobile.modules;

import android.content.Context;

import com.challenge.mobile.core.PhotoApplication;
import com.challenge.mobile.core.SharedPrefHelper;
import com.challenge.mobile.gallery.PhotoGalleryActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created by robertzzy on 31/07/17.
 */
@Module(
		injects = {PhotoGalleryActivity.class, PhotoApplication.class},
		//Need dependencies from other modules
		complete = false
)
public class AndroidModule {
	private final Context context;

	public AndroidModule(Context context) {
		this.context = context;
	}

	@Provides
	public SharedPrefHelper getSharedPrefHelper() {
		return new SharedPrefHelper(context);
	}
}
