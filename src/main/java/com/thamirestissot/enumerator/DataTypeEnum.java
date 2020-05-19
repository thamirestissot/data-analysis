package com.thamirestissot.enumerator;

public enum DataTypeEnum {

    SALESMAN(1), CUSTOMER(2), SALE(3);

    private int code;

    DataTypeEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }
}