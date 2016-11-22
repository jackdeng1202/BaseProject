package com.optimumnano.autocharge;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.lgm.baseframe.base.BaseActivity;
import com.optimumnano.autocharge.common.BaiduNavigation;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @Bind(R.id.j)
    TextView mJ;
    @Bind(R.id.w)
    TextView mW;
    private BaiduNavigation mBaiduNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        ButterKnife.bind(this);

        mBaiduNavigation = new BaiduNavigation(this, BNRoutePlanNode.CoordinateType.GCJ02);
        setRightCustomBtn("开始导航", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double jd= 0;
                double wd= 0;
                try {
                    String j = mJ.getText().toString();
                    jd = Double.parseDouble(j);
                    String w = mW.getText().toString();
                    wd = Double.parseDouble(w);

                    if (j==null||w==null)
                        return;
                } catch (NumberFormatException e) {
                    showShortToast("请输入经纬度");
                    e.printStackTrace();
                }

                LatLng sLatLng = new LatLng(22.7236035873, 114.3793395781);//114.3793395781,22.7236035873
                LatLng eLatLng = new LatLng(wd, jd);
                mBaiduNavigation.start(sLatLng, eLatLng);
//              startActivity(new Intent(MainActivity.this, BaseMapActivity.class));
            }
        });
    }
}
