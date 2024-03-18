package com.zyf.http.framework;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum Method {
    POST("POST"),
    GET("GET"),
    DELETE("DELETE"),
    NULL_DATA("NULL");

    public static Method getMethod(String method) {
        for (Method value : Method.values()) {
            if (value.getMethod().equals(method)) {
                return value;
            }
        }
        log.info("not match method:{}", method);
        return NULL_DATA;
    }

    Method(String method) {
        this.method = method;
    }

    final String method;

    public String getMethod() {
        return method;
    }
}
