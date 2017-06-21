package com.atguigu.shop.controller.type.fragment;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.atguigu.shop.R;
import com.atguigu.shop.base.BaseFragment;
import com.atguigu.shop.controller.type.view.FlowLayout;
import com.atguigu.shop.model.typebean.TagBean;
import com.atguigu.shop.model.NetManager;
import com.atguigu.shop.utils.DensityUtil;
import com.atguigu.shop.utils.DrawUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * 作者：尚硅谷-杨光福 on 2016/12/24 09:41
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：TagFragment
 */
public class TagFragment extends BaseFragment {

    @BindView(R.id.fl_tag)
    FlowLayout flTag;
    private List<TagBean.ResultBean> result;

    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.fragment_tag, null);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void initData() {
        super.initData();

        getDataFromNet(NetManager.TAG_URL);
    }

    /**
     * 根据url联网请求数据
     *
     * @param url
     */
    private void getDataFromNet(String url) {

        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(mContext, "TagFragment联网失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        processData(response);
                    }
                });
    }

    private void processData(String response) {

        TagBean tagBean = JSON.parseObject(response, TagBean.class);
        result = tagBean.getResult();

        if (result != null && result.size() > 0) {

            for (int i = 0; i < result.size(); i++) {
                final TextView tv = new TextView(getContext());

                //设置属性
                tv.setText(result.get(i).getName());
                ViewGroup.MarginLayoutParams mp = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                mp.leftMargin = DensityUtil.dip2px(mContext, 5);
                mp.rightMargin = DensityUtil.dip2px(mContext, 5);
                mp.topMargin = DensityUtil.dip2px(mContext, 10);
                mp.bottomMargin = DensityUtil.dip2px(mContext, 10);
                tv.setLayoutParams(mp);//设置边距

                int padding = DensityUtil.dip2px(mContext, 5);
                tv.setPadding(padding, padding, padding, padding);//设置内边距

                tv.setTextSize(DensityUtil.dip2px(mContext, 12));

                Random random = new Random();
                int red = random.nextInt(150) + 100;
                int green = random.nextInt(150) +100;
                int blue = random.nextInt(150) + 100;

                //设置具有选择器功能的背景
                tv.setBackground(DrawUtils.getSelector(DrawUtils.getDrawable(Color.rgb(red, green, blue), DensityUtil.dip2px(mContext, 5)), DrawUtils.getDrawable(Color.WHITE, DensityUtil.dip2px(mContext, 5))));
                //设置textView是可点击的.如果设置了点击事件，则TextView就是可点击的。

                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(mContext, tv.getText().toString(), Toast.LENGTH_SHORT).show();
                    }
                });

                flTag.addView(tv);
            }
        }
    }
}
