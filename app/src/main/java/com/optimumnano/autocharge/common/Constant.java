package com.optimumnano.autocharge.common;

import android.os.Build;

import com.optimumnano.autocharge.BuildConfig;

import java.util.Locale;

/**
 * 作者：刘广茂 on 2016/11/18 16:07
 * <p>
 * 邮箱：liuguangmao@optimumchina.com
 */
public class Constant {
    public static final String URL_BASE = "";
    public static final String URL_VERIFACATION_CODE = URL_BASE+ "/getVerificationCode";
    public static final String URL_LOGIN = URL_BASE+ "/user/login";
    public static final String URL_SUBMIT_PUSH_ID = URL_BASE+"/user/submitRegisterId";
    public static final String URL_GET_ORDERS = URL_BASE+"/order/getOrders";
    public static final String URL_CANCEL_ORDER = URL_BASE+"/order/cancelOrder";
    public static final String URL_CHANGE_ORDER_STATE = URL_BASE+"/order/changeState";
    public static final String USER_AGENT = "android/"+ Build.VERSION.SDK_INT+"/zhudongchongwei/"+ BuildConfig.VERSION_NAME
            + Locale.getDefault();

}
