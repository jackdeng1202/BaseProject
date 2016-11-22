package com.lgm.baseframe.common.http;

import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.lgm.baseframe.common.LogUtil;
import com.lgm.baseframe.common.ThreadPoolManager;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * http连接中心
 *
 * @author 刘广茂
 */
public class HttpUtil implements Runnable {

    public static final int DID_START = 0;
    public static final int DID_ERROR = 1;
    public static final int DID_SUCCEED = 2;
    private static final int GET = 0;
    private static final int POST = 1;
    private static final int PUT = 2;
    private static final int DELETE = 3;


    private RequestBodyType requestBodyType;

    public enum RequestMethod {
        GET, POST, PUT, DELETE
    }

    public enum RequestBodyType {
        FORM, JSON
    }

    private static final int BITMAP = 4;
    private static final String RESULT_FAIL = "fail";

    private String url;
    private int method;
    private String data;
    private HttpCallbackListener listener;
    private Map<String, Object> requestData;
    private OkHttpClient httpClient;


    /**
     * 发送get请求
     *
     * @param url 请求地址
     */
    public void get(String url, HttpCallbackListener listener) {
        this.method = GET;
        this.url = url;
        this.listener = listener;
        ThreadPoolManager.getInstance().push(this);
    }


    public void get(String url, Map<String, Object> params,
                    HttpCallbackListener listener) {
        get(constructGetParams(url, params), listener);
    }


    public void post(String url, Map<String, Object> params, RequestBodyType bodyType,
                     HttpCallbackListener listener) {
        this.method = POST;
        initRequest(url, params, bodyType, listener);
    }


    public void put(String url, Map<String, Object> params, RequestBodyType bodyType,
                    HttpCallbackListener listener) {
        this.method = PUT;
        initRequest(url, params, bodyType, listener);

    }

    public void delete(String url, Map<String, Object> params, RequestBodyType bodyType,
                       HttpCallbackListener listener) {
        this.method = DELETE;
        initRequest(url, params, bodyType, listener);

    }

    private void initRequest(String url, Map<String, Object> params, RequestBodyType bodyType, HttpCallbackListener listener) {
        this.requestData = params;
        this.listener = listener;
        this.url = url;
        if (bodyType == null) {
            bodyType = RequestBodyType.FORM;
        }
        requestBodyType = bodyType;
        ThreadPoolManager.getInstance().push(this);
    }

    public interface HTTPLiStener {

        /**
         * 请求成功
         *
         * @param result      请求结果
         * @param requestData 请求数据
         */
        void onRequestSuccess(String result, Object requestData);

        /**
         * 错误结果
         *
         * @param resultCode
         */
        boolean onRequestFailed(int resultCode);

        /**
         * 连接服务器失败，包括网络超时等
         */
        void onConnectionFailed(Exception ex);

        /**
         * 服务器错误
         *
         * @param statusCode 请求状态码
         */
        void onServerError(String result, int statusCode);

    }


    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case HttpUtil.DID_START: {
                    break;
                }
                case HttpUtil.DID_SUCCEED: {
                    postRequestCallback(message);
                    break;
                }
                case HttpUtil.DID_ERROR: {
                    break;
                }
            }
        }

    };


    /**
     * post回调
     *
     * @param message 请求结果
     */
    private void postRequestCallback(Message message) {
        if (listener != null) {
            if (message.arg2 < 0) {
                Exception obj = (Exception) message.obj;
                listener.onConnectionFailed(obj);
                LogUtil.v("result", "code:" + message.arg2 + "; url:" + url + ";resultMsg:"
                        + obj.getMessage());
                return;
            }
            String resultObj = (String) message.obj;
            LogUtil.v("result", "code:" + message.arg2 + ";url:" + url + "; resultMsg:" + resultObj);
            if (TextUtils.isEmpty(resultObj) || "fail".equals(resultObj)) {

                if (message.arg2 >= 200 && message.arg2 < 300) {
                    String result = (String) message.obj;
                    listener.onRequestSuccess(result, requestData);
                } else {
                    listener.onServerError("fail", message.arg2);
                }
            } else {
                if (message.arg2 > 300 || message.arg2 < 200) {
                    listener.onServerError(resultObj, message.arg2);
                    return;
                }
                try {
                    String result = (String) message.obj;
                    listener.onRequestSuccess(result, requestData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    public void run() {
        httpClient = HttpClientManager.getInstance().getOkHttpClient();
        try {
            sendRequest();
        } catch (IOException e) {
                this.sendPostMessage(e, -3);
            e.printStackTrace();
        }
        ThreadPoolManager.getInstance().didComplete(this);
    }


    private void sendRequest() throws IOException {
        Request request = null;

        try {
            switch (method) {
                case GET:
                    LogUtil.v("method", "GET");
                    request = initGetRequest();
                    break;
                case POST:
                    LogUtil.v("method", "POST");
                    request = initPostRequest();
                    break;
                case PUT:
                    LogUtil.v("method", "PUT");
                    request = initPutRequest();
                    break;
                case DELETE:
                    LogUtil.v("method", "DELETE");
                    request = initDeleteRequest();
                    break;
            }
        } catch (Exception e) {
            sendPostMessage(e, -5);
            e.printStackTrace();
            return;
        }
        try {
            Response response = httpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                String responseStr = response.body().string();
                this.sendPostMessage(responseStr, response.code());
            } else {
                LogUtil.v("Fail Exception", response.toString());
                this.sendPostMessage(response.body().string(), response.code());
            }
        } catch (IOException e) {
            LogUtil.e(e.getClass().getName(), request.url().toString());
            e.printStackTrace();
            this.sendPostMessage(e, -3);
        }
    }

    private Request initPostRequest() {
        RequestBody requestBody = getRequestBody();
        Request.Builder builder = new Request.Builder()
                .url(url)
                .post(requestBody)
                .header("User-Agent", HttpClientManager.getInstance().getUserAgent());
        Request request = builder.build();
        return request;
    }


    private Request initPutRequest() {
        RequestBody requestBody = getRequestBody();
        Request.Builder builder = new Request.Builder()
                .url(url)
                .header("User-Agent", HttpClientManager.getInstance().getUserAgent())
                .put(requestBody);
        Request request = builder.build();
        return request;
    }

    private Request initDeleteRequest() {
        RequestBody requestBody = getRequestBody();
        Request.Builder builder = new Request.Builder()
                .url(url)
                .header("User-Agent", HttpClientManager.getInstance().getUserAgent())
                .delete(requestBody);
        Request request = builder.build();
        return request;
    }


    private Request initGetRequest() {
        Request request = new Request.Builder()
                .tag(this)
                .header("User-Agent", HttpClientManager.getInstance().getUserAgent())
                .url(url).build();
        return request;
    }

    @NonNull
    private RequestBody getRequestBody() {
        RequestBody requestBody = null;
        if (requestBodyType == RequestBodyType.FORM) {
            if (requestData != null) {
                boolean hasFile = isParamHasFile();
                if (hasFile) {
                    requestBody = getMultipartRequestBody();
                } else {
                    requestBody = getFormRequestBody();
                }
            }
        } else {
            String msg = JSON.toJSONString(requestData);
            LogUtil.v("params", msg);
            requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), msg);
        }
        if (requestBody == null) {
            requestBody = new FormBody.Builder().build();
        }

        return requestBody;
    }

    @NonNull
    private RequestBody getFormRequestBody() {
        RequestBody requestBody;
        Set<Entry<String, Object>> entries = requestData.entrySet();
        Iterator<Entry<String, Object>> iterator = entries.iterator();
        FormBody.Builder formEncodingBuilder = new FormBody.Builder();
        while (iterator.hasNext()) {
            Entry<String, Object> next = iterator.next();
            String value;
            if (next.getValue() instanceof List) {
                value = JSON.toJSONString(next.getValue(), true);
            } else {
                value = String.valueOf(next.getValue()).trim();
            }
            String key = next.getKey().trim();
            formEncodingBuilder.add(key, value);
            LogUtil.v("params", key + "-------------------------->" + value);
        }
        requestBody = formEncodingBuilder.build();
        return requestBody;
    }

    @NonNull
    private RequestBody getMultipartRequestBody() {
        RequestBody requestBody;
        Set<Entry<String, Object>> entries = requestData.entrySet();
        Iterator<Entry<String, Object>> iterator = entries.iterator();
        MultipartBody.Builder multipartBuilder = new MultipartBody.Builder();
        multipartBuilder.setType(MultipartBody.FORM);
        while (iterator.hasNext()) {
            Entry<String, Object> next = iterator.next();
            if (next.getValue() instanceof File) {
                File file = (File) next.getValue();
                multipartBuilder.addFormDataPart(next.getKey(), file.getName(),
                        RequestBody.create(MediaType.parse("file/*"), (File) next.getValue()));
            } else {
                String value = String.valueOf(next.getValue());
                String key = next.getKey();
                multipartBuilder.addFormDataPart(key, value);
                LogUtil.v("params", key + "-------------------------->" + value);
            }
        }
        requestBody = multipartBuilder.build();
        return requestBody;
    }

    private boolean isParamHasFile() {
        Set<Entry<String, Object>> entries = requestData.entrySet();
        Iterator<Entry<String, Object>> iterator = entries.iterator();
        boolean hasFile = false;
        while (iterator.hasNext()) {
            Entry<String, Object> next = iterator.next();
            if (next.getValue() instanceof File) {
                hasFile = true;
                break;
            }

        }
        return hasFile;
    }

    public static String constructGetParams(String url, Map<String, Object> hm) {
        if (hm == null) {
            return url;
        }
        Iterator iter = hm.entrySet().iterator();
        Builder builder = Uri.parse(url).buildUpon();
        while (iter.hasNext()) {
            Entry entry = (Entry) iter.next();
            String key = String.valueOf(entry.getKey());
            String value = String.valueOf(entry.getValue());
            builder.appendQueryParameter(key, value);
        }
        return builder.build().toString();
    }

    private void sendMessage(String result, int requestCode) {
        Message message = Message.obtain(handler, DID_SUCCEED, listener);
        Bundle data = new Bundle();
        data.putString("callbackkey", result);
        data.putString("requestString", this.data);
        message.setData(data);
        message.arg1 = GET;
        message.arg2 = requestCode;
        handler.sendMessage(message);
    }

    private void sendPostMessage(Object result, int requestCode) {
        Message message = Message.obtain(handler, DID_SUCCEED, result);
        message.arg1 = POST;
        message.arg2 = requestCode;
        handler.sendMessage(message);
    }

    public void disConnect() {
        try {
            for (Call call : httpClient.dispatcher().queuedCalls()) {
                if (call.request().tag().equals(this))
                    call.cancel();
            }
            for (Call call : httpClient.dispatcher().runningCalls()) {
                if (call.request().tag().equals(this))
                    call.cancel();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public OkHttpClient getHttpClient() {
        return httpClient;
    }

    public void setHttpClient(OkHttpClient httpClient) {
        this.httpClient = httpClient;
    }

}
