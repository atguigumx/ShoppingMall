package com.atguigu.shop.controller.community.fragment;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;

import com.atguigu.shop.R;
import com.atguigu.shop.base.BaseFragment;
import com.atguigu.shop.controller.community.adapter.CommunityViewPagerAdapter;
import com.atguigu.shop.controller.community.view.StickyLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者：尚硅谷-杨光福 on 2016/12/20 10:22
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：发现Fragment
 */
public class CommunityFragment extends BaseFragment {

    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.tablayout)
    TabLayout tablayout;
    @BindView(R.id.sl_community)
    StickyLayout slCommunity;

    private CommunityViewPagerAdapter adapter;
    private ArrayList<BaseFragment> fragments;

    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.fragment_community, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void initData() {
        super.initData();

        initFragment();

        adapter = new CommunityViewPagerAdapter(getFragmentManager(),fragments);
        viewPager.setAdapter(adapter);

        //在设置适配器之后
        tablayout.setupWithViewPager(viewPager);
        tablayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        initListener();
    }

    private void initListener() {
        slCommunity.setOnGiveUpTouchEventListener(new StickyLayout.OnGiveUpTouchEventListener() {
            @Override
            public boolean giveUpTouchEvent(MotionEvent event) {
                return false;
            }
        });
    }

    private void initFragment() {
        fragments = new ArrayList<>();
        fragments.add(new NewPostFragment());
        fragments.add(new HotPostFragment());
    }
}
