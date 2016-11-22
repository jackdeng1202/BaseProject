package com.optimumnano.autocharge.view;

import com.lgm.baseframe.ui.IBaseView;
import com.optimumnano.autocharge.models.Order;

/**
 * 作者：刘广茂 on 2016/11/18 16:20
 * <p>
 * 邮箱：liuguangmao@optimumchina.com
 */
public interface IOrderView extends IBaseView {
      void onOrderCanceled(Order order);
      void onOrderStateChanged(Order order,int oldState,int newState);


}
