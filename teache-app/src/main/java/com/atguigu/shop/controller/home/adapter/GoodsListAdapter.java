package com.atguigu.shop.controller.home.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.atguigu.shop.R;
import com.atguigu.shop.model.homebean.GoodsListBean;
import com.atguigu.shop.model.NetManager;
import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者：尚硅谷-杨光福 on 26/12/2016 14:44
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：商品列表的适配器-RecyclerView
 */
public class GoodsListAdapter extends RecyclerView.Adapter<GoodsListAdapter.MyViewHolder> {

    private final Context mContext;
    private  List<GoodsListBean.ResultBean.PageDataBean> datas;

    public GoodsListAdapter(Context context, List<GoodsListBean.ResultBean.PageDataBean> result) {
        this.mContext = context;
        this.datas = result;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = View.inflate(mContext, R.layout.item_goods_list, null);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        //绑定数据
        //1.根据位置得到对应的数据
        GoodsListBean.ResultBean.PageDataBean pageDataBean = datas.get(position);
        //2.绑定数据
        Glide.with(mContext).load(NetManager.BASE_URL_IMAGE+pageDataBean.getFigure()).into(holder.ivHot);
        //文本
        holder.tvName.setText(pageDataBean.getName());
        //价格
        holder.tvPrice.setText("￥"+pageDataBean.getCover_price());
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public void addAll(List<GoodsListBean.ResultBean.PageDataBean> page_data) {

        datas.addAll(page_data);

        notifyItemRangeChanged(0,datas.size());
    }

    /**
     * 得到数据集合
     * @return
     */
    public List<GoodsListBean.ResultBean.PageDataBean>  getDatas(){
        return datas;
    }

    public void update(List<GoodsListBean.ResultBean.PageDataBean> result) {
        this.datas = result;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_hot)
        ImageView ivHot;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_price)
        TextView tvPrice;

        public MyViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onItemClickListener != null){
                        onItemClickListener.setOnItemClickListener(datas.get(getLayoutPosition()));
                    }
                }
            });
        }
    }

    /**
     * 设置item的点击事件的监听
     */
    public interface OnItemClickListener {
       public void setOnItemClickListener(GoodsListBean.ResultBean.PageDataBean data);
    }

    private OnItemClickListener onItemClickListener;

    /**
     * 设置点名某条的监听
     * @param onItemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
