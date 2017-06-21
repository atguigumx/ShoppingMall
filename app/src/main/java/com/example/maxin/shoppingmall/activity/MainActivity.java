package com.example.maxin.shoppingmall.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioGroup;

import com.example.maxin.shoppingmall.R;
import com.example.maxin.shoppingmall.base.BaseFragment;
import com.example.maxin.shoppingmall.community.fragment.CommunityFragment;
import com.example.maxin.shoppingmall.home.fragment.HomeFragment;
import com.example.maxin.shoppingmall.shoppingcart.fragment.ShoppingCartFragment;
import com.example.maxin.shoppingmall.type.fragment.TypeFragment;
import com.example.maxin.shoppingmall.user.fragment.UserFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.maxin.shoppingmall.activity.GoodsInfoActivity.CHECK_ID;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.rg_main)
    RadioGroup rgMain;
    private List<BaseFragment> fragments;
    private BaseFragment tempFragment;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initFragment();

        initListener();


    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        int checkId = intent.getIntExtra(CHECK_ID, R.id.rb_home);
        switch (checkId) {
            case R.id.rb_cart :
                rgMain.check(R.id.rb_cart);
                break;
        }
        switch (checkId) {
            case R.id.rb_home :
                rgMain.check(R.id.rb_home);
                break;
        }
    }

    private void initListener() {
        rgMain.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_home :
                        position=0;
                        break;
                    case R.id.rb_type :
                        position=1;
                        break;
                    case R.id.rb_cart:
                        position=3;
                        break;
                    case R.id.rb_community :
                        position=2;
                        break;
                    case R.id.rb_user :
                        position=4;
                        break;
                }
                BaseFragment fragment = fragments.get(position);
                switchFragment(fragment);
            }

        });


        rgMain.check(R.id.rb_home);
    }

    private void switchFragment(BaseFragment currentFragment) {
        if (currentFragment != tempFragment) {

            if (currentFragment != null) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

                    //如果没有添加就添加
                if (!currentFragment.isAdded()) {
                    //隐藏之前的
                    if (tempFragment != null) {
                        ft.hide(tempFragment);
                    }

                    //添加Fragment
                    ft.add(R.id.fl_main, currentFragment);

                }
                   //如果添加了就隐藏
                else {
                    //隐藏上次显示的
                    if (tempFragment != null) {
                        ft.hide(tempFragment);
                    }

                    //显示
                    ft.show(currentFragment);
                }

                //最后统一提交
                ft.commit();
                //重新赋值
                tempFragment = currentFragment;
            }

        }
    }

    private void initFragment() {
        fragments=new ArrayList<>();
        fragments.add(new HomeFragment());
        fragments.add(new TypeFragment());
        fragments.add(new CommunityFragment());
        fragments.add(new ShoppingCartFragment());
        fragments.add(new UserFragment());
        switchFragment(fragments.get(position));
    }

}
