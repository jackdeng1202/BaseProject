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
    public static final String URL_BASE = "http://192.168.101.148:4711/";
    public static final String URL_VERIFACATION_CODE = URL_BASE+ "app/getVerificationCode";
    public static final String URL_LOGIN = URL_BASE+ "app/user/login";
    public static final String URL_SUBMIT_PUSH_ID = URL_BASE+"app/user/submitRegisterId";
    public static final String URL_GET_ORDERS = URL_BASE+"app/order/getOrders";
    public static final String URL_CANCEL_ORDER = URL_BASE+"app/order/cancelOrder";
    public static final String URL_CHANGE_ORDER_STATE = URL_BASE+"app/order/changeState";
    public static final String USER_AGENT = "android/"+ Build.VERSION.SDK_INT+"/zhudongchongwei/"+ BuildConfig.VERSION_NAME
            + Locale.getDefault();

}
