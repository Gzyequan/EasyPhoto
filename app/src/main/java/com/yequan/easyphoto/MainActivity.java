package com.yequan.easyphoto;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.yequan.easyphoto.utils.permission.CheckPermission;
import com.yequan.easyphoto.utils.permission.PermissionListener;
import com.yequan.easyphoto.utils.permission.PermissionSettingHelp;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends BaseActivity implements View.OnClickListener, GestureDetector.OnGestureListener {

    private ImageView backgroundImage, mainSetting;
    private RelativeLayout beautify, jigsaw, matting, simpleCanvas, blank, wallpaper, gifMaker;
    private LinearLayout openCamera;
    private View animationCover;
    private int[] backgroundIds = {R.drawable.ep_background_1, R.drawable.ep_background_2, R.drawable.ep_background_4};
    private AnimationDrawable animation;
    private int[] itemBackground = {
            R.drawable.bg_item_background_blue, R.drawable.bg_item_background_bright,
            R.drawable.bg_item_background_grass, R.drawable.bg_item_background_gray,
            R.drawable.bg_item_background_lightblue, R.drawable.bg_item_background_orange,
            R.drawable.bg_item_background_purple, R.drawable.bg_item_background_yellow
    };
    private GestureDetector detector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBackgroundAnimation();
        detector = new GestureDetector(this, this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        backgroundImage = (ImageView) findViewById(R.id.ep_mainactivity_bg);
        mainSetting = (ImageView) findViewById(R.id.ep_main_setting);
        beautify = (RelativeLayout) findViewById(R.id.ep_rl_beautify);
        jigsaw = (RelativeLayout) findViewById(R.id.ep_rl_jigsaw);
        matting = (RelativeLayout) findViewById(R.id.ep_rl_matting);
        simpleCanvas = (RelativeLayout) findViewById(R.id.ep_rl_canvas);
        blank = (RelativeLayout) findViewById(R.id.ep_rl_blank);
        wallpaper = (RelativeLayout) findViewById(R.id.ep_rl_wallpaper);
        gifMaker = (RelativeLayout) findViewById(R.id.ep_rl_gif);
        openCamera = (LinearLayout) findViewById(R.id.ep_main_item_ll_openCamera);
        animationCover = findViewById(R.id.ep_camera_enter_animation_cover);
    }

    @Override
    public void initEvent() {
        mainSetting.setOnClickListener(this);
        openCamera.setOnClickListener(this);
    }

    private PermissionListener permissionListener;
    private String[] permissions = {
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ep_main_item_ll_openCamera:
                permissionListener =
                        new CheckPermission(this).requestRuntimePermission(permissions, this, new PermissionListener() {
                            @Override
                            public void onGranted() {
                                intentToCameraActivity();
                            }

                            @Override
                            public void onDenied(List<String> deniedPermission) {
                                PermissionSettingHelp.showHelpDialog(MainActivity.this);
                            }
                        });
                break;
            case R.id.ep_main_setting:
                Toast.makeText(this, "设置", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void intentToCameraActivity() {
        Animation openCameraAnimation = AnimationUtils.loadAnimation(this, R.anim.button_open_camera_animation);
        openCamera.startAnimation(openCameraAnimation);
        final Intent intentCamera = new Intent(this, CameraActivity.class);
        openCameraAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                startActivity(intentCamera);
                overridePendingTransition(R.anim.camera_activity_enter_action, R.anim.camera_activity_exit_action);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void setItemRandomColor() {
        Random random = new Random();
        int randomKey = itemBackground.length;
        beautify.setBackgroundResource(itemBackground[random.nextInt(randomKey)]);
        jigsaw.setBackgroundResource(itemBackground[random.nextInt(randomKey)]);
        matting.setBackgroundResource(itemBackground[random.nextInt(randomKey)]);
        simpleCanvas.setBackgroundResource(itemBackground[random.nextInt(randomKey)]);
        blank.setBackgroundResource(itemBackground[random.nextInt(randomKey)]);
        wallpaper.setBackgroundResource(itemBackground[random.nextInt(randomKey)]);
        gifMaker.setBackgroundResource(itemBackground[random.nextInt(randomKey)]);
    }

    private void initBackgroundAnimation() {
        animation = new AnimationDrawable();
        animation.addFrame(getResources().getDrawable(backgroundIds[0]), 10000);
        animation.addFrame(getResources().getDrawable(backgroundIds[1]), 10000);
        animation.addFrame(getResources().getDrawable(backgroundIds[2]), 10000);
        animation.setEnterFadeDuration(2000);
        animation.setExitFadeDuration(2000);
        animation.setOneShot(false);
        backgroundImage.setImageDrawable(animation);
    }

    private void startBackgroundAnimation() {
        if (null != animation && !animation.isRunning()) {
            animation.start();
        }
    }

    private void stopBackgroundAnimation() {
        if (null != animation && animation.isRunning()) {
            animation.stop();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        startBackgroundAnimation();
        setItemRandomColor();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopBackgroundAnimation();
    }

    private long firstTime = 0;

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                long secondTime = System.currentTimeMillis();
                if (secondTime - firstTime > 2000) {
                    Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    firstTime = secondTime;
                    return true;
                } else {
                    System.exit(0);
                }
                break;
        }
        return super.onKeyUp(keyCode, event);
    }

    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {//activity自带方法
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    List<String> deniedPermissions = new ArrayList<>();
                    for (int i = 0; i < grantResults.length; i++) {
                        int grantResult = grantResults[i];
                        String permission = permissions[i];
                        if (grantResult != PackageManager.PERMISSION_GRANTED) {
                            deniedPermissions.add(permission);
                        }
                    }
                    if (deniedPermissions.isEmpty()) {
                        permissionListener.onGranted();
                    } else {
                        permissionListener.onDenied(deniedPermissions);
                    }
                }
                break;
            default:
                break;
        }
    }

    //将该activity上的触碰事件交给GestureDetector处理
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                animationCover.setAlpha(0);
                animationCover.setVisibility(View.GONE);
                break;
        }
        return detector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        float alpha = 0;
        float scrollDistance = Math.abs(e2.getY() - e1.getY());
        alpha = scrollDistance >= minMove ? 1 : (scrollDistance / minMove);
        animationCover.setVisibility(View.VISIBLE);
        animationCover.setAlpha(alpha);
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
    }

    //最小滑动距离
    float minMove = 400;

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        float minVelocity = 0;     //最小滑动速度
        float scrollDistance = Math.abs(e2.getY() - e1.getY());
        if (scrollDistance > minMove && Math.abs(velocityY) > minVelocity) {
            Intent intentCamera = new Intent(this, CameraActivity.class);
            startActivity(intentCamera);
            overridePendingTransition(R.anim.hold_action, R.anim.fade_action);
        }
        return false;
    }
}
