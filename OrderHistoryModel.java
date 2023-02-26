package com.example.wagbaapplication;

public class OrderHistoryModel {
    private String orderNumber;
    private String status;

    public String getOrderNumber() {
        return orderNumber;
    }
    public String getStatus() {
        return status;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}
