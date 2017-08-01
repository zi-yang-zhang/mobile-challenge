package com.challenge.mobile;

import com.challenge.mobile.model.network.request.PhotosRequest;
import com.challenge.mobile.model.network.response.PhotosResponse;
import com.challenge.mobile.modules.NetworkModule;
import com.challenge.mobile.network.Api;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;

import dagger.Module;
import dagger.ObjectGraph;
import retrofit2.Retrofit;
import rx.observers.TestSubscriber;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class NetworkTest {

	@Inject
	protected Retrofit retrofit;

	private Api api;

	@Before
	public void setup() {
		ObjectGraph.create(new NetworkModule(), new TestModule()).inject(this);
		api = retrofit.create(Api.class);
	}

	@Test
	public void testGetPhotos() throws Exception {
		PhotosRequest request = new PhotosRequest("fresh_today", "rating", "4", "1", "Nude");
		TestSubscriber<PhotosResponse> responseTestSubscriber = new TestSubscriber<>();
		api.getPhotos(request.toQueryMap()).subscribe(responseTestSubscriber);
		responseTestSubscriber.awaitTerminalEvent();
		responseTestSubscriber.assertCompleted();
		responseTestSubscriber.assertNoErrors();
		responseTestSubscriber.assertValueCount(1);
		Assert.assertNotEquals(0, responseTestSubscriber.getOnNextEvents().get(0).getPhotos().size());
	}
	@Module(
			injects = NetworkTest.class,
			//Need dependencies from other modules
			complete = false
	)
	protected class TestModule {
	}
}