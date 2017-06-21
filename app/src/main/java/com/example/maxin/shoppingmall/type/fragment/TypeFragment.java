package com.example.maxin.shoppingmall.type.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.maxin.shoppingmall.R;
import com.example.maxin.shoppingmall.base.BaseFragment;
import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by shkstart on 2017/6/11.
 */

public class TypeFragment extends BaseFragment {

    @BindView(R.id.tl_1)
    SegmentTabLayout tl1;
    @BindView(R.id.iv_type_search)
    ImageView ivTypeSearch;
    Unbinder unbinder;
    private List<BaseFragment> fragments;
    String[] titls = {"分类","标签"};
    private Fragment tempFragment;
    @Override
    public View initView() {
        Log.e("TAG", "分类视图被初始化了");
        View view = View.inflate(mContext, R.layout.fragment_type, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        initSegmentTabLayout();
        initFragment();
    }

    private void initFragment() {

        fragments=new ArrayList<>();
        fragments.add(new ListFragment());
        fragments.add(new TagFragment());
        switchFragment(fragments.get(0));

    }

    private void initSegmentTabLayout() {

        tl1.setTabData(titls);
        tl1.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                switchFragment(fragments.get(position));
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
    }

    private void switchFragment(BaseFragment baseFragment) {
        if(tempFragment!=baseFragment) {
            if(baseFragment!=null) {
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                if(!baseFragment.isAdded()) {
                    //判断baseFragment 有没有添加，如果没有就添加
                    if(tempFragment!=null) {
                        //隐藏之前的
                        ft.hide(tempFragment);
                    }
                    ft.add(R.id.fl_type,baseFragment);
                }else {
                    //否则就显示
                    if(tempFragment!=null) {
                        //隐藏之前的
                        ft.hide(tempFragment);

                    }
                    //显示现在的
                        ft.show(baseFragment);
                }
                ft.commit();

            }
            tempFragment=baseFragment;
        }
    }

    @OnClick(R.id.iv_type_search)
    public void onViewClicked() {
        Toast.makeText(mContext, "搜索", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
