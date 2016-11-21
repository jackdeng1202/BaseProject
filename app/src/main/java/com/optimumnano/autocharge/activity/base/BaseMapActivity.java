package com.optimumnano.autocharge.activity.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.optimumnano.autocharge.R;
import com.optimumnano.autocharge.common.BaiduNavigation;

public class BaseMapActivity extends AppCompatActivity implements View.OnClickListener,BDLocationListener,ILocation {
    public LocationClient mLocationClient = null;
    MapView mMapView = null;
    private BaiduMap mBaiduMap;
    private double mLatitude;
    private double mLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现

        setContentView(R.layout.activity_basemap);
        //获取地图控件引用
        initBaiduMap();
        registerLocationListener();
        initLocationButton();

        mBaiduMap.setMyLocationEnabled(true);
        startLocation();
        initLocation(mLocationClient);
    }


    @Override
    public void initBaiduMap() {
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setOnMapLongClickListener(new BaiduMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                System.out.println("latitude=" + latLng.latitude + "longtitude=" + latLng.longitude);
                Toast.makeText(BaseMapActivity.this, "latitude=" + latLng.latitude + "longtitude=" + latLng.longitude, Toast.LENGTH_SHORT).show();
                BaiduNavigation baiduNavigation=new BaiduNavigation(BaseMapActivity.this);
                baiduNavigation.start(new LatLng(mLatitude,mLongitude),latLng);
            }
        });
    }

    @Override
    public void registerLocationListener() {
        mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
        mLocationClient.registerLocationListener( this );    //注册监听函数
    }

    @Override
    public void initLocationButton() {
        findViewById(R.id.location).setOnClickListener(this);
    }

    public void initLocation(LocationClient locationClient){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span=1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        locationClient.setLocOption(option);
    }

    public void startLocation(){
        mLocationClient.start();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
                    case R.id.location:
                        startLocation();
                        break;
                }

    }

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        mLongitude = bdLocation.getLongitude();
        mLatitude = bdLocation.getLatitude();
        showMap(bdLocation,mBaiduMap);
    }

/*    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            mLongitude = bdLocation.getLongitude();
            mLatitude = bdLocation.getLatitude();
            showMap(bdLocation,mBaiduMap);

        }
    }*/

    protected void showMap(BDLocation bdLocation, BaiduMap baiduMap) {
        MyLocationData locData = new MyLocationData.Builder()
                .accuracy(bdLocation.getRadius())
                // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(100).latitude(bdLocation.getLatitude())
                .longitude(bdLocation.getLongitude()).build();
        baiduMap.setMyLocationData(locData);
        BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_geo);
        MyLocationConfiguration config = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, mCurrentMarker);
        baiduMap.setMyLocationConfigeration(config);
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(new LatLng(bdLocation.getLatitude(),bdLocation.getLongitude()));
        builder.zoom(18);
        //移动当前位置到屏幕中心
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(builder.build());
        baiduMap.setMapStatus(mapStatusUpdate);

        //停止获取位置
        mLocationClient.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
        mLocationClient.unRegisterLocationListener(this);
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

}
