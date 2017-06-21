package com.example.maxin.shoppingmall.type.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.maxin.shoppingmall.R;
import com.example.maxin.shoppingmall.activity.GoodsInfoActivity;
import com.example.maxin.shoppingmall.home.bean.GoodsBean;
import com.example.maxin.shoppingmall.type.bean.TypeBean;
import com.example.maxin.shoppingmall.utils.Constants;
import com.example.maxin.shoppingmall.utils.DensityUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.maxin.shoppingmall.home.adapter.HomeAdapter.GOODSBEAN;

/**
 * Created by shkstart on 2017/6/17.
 */

public class TypeRightAdapterTwo extends RecyclerView.Adapter {
    /**
     * 热卖推荐
     */
    private static final int HOT = 0;
    /**
     * 常用分类
     */
    private static final int COMMON = 1;

    /**
     * 当前类型
     */
    private int currentType = HOT;
    private final Context mContext;
    private final List<TypeBean.ResultBean.ChildBean> child;
    private final List<TypeBean.ResultBean.HotProductListBean> hot_product_list;

    public TypeRightAdapterTwo(Context mContext, List<TypeBean.ResultBean> result) {
        this.mContext = mContext;
        child = result.get(0).getChild();
        hot_product_list = result.get(0).getHot_product_list();
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == HOT) {
            currentType = HOT;
        } else if (position == COMMON) {
            currentType = COMMON;
        }
        return currentType;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == HOT) {
            View view = View.inflate(mContext, R.layout.item_hot_right, null);
            return new HotViewHolder(mContext, view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == HOT) {
            HotViewHolder hotViewHolder = (HotViewHolder) holder;
            hotViewHolder.setData(hot_product_list);
        }
    }

    class HotViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ll_hot_right)
        LinearLayout llHotRight;
        @BindView(R.id.hsl_hot_right)
        HorizontalScrollView hslHotRight;
        public HotViewHolder(Context mContext, View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void setData(final List<TypeBean.ResultBean.HotProductListBean> hot_product_list) {
            for (int i = 0; i < hot_product_list.size(); i++) {
                TypeBean.ResultBean.HotProductListBean listBean = hot_product_list.get(i);
                //创建一个线性布局
                final LinearLayout myLinear = new LinearLayout(mContext);
                LinearLayout.LayoutParams llParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, -2);
                llParams.setMargins(DensityUtil.dip2px(mContext, 5), 0, DensityUtil.dip2px(mContext, 5), DensityUtil.dip2px(mContext, 20));
                myLinear.setGravity(Gravity.CENTER);
                myLinear.setOrientation(LinearLayout.VERTICAL);

                //图片
                ImageView imageView = new ImageView(mContext);
                LinearLayout.LayoutParams ivParams = new LinearLayout.LayoutParams(DensityUtil.dip2px(mContext, 80), DensityUtil.dip2px(mContext, 80));
                ivParams.setMargins(0, 0, 0, DensityUtil.dip2px(mContext, 10));

                //联网请求图片
                Glide.with(mContext).load(Constants.BASE_URL_IMAGE + listBean.getFigure()).into(imageView);
                //添加到线性布局
                myLinear.addView(imageView, ivParams);

                // 文本
                LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                TextView textView = new TextView(mContext);
                textView.setTextColor(Color.parseColor("#ed3f3f"));
                textView.setText("￥" + listBean.getCover_price());
                //添加到线性布局
                myLinear.addView(textView, tvParams);

                myLinear.setTag(i);

                myLinear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position = (int) myLinear.getTag();

                        GoodsBean goodsBean = new GoodsBean();
                        goodsBean.setName(hot_product_list.get(position).getName());
                        goodsBean.setCover_price(hot_product_list.get(position).getCover_price());
                        goodsBean.setFigure(hot_product_list.get(position).getFigure());
                        goodsBean.setProduct_id(hot_product_list.get(position).getProduct_id());

                        Intent intent = new Intent(mContext, GoodsInfoActivity.class);
                        intent.putExtra(GOODSBEAN, goodsBean);
                        mContext.startActivity(intent);
                    }
                });

                llHotRight.addView(myLinear);
            }
        }
    }
}
