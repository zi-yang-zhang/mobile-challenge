package com.challenge.mobile.core;

import android.app.Application;

import com.challenge.mobile.modules.AndroidModule;
import com.challenge.mobile.modules.ManagerModule;
import com.challenge.mobile.modules.NetworkModule;
import com.facebook.drawee.backends.pipeline.Fresco;

import dagger.ObjectGraph;
import timber.log.Timber;

/**
 * Created by robertzzy on 31/07/17.
 */

public class PhotoApplication extends Application {

	private ObjectGraph objectGraph;

	@Override
	public void onCreate() {
		super.onCreate();
		Fresco.initialize(this);
		Timber.plant(new Timber.DebugTree());
		objectGraph = ObjectGraph.create(new AndroidModule(this), new ManagerModule(), new NetworkModule());
		objectGraph.inject(this);
	}

	public ObjectGraph getObjectGraph() {
		return objectGraph;
	}
}
