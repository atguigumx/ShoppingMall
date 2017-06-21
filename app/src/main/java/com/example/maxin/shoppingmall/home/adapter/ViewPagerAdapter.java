package com.example.maxin.shoppingmall.home.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.maxin.shoppingmall.home.bean.HomeBean;
import com.example.maxin.shoppingmall.utils.Constants;

import java.util.List;

/**
 * Created by shkstart on 2017/6/12.
 */

public class ViewPagerAdapter extends PagerAdapter {
    private final Context mContext;
    private final List<HomeBean.ResultBean.ActInfoBean> datas;

    public ViewPagerAdapter(Context mContext, List<HomeBean.ResultBean.ActInfoBean> act_info) {
        this.mContext=mContext;
        this.datas=act_info;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        ImageView imageView = new ImageView(mContext);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        //添加到容器中
        container.addView(imageView);
        //联网请求图片
        HomeBean.ResultBean.ActInfoBean actInfoBean = datas.get(position);
        Glide.with(mContext).load(Constants.BASE_URL_IMAGE+actInfoBean.getIcon_url()).into(imageView);

        //设置item的点击事件
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "position=="+position, Toast.LENGTH_SHORT).show();

            }
        });
        return imageView;
    }

/*    @Override
    public int getCount() {
        return datas.size();
    }*/

/*    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        ImageView imageView=new ImageView(mCotext);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        HomeBean.ResultBean.ActInfoBean actInfoBean = datas.get(position);
        Glide.with(mCotext).load(Constants.BASE_URL_IMAGE+actInfoBean.getIcon_url()).into(imageView);
        //设置item的点击事件
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mCotext, "position=="+position, Toast.LENGTH_SHORT).show();

            }
        });
        container.addView(imageView);
        return imageView;
    }*/

/*    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }*/

/*    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }*/
}
