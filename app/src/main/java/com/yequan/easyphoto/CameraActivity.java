package com.yequan.easyphoto;

import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.yequan.easyphoto.camera.CameraGLSurfaceView;
import com.yequan.easyphoto.utils.CameraInterface;
import com.yequan.easyphoto.utils.DisplayUtils;
import com.yequan.easyphoto.utils.FileUtils;


public class CameraActivity extends BaseActivity implements View.OnClickListener {

    private CameraGLSurfaceView cameraGLSurfaceView;
    private ImageView cameraExit, cameraSwitch, cameraSetting, photoAbandon, photoSelected;
    private Button cameraTakePhoto;
    private RelativeLayout photoChoose;
    private CameraInterface cameraInterface = null;
    private byte[] photoData = null;

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
        cameraSwitch = (ImageView) findViewById(R.id.ep_camera_switch);
        cameraSetting = (ImageView) findViewById(R.id.ep_camera_setting);
        cameraTakePhoto = (Button) findViewById(R.id.ep_camera_take_photo);
        photoChoose = (RelativeLayout) findViewById(R.id.ep_camera_photo_choose);
        photoAbandon = (ImageView) findViewById(R.id.ep_camera_photo_abandon);
        photoSelected = (ImageView) findViewById(R.id.ep_camera_photo_selected);
    }

    @Override
    public void initEvent() {
        cameraExit.setOnClickListener(this);
        cameraSwitch.setOnClickListener(this);
        cameraSetting.setOnClickListener(this);
        cameraTakePhoto.setOnClickListener(this);
        photoAbandon.setOnClickListener(this);
        photoSelected.setOnClickListener(this);
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
            case R.id.ep_camera_switch:

                break;
            case R.id.ep_camera_setting:

                break;
            case R.id.ep_camera_take_photo:
                cameraInterface.doTakePicture(new CameraInterface.PhotoDataTransferListener() {
                    @Override
                    public void onSend(byte[] data) {
                        photoData = data;
                    }
                });
                viewGone();
                photoChoose.setVisibility(View.VISIBLE);
                break;
            case R.id.ep_camera_photo_abandon:
                photoData = null;
                cameraInterface.restartPreview();
                photoChoose.setVisibility(View.GONE);
                viewRecover();
                break;
            case R.id.ep_camera_photo_selected:
                if (null != photoData) {
                    FileUtils.savePhoto(photoData, new FileUtils.SavePhotoListener() {
                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onSuccess(byte[] data, String photoName) {
                            Toast.makeText(CameraActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                            photoData = null;
                        }

                        @Override
                        public void onError() {
                            Toast.makeText(CameraActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
                            photoData = null;
                        }
                    });
                    cameraInterface.restartPreview();
                    photoChoose.setVisibility(View.GONE);
                    viewRecover();
                }
                break;
        }
    }

    private void viewGone() {
        Animation animationGone = AnimationUtils.loadAnimation(this, R.anim.camera_view_control_gone_animation);
        cameraTakePhoto.setAnimation(animationGone);
        cameraExit.setAnimation(animationGone);
        cameraSwitch.setAnimation(animationGone);
        cameraSetting.setAnimation(animationGone);
        animationGone.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cameraTakePhoto.setVisibility(View.GONE);
                cameraExit.setVisibility(View.GONE);
                cameraSwitch.setVisibility(View.GONE);
                cameraSetting.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        animationGone.start();
    }

    private void viewRecover() {
        Animation animationRecover = AnimationUtils.loadAnimation(this, R.anim.camera_view_control_recover_animation);
        cameraTakePhoto.setAnimation(animationRecover);
        cameraExit.setAnimation(animationRecover);
        cameraSwitch.setAnimation(animationRecover);
        cameraSetting.setAnimation(animationRecover);
        animationRecover.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cameraTakePhoto.setVisibility(View.VISIBLE);
                cameraExit.setVisibility(View.VISIBLE);
                cameraSwitch.setVisibility(View.VISIBLE);
                cameraSetting.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        animationRecover.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraGLSurfaceView.onResume();
        photoChoose.setVisibility(View.GONE);
        viewRecover();
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraGLSurfaceView.onPause();
        photoData = null;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.camera_activity_enter_action, R.anim.camera_activity_exit_action);
    }
}
