package com.yequan.easyphoto.utils;

import android.content.Context;
import android.graphics.Point;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class CameraParamUtils {
    private static final String TAG = "ALVASystems";
    private static CameraParamUtils myCamPara = null;
    private Context mContext;

    private CameraParamUtils(Context context) {
        this.mContext = context;
    }

    public static CameraParamUtils getInstance(Context context) {
        if (myCamPara == null) {
            myCamPara = new CameraParamUtils(context);
            return myCamPara;
        } else {
            return myCamPara;
        }
    }

    public Size getPropPreviewSize(Camera camera) {
        int diff = Integer.MAX_VALUE;
        int bestX = 0;
        int bestY = 0;
        float tempRate;
        float screenRate = DisplayUtils.getScreenRate(mContext);
        List<String> previewSizes = getAllPreviewSizes(camera);
        if (previewSizes == null) {
            Point screenMetrics = DisplayUtils.getScreenMetrics(mContext);
            return camera.new Size(screenMetrics.x, screenMetrics.y);
        }
        for (String previewSize : previewSizes) {
            previewSize = previewSize.trim();
            int dimPosition = previewSize.indexOf("x");
            if (dimPosition == -1) {
                Log.e(TAG, "previewSizeException  : Bad pictureSizeString:" + previewSize);
                continue;
            }
            int newX = 0;
            int newY = 0;
            newX = DataUtils.stringToInt(previewSize.substring(0, dimPosition));
            newY = DataUtils.stringToInt(previewSize.substring(dimPosition + 1));
            float floatX = DataUtils.intToFloat(newX);
            float floatY = DataUtils.intToFloat(newY);
            tempRate = floatX / floatY;
            Point screenResolution = DisplayUtils.getScreenMetrics(mContext);
            int newDiff = Math.abs(newX - screenResolution.x) + Math.abs(newY - screenResolution.y);
            if (newDiff == diff) {
                bestX = newX;
                bestY = newY;
            } else if (newDiff < diff) {
                if (Math.abs(tempRate - screenRate) <= 0.1f) {
                    bestX = newX;
                    bestY = newY;
                    diff = newDiff;
                }
            }
        }
        if (bestX > 0 && bestY > 0) {
            return camera.new Size(bestX, bestY);
        }
        return null;
    }

    public List<String> getAllPreviewSizes(Camera camera) {
        List<String> allPreviewSize = new ArrayList<String>();
        Camera.Parameters parameters = camera.getParameters();
        String previewSizeString = parameters.get("preview-size-values");
        if (previewSizeString == null) {
            previewSizeString = parameters.get("preview-size-value");
        }
        if (previewSizeString == null) {
            return null;
        }
        String[] previewSizesString = previewSizeString.split(",");
        for (String previewSize : previewSizesString) {
            allPreviewSize.add(previewSize);
        }
        return allPreviewSize;
    }

}
