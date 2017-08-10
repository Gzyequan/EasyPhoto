package com.yequan.easyphoto.camera;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.GLES11Ext;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;

import com.yequan.easyphoto.utils.CameraInterface;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Administrator on 2017/6/5 0005.
 */

public class CameraGLSurfaceView extends GLSurfaceView implements GLSurfaceView.Renderer,
        SurfaceTexture.OnFrameAvailableListener, CameraInterface.CameraOpenListener {
    private static String TAG = "EasyPhoto";
    private Context mContext;
    private DirectDrawer mDirectDrawer;
    private int mTextureId = -1;
    private SurfaceTexture mSurface;

    public CameraGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        setEGLContextClientVersion(2);
        setRenderer(this);
        setRenderMode(RENDERMODE_CONTINUOUSLY);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES30.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        mTextureId = createTextureId();
        mSurface = new SurfaceTexture(mTextureId);
        mSurface.setOnFrameAvailableListener(this);
        mDirectDrawer = new DirectDrawer(mContext, mTextureId);
        CameraInterface.getInstance(mContext).doOpenCamera(0, this);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES30.glViewport(0, 0, width, height);
        if (!CameraInterface.getInstance(mContext).isPreviewing()) {
            CameraInterface.getInstance(mContext).doStartPreview(mSurface);
        }
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT | GLES30.GL_DEPTH_BUFFER_BIT);
        mSurface.updateTexImage();
        mDirectDrawer.drawSelf();
    }

    //创建纹理
    private int createTextureId() {
        int[] textureId = new int[1];

        GLES30.glGenTextures(1, textureId, 0);
        GLES30.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, textureId[0]);
        GLES30.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
        GLES30.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
        GLES30.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
        GLES30.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);

        return textureId[0];
    }

    @Override
    public void onResume() {
        super.onResume();
        CameraInterface.getInstance(mContext).doStartPreview(mSurface);
    }

    @Override
    public void onPause() {
        super.onPause();
        CameraInterface.getInstance(mContext).doStopCamera();
    }

    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        this.requestRender();
    }

    @Override
    public void cameraOpening() {
        Log.i(TAG, "camera opening...");
    }

    @Override
    public void cameraHasOpened() {
        Log.i(TAG, "camera has opened");
    }

    @Override
    public void cameraOpenedError(Throwable throwable) {
        Log.e(TAG, "camera has open in error");
    }
}
