package com.atguigu.shop.base;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 *BaseFragment
 *首页：HomeFragment
 分类：TypeFragment
 发现：CommunityFragment
 购物车：ShoppingCartFragment
 用户中心：UserFragemnt
 */
public abstract class BaseFragment extends Fragment {

    /**
     * 上下文
     */
    public Context mContext;

    /**
     * 当BaseFragment被系统创建的时候回调
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getActivity();//MainActivity
    }

    /**
     * 当系统要创建视图的时候回调这个方法
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return initView();
    }

    /**
     * 初始化子类的视图，子类强制实现
     * @return
     */
    public abstract View initView();

    /**
     * 当Activity被创建完成的时候回调
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initData();
    }

    /**
     * 当子类
     * 1.需要绑定数据到视图上的时候重写该方法
     * 2.联网请求数据得到数据，并且要绑定数据到视图上的时候重写该方法
     */
    public void initData() {

    }
}
