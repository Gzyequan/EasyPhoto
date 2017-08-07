package com.yequan.easyphoto;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.yequan.easyphoto.adapter.SplashViewPagerAdapter;
import com.yequan.easyphoto.utils.PreferencesUtil;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends BaseActivity {

    private ViewPager mSplash_vp;
    private List<View> mViewList;
    private SplashViewPagerAdapter pagerAdapter;
    private LinearLayout mDotContainer;
    private ImageView mCurrentDot;
    private Button mOpen;
    private ImageView mDot1, mDot2, mDot3;
    private int dotDistance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullScreen();
        initData();

        if (null != mViewList && 0 != mViewList.size()) {
            pagerAdapter = new SplashViewPagerAdapter(mViewList);
            mSplash_vp.setAdapter(pagerAdapter);
            mSplash_vp.setPageTransformer(true, new ViewPagerAnimation());
        }

        initDot();
        moveDot();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public void initView() {
        mSplash_vp = (ViewPager) findViewById(R.id.ep_splash_viewpager);
        mDotContainer = (LinearLayout) findViewById(R.id.ep_splash_ll);
        mCurrentDot = (ImageView) findViewById(R.id.ep_splash_current_iv);
        mOpen = (Button) findViewById(R.id.bt_open);
    }

    private void initData() {
        mViewList = new ArrayList<>();
        LayoutInflater layoutInflater = getLayoutInflater().from(this);
        View page1 = layoutInflater.inflate(R.layout.ep_indicator_page1, null);
        View page2 = layoutInflater.inflate(R.layout.ep_indicator_page2, null);
        View page3 = layoutInflater.inflate(R.layout.ep_indicator_page3, null);
        mViewList.add(page1);
        mViewList.add(page2);
        mViewList.add(page3);
    }

    private void initDot() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 40, 0);
        mDot1 = new ImageView(this);
        mDot1.setImageResource(R.drawable.ep_shape_indicator_light);
        mDotContainer.addView(mDot1, layoutParams);
        mDot2 = new ImageView(this);
        mDot2.setImageResource(R.drawable.ep_shape_indicator_light);
        mDotContainer.addView(mDot2, layoutParams);
        mDot3 = new ImageView(this);
        mDot3.setImageResource(R.drawable.ep_shape_indicator_light);
        mDotContainer.addView(mDot3, layoutParams);
        setClickListener();
    }

    @Override
    public void initEvent() {
        mOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PreferencesUtil.getPreferences().putBoolean(SplashActivity.this, "IS_FIRST_LAUNCH", false);
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.scale_action, R.anim.fade_action);
                finish();
            }
        });
    }

    private void moveDot() {
        mCurrentDot.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                dotDistance = mDotContainer.getChildAt(1).getLeft() - mDotContainer.getChildAt(0).getLeft();
                mCurrentDot.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
        mSplash_vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                float leftMargin = dotDistance * (position + positionOffset);
                RelativeLayout.LayoutParams layoutParams = ((RelativeLayout.LayoutParams) mCurrentDot.getLayoutParams());
                layoutParams.leftMargin = (int) leftMargin;
                mCurrentDot.setLayoutParams(layoutParams);
                if (position == 2) {
                    mOpen.setVisibility(View.VISIBLE);
                }
                if (position != 2 && mOpen.getVisibility() == View.VISIBLE) {
                    mOpen.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setClickListener() {
        mDot1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSplash_vp.setCurrentItem(0);
            }
        });
        mDot2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSplash_vp.setCurrentItem(1);
            }
        });
        mDot3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSplash_vp.setCurrentItem(2);
            }
        });
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }
}
