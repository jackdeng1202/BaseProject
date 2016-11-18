package com.lgm.baseframe.common.http;

import android.content.Context;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

/**
 * Created by LGM on 2015/12/2.
 */
public class HttpClientManager {

	private static HttpClientManager httpClientManager;


	private HttpClientManager() {
	}

	private static OkHttpClient okHttpClient;


	public static HttpClientManager getInstance() {
		if (httpClientManager == null) {


			httpClientManager = new HttpClientManager();
		}
		return httpClientManager;
	}

	private PersistentCookieStore cookieStore;

	public void initOkHttpClient(Context context) {
		cookieStore = new PersistentCookieStore(context);
		okHttpClient = getOkHttpClient();
	}

	private boolean hasCookie = false;

	public OkHttpClient getOkHttpClient() {
		if(!hasCookie){
			OkHttpClient.Builder builder = new OkHttpClient.Builder();
			builder.connectTimeout(20, TimeUnit.SECONDS);
			builder.readTimeout(20, TimeUnit.SECONDS);
			builder.cookieJar(new CookieJar() {
				@Override
				public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
					if (cookieStore != null) {
						if (cookies != null && cookies.size() > 0) {
							for (Cookie item : cookies) {
								cookieStore.add(url, item);
							}
						}
					}
				}
				@Override
				public List<Cookie> loadForRequest(HttpUrl url) {
					if (cookieStore != null) {
						List<Cookie> cookies = cookieStore.get(url);
						return cookies;
					}
					return null;
				}
			});
			okHttpClient = builder.build();
			hasCookie = true;
		}

		return okHttpClient;
	}

}
