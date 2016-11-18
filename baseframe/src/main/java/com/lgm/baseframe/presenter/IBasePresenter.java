package com.lgm.baseframe.presenter;

import com.lgm.baseframe.ui.IBaseView;

/**
 * Created by mfwn on 2016/11/12.
 */

public interface IBasePresenter<V extends IBaseView> {

    V getView();
}
