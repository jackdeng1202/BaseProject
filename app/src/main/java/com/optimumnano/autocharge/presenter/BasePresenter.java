package com.optimumnano.autocharge.presenter;

import com.lgm.baseframe.presenter.IBasePresenter;
import com.lgm.baseframe.ui.IBaseView;

import java.util.HashMap;
import java.util.Map;

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

    public Map<String,Object> getRequestParams(Map<String,Object> bizParams){

        Map<String,Object> params = new HashMap<>();

        params.put("params",bizParams);
        return params;
    }

}
