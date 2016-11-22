package com.optimumnano.autocharge.activity;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioGroup;

import com.lgm.baseframe.base.BaseActivity;
import com.optimumnano.autocharge.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

/**
 * 作者：刘广茂 on 2016/11/18 16:38
 * <p>
 * 邮箱：liuguangmao@optimumchina.com
 */
public class OrderManageActivity extends BaseActivity {

    @Bind(R.id.order_display_controller)
    RadioGroup orderDisplayController;
    private UndoneOrderFragment undoneOrderFragment;
    private DoneOrderFragment doneOrderFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_manage);
        final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        undoneOrderFragment = new UndoneOrderFragment();
        doneOrderFragment = new DoneOrderFragment();
        fragmentTransaction
                .add(R.id.child_container, undoneOrderFragment)
                .add(R.id.child_container, doneOrderFragment)
                .hide(doneOrderFragment)
                .commit();
//        orderDisplayController.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
//                switch (checkedId){
//                    case R.id.done:
//                        fragmentTransaction.hide(undoneOrderFragment).show(doneOrderFragment);
//                        break;
//                    case R.id.undone:
//                        fragmentTransaction.hide(doneOrderFragment).show(undoneOrderFragment);
//                        break;
//
//                }
//                fragmentTransaction.commit();
//            }
//        });
    }





}
