package com.yequan.easyphoto.opencv;

/**
 * Created by Administrator on 2017/8/9 0009.
 */

public class OpenCVProcess {
    static {
        System.loadLibrary("OpenCVProcess");
    }

    public static native int[] EP_I_Gray(int[] buf, int w, int h);
}
