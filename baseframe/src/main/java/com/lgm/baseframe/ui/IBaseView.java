package com.lgm.baseframe.ui;

/**
 * Created by mfwn on 2016/11/10.
 */

public interface IBaseView {
    void showLoading();

    void hideLoading();

    void onError(int errorCode,String errorMsg);

}
