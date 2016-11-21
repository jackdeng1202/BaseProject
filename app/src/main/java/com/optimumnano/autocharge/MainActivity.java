package com.optimumnano.autocharge;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.lgm.baseframe.base.BaseActivity;
import com.optimumnano.autocharge.activity.base.BaseMapActivity;
import com.optimumnano.autocharge.common.BaiduNavigation;

public class MainActivity extends BaseActivity {

    private BaiduNavigation mBaiduNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBaiduNavigation = new BaiduNavigation(this);
        setRightCustomBtn("开始导航", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  LatLng sLatLng=new LatLng(114.3858490000, 22.7297140000);
                LatLng eLatLng=new LatLng(114.3897480000,22.7443130000);
                mBaiduNavigation.start(sLatLng, eLatLng);*/
                startActivity(new Intent(MainActivity.this, BaseMapActivity.class));
            }
        });
    }
}
