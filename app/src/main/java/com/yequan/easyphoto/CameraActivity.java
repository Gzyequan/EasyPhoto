package com.yequan.easyphoto;

import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import com.yequan.easyphoto.camera.CameraGLSurfaceView;
import com.yequan.easyphoto.utils.CameraInterface;
import com.yequan.easyphoto.utils.DisplayUtils;


public class CameraActivity extends BaseActivity implements View.OnClickListener {

    private CameraGLSurfaceView cameraGLSurfaceView;
    private ImageView cameraExit;
    private CameraInterface cameraInterface = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initParams();
        cameraInterface = CameraInterface.getInstance(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_camera;
    }

    @Override
    public void initView() {
        cameraGLSurfaceView = (CameraGLSurfaceView) findViewById(R.id.ep_camera_activity_glsurfaceView);
        cameraExit = (ImageView) findViewById(R.id.ep_camera_exit);
    }

    @Override
    public void initEvent() {
        cameraExit.setOnClickListener(this);
    }

    private void initParams() {
        LayoutParams layoutParams = cameraGLSurfaceView.getLayoutParams();
        Point screenMetrics = DisplayUtils.getScreenMetrics(this);
        layoutParams.width = screenMetrics.x;
        layoutParams.height = screenMetrics.y;
        cameraGLSurfaceView.setLayoutParams(layoutParams);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ep_camera_exit:
                finish();
                overridePendingTransition(R.anim.camera_activity_enter_action, R.anim.camera_activity_exit_action);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraGLSurfaceView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraGLSurfaceView.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.camera_activity_enter_action, R.anim.camera_activity_exit_action);
    }
}
