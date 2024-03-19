package com.zyf.http.framework;

import java.util.HashMap;

public class HttpRepository {
    private static final HashMap<String, String> httpConfig = new HashMap();

    public static void setConfig(String k, String v) {
        httpConfig.put(k, v);
    }

    public static String getConfig(String k) {
        return httpConfig.get(k);
    }
}
