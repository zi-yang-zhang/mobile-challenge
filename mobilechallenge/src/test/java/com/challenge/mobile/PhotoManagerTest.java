package com.challenge.mobile;

import com.challenge.mobile.manager.PhotoManager;
import com.challenge.mobile.model.Photo;
import com.challenge.mobile.modules.ManagerModule;
import com.challenge.mobile.modules.NetworkModule;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import javax.inject.Inject;

import dagger.Module;
import dagger.ObjectGraph;
import rx.observers.TestSubscriber;

/**
 * Created by robertzzy on 31/07/17.
 */

public class PhotoManagerTest {

	@Inject
	protected PhotoManager photoManager;

	@Before
	public void setup() {
		ObjectGraph.create(new NetworkModule(), new ManagerModule(), new TestModule()).inject(this);
	}

	@Test
	public void testGetPhotosWithCurrentPage() throws Exception {
		TestSubscriber<List<Photo>> responseTestSubscriber = new TestSubscriber<>();
		photoManager.getPhotos(false).subscribe(responseTestSubscriber);
		responseTestSubscriber.awaitTerminalEvent();
		responseTestSubscriber.assertCompleted();
		responseTestSubscriber.assertNoErrors();
		responseTestSubscriber.assertValueCount(1);
		Assert.assertEquals(1, photoManager.getCurrentPage());
		Assert.assertEquals(20, responseTestSubscriber.getOnNextEvents().get(0).size());

	}

	@Test
	public void testGetPhotosWithNextPage() throws Exception {
		TestSubscriber<List<Photo>> responseTestSubscriber = new TestSubscriber<>();
		photoManager.getPhotos(false).subscribe();
		photoManager.getPhotos(true).subscribe(responseTestSubscriber);
		responseTestSubscriber.awaitTerminalEvent();
		responseTestSubscriber.assertCompleted();
		responseTestSubscriber.assertNoErrors();
		responseTestSubscriber.assertValueCount(1);
		Assert.assertEquals(2, photoManager.getCurrentPage());
		Assert.assertEquals(20, responseTestSubscriber.getOnNextEvents().get(0).size());

	}

	@Module(
			injects = PhotoManagerTest.class,
			//Need dependencies from other modules
			complete = false
	)
	protected class TestModule {
	}

}
