package com.common.events;

import java.util.List;

/**
 * Created by zhangruntao on 15/5/19.
 */
public class ZxingEvent {

    private String result; //单个扫描结果

    private List<String> results;//多个扫描结果集

    public ZxingEvent(List<String> results) {
        this.results = results;
    }

    public ZxingEvent(String result) {
        this.result = result;
    }

    public List<String> getResults() {
        return results;
    }

    public void setResults(List<String> results) {
        this.results = results;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }
}
