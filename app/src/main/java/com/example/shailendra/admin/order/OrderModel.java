package com.example.shailendra.admin.order;

import java.util.List;

public class OrderModel {

    private String orderId;
    private String orderByUserPhone;
    private String orderByUserId;

//
    // pay and bill
    private String orderBillAmount;
    private String orderDeliveryCharge;
    private String orderPayMode;

    // delivery details
    private String orderRecieverName;
    private String orderDeliveryAdd;
    private String orderDeliveryPin;
    private String orderDeliveryStatus;

    // Date and time..
    private String orderDateAndDay;
    private String orderTime;

    // No Of Product ... List
    private long noOfProducts;
    private List<OrderProductModel> orderProductModelList;

    public OrderModel(String orderId, String orderByUserPhone, String orderByUserId, String orderBillAmount, String orderDeliveryCharge, String orderPayMode, String orderRecieverName, String orderDeliveryAdd, String orderDeliveryPin, String orderDeliveryStatus, String orderDateAndDay, String orderTime, long noOfProducts, List <OrderProductModel> orderProductModelList) {
        this.orderId = orderId;
        this.orderByUserPhone = orderByUserPhone;
        this.orderByUserId = orderByUserId;
        this.orderBillAmount = orderBillAmount;
        this.orderDeliveryCharge = orderDeliveryCharge;
        this.orderPayMode = orderPayMode;
        this.orderRecieverName = orderRecieverName;
        this.orderDeliveryAdd = orderDeliveryAdd;
        this.orderDeliveryPin = orderDeliveryPin;
        this.orderDeliveryStatus = orderDeliveryStatus;
        this.orderDateAndDay = orderDateAndDay;
        this.orderTime = orderTime;
        this.noOfProducts = noOfProducts;
        this.orderProductModelList = orderProductModelList;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderByUserId() {
        return orderByUserId;
    }

    public void setOrderByUserId(String orderByUserId) {
        this.orderByUserId = orderByUserId;
    }

    public String getOrderByUserPhone() {
        return orderByUserPhone;
    }

    public void setOrderByUserPhone(String orderByUserPhone) {
        this.orderByUserPhone = orderByUserPhone;
    }

    public String getOrderBillAmount() {
        return orderBillAmount;
    }

    public void setOrderBillAmount(String orderBillAmount) {
        this.orderBillAmount = orderBillAmount;
    }

    public String getOrderDeliveryCharge() {
        return orderDeliveryCharge;
    }

    public void setOrderDeliveryCharge(String orderDeliveryCharge) {
        this.orderDeliveryCharge = orderDeliveryCharge;
    }

    public String getOrderPayMode() {
        return orderPayMode;
    }

    public void setOrderPayMode(String orderPayMode) {
        this.orderPayMode = orderPayMode;
    }

    public String getOrderRecieverName() {
        return orderRecieverName;
    }

    public void setOrderRecieverName(String orderRecieverName) {
        this.orderRecieverName = orderRecieverName;
    }

    public String getOrderDeliveryAdd() {
        return orderDeliveryAdd;
    }

    public void setOrderDeliveryAdd(String orderDeliveryAdd) {
        this.orderDeliveryAdd = orderDeliveryAdd;
    }

    public String getOrderDeliveryPin() {
        return orderDeliveryPin;
    }

    public void setOrderDeliveryPin(String orderDeliveryPin) {
        this.orderDeliveryPin = orderDeliveryPin;
    }

    public String getOrderDeliveryStatus() {
        return orderDeliveryStatus;
    }

    public void setOrderDeliveryStatus(String orderDeliveryStatus) {
        this.orderDeliveryStatus = orderDeliveryStatus;
    }

    public String getOrderDateAndDay() {
        return orderDateAndDay;
    }

    public void setOrderDateAndDay(String orderDateAndDay) {
        this.orderDateAndDay = orderDateAndDay;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public long getNoOfProducts() {
        return noOfProducts;
    }

    public void setNoOfProducts(long noOfProducts) {
        this.noOfProducts = noOfProducts;
    }

    public List <OrderProductModel> getOrderProductModelList() {
        return orderProductModelList;
    }

    public void setOrderProductModelList(List <OrderProductModel> orderProductModelList) {
        this.orderProductModelList = orderProductModelList;
    }
}
