package com.optimumnano.autocharge;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;
import com.lgm.baseframe.common.http.HttpClientManager;
import com.optimumnano.autocharge.common.Constant;

/**
 * 作者：刘广茂 on 2016/11/18 16:37
 * <p>
 * 邮箱：liuguangmao@optimumchina.com
 */
public class MainApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        SDKInitializer.initialize(this);

        HttpClientManager httpClientManager = HttpClientManager.getInstance();
        httpClientManager.initOkHttpClient(this);
        httpClientManager.setUserAgent(Constant.USER_AGENT);
    }
}
