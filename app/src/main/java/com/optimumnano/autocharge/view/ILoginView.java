package com.optimumnano.autocharge.view;

import com.lgm.baseframe.ui.IBaseView;

/**
 * 作者：刘广茂 on 2016/11/18 14:09
 * <p>
 * 邮箱：liuguangmao@optimumchina.com
 */
public interface ILoginView extends IBaseView {

    String getPhoneNumber();

    String getVerificationCode();

    String getPlateNumber();

    void hidePlateNumberView();

    void clearInputs();

    void loginSuccess(Object object);

}
