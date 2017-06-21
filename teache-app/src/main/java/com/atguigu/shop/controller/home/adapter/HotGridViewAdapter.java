package com.atguigu.shop.controller.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.atguigu.shop.R;
import com.atguigu.shop.model.homebean.HomeBean;
import com.atguigu.shop.model.NetManager;
import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者：尚硅谷-杨光福 on 2016/12/21 09:49
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：热卖的适配器
 */
public class HotGridViewAdapter extends BaseAdapter {
    private final Context mContext;
    private List<HomeBean.ResultBean.HotInfoBean> datas;

    private LayoutInflater inflater;

    public HotGridViewAdapter(Context mContext) {
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {

        if (datas != null && datas.size() > 0) {
            return datas.size();
        }

        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_hot_grid_view, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //根据位置得到对应的数据
        HomeBean.ResultBean.HotInfoBean hotInfoBean = datas.get(position);
        //绑定数据
        Glide.with(mContext).load(NetManager.BASE_URL_IMAGE+hotInfoBean.getFigure()).into(viewHolder.ivHot);
        viewHolder.tvName.setText(hotInfoBean.getName());
        viewHolder.tvPrice.setText("￥"+hotInfoBean.getCover_price());

        return convertView;
    }

    public void setData(List<HomeBean.ResultBean.HotInfoBean> hot_info) {
        this.datas = hot_info;
    }

    static class ViewHolder {
        @BindView(R.id.iv_hot)
        ImageView ivHot;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_price)
        TextView tvPrice;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
