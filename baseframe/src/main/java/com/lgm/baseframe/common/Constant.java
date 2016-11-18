package com.lgm.baseframe.common;


import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.lgm.baseframe.BuildConfig;

/**
 * Created by LGM on 2015/12/2.
 */
public class Constant {


	public static String userAgent = "cheyouji/" + BuildConfig.VERSION_NAME + "/android/zh";

	public static final String FILE_UPLOAD_URL = "http://121.40.221.223:5002/qiniu/bucket/avator";

	/**
	 * 基本URL
	 */
	public static  String BASE_URL = "http://motor.adonging.com/";

	//public static  String BASE_URL = "http://121.40.208.200:4001/";

	public static final String RELEASE_URL = "http://motor.adonging.com/";

	public static final String DEBUG_URL = "http://121.40.208.200:4001/";


	/**
	 * 用户登录注册相关
	 */
	public static String LOGIN_URL = BASE_URL + "user";

	/**
	 * 用户信息相关
	 */
	public static String USER_URL = BASE_URL + "api/user";

	/**
	 * 设备信息
	 */
	public static String DEVICE_URL = BASE_URL + "api/device";

	/**
	 * 设备绑定模块
	 */
	public static String BIND_URL = BASE_URL + "api/bind";

	public static String MANIFEST_URL = BASE_URL + "api/manifest";

	public static void initUrls(Context context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences("serverConfig", Context.MODE_PRIVATE);
		String url = sharedPreferences.getString("serverUrl", BASE_URL);
		if (!TextUtils.isEmpty(BASE_URL)) {
			BASE_URL = url;
		}
		LOGIN_URL = BASE_URL + "user";
		USER_URL = BASE_URL + "api/user";
		DEVICE_URL = BASE_URL + "api/device";
		BIND_URL = BASE_URL + "api/bind";
		MANIFEST_URL = BASE_URL + "api/manifest";
		PLACE_URL = BASE_URL + "api/device";
	}


	public static final String PAY_SUCCESS = "success";
	public static final String PAY_FAILED = "fail";
	public static final String PAY_CANCLE = "cancel";
	public static final String PAY_INVALID = "invalid";


	/**
	 * 设备信息
	 */
	public static String PLACE_URL = BASE_URL + "api/device";

	public static final String getAlarmUrl(String deviceId) {
		return DEVICE_URL + "/" + deviceId + "/alarm";
	}

	public static final String getFamilyUrl(String deviceId) {
		return DEVICE_URL + "/" + deviceId + "/follower";
	}


	public static final String getChangMemberStateUrl(String deviceId,String uid) {
		return DEVICE_URL + "/" + deviceId + "/follower/" + uid;
	}

	public static final String getVoiceUrl(String deviceId) {
		return DEVICE_URL + "/" + deviceId + "/voice";
	}

	public static final String getOrderUrl(String deviceId) {
		return DEVICE_URL + "/" + deviceId + "/order";
	}

	public static final String getTrackPointOffset(String deviceId, long offset) {
		return DEVICE_URL + "/" + deviceId + "/track/" + offset;
	}

	public static final String getTrackPointRange(String deviceId, long startTime, long endTime) {
		return DEVICE_URL + "/" + deviceId + "/track/range/" + startTime + ":" + endTime;
	}

	public static final String getSafeAreaList(String deviceId) {
		return DEVICE_URL + "/" + deviceId + "/safearea";
	}

	public static final String getSafeArea(String deviceId, String areaId) {
		return DEVICE_URL + "/" + deviceId + "/safearea/" + areaId;
	}


	public static final String getPlaceUrl(String deviceId) {
		return DEVICE_URL + "/" + deviceId + "/place";
	}

	public static final String getWatchUrl(String deviceId) {
		return DEVICE_URL + "/" + deviceId + "/watch";
	}

	public static final String getBrandUrl() {
		return BASE_URL + "brand";
	}


}
