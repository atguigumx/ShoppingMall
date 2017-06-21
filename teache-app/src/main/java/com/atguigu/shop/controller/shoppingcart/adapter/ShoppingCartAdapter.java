package com.atguigu.shop.controller.shoppingcart.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.atguigu.shop.R;
import com.atguigu.shop.model.homebean.GoodsBean;
import com.atguigu.shop.controller.shoppingcart.utils.CartStorage;
import com.atguigu.shop.controller.shoppingcart.view.AddSubView;
import com.atguigu.shop.model.NetManager;
import com.bumptech.glide.Glide;

import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者：尚硅谷-杨光福 on 2016/12/23 09:18
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：购物车的适配器
 */
public class ShoppingCartAdapter extends RecyclerView.Adapter<ShoppingCartAdapter.MyViewHolder> {

    private final Context mContext;
    private final List<GoodsBean> datas;
    private final CheckBox checkboxAll;
    private final TextView tvShopcartTotal;
    private final CheckBox checkboxDeleteAll;

    public ShoppingCartAdapter(Context mContext, List<GoodsBean> datas, CheckBox checkboxAll, TextView tvShopcartTotal, CheckBox checkboxDeleteAll) {
        this.mContext = mContext;
        this.datas = datas;
        this.checkboxAll = checkboxAll;
        this.tvShopcartTotal = tvShopcartTotal;
        this.checkboxDeleteAll = checkboxDeleteAll;

        //显示总价格
        showTotalPrice();
    }

    public void showTotalPrice() {
        tvShopcartTotal.setText("合计:" + getTotalPrice());
    }

    /**
     * 得到总价格
     *
     * @return
     */
    public double getTotalPrice() {

        double totalPrice = 0.0;

        if (datas != null && datas.size() > 0) {

            for (int i = 0; i < datas.size(); i++) {

                GoodsBean goodsBean = datas.get(i);

                if (goodsBean.isChecked()) {
                    //被选中的
                    totalPrice = totalPrice + goodsBean.getNumber() * Double.parseDouble(goodsBean.getCover_price());
                }
            }
        }

        return totalPrice;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = View.inflate(mContext, R.layout.item_shopping_cart, null);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        //1.根据位置得到对应的数据
        final GoodsBean goodsBean = datas.get(position);
        //2.绑定数据
        //根据Bean对象，校验是否真正的选中
        holder.cbGov.setChecked(goodsBean.isChecked());
        //图片
        Glide.with(mContext).load(NetManager.BASE_URL_IMAGE + goodsBean.getFigure()).into(holder.ivGov);
        holder.tvDescGov.setText(goodsBean.getName());//商品名字
        holder.tvPriceGov.setText("￥" + goodsBean.getCover_price());//价格
        //设置value
        holder.addSubView.setValue(goodsBean.getNumber());
        //至少要购买一个
        holder.addSubView.setMinValue(1);
        //设置商品库存
        holder.addSubView.setMaxValue(100);
        //设置增加和减少按钮的监听
        holder.addSubView.setOnNumberChangeListener(new AddSubView.OnNumberChangeListener() {
            @Override
            public void numberChange(int value) {
                //1.设置Bean对象中
                goodsBean.setNumber(value);
                //2.重新计算总价格
                showTotalPrice();
                //3.同步到内存和本地中
                CartStorage.getInstance(mContext).updateData(goodsBean);
            }
        });

        //设置发货
        holder.tvSeller.setText("由"+goodsBean.getSeller()+" 发货");
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public void checkAll() {

        if (datas != null && datas.size() > 0) {
            //有数据
            for (int i = 0; i < datas.size(); i++) {
                GoodsBean goodsBean = datas.get(i);

                //判断是否被选中
                if (!goodsBean.isChecked()) {
                    //不被选中的
                    checkboxAll.setChecked(false);
                    checkboxDeleteAll.setChecked(false);
                    return;
                }

            }
            //下面代码没有几乎支持
            //全选
            checkboxAll.setChecked(true);
            checkboxDeleteAll.setChecked(true);
        } else {
            //没有数据-没有集合
            checkboxAll.setChecked(false);
            checkboxDeleteAll.setChecked(false);
        }
    }

    /**
     * 让所有的数据选中或者非选择
     *
     * @param isChecked
     */
    public void checkAll_none(boolean isChecked) {

        if (datas != null && datas.size() > 0) {
            //有数据
            for (int i = 0; i < datas.size(); i++) {
                GoodsBean goodsBean = datas.get(i);
                goodsBean.setChecked(isChecked);
                //刷新适配器了
                notifyItemChanged(i);
            }
        }
    }

    public void deleteData() {
        if (datas != null && datas.size() > 0) {

            for (Iterator iterator = datas.iterator(); iterator.hasNext(); ) {
                GoodsBean goodsBean = (GoodsBean) iterator.next();
                if (goodsBean.isChecked()) {
                    int i = datas.indexOf(goodsBean);
                    //移除
                    iterator.remove();
                    //在本地也同步更新
                    CartStorage.getInstance(mContext).deleteData(goodsBean);
                    //刷新适配器
                    notifyItemRemoved(i);
                }
            }
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cb_gov)
        CheckBox cbGov;
        @BindView(R.id.iv_gov)
        ImageView ivGov;
        @BindView(R.id.tv_desc_gov)
        TextView tvDescGov;
        @BindView(R.id.tv_price_gov)
        TextView tvPriceGov;
        @BindView(R.id.addSubView)
        AddSubView addSubView;
        @BindView(R.id.tv_seller)
        TextView tvSeller;

        public MyViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(MyViewHolder.this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (clickListener != null) {
                        clickListener.onItemClick(v, getLayoutPosition());
                    }
                }
            });
        }
    }

    /**
     * 设置点击某条的监听器
     */
    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    private OnItemClickListener clickListener;

    /**
     * 设置item的点击监听
     *
     * @param l--MyOnItemClickListener()
     */
    public void setOnItemClickListener(OnItemClickListener l) {
        this.clickListener = l;
    }
}
