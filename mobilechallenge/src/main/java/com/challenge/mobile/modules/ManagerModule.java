package com.challenge.mobile.modules;

import com.challenge.mobile.manager.PhotoManager;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by robertzzy on 31/07/17.
 */
@Module(
		//Need dependencies from other modules
		complete = false,
		library = true
)
public class ManagerModule {

	@Provides
	PhotoManager providePhotoManager(Retrofit retrofit) {
		return new PhotoManager(retrofit);
	}
}
