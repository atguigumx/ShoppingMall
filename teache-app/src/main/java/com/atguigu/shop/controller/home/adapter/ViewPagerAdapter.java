package com.atguigu.shop.controller.home.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.atguigu.shop.model.homebean.HomeBean;
import com.atguigu.shop.model.NetManager;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * 作者：尚硅谷-杨光福 on 2016/12/20 15:19
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：ViewPager的适配器
 */
public class ViewPagerAdapter extends PagerAdapter {

    private final Context mContext;
    private final List<HomeBean.ResultBean.ActInfoBean> datas;

    public ViewPagerAdapter(Context mContext, List<HomeBean.ResultBean.ActInfoBean> act_info) {
        this.mContext = mContext;
        this.datas = act_info;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        ImageView imageView = new ImageView(mContext);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        //添加到容器中
        container.addView(imageView);
        //联网请求图片
        HomeBean.ResultBean.ActInfoBean actInfoBean = datas.get(position);
        Glide.with(mContext).load(NetManager.BASE_URL_IMAGE+actInfoBean.getIcon_url()).into(imageView);

        //设置item的点击事件
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(clickListener != null){
                    clickListener.onItemClick(position);
                }
            }
        });

        return imageView;
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

    /**
     * item点击的监听器
     */
    public interface  OnItemClickListener{

        /**
         * 当点击某条的时候回调
         * @param position 该条对应的位置
         */
        public void onItemClick(int position);
    }

    private OnItemClickListener clickListener;

    /**
     * 设置item的点击事件的监听
     * @param l
     */
    public void setOnItemClickListener(OnItemClickListener l) {
        this.clickListener = l;
    }
}
