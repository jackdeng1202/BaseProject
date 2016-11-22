package com.optimumnano.autocharge.view;

import com.lgm.baseframe.ui.IBaseView;
import com.optimumnano.autocharge.models.HttpResult;

/**
 * 作者：刘广茂 on 2016/11/18 14:09
 * <p>
 * 邮箱：liuguangmao@optimumchina.com
 */
public interface ILoginView extends IBaseView {

    String getPhoneNumber();

    String getVerificationCode();

    String getPlateNumber();

    void showPlateNumberView();

    boolean hidePlateNumberView();

    boolean isBindPlate();

    void clearInputs();

    void getVerificationCodeSuccess(int resultCode);

    void getVerificationCodeFailed(HttpResult resultMsg);

    void loginSuccess(Object object);

    void loginFailed(HttpResult resultMsg);

    void onUserBindError(int resultCode);

    void onConnectionFailed(Exception ex);

    void onRequestFailed(int resultCode);
}
