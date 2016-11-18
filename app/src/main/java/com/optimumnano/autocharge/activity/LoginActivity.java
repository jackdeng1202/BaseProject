package com.optimumnano.autocharge.activity;

import android.os.Bundle;
import android.view.View;

import com.lgm.baseframe.base.BaseActivity;
import com.lgm.baseframe.common.http.HttpClientManager;
import com.optimumnano.autocharge.presenter.LoginPresenter;
import com.optimumnano.autocharge.view.ILoginView;

/**
 * 作者：刘广茂 on 2016/11/18 14:24
 * <p>
 * 邮箱：liuguangmao@optimumchina.com
 */
public class LoginActivity extends BaseActivity implements ILoginView {

    LoginPresenter loginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("登录");
        HttpClientManager.getInstance().initOkHttpClient(this);
        loginPresenter = new LoginPresenter(this);
        setRightCustomBtn("请求", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginPresenter.login();
            }
        });
    }



    @Override
    public String getPhoneNumber() {
        return null;
    }

    @Override
    public String getVerificationCode() {
        return null;
    }

    @Override
    public String getPlateNumber() {
        return null;
    }

    @Override
    public void hidePlateNumberView() {

    }

    @Override
    public void clearInputs() {

    }

    @Override
    public void loginSuccess(Object object) {

    }
}
