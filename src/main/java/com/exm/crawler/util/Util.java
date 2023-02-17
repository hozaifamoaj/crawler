package com.exm.crawler.util;

import java.net.URL;

public class Util {

    public static boolean validateUrl(String url) {
        try {
            new URL(url).toURI();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isCustomValidatorPass(String path) {
        if (path.equals("/") || path.equals("/#") || path.isEmpty()
                || path.equals("#")
                || path.endsWith("#")
                || path.startsWith("#")
                || path.startsWith("mail")
                || path.contains("login")
        ) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean isOk(String str) {
        return !(str == null || str.trim().isEmpty());
    }
}
