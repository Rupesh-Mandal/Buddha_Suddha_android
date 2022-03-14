package com.softkali.buddhasuddha.utils;

import com.softkali.buddhasuddha.dashboard.model.OrderModel;

public interface OrdersOnClick {
    void onClick(OrderModel orderModel);
    void onCancel(OrderModel orderModel);
}
