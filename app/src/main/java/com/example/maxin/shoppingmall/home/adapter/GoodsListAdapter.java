package com.example.maxin.shoppingmall.home.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.maxin.shoppingmall.R;
import com.example.maxin.shoppingmall.home.bean.TypeListBean;
import com.example.maxin.shoppingmall.utils.Constants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by shkstart on 2017/6/13.
 */

public class GoodsListAdapter extends RecyclerView.Adapter<GoodsListAdapter.MyViewHolder> {


    private final Context context;
    private final List<TypeListBean.ResultBean.PageDataBean> datas;



    public GoodsListAdapter(Context context, TypeListBean.ResultBean result) {
        this.context = context;
        this.datas = result.getPage_data();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_goods_list, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        TypeListBean.ResultBean.PageDataBean bean = datas.get(position);
        holder.tvName.setText("$"+bean.getName());
        holder.tvPrice.setText("$"+bean.getCover_price());
        Glide.with(context).load(Constants.BASE_URL_IMAGE+bean.getFigure()).into(holder.ivHot);
    }

    @Override
    public int getItemCount() {
        return datas.size();
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
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onItemClickListener!=null) {
                        onItemClickListener.setOnItemClickListener(datas.get(getLayoutPosition()));
                    }
                }
            });
        }
    }
    public interface OnItemClickListener{
         void setOnItemClickListener(TypeListBean.ResultBean.PageDataBean pageDataBean);
    }
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener l){
        this.onItemClickListener=l;
    }
}
