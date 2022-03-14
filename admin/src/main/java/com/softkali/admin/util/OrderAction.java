package com.softkali.admin.util;

import com.softkali.admin.dashboard.model.OrderModel;

import org.json.JSONObject;

public interface OrderAction {
    void cancel(OrderModel orderModel);
    void setPickup(OrderModel orderModel);
    void onDetail(OrderModel orderModel);
}
