package com.shop.bookshop.util.constant;

public enum StatusEnum {
    PENDING, SHIPPING, COMPLETE;

    public String getDisplayName() {
        switch (this) {
            case PENDING:
                return "Đang xử lý";
            case SHIPPING:
                return "Đang giao";
            case COMPLETE:
                return "Đã hoàn thành";
            default:
                return "";
        }
    }

    public String getColor() {
        switch (this) {
            case PENDING:
                return "badge badge-warning";
            case SHIPPING:
                return "badge badge-info";
            case COMPLETE:
                return "badge badge-success";
            default:
                return "";
        }
    }

}