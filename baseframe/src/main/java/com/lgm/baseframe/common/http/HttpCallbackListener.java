package com.lgm.baseframe.common.http;

/**
 * Created by Administrator on 2016/1/14.
 */
public abstract class HttpCallbackListener implements HttpUtil.HTTPLiStener {
	/**
	 * 请求成功
	 *
	 * @param result      请求结果
	 * @param requestData 请求数据
	 */
	public abstract void onRequestSuccess(String result, Object requestData);

	/**
	 * 错误结果
	 *
	 * @param resultCode
	 */
	public boolean onRequestFailed(int resultCode) {
		return false;
	}

	/**
	 * 连接服务器失败，包括网络超时等
	 */
	public void onConnectionFailed() {
	}

	/**
	 * 服务器错误
	 *
	 * @param statusCode 请求状态码
	 */
	public abstract void onServerError(String result, int statusCode);
}
