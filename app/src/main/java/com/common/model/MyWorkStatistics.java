package com.common.model;

public class MyWorkStatistics {
    private String doorNum;
    /**出库数/派件数*/
    private String out;

    private String outdiver;

    private String outexpress;

    private String storesNum;

    /**揽收数*/
    private String total;

    public void setDoorNum(String doorNum) {
        this.doorNum = doorNum;
    }

    public String getDoorNum() {
        return this.doorNum;
    }

    public void setOut(String out) {
        this.out = out;
    }

    public String getOut() {
        return this.out;
    }

    public void setOutdiver(String outdiver) {
        this.outdiver = outdiver;
    }

    public String getOutdiver() {
        return this.outdiver;
    }

    public void setOutexpress(String outexpress) {
        this.outexpress = outexpress;
    }

    public String getOutexpress() {
        return this.outexpress;
    }

    public void setStoresNum(String storesNum) {
        this.storesNum = storesNum;
    }

    public String getStoresNum() {
        return this.storesNum;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getTotal() {
        return this.total;
    }

}