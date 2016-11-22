package com.optimumnano.autocharge.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.lgm.baseframe.base.BaseActivity;
import com.lgm.baseframe.common.LogUtil;
import com.lgm.baseframe.common.Utils;
import com.optimumnano.autocharge.MainActivity;
import com.optimumnano.autocharge.R;
import com.optimumnano.autocharge.common.PhoneUtils;
import com.optimumnano.autocharge.common.StringUtils;
import com.optimumnano.autocharge.models.HttpResult;
import com.optimumnano.autocharge.presenter.LoginPresenter;
import com.optimumnano.autocharge.view.ILoginView;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 作者：刘广茂 on 2016/11/18 14:24
 * <p>
 * 邮箱：liuguangmao@optimumchina.com
 */
public class LoginActivity extends BaseActivity implements ILoginView {

    private static final String TAG = LoginActivity.class.getSimpleName();
    LoginPresenter loginPresenter;
    @Bind(R.id.et_act_login_phonenum)
    EditText mEtPhonenum;
    @Bind(R.id.bt_act_login_getcode)
    Button mBtGetcode;
    @Bind(R.id.et_act_login_code)
    EditText mEtVerCode;
    @Bind(R.id.bt_act_login_platetitle)
    Button mBtPlatetitle;
    @Bind(R.id.et_act_login_platenum)
    EditText mEtPlatenum;
    @Bind(R.id.bt_act_login_login)
    Button mBtLogin;
    @Bind(R.id.ll_act_login_platemodule)
    LinearLayout mLlPlatemodule;
    private PopupWindow mPw;
    private View mPopView;
    private View mRootview;
    private boolean isBindPlate = false;
    private TimerTask timerTask;
    private int mTimess;
    private Timer timer;
    private boolean isExit=false;
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        mLlPlatemodule.setVisibility(View.VISIBLE);
        mEtPlatenum.setText("粤ATT002");
        setTitle("用户登录");
        leftView.setVisibility(View.GONE);
        loginPresenter = new LoginPresenter(this);
        initPopwindow();
    }

    private void initPopwindow() {
        mRootview = LayoutInflater.from(LoginActivity.this).inflate(R.layout.activity_login, null);
        mPopView = View.inflate(this, R.layout.popview, null);
        mPw = new PopupWindow(mPopView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mPw.setContentView(mPopView);
        mPw.setOutsideTouchable(true);

        mPopView.findViewById(R.id.popview_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPw.dismiss();
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (mPw.isShowing() && mPw != null) {
            mPw.dismiss();
        }
        else {
            mPw = null;
            mPopView = null;
            mRootview = null;
            finish();
        }
    }

    @Override
    public String getPhoneNumber() {
        String phoneNum = mEtPhonenum.getText().toString();
        if (TextUtils.isEmpty(phoneNum)) {
            showShortToast("电话号码为空");
            return null;
        }
        else if (!PhoneUtils.isPhoneNumber(phoneNum)) {
            showShortToast("电话号码格式错误");
            return null;
        }
        else {
            return phoneNum;
        }
    }

    @Override
    public String getVerificationCode() {
        String verCode = mEtVerCode.getText().toString();
        if (TextUtils.isEmpty(verCode) || verCode.length() != 6 || !StringUtils.isDigit(verCode)) {
            showShortToast("请输入正确的验证码" + verCode);
            return null;
        }
        else {
            return verCode;
        }
    }

    @Override
    public String getPlateNumber() {
        String plateNum = mEtPlatenum.getText().toString();
        String regex = "^[\u4e00-\u9fa5]{1}[A-Z]{1}[A-Z_0-9]{5}$";
        if (TextUtils.isEmpty(plateNum)) {
            showShortToast("车牌号为空");
            return null;
        }
        else if (!PhoneUtils.isMatch(regex, plateNum)) {
            showShortToast("车牌号格式错误");
            return null;
        }
        else {
            return plateNum;
        }
    }

    @Override
    public void showPlateNumberView() {
        isBindPlate = false;
        mLlPlatemodule.setVisibility(View.VISIBLE);

    }

    @Override
    public boolean hidePlateNumberView() {
        isBindPlate = true;
        mLlPlatemodule.setVisibility(View.INVISIBLE);
        return isBindPlate;
    }

    @Override
    public boolean isBindPlate() {
        return isBindPlate;
    }

    @Override
    public void clearInputs() {

    }

    @Override
    public void onError(int errorCode, String errorMsg) {
        Utils.showShortToast(this, "服务器异常");
        LogUtil.i(TAG, "errorCode=" + errorCode + "errorMsg=" + errorMsg);
    }

    @Override
    public void getVerificationCodeSuccess(int resultCode) {
        startTimer();
        Utils.showShortToast(this, "获取验证码成功");
        LogUtil.i(TAG, "resultCode=" + resultCode);
    }

    @Override
    public void getVerificationCodeFailed(HttpResult resultMsg) {
        stopTimer();
        if (resultMsg.status == 10002) {
            Utils.showShortToast(this, "请求时间过短,2分钟后重试");
        }
        else {
            Utils.showShortToast(this, resultMsg.resultMsg);
        }

        LogUtil.i(TAG, "resultCode=" + resultMsg.status);
    }

    @Override
    public void loginSuccess(Object object) {
        Utils.showShortToast(this, "登录成功");
        LogUtil.i(TAG, "result=" + ((HttpResult) object).status);
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void loginFailed(HttpResult resultMsg) {
        Utils.showShortToast(this, resultMsg.resultMsg);
        LogUtil.i(TAG, "result=" + resultMsg.resultMsg);
    }

    @Override
    public void onUserBindError(int resultCode) {
        Utils.showShortToast(this, "用户未绑定车辆");
        LogUtil.i(TAG, "resultCode=" + resultCode);
    }

    @Override
    public void onConnectionFailed(Exception ex) {
        LogUtil.i("result", "onConnectionFailed");
        Utils.showShortToast(this, "网络链接异常");
        ex.printStackTrace();
    }

    @Override
    public void onRequestFailed(int resultCode) {
        Utils.showShortToast(this, "请求失败");
        LogUtil.i("result", "onRequestFailed" + resultCode);
    }

    @OnClick({R.id.bt_act_login_getcode, R.id.bt_act_login_platetitle, R.id.bt_act_login_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_act_login_getcode:
                if (getPhoneNumber() != null)
                    loginPresenter.getVerificationCode();
                break;
            case R.id.bt_act_login_platetitle:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    mPw.showAtLocation(mRootview, Gravity.BOTTOM, 0, 0);
                }

                break;
            case R.id.bt_act_login_login:
                //隐藏输入法
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mBtLogin.getWindowToken(), 0);

                    setProgressDialogTitle("正在登录...");
                //TODO:是否增加隐藏车牌功能
                if (isBindPlate) {
                    LogUtil.i("test", "" + isBindPlate);
                    if (getPhoneNumber() != null && getVerificationCode() != null) {
                        loginPresenter.login();
                    }
                } else {
                    if (getPhoneNumber() != null && getVerificationCode() != null && getPlateNumber() != null) {
                        loginPresenter.login();
                    }
                }

                break;

        }
    }


    private void startTimer() {
        mTimess = 120;
        mBtGetcode.setClickable(false);
        mBtGetcode.setText(mTimess + "s");
        if (timerTask == null) {
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mTimess--;
                            if (mTimess <= 0) {
                                stopTimer();
                                return;
                            }
                            if (mBtGetcode!=null)
                            mBtGetcode.setText(mTimess + "s");
                        }
                    });
                }
            };
        }
        if (timer == null) {
            timer = new Timer();
        }
        timer.schedule(timerTask, 1000, 1000);
    }

    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
        mBtGetcode.setText("重新获取");
        mBtGetcode.setClickable(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPw.isShowing() && mPw != null) {
            mPw.dismiss();
        }
        mPw = null;
        mPopView = null;
        mRootview = null;
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
        finish();
    }

    private void exit() {
        if (!isExit) {
            isExit = true;
            // 利用handler延迟发送更改状态信息
            Utils.showShortToast(this, "再按一次退出程序");
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            finish();
            System.exit(0);
        }
    }
}
