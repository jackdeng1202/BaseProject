package com.optimumnano.autocharge.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.lgm.baseframe.base.BaseActivity;
import com.lgm.baseframe.base.BaseFragment;
import com.optimumnano.autocharge.R;

import butterknife.Bind;

/**
 * 作者：刘广茂 on 2016/11/19 12:01
 * <p>
 * 邮箱：liuguangmao@optimumchina.com
 */
public class DoneOrderFragment extends BaseFragment {
    @Bind(R.id.order_list)
    RecyclerView orderList;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_order_manage;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        orderList.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        orderList.setAdapter(new OrderAdapter((BaseActivity) getActivity()));
    }
}
