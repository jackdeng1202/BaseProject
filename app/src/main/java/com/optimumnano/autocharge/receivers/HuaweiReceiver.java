package com.optimumnano.autocharge.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 作者：刘广茂 on 2016/11/21 17:05
 * <p>
 * 邮箱：liuguangmao@optimumchina.com
 */
public class HuaweiReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println(intent.getAction());
    }
}
