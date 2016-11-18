package com.lgm.baseframe.common.http;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lgm.baseframe.common.Utils;
import com.lgm.baseframe.ui.IBaseView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RequestUtil {

    private static Map<Object, List<RequestCreator>> connectedHttp = new HashMap<>();

    private static RequestUtil requestUtil;

    private RequestUtil() {

    }

    public static RequestUtil getInstance() {
        if (requestUtil == null) {
            requestUtil = new RequestUtil();
        }
        return requestUtil;
    }


    public void cancel(Object tag) {
        List<RequestCreator> requestCreators = connectedHttp.get(tag);
        if (requestCreators == null) {
            return;
        }
        for (RequestCreator requestCreator : requestCreators) {
            requestCreator.cancel();
        }
        connectedHttp.remove(tag);
    }

    public void disConnectAll() {
        Set<Map.Entry<Object, List<RequestCreator>>> entries = connectedHttp.entrySet();
        Iterator<Map.Entry<Object, List<RequestCreator>>> iterator = entries.iterator();
        while (iterator.hasNext()) {
            Map.Entry<Object, List<RequestCreator>> next = iterator.next();
            cancel(next.getKey());
            entries.remove(next);
        }
    }

    public void tag(Object tag, RequestCreator creator) {
        if (connectedHttp.containsKey(tag)) {
            connectedHttp.get(tag).add(creator);
        } else {
            List<RequestCreator> creators = new ArrayList<>();
            creators.add(creator);
            connectedHttp.put(tag, creators);
        }
    }


    public static RequestCreator url(String url) {
        if (url == null || url.equals("")) {
            throw new IllegalArgumentException("url can not be null");
        }
        RequestCreator requestCreator = new RequestCreator(url);
        return requestCreator;
    }




}
