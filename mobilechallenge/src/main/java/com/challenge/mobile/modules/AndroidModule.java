package com.challenge.mobile.modules;

import com.challenge.mobile.photos.PhotoGalleryActivity;

import dagger.Module;

/**
 * Created by robertzzy on 31/07/17.
 */
@Module(
		injects = {PhotoGalleryActivity.class},
		//Need dependencies from other modules
		complete = false
)
public class AndroidModule {
}
