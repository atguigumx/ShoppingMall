package com.atguigu.shop.controller.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.RadioGroup;

import com.atguigu.shop.R;
import com.atguigu.shop.base.BaseFragment;
import com.atguigu.shop.controller.community.fragment.CommunityFragment;
import com.atguigu.shop.controller.home.fragment.HomeFragment;
import com.atguigu.shop.controller.shoppingcart.fragment.ShoppingCartFragment;
import com.atguigu.shop.controller.type.fragment.TypeFragment;
import com.atguigu.shop.controller.user.UserFragment;
import com.atguigu.shop.utils.zxing.activity.ScanResultActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.frameLayout)
    FrameLayout frameLayout;
    @BindView(R.id.rg_main)
    RadioGroup rgMain;
    /**
     * 装各个Fragment的集合
     */
    ArrayList<BaseFragment> fragments;
    /**
     * 之前显示的Fragment
     */
    private Fragment tempFragment;

    /**
     * 被选中页面的位置
     */
    private int position;
    //用于处理用户头像
    private static final int PICTURE = 100;
    private static final int CAMERA = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initFragment();

        initListener();
    }

    /**
     * 监听RadioGroup专题的变化
     */
    private void initListener() {
        rgMain.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_home://主页
                        position = 0;
                        break;
                    case R.id.rb_type://分类
                        position = 1;
                        break;
                    case R.id.rb_community://发现
                        position = 2;
                        break;
                    case R.id.rb_cart://购物车
                        position = 3;
                        break;
                    case R.id.rb_user://个人中心
                        position = 4;
                        break;
                }

                Fragment currentFragment = getFragment(position);//HomeFragment
                switchFragment (currentFragment);

            }
        });

        //默认选中首页-放在setOnCheckedChangeListener 执行之后
        rgMain.check(R.id.rb_home);

    }

    /**
     * 传入当前要显示的Fragment
     * @param currentFragment
     */
    private void switchFragment(Fragment currentFragment) {
        if(tempFragment != currentFragment){

            if(currentFragment != null){
                //开启事务
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                if(!currentFragment.isAdded()){
                    //隐藏之前显示的
                    if(tempFragment != null){
                        ft.hide(tempFragment);
                    }
                    //判断currentFragment 有没有添加，如果没有就添加
                    ft.add(R.id.frameLayout,currentFragment);


                }else{
                    //隐藏之前显示的
                    if(tempFragment != null){
                        ft.hide(tempFragment);
                    }
                    //否则就显示
                    ft.show(currentFragment);
                }

                ft.commit();//统一提交

            }

            tempFragment = currentFragment;
        }

    }


    /**
     * 根据位置得到Fragment
     * @param position
     * @return
     */
    private Fragment getFragment(int position) {
        if(fragments != null && fragments.size() > 0){
            return fragments.get(position);
        }
        return null;
    }

    /**
     * 初始化Fragment
     */
    private void initFragment() {
        fragments = new ArrayList<>();
        fragments.add(new HomeFragment());//主页
        fragments.add(new TypeFragment());//分类
        fragments.add(new CommunityFragment());//发现
        fragments.add(new ShoppingCartFragment());//购物车
        fragments.add(new UserFragment());//个人中心

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        int id = intent.getIntExtra("checkedid",R.id.rb_home);
        switch (id){
            case R.id.rb_home:
                rgMain.check(R.id.rb_home);
                break;
            case R.id.rb_cart:
                rgMain.check(R.id.rb_cart);
                break;
        }
    }


    /**
     * 处理二维码
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CAMERA && resultCode == RESULT_OK && data != null){
            UserFragment fragment = (UserFragment) fragments.get(4);
            if(fragment != null){
                fragment.showCameraImage(data);
            }
        }else if(requestCode == PICTURE && resultCode == RESULT_OK && data != null){
            UserFragment fragment = (UserFragment) fragments.get(4);
            if(fragment != null){
                fragment.showPictureImage(data);
            }
        }else if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            Intent intent = new Intent(this, ScanResultActivity.class);
            if (bundle != null && bundle.size() != 0) {
                intent.putExtra("data", bundle);
            }
            startActivity(intent);
        }
    }

}
