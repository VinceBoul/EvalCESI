package com.vince.evalcesi.model;

/**
 * Created by Vince on 07/11/2016.
 */
public class HttpResult {

    public int code;
    public String json;

    public HttpResult(int code, String s) {
        this.code = code;
        this.json = s;
    }
}