package com.optimumnano.autocharge.presenter;

import com.lgm.baseframe.common.http.HttpCallbackListener;
import com.lgm.baseframe.common.http.RequestUtil;
import com.optimumnano.autocharge.view.IOrderView;

import java.util.HashMap;
import java.util.Map;

/**
 * 作者：刘广茂 on 2016/11/18 16:20
 * <p>
 * 邮箱：liuguangmao@optimumchina.com
 */
public class OrderManagePresenter extends BasePresenter<IOrderView> {


    public OrderManagePresenter(IOrderView mView) {
        super(mView);
    }

    public void cancelOrder(String orderId,String reason,int code){
        Map<String,Object> params = new HashMap<>();
        params.put("orderId",orderId);
        params.put("reasonChose",code);
        params.put("reason",reason);
        RequestUtil.url("")
                .injectView(getView())
                .params(params)
                .callback(new HttpCallbackListener() {
                    @Override
                    public void onRequestSuccess(String result, Object requestData) {

                    }

                    @Override
                    public void onServerError(String result, int statusCode) {

                    }
                })
                .post();

    }

    public void changeOrderState(int state,String orderId){
        Map<String,Object> params = new HashMap<>();
        params.put("orderId",orderId);
        params.put("status",state);
        RequestUtil.url("")
                .injectView(getView())
                .params(params)
                .callback(new HttpCallbackListener() {
                    @Override
                    public void onRequestSuccess(String result, Object requestData) {

                    }

                    @Override
                    public void onServerError(String result, int statusCode) {

                    }
                })
                .post();

    }




}
