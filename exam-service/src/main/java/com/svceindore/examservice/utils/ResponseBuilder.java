package com.svceindore.examservice.utils;

import net.minidev.json.JSONObject;

/**
 * Date: 20/03/21
 * Time: 2:23 AM
 **/
public class ResponseBuilder {
    private JSONObject jsonObject = new JSONObject();

    public ResponseBuilder(boolean status, String message) {
        jsonObject.appendField("status", status);
        jsonObject.appendField("message", message);
    }

    public static ResponseBuilder ResponseBuilder(boolean status, String message) {
        return new ResponseBuilder(status, message);
    }

    public void appendField(String key, Object value) {
        jsonObject.appendField(key, value);
    }

    @Override
    public String toString() {
        return jsonObject.toJSONString();
    }
}
