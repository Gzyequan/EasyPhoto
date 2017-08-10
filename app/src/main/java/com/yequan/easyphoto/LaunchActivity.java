package com.yequan.easyphoto;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.yequan.easyphoto.utils.PreferencesUtil;

public class LaunchActivity extends BaseActivity {

    private TextView mTimer;
    private int count = 2;
    private boolean is_first_launch;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        is_first_launch = PreferencesUtil.getPreferences().getBoolean(this, "IS_FIRST_LAUNCH", true);
        handler.sendEmptyMessageDelayed(0, 1000);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_launch;
    }

    @Override
    public void initView() {
        mTimer = (TextView) findViewById(R.id.ep_launcher_timer);
        mTimer.setText(count + "s");
    }

    private int getCount() {
        count--;
        if (count == 0) {
            if (is_first_launch) {
                intent = new Intent(LaunchActivity.this, SplashActivity.class);
            } else {
                intent = new Intent(LaunchActivity.this, MainActivity.class);
            }
            startActivity(intent);
            overridePendingTransition(R.anim.fade_action, R.anim.hold_action);
            finish();
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
