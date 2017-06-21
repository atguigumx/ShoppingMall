package com.example.maxin.shoppingmall.user.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maxin.shoppingmall.R;
import com.example.maxin.shoppingmall.activity.LoginActivity;
import com.example.maxin.shoppingmall.base.BaseFragment;
import com.hankkin.gradationscroll.GradationScrollView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by shkstart on 2017/6/11.
 */

public class UserFragment extends BaseFragment implements GradationScrollView.ScrollViewListener{
    @BindView(R.id.ib_user_icon_avator)
    ImageButton ibUserIconAvator;
    @BindView(R.id.tv_username)
    TextView tvUsername;
    @BindView(R.id.rl_header)
    RelativeLayout rlHeader;
    @BindView(R.id.tv_all_order)
    TextView tvAllOrder;
    @BindView(R.id.tv_user_pay)
    TextView tvUserPay;
    @BindView(R.id.tv_user_receive)
    TextView tvUserReceive;
    @BindView(R.id.tv_user_finish)
    TextView tvUserFinish;
    @BindView(R.id.tv_user_drawback)
    TextView tvUserDrawback;
    @BindView(R.id.tv_user_location)
    TextView tvUserLocation;
    @BindView(R.id.tv_user_collect)
    TextView tvUserCollect;
    @BindView(R.id.tv_user_coupon)
    TextView tvUserCoupon;
    @BindView(R.id.tv_user_score)
    TextView tvUserScore;
    @BindView(R.id.tv_user_prize)
    TextView tvUserPrize;
    @BindView(R.id.tv_user_ticket)
    TextView tvUserTicket;
    @BindView(R.id.tv_user_invitation)
    TextView tvUserInvitation;
    @BindView(R.id.tv_user_callcenter)
    TextView tvUserCallcenter;
    @BindView(R.id.tv_user_feedback)
    TextView tvUserFeedback;
    @BindView(R.id.ll_root)
    LinearLayout llRoot;
    @BindView(R.id.scrollview)
    GradationScrollView scrollview;
    @BindView(R.id.tv_usercenter)
    TextView tvUsercenter;
    @BindView(R.id.ib_user_setting)
    ImageButton ibUserSetting;
    @BindView(R.id.ib_user_message)
    ImageButton ibUserMessage;
    private int height;

    @Override
    public View initView() {
        View rootView=View.inflate(mContext, R.layout.fragment_user,null);
        ButterKnife.bind(this, rootView);
        tvUsercenter.setBackgroundColor(Color.argb((int) 0, 255,0,0));
        tvUsercenter.setTextColor(Color.argb((int) 0, 255,255,255));
        return rootView;
    }

    @Override
    public void initData() {
        super.initData();
        initListeners();

    }
    private void initListeners() {

        //视图树观察者
        ViewTreeObserver vto = rlHeader.getViewTreeObserver();

        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //移除监听
                rlHeader.getViewTreeObserver().removeGlobalOnLayoutListener(
                        this);

                //得到相对布局的高
                height = rlHeader.getHeight();

                //设置ScrollView的滑动监听
                scrollview.setScrollViewListener(UserFragment.this);
            }
        });
    }
    @OnClick({R.id.tv_username, R.id.ib_user_setting, R.id.ib_user_message})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_username:
//                Toast.makeText(mContext, "登录/注册", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext,LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.ib_user_setting:
                Toast.makeText(mContext, "设置", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ib_user_message:
                Toast.makeText(mContext, "消息", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onScrollChanged(GradationScrollView scrollView, int x, int y, int oldx, int oldy) {
        if (y <= 0) {   //设置标题的背景颜色
            tvUsercenter.setBackgroundColor(Color.argb((int) 0, 255,0,0));

        } else if (y > 0 && y <= height) { //滑动距离小于banner图的高度时，设置背景和字体颜色颜色透明度渐变

            //滑动的距离：最大距离（相对布局高度） = 透明的改变 ： 最大透明度

            //透明的改变 =  (滑动的距离/最大距离)*255

            //(滑动的距离/最大距离)
            float scale = (float) y / height;
            //透明度
            float alpha = ( scale*255);
            tvUsercenter.setTextColor(Color.argb((int) alpha, 255,255,255));
            tvUsercenter.setBackgroundColor(Color.argb((int) alpha, 255,0,0));
        } else {    //滑动到banner下面设置普通颜色
            //y>height
            //透明度：0~255
            tvUsercenter.setBackgroundColor(Color.argb((int) 255, 255,0,0));

        }
    }
}
