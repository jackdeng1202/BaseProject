package com.lgm.baseframe.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.lgm.baseframe.common.http.HttpCallbackListener;
import com.lgm.baseframe.common.http.HttpUtil;
import com.lgm.baseframe.common.http.RequestUtil;
import com.lgm.baseframe.ui.IBaseView;

import java.util.Map;

import butterknife.ButterKnife;


/**
 * Created by Administrator on 2015/12/28.
 */
public abstract class BaseFragment extends Fragment implements View.OnClickListener,IBaseView {

	private View rootView;



	public View getRootView() {
		return rootView;
	}


	public void doOnActivityCreated() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		if (getLayoutId() == 0) {
			return null;
		}
		if (rootView == null) {
			rootView = inflater.inflate(getLayoutId(), null, false);
		}
		ViewGroup parent = (ViewGroup) rootView.getParent();
		if (parent != null) {
			parent.removeView(rootView);
		}
		ButterKnife.bind(this, rootView);
		return rootView;
	}



	protected abstract int getLayoutId();

	protected void getViews() {
	}


	protected void setListeners() {
	}


	@SuppressWarnings("unchecked")
	protected <T extends View> T findViewById(int id) {
		return (T) getView().findViewById(id);
	}


	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getViews();
		setListeners();

	}



	public String getPageName() {
		return getClass().getName();
	}


	/**
	 * 得到根Fragment
	 *
	 * @return
	 */
	public Fragment getRootFragment() {
		Fragment fragment = getParentFragment();
		while (fragment.getParentFragment() != null) {
			fragment = fragment.getParentFragment();
		}
		return fragment;

	}

	public void showShortToast(String string) {
		Toast.makeText(getActivity(), string, Toast.LENGTH_SHORT).show();
	}


	@Override
	public void onClick(View v) {

	}

	public boolean onBackPressed() {
		return false;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		ButterKnife.unbind(this);
	}

	@Override
	public void showLoading() {

	}

	@Override
	public void hideLoading() {

	}

	@Override
	public void onError(int errorCode, String errorMsg) {

	}

}
