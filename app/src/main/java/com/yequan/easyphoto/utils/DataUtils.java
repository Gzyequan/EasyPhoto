package com.yequan.easyphoto.utils;

/**
 * Created by Administrator on 2016/12/2 0002.
 */

public class DataUtils {
    private DataUtils() {
    }

    public static float intToFloat(int value) {
        return Float.parseFloat(Integer.toString(value));
    }

    public static int stringToInt(String value) {
        return Integer.parseInt(value);
    }
}
