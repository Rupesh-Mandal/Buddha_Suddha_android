package com.softkali.admin.util;

public class Constant {
    public static final String BaseUrl="http://192.168.1.67:8080";

    public static final String AdminSignin=BaseUrl+"/admin/api/v1/sign_in";
    public static final String getAllPickupBoy=BaseUrl+"/admin/api/v1/getAllPickupBoy";
    public static final String sign_up_pickup_boy=BaseUrl+"/admin/api/v1/sign_up_pickup_boy";


    public static final String get_all_product=BaseUrl+"/admin/api/v1/get_all_product";
    public static final String delet_product=BaseUrl+"/admin/api/v1/delet_product";
    public static final String add_product=BaseUrl+"/admin/api/v1/add_product";
    public static final String getAllCategory=BaseUrl+"/admin/api/v1/getAllCategory";
    public static final String update_product=BaseUrl+"/admin/api/v1/update_product";

    public static final String findByLocation=BaseUrl+"/api/v1/order/findByLocation";
    public static final String cancelOrderBySeller=BaseUrl+"/api/v1/order/cancelOrderBySeller";
    public static final String setPickup=BaseUrl+"/api/v1/order/setPickup";

}
