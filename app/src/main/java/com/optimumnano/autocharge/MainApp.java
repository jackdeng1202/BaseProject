package com.optimumnano.autocharge;

import android.app.Application;
import android.content.Context;

import com.baidu.mapapi.SDKInitializer;
import com.lgm.baseframe.common.http.HttpClientManager;
import com.optimumnano.autocharge.common.Constant;

/**
 * 作者：刘广茂 on 2016/11/18 16:37
 * <p>
 * 邮箱：liuguangmao@optimumchina.com
 */
public class MainApp extends Application {

    private Context mContext;
    private static MainApp sMainApp;

    public static MainApp getInstance() {
        if (sMainApp == null) {
            sMainApp = new MainApp();
        }
        return sMainApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = getApplicationContext();
        SDKInitializer.initialize(this);
        HttpClientManager httpClientManager = HttpClientManager.getInstance();
        httpClientManager.initOkHttpClient(this);
        httpClientManager.setUserAgent(Constant.USER_AGENT);
    }


    public Context getContext() {
        return mContext;
    }

}
