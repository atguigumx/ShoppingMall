package com.atguigu.shop.controller.type.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.atguigu.shop.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者：尚硅谷-杨光福 on 2016/12/24 10:19
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：ListView的适配器
 */
public class TypeLeftAdapter extends BaseAdapter {
    private final Context mContext;
    private final String[] datas;
    private int prePosition;

    public TypeLeftAdapter(Context mContext, String[] titles) {
        this.mContext = mContext;
        this.datas = titles;
    }

    @Override
    public int getCount() {
        return datas.length;
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
            convertView = View.inflate(mContext, R.layout.item_type, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvTitle.setText(datas[position]);

        if(prePosition ==position){
            //刚才被点击的item
            convertView.setBackgroundResource(R.drawable.type_item_background_selector);
            //选中项背景
            viewHolder.tvTitle.setTextColor(Color.parseColor("#fd3f3f"));
        }else {
            //设置默认
            convertView.setBackgroundResource(R.drawable.bg2);  //其他项背景
            viewHolder.tvTitle.setTextColor(Color.parseColor("#323437"));
        }

        return convertView;
    }

    /**
     * 传入被点击的位置
     * @param position
     */
    public void changeSelected(int position) {
        this.prePosition = position;
    }

    static class ViewHolder {
        @BindView(R.id.tv_title)
        TextView tvTitle;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
