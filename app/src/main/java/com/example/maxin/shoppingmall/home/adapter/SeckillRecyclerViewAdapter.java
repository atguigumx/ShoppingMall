package com.example.maxin.shoppingmall.home.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
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
 * Created by shkstart on 2017/6/12.
 */

public class SeckillRecyclerViewAdapter extends RecyclerView.Adapter<SeckillRecyclerViewAdapter.MyViewHolder> {


    private final Context mContext;
    private final List<HomeBean.ResultBean.SeckillInfoBean.ListBean> datas;


    public SeckillRecyclerViewAdapter(Context mContext, List<HomeBean.ResultBean.SeckillInfoBean.ListBean> list) {
        this.mContext = mContext;
        this.datas=list;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_seckill, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        HomeBean.ResultBean.SeckillInfoBean.ListBean listBean = datas.get(position);
        Glide.with(mContext).load(Constants.BASE_URL_IMAGE+listBean.getFigure()).into(holder.ivFigure);
        holder.tvCoverPrice.setText("$"+listBean.getCover_price());
        Log.e("TT", "图片"+Constants.BASE_URL_IMAGE+listBean.getFigure());
        holder.tvOriginPrice.setText("$"+listBean.getOrigin_price());
    }

    @Override
    public int getItemCount() {
        return datas.size(); //不重写此方法不显示
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_figure)
        ImageView ivFigure;
        @BindView(R.id.tv_cover_price)
        TextView tvCoverPrice;
        @BindView(R.id.tv_origin_price)
        TextView tvOriginPrice;
        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onItemClickListener!=null) {
                        onItemClickListener.OnItemClick(getLayoutPosition());
                    }
                }
            });
        }

    }
    public interface OnItemClickListener{
        public void OnItemClick(int position);
    }
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener){
        this.onItemClickListener=listener;
    }
}
