package com.atguigu.shop.controller.home.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.atguigu.shop.R;
import com.atguigu.shop.model.homebean.HomeBean;
import com.atguigu.shop.model.NetManager;
import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者：尚硅谷-杨光福 on 2016/12/20 16:22
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：秒杀的RecyclerView的适配器
 */
public class SeckillRecyclerViewAdapter extends RecyclerView.Adapter<SeckillRecyclerViewAdapter.ViewHolder> {

    private final Context mContext;
    private final List<HomeBean.ResultBean.SeckillInfoBean.ListBean> datas;

    private LayoutInflater inflater;

    public SeckillRecyclerViewAdapter(Context mContext, HomeBean.ResultBean.SeckillInfoBean seckill_info) {
        this.mContext = mContext;
        this.datas = seckill_info.getList();
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.item_seckill, null);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //1.根据位置得到对应的数据
        HomeBean.ResultBean.SeckillInfoBean.ListBean listBean = datas.get(position);

        //2.绑定图片，价格，原价
        Glide.with(mContext).load(NetManager.BASE_URL_IMAGE+listBean.getFigure()).into(holder.ivFigure);
        holder.tvCoverPrice.setText("￥"+listBean.getCover_price());
        holder.tvOriginPrice.setText("￥"+listBean.getOrigin_price());
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class ViewHolder extends  RecyclerView.ViewHolder{
        @BindView(R.id.iv_figure)
        ImageView ivFigure;
        @BindView(R.id.tv_cover_price)
        TextView tvCoverPrice;
        @BindView(R.id.tv_origin_price)
        TextView tvOriginPrice;
        @BindView(R.id.ll_root)
        LinearLayout llRoot;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

            //设置点击事件
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(clickListener != null){
                        clickListener.onItemClick(getLayoutPosition());
                    }
                }
            });
        }
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
