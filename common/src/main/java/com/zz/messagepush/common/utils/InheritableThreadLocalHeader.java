package com.zz.messagepush.common.utils;


import java.util.HashMap;

public class InheritableThreadLocalHeader {

    private InheritableThreadLocalHeader() {
    }


    private static final InheritableThreadLocal<HashMap<String, String>> HEADER = new InheritableThreadLocal<>();

    public static void clear() {
        HEADER.remove();
    }


    public static void set(HashMap<String, String> headers) {
        HEADER.set(headers);
    }


    public static HashMap<String, String> get() {
        return HEADER.get();
    }

}
