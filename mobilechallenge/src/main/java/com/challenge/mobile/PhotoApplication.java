package com.challenge.mobile;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

import timber.log.Timber;

/**
 * Created by robertzzy on 31/07/17.
 */

public class PhotoApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		Fresco.initialize(this);
		Timber.plant(new Timber.DebugTree());
	}
}
