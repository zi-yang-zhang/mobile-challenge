package com.challenge.mobile.modules;

import com.challenge.mobile.BuildConfig;

import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by robertzzy on 31/07/17.
 */
@Module(
		//Need dependencies from other modules
		complete = false,
		//Provides dependencies to other modules
		library = true
)
public class NetworkModule {
	@Provides
	Retrofit provideRetrofit() {
		HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(
				new HttpLoggingInterceptor.Logger() {
					@Override
					public void log(String message) {
						Timber.tag("OkHttp").d(message);
					}
				})
				.setLevel(HttpLoggingInterceptor.Level.BODY);
		OkHttpClient httpClient = new OkHttpClient
				.Builder()
				.connectTimeout(6, TimeUnit.SECONDS)
				.readTimeout(10, TimeUnit.SECONDS)
				.writeTimeout(10, TimeUnit.SECONDS)
				.addInterceptor(loggingInterceptor)
				.build();
		return new Retrofit
				.Builder()
				.baseUrl(BuildConfig.ENDPOINT)
				.client(httpClient)
				.addConverterFactory(GsonConverterFactory.create())
				.addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
				.build();
	}
}
