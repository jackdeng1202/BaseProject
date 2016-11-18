package com.optimumnano.autocharge.presenter;

import com.lgm.baseframe.common.http.HttpCallbackListener;
import com.lgm.baseframe.common.http.RequestUtil;
import com.lgm.baseframe.presenter.BasePresenter;
import com.optimumnano.autocharge.view.ILoginView;

/**
 * 作者：刘广茂 on 2016/11/18 14:24
 * <p>
 * 邮箱：liuguangmao@optimumchina.com
 */
public class LoginPresenter extends BasePresenter<ILoginView> {
    public LoginPresenter(ILoginView mView) {
        super(mView);
    }

    public void getVerificationCode() {
        RequestUtil.url("http://www.baidu.com")
                .callback(new HttpCallbackListener() {
            @Override
            public void onRequestSuccess(String result, Object requestData) {
                getView().hidePlateNumberView();

            }

            @Override
            public void onServerError(String result, int statusCode) {

            }
        }).get();
    }


    public void login(){
        RequestUtil.url("http://www.baidu.com")
                .injectView(getView())
                .callback(new HttpCallbackListener() {
                    @Override
                    public void onRequestSuccess(String result, Object requestData) {

                    }

                    @Override
                    public void onServerError(String result, int statusCode) {

                    }
                }).get();

    }

}
