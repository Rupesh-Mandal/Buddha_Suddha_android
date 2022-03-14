package com.softkali.buddhasuddha.utils;

public class Constant {
    public static final String BaseUrl="http://192.168.1.67:8080";

    public static final String sign_in_user=BaseUrl+"/user/api/v1/auth/sign_in_user";
    public static final String sign_up_user=BaseUrl+"/user/api/v1/auth/sign_up_user";

    public static final String getAllLocation=BaseUrl+"/admin/api/v1/getAllLocation";

    public static final String get_all_product=BaseUrl+"/api/v1/product/get_all_product";
    public static final String getAllCategory=BaseUrl+"/api/v1/product/getAllCategory";
    public static final String findByProductCategory=BaseUrl+"/api/v1/product/findByProductCategory";
    public static final String loadSearch=BaseUrl+"/api/v1/product/loadSearch";

    public static final String addAddress=BaseUrl+"/api/v1/addressBook/addAddress";
    public static final String deleltAddress=BaseUrl+"/api/v1/addressBook/deleltAddress";
    public static final String getAddressByUserId=BaseUrl+"/api/v1/addressBook/getAddressByUserId";
    public static final String addOrder=BaseUrl+"/api/v1/order/addOrder";
    public static final String findOrderByUserId=BaseUrl+"/api/v1/order/findByUserId";


}
