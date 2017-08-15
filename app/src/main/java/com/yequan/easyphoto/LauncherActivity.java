package com.yequan.easyphoto;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.yequan.easyphoto.utils.PreferencesUtil;

public class LauncherActivity extends BaseActivity {

    private ImageView launcherPhoto;
    private TextView mTimer;
    private int count = 3;
    private boolean is_first_launch;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        is_first_launch = PreferencesUtil.getPreferences().getBoolean(this, "IS_FIRST_LAUNCH", true);
        launcherPhotoAnimation();
        handler.sendEmptyMessageDelayed(0, 1000);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_launch;
    }

    @Override
    public void initView() {
        launcherPhoto = (ImageView) findViewById(R.id.ep_launcher_background);
        mTimer = (TextView) findViewById(R.id.ep_launcher_timer);
        mTimer.setText(count + "s");
    }

    private void launcherPhotoAnimation() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.launcher_photo_animation);
        launcherPhoto.setAnimation(animation);
        animation.start();
    }

    private int getCount() {
        count--;
        if (count == 0) {
            if (is_first_launch) {
                intent = new Intent(LauncherActivity.this, SplashActivity.class);
            } else {
                intent = new Intent(LauncherActivity.this, MainActivity.class);
            }
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.hold_action, R.anim.fade_action);
        }
        return count;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                mTimer.setText(getCount() + "s");
                handler.sendEmptyMessageDelayed(0, 1000);
            }
        }
    };

    @Override
    public void onBackPressed() {

    }

}
