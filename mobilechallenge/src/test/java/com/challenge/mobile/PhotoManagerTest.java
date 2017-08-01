package com.challenge.mobile;

import com.challenge.mobile.core.SharedPrefHelper;
import com.challenge.mobile.manager.PhotoManager;
import com.challenge.mobile.model.Photo;
import com.challenge.mobile.modules.ManagerModule;
import com.challenge.mobile.modules.NetworkModule;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;

import javax.inject.Inject;

import dagger.Module;
import dagger.ObjectGraph;
import dagger.Provides;
import rx.observers.TestSubscriber;


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
		TestSubscriber<Integer> responseTestSubscriber = new TestSubscriber<>();
		photoManager.refreshPhotos().subscribe(responseTestSubscriber);
		responseTestSubscriber.awaitTerminalEvent();
		responseTestSubscriber = new TestSubscriber<>();
		photoManager.getMorePhotos().subscribe(responseTestSubscriber);
		responseTestSubscriber.awaitTerminalEvent();
		responseTestSubscriber.assertValue(20);
		Assert.assertEquals(2, photoManager.getCurrentPage());
		Assert.assertEquals(40, photoManager.getCachedPhotos().size());

	}

	@Test
	public void testGetPhotosWithFirstPage() throws Exception {
		TestSubscriber<Integer> responseTestSubscriber = new TestSubscriber<>();
		photoManager.refreshPhotos().subscribe(responseTestSubscriber);
		responseTestSubscriber.awaitTerminalEvent();
		responseTestSubscriber.assertValue(-1);
		Assert.assertEquals(1, photoManager.getCurrentPage());
		Assert.assertEquals(20, photoManager.getCachedPhotos().size());

	}


	@Module(
			injects = PhotoManagerTest.class,
			//Need dependencies from other modules
			complete = false,
			library = true
	)
	protected class TestModule {
		@Provides
		public SharedPrefHelper getSharedPrefHelper() {
			return Mockito.mock(SharedPrefHelper.class);
		}
	}

}
