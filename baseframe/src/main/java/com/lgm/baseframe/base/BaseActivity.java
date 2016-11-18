package com.lgm.baseframe.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lgm.baseframe.R;
import com.lgm.baseframe.common.Constant;
import com.lgm.baseframe.common.http.RequestUtil;
import com.lgm.baseframe.ui.IBaseView;

import java.util.List;

import butterknife.ButterKnife;


/**
 *
 */
public class BaseActivity extends AppCompatActivity implements OnClickListener,IBaseView{




	public static final int LOGIN_COMMAND = 1091;

	protected Context mContext;

	/**
	 * 容器
	 */
	private ViewGroup container;

	/**
	 * 标题View
	 */
	protected RelativeLayout headView;

	/**
	 * 标题textview
	 */
	protected TextView titleView;

	/**
	 * 标题左按钮
	 */
	public ImageView leftView;
	/**
	 * 标题右按钮
	 */
	protected ImageView rightView;

	/**
	 * 标题右按钮
	 */
	protected TextView rightBtnView;

	private ProgressDialog progressDialog;


	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		mContext = this;
		Constant.initUrls(this);
		super.setContentView(R.layout.activity_base);
		container = (ViewGroup) findViewById(R.id.container);
		headView = (RelativeLayout) findViewById(R.id.title_layout);
		titleView = (TextView) findViewById(R.id.txt_title);
		leftView = (ImageView) findViewById(R.id.img_left);
		rightView = (ImageView) findViewById(R.id.img_r);
		rightBtnView = (TextView) findViewById(R.id.txt_r);
		progressDialog = new ProgressDialog(mContext);
	}



	public void setTitle(CharSequence str) {
		titleView.setText(str);
	}

	public void setTitle(int str) {
		titleView.setText(str);
	}

	public void hideTitle() {
	headView.setVisibility(View.GONE);
	}

	public void showTitle() {
		headView.setVisibility(View.VISIBLE);
	}



	@Override
	public void setContentView(int layoutResID) {
		if (container != null) {
			container.addView(View.inflate(mContext, layoutResID, null));
		} else {
			super.setContentView(layoutResID);
		}
		ButterKnife.bind(this);
	}

	public CharSequence getPageName() {
		return titleView == null ? getClass().getName() : titleView.getText();
	}


	@Override
	public void setContentView(View view) {
		if (container != null) {
			container.addView(view);
		} else {
			super.setContentView(view);
		}
		ButterKnife.bind(this);
	}

	@Override
	public void setContentView(View view, LayoutParams params) {
		if (container != null) {
			container.addView(view, params);
		} else {
			super.setContentView(view);
		}
		ButterKnife.bind(this);
	}

	@Override
	protected void onDestroy() {
		System.gc();
		ButterKnife.unbind(this);
		super.onDestroy();
	}



	/**
	 * 显示短时间提示
	 *
	 * @param msg 提示消息
	 */
	public void showShortToast(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 显示短时间提示
	 *
	 * @param msgId 提示消息
	 */
	public void showShortToast(int msgId) {
		Toast.makeText(this, msgId, Toast.LENGTH_SHORT).show();
	}


	public void onClick(View v) {
		if (v.getId() == R.id.img_left) {
			finish();
		}
	}




	public void setRightCustomBtn(String text, OnClickListener l) {
		rightBtnView.setVisibility(View.VISIBLE);
		rightView.setVisibility(View.INVISIBLE);
		rightBtnView.setText(text);
		rightBtnView.setOnClickListener(l);
	}



	protected void hideInput() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
			imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
		}
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		FragmentManager fm = getSupportFragmentManager();
		int index = requestCode >> 16;
		if (index != 0) {
			index--;
			if (fm.getFragments() == null || index < 0
					|| index >= fm.getFragments().size()) {
//				Log.w(TAG, "Activity result fragment index out of range: 0x"
//						+ Integer.toHexString(requestCode));
				return;
			}
			Fragment frag = fm.getFragments().get(index);
			if (frag == null) {
//				Log.w(TAG, "Activity result no fragment exists for index: 0x"
//						+ Integer.toHexString(requestCode));
			} else {
				handleResult(frag, requestCode, resultCode, data);
			}
			return;
		}

	}


	/**
	 * 递归调用，对所有子Fragement生效
	 *
	 * @param frag
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 */
	private void handleResult(Fragment frag, int requestCode, int resultCode,
	                          Intent data) {
		frag.onActivityResult(requestCode & 0xffff, resultCode, data);
		List<Fragment> frags = frag.getChildFragmentManager().getFragments();
		if (frags != null) {
			for (Fragment f : frags) {
				if (f != null)
					handleResult(f, requestCode, resultCode, data);
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		//MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		//MobclickAgent.onPause(this);
	}


	@Override
	public void showLoading() {
		if(progressDialog!=null&&!progressDialog.isShowing()){
			progressDialog.show();
		}

	}

	@Override
	public void hideLoading() {
		if(progressDialog!=null&&progressDialog.isShowing()){
			progressDialog.dismiss();
		}
	}

	@Override
	public void onError(int errorCode, String errorMsg) {

	}
}
