package com.yequan.easyphoto;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private ImageView backgroundImage;
    private RelativeLayout beautify, jigsaw, matting, simpleCanvas, blank, wallpaper, gifMaker;
    private LinearLayout openCamera;
    private int[] backgroundIds = {R.drawable.girl_1, R.drawable.girl_2, R.drawable.girl_4};
    private AnimationDrawable animation;
    private int[] itemBackground = {
            R.drawable.bg_item_background_blue, R.drawable.bg_item_background_bright,
            R.drawable.bg_item_background_grass, R.drawable.bg_item_background_gray,
            R.drawable.bg_item_background_lightblue, R.drawable.bg_item_background_orange,
            R.drawable.bg_item_background_purple, R.drawable.bg_item_background_yellow
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBackgroundAnimation();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        backgroundImage = (ImageView) findViewById(R.id.ep_mainactivity_bg);
        beautify = (RelativeLayout) findViewById(R.id.ep_rl_beautify);
        jigsaw = (RelativeLayout) findViewById(R.id.ep_rl_jigsaw);
        matting = (RelativeLayout) findViewById(R.id.ep_rl_matting);
        simpleCanvas = (RelativeLayout) findViewById(R.id.ep_rl_canvas);
        blank = (RelativeLayout) findViewById(R.id.ep_rl_blank);
        wallpaper = (RelativeLayout) findViewById(R.id.ep_rl_wallpaper);
        gifMaker = (RelativeLayout) findViewById(R.id.ep_rl_gif);
        openCamera = (LinearLayout) findViewById(R.id.ep_main_item_ll_openCamera);
    }

    @Override
    public void initEvent() {
        openCamera.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ep_main_item_ll_openCamera:
                Animation openCameraAnimation = AnimationUtils.loadAnimation(this, R.anim.button_open_camera_animation);
                openCamera.startAnimation(openCameraAnimation);
//                Transition explode = TransitionInflater.from(this).inflateTransition(R.transition.explode);
                final Intent intentCamera = new Intent(this, CameraActivity.class);
                openCameraAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        startActivity(intentCamera);
                        overridePendingTransition(R.anim.scale_camera_activity_action, R.anim.hold_action);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                break;
        }
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

}
