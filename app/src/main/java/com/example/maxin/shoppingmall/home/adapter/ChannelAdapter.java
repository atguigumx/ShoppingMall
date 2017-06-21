package com.example.maxin.shoppingmall.home.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.maxin.shoppingmall.R;
import com.example.maxin.shoppingmall.home.bean.HomeBean;
import com.example.maxin.shoppingmall.utils.Constants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by shkstart on 2017/6/11.
 */

public class ChannelAdapter extends BaseAdapter {
    private final Context mContext;
    private final List<HomeBean.ResultBean.ChannelInfoBean> channel;

    public ChannelAdapter(Context mContext, List<HomeBean.ResultBean.ChannelInfoBean> channel_info) {
        this.mContext=mContext;
        this.channel=channel_info;
    }

    @Override
    public int getCount() {
        return channel.size();
    }

    @Override
    public Object getItem(int i) {
        return channel.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holer;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_channel, null);
            holer = new ViewHolder(convertView);
            convertView.setTag(holer);
        } else {
            holer = (ViewHolder) convertView.getTag();
        }

        HomeBean.ResultBean.ChannelInfoBean channelInfoBean = channel.get(i);
        holer.tvChannel.setText(channelInfoBean.getChannel_name());
        //Log.e("Tag",channelInfoBean.getChannel_name());
        Glide.with(mContext)
                .load(Constants.BASE_URL_IMAGE+channelInfoBean.getImage())
                .into(holer.ivChannel);
        return convertView;
    }
    class ViewHolder {
        @BindView(R.id.iv_channel)
        ImageView ivChannel;
        @BindView(R.id.tv_channel)
        TextView tvChannel;

        ViewHolder(View view) {
            ButterKnife.bind(this,view);
        }
    }
}
