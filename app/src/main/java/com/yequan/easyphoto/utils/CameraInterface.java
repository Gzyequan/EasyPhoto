package com.yequan.easyphoto.utils;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;
import java.util.List;

/**
 * Created by Administrator on 2017/6/6 0006.
 */

public class CameraInterface {
    private static String TAG = "ALVASystems";
    private static CameraInterface mCameraInterface;
    private Camera mCamera;
    private Camera.Parameters parameters;
    private boolean isPreviewing = false;
    private Context mContext;
    private MediaPlayer mMediaPlayer = null;
    private Camera.Size propPreviewSize;


    public interface CameraOpenListener {
        public void cameraOpening();

        public void cameraHasOpened();

        public void cameraOpenedError(Throwable throwable);
    }


    private CameraInterface(Context context) {
        this.mContext = context;
        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
        }
    }

    public static synchronized CameraInterface getInstance(Context context) {
        if (mCameraInterface == null) {
            mCameraInterface = new CameraInterface(context);
        }
        return mCameraInterface;
    }

    public CameraInterface doOpenCamera(int cameraId, CameraOpenListener cameraOpenListener) {
        if (mCamera == null) {
            if (cameraOpenListener != null) {
                cameraOpenListener.cameraOpening();
            }
            mCamera = Camera.open(cameraId);
            if (cameraOpenListener != null) {
                cameraOpenListener.cameraHasOpened();
                Log.i(TAG, "camera opened...");
            }
        } else {
            Log.i(TAG, "camera opened error...");
            if (cameraOpenListener != null) {
                cameraOpenListener.cameraOpenedError(new Throwable("camera has being occupied"));
            }
            doStopCamera();
        }
        return mCameraInterface;
    }

    public CameraInterface doStartPreview(SurfaceTexture surfaceTexture) {
        if (isPreviewing) {
            mCamera.stopPreview();
            return mCameraInterface;
        }
        if (mCamera != null) {
            try {
                mCamera.setPreviewTexture(surfaceTexture);
            } catch (IOException e) {
                Log.e(TAG, "doStartPreview:   " + e.getMessage());
            }
            initCamera();
        }
        return mCameraInterface;
    }

    public CameraInterface initCamera() {
        if (mCamera != null) {
            parameters = mCamera.getParameters();
            propPreviewSize = CameraParamUtils.getInstance(mContext).getPropPreviewSize(mCamera);
            previewSize = propPreviewSize;
            parameters.setPreviewSize(propPreviewSize.width, propPreviewSize.height);
            List<String> focusModes = parameters.getSupportedFocusModes();
            if (focusModes.contains("continuous-video")) {
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
            }
            mCamera.setParameters(parameters);
            mCamera.startPreview();
            isPreviewing = true;
            parameters = mCamera.getParameters();
        }
        return mCameraInterface;
    }

    public CameraInterface doStopCamera() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            isPreviewing = false;
            mCamera.release();
            mCamera = null;
        }
        return mCameraInterface;
    }

    public CameraInterface doGetPreviewData(PreviewCallback previewCallback) {
        if (null != previewCallback) {
            mCamera.setPreviewCallback(previewCallback);
        }
        return mCameraInterface;
    }

    private Camera.Size previewSize = null;

    public Camera.Size getPreviewSize() {
        return previewSize;
    }

    public boolean isPreviewing() {
        return isPreviewing;
    }

}
