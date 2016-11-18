package com.lgm.baseframe.presenter;

import com.lgm.baseframe.ui.IBaseView;

/**
 * Created by mfwn on 2016/11/12.
 */

public class BasePresenter<V extends IBaseView> implements IBasePresenter<IBaseView> {
    private final V mView;

    public BasePresenter(V mView) {
        this.mView = mView;
    }

    @Override
    public V getView() {
        return (V)mView;
    }
}
