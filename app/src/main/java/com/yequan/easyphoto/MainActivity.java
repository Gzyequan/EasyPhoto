package com.yequan.easyphoto;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.ImageView;

public class MainActivity extends BaseActivity {

    private ImageView backgroundImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AnimationDrawable animation = new AnimationDrawable();
        animation.addFrame(getResources().getDrawable(R.drawable.girl_1), 6000);
        animation.addFrame(getResources().getDrawable(R.drawable.girl_2), 6000);
        animation.addFrame(getResources().getDrawable(R.drawable.girl_4), 6000);
        animation.setEnterFadeDuration(1000);
        animation.setExitFadeDuration(1000);
        animation.setOneShot(false);
        animation.start();
        backgroundImage.setImageDrawable(animation);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        backgroundImage = (ImageView) findViewById(R.id.ep_mainactivity_bg);
    }

    private long[] pressArray = new long[2];
    private int count = 0;

    @Override
    public void onBackPressed() {
        if (count == 0) {
            long first = System.currentTimeMillis();
            pressArray[0] = first;
            count = count + 1;
            showToast(this, "再按一次退出程序");
        } else {
            count = 0;
            long second = System.currentTimeMillis();
            pressArray[1] = second;
            if (pressArray[1] - pressArray[0] < 1000) {
                MainActivity.this.finish();
            }
        }

    }
}
