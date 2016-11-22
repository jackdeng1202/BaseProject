package com.optimumnano.autocharge.presenter;

import com.alibaba.fastjson.JSON;
import com.lgm.baseframe.common.LogUtil;
import com.lgm.baseframe.common.http.HttpCallbackListener;
import com.lgm.baseframe.common.http.HttpUtil;
import com.lgm.baseframe.common.http.RequestUtil;
import com.optimumnano.autocharge.common.Constant;
import com.optimumnano.autocharge.models.HttpResult;
import com.optimumnano.autocharge.view.ILoginView;

import java.util.HashMap;
import java.util.Map;

/**
 * 作者：刘广茂 on 2016/11/18 14:24
 * <p>
 * 邮箱：liuguangmao@optimumchina.com
 */
public class LoginPresenter extends BasePresenter<ILoginView> {
    private final ILoginView mView;

    public LoginPresenter(ILoginView mView) {
        super(mView);
        this.mView=mView;
    }

    public void getVerificationCode() {


        Map<String,Object> params = new HashMap<>();
        params.put("mobileNumber",mView.getPhoneNumber());
        params.put("purpose","login");
        RequestUtil.url(Constant.URL_VERIFACATION_CODE)
                .injectView(getView())
                .params(getRequestParams(params))
                .requestType(HttpUtil.RequestBodyType.JSON)
                .callback(new HttpCallbackListener() {
                    @Override
                    public void onRequestSuccess(String result, Object requestData) {
                        HttpResult requestResult = (HttpResult) JSON.parseObject(result,HttpResult.class);
                        LogUtil.i("result","onRequestSuccess"+requestResult.status);
                        if(requestResult.status==0){

                            String userState=JSON.parseObject(requestResult.result).getString("userState");
                            int userResultCode= Integer.parseInt(userState);
                            if (userResultCode==0){
                              //  mView.hidePlateNumberView();
                                mView.getVerificationCodeSuccess(userResultCode);
                            }else {
                               // mView.showPlateNumberView();
                                mView.onUserBindError(userResultCode);
                            }
                        }else {
                            if (requestResult.status==10003)
                                //mView.showPlateNumberView();
                            mView.getVerificationCodeFailed(requestResult);
                        }
                    }

                    @Override
                    public void onServerError(String result, int statusCode) {
                        mView.onError(statusCode,result);
                    }

                    @Override
                    public void onConnectionFailed(Exception ex) {
                        mView.onConnectionFailed(ex);
                    }

                    @Override
                    public boolean onRequestFailed(int resultCode) {
                        mView.onRequestFailed(resultCode);
                        return super.onRequestFailed(resultCode);
                    }
                }).post();
    }


    public void login(){
        Map<String,Object> params = new HashMap<>();
        params.put("mobileNumber", mView.getPhoneNumber());
      //  if (!mView.isBindPlate())
        params.put("plateNumber",mView.getPlateNumber());
        params.put("verificationCode",mView.getVerificationCode());
        //params.put("purpose", " login");
        //TODO 添加业务参数
        RequestUtil.url(Constant.URL_LOGIN)
                .injectView(getView())
                .params(getRequestParams(params))
                .requestType(HttpUtil.RequestBodyType.JSON)
                .callback(new HttpCallbackListener() {
                    @Override
                    public void onRequestSuccess(String result, Object requestData) {
                        HttpResult loginResult = (HttpResult) JSON.parseObject(result,HttpResult.class);
                        if (loginResult.status==0){
                            mView.loginSuccess(loginResult);
                        }else {
                            mView.loginFailed(loginResult);
                        }
                    }

                    @Override
                    public void onServerError(String result, int statusCode) {
                        mView.onError(statusCode,result);
                    }

                    @Override
                    public void onConnectionFailed(Exception ex) {
                        mView.onConnectionFailed(ex);
                    }

                    @Override
                    public boolean onRequestFailed(int resultCode) {
                        mView.onRequestFailed(resultCode);
                        return super.onRequestFailed(resultCode);
                    }
                }).post();

    }

}
