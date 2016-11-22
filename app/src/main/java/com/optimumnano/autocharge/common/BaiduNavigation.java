package com.optimumnano.autocharge.common;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.baidu.navisdk.adapter.BNOuterLogUtil;
import com.baidu.navisdk.adapter.BNOuterTTSPlayerCallback;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BNaviSettingManager;
import com.baidu.navisdk.adapter.BaiduNaviManager;
import com.lgm.baseframe.common.LogUtil;
import com.optimumnano.autocharge.activity.BNDemoGuideActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.baidu.navisdk.adapter.BNRoutePlanNode.CoordinateType.WGS84;

/**
 * Created by jack on 2016/11/19 0019.
 */

public class BaiduNavigation {


    public static List<Activity> activityList = new LinkedList<Activity>();

    private static final String APP_FOLDER_NAME = "wtmautocharge";
    private final Activity mContext;
    private String mSDCardPath = null;
    private BNRoutePlanNode.CoordinateType mCoordinateType;
    //默认经纬度坐标为百度坐标
    public static final BNRoutePlanNode.CoordinateType DEFULT_COORDINATETYPE = WGS84;
    public static final String ROUTE_PLAN_NODE = "routePlanNode";
    public static final String SHOW_CUSTOM_ITEM = "showCustomItem";
    public static final String RESET_END_NODE = "resetEndNode";
    public static final String VOID_MODE = "voidMode";

    public BaiduNavigation(Activity context, BNRoutePlanNode.CoordinateType CoordinateType) {
        this.mContext = context;
        mCoordinateType = CoordinateType;
        initDirs();
        if (initDirs()) {
            initNavi();
        }
        activityList.add(mContext);
        // 打开log开关
        BNOuterLogUtil.setLogSwitcher(false);
    }

    public BaiduNavigation(Activity context) {
        this.mContext = context;
        mCoordinateType = DEFULT_COORDINATETYPE;
        initDirs();
        if (initDirs()) {
            initNavi();
        }
        activityList.add(mContext);
        // 打开log开关
        BNOuterLogUtil.setLogSwitcher(false);
    }

    /**
     * 开始导航
     *
     * @param slongitude 起点经度
     * @param slatitude  起点纬度
     * @param elongitude 终点经度
     * @param elatitude  终点纬度
     */
    public void start(double slongitude, double slatitude, double elongitude, double elatitude,String sNodeName, String eNodeName) {
        if (BaiduNaviManager.isNaviInited()) {
            routeplanToNavi(mCoordinateType, slongitude, slatitude, elongitude, elatitude,sNodeName,eNodeName);
        }else {
            showToastMsg("初始失败");
        }
    }
    public void start(double slongitude, double slatitude, double elongitude, double elatitude) {
        start(slongitude, slatitude,elongitude,elatitude,null,null);
    }
    /**
     * 开始导航
     *
     * @param sLatLng 起点经纬度
     * @param eLatLng 起点经纬度
     * @param sNodeName  起点节点名字
     * @param sNodeName  终点节点名字
     */
    public void start(LatLng sLatLng, LatLng eLatLng,String sNodeName, String eNodeName) {
        start(sLatLng.longitude, sLatLng.latitude, eLatLng.longitude, eLatLng.latitude,sNodeName,eNodeName);
    }
    public void start(LatLng sLatLng, LatLng eLatLng) {
        start(sLatLng.longitude, sLatLng.latitude, eLatLng.longitude, eLatLng.latitude,null,null);
    }
    private boolean initDirs() {
        mSDCardPath = getSdcardDir();
        if (mSDCardPath == null) {
            return false;
        }
        File f = new File(mSDCardPath, APP_FOLDER_NAME);
        if (!f.exists()) {
            try {
                f.mkdir();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    String authinfo = null;

    /**
     * 内部TTS播报状态回传handler
     */
    private Handler ttsHandler = new Handler() {
        public void handleMessage(Message msg) {
            int type = msg.what;
            switch (type) {
                case BaiduNaviManager.TTSPlayMsgType.PLAY_START_MSG: {
//                    showToastMsg("Handler : TTS play start");
                    break;
                }
                case BaiduNaviManager.TTSPlayMsgType.PLAY_END_MSG: {
//                    showToastMsg("Handler : TTS play end");
                    break;
                }
                default:
                    break;
            }
        }
    };

    /**
     * 内部TTS播报状态回调接口
     */
    private BaiduNaviManager.TTSPlayStateListener ttsPlayStateListener = new BaiduNaviManager.TTSPlayStateListener() {

        @Override
        public void playEnd() {
//            showToastMsg("TTSPlayStateListener : TTS play end");
        }

        @Override
        public void playStart() {
//            showToastMsg("TTSPlayStateListener : TTS play start");
        }
    };

    public void showToastMsg(final String msg) {
        mContext.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initNavi() {

        BNOuterTTSPlayerCallback ttsCallback = null;

        BaiduNaviManager.getInstance().init(mContext, mSDCardPath, APP_FOLDER_NAME, new BaiduNaviManager.NaviInitListener() {
            @Override
            public void onAuthResult(int status, String msg) {
                if (0 == status) {
                    authinfo = "key校验成功!";
                }
                else {
                    authinfo = "key校验失败, " + msg;
                }
                showToastMsg(authinfo);

            }

            public void initSuccess() {
                showToastMsg("百度导航引擎初始化成功");
                initSetting();
            }

            public void initStart() {

                showToastMsg("百度导航引擎初始化开始");
            }

            public void initFailed() {

                showToastMsg("百度导航引擎初始化失败");
            }


        }, null, ttsHandler, ttsPlayStateListener);

    }

    private String getSdcardDir() {
        if (Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().toString();
        }
        return null;
    }

    private void routeplanToNavi(BNRoutePlanNode.CoordinateType coType, double slongitude, double slatitude, double elongitude, double elatitude, String sNodeName, String eNodeName) {
        BNRoutePlanNode sNode = null;
        BNRoutePlanNode eNode = null;
        sNode = new BNRoutePlanNode(slongitude, slatitude, sNodeName, null, coType);
        eNode = new BNRoutePlanNode(elongitude, elatitude, eNodeName, null, coType);

        if (sNode != null && eNode != null) {
            List<BNRoutePlanNode> list = new ArrayList<BNRoutePlanNode>();
            list.add(sNode);
            list.add(eNode);
            BaiduNaviManager.getInstance().launchNavigator(mContext, list, 1, true, new DemoRoutePlanListener(sNode));
        }
    }

    public class DemoRoutePlanListener implements BaiduNaviManager.RoutePlanListener {

        private BNRoutePlanNode mBNRoutePlanNode = null;

        public DemoRoutePlanListener(BNRoutePlanNode node) {
            mBNRoutePlanNode = node;
        }

        @Override
        public void onJumpToNavigator() {
            /*
			 * 设置途径点以及resetEndNode会回调该接口
			 */

            for (Activity ac : activityList) {

                if (ac.getClass().getName().endsWith("BNDemoGuideActivity")) {
                    LogUtil.i("tag", "不能从当前activity开启导航=" + mContext.getClass().getSimpleName());
                    return;
                }
            }
            Intent intent = new Intent(mContext, BNDemoGuideActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(ROUTE_PLAN_NODE, (BNRoutePlanNode) mBNRoutePlanNode);
            intent.putExtras(bundle);
            mContext.startActivity(intent);


        }

        @Override
        public void onRoutePlanFailed() {
            showToastMsg("算路失败");
        }
    }

    private void initSetting() {
        // 设置是否双屏显示
        BNaviSettingManager.setShowTotalRoadConditionBar(BNaviSettingManager.PreViewRoadCondition.ROAD_CONDITION_BAR_SHOW_ON);
        // 设置导航播报模式
        BNaviSettingManager.setVoiceMode(BNaviSettingManager.VoiceMode.Veteran);
        // 是否开启路况
        BNaviSettingManager.setRealRoadCondition(BNaviSettingManager.RealRoadCondition.NAVI_ITS_ON);
    }

    private BNOuterTTSPlayerCallback mTTSCallback = new BNOuterTTSPlayerCallback() {

        @Override
        public void stopTTS() {
            // TODO Auto-generated method stub
            Log.e("test_TTS", "stopTTS");
        }

        @Override
        public void resumeTTS() {
            // TODO Auto-generated method stub
            Log.e("test_TTS", "resumeTTS");
        }

        @Override
        public void releaseTTSPlayer() {
            // TODO Auto-generated method stub
            Log.e("test_TTS", "releaseTTSPlayer");
        }

        @Override
        public int playTTSText(String speech, int bPreempt) {
            // TODO Auto-generated method stub
            Log.e("test_TTS", "playTTSText" + "_" + speech + "_" + bPreempt);

            return 1;
        }

        @Override
        public void phoneHangUp() {
            // TODO Auto-generated method stub
            Log.e("test_TTS", "phoneHangUp");
        }

        @Override
        public void phoneCalling() {
            // TODO Auto-generated method stub
            Log.e("test_TTS", "phoneCalling");
        }

        @Override
        public void pauseTTS() {
            // TODO Auto-generated method stub
            Log.e("test_TTS", "pauseTTS");
        }

        @Override
        public void initTTSPlayer() {
            // TODO Auto-generated method stub
            Log.e("test_TTS", "initTTSPlayer");
        }

        @Override
        public int getTTSState() {
            // TODO Auto-generated method stub
            Log.e("test_TTS", "getTTSState");
            return 1;
        }
    };

}
