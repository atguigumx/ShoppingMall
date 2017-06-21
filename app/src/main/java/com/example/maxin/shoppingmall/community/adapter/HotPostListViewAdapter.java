package com.example.maxin.shoppingmall.community.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.maxin.shoppingmall.R;
import com.example.maxin.shoppingmall.community.bean.HotPostBean;
import com.example.maxin.shoppingmall.utils.Constants;
import com.example.maxin.shoppingmall.utils.DensityUtil;

import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by shkstart on 2017/6/17.
 */

public class HotPostListViewAdapter extends BaseAdapter {
    private final Context mContext;
    private final List<HotPostBean.ResultBean> datas;

    public HotPostListViewAdapter(Context mContext, List<HotPostBean.ResultBean> result) {
        this.mContext = mContext;
        this.datas = result;
    }

    @Override
    public int getCount() {
        return datas == null ? 0 : datas.size();
    }

    @Override
    public Object getItem(int i) {
        return datas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = View.inflate(mContext, R.layout.item_hotpost_listview, null);
            viewHolder=new ViewHolder(view);
            view.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) view.getTag();
        }
        HotPostBean.ResultBean resultBean = datas.get(i);
        SimpleDateFormat myFmt = new SimpleDateFormat("MM-dd HH:mm");
        //?
        viewHolder.tvHotAddtime.setText(myFmt.format(Integer.parseInt(resultBean.getAdd_time())));

        Glide.with(mContext).load(Constants.BASE_URL_IMAGE+resultBean.getAvatar()).into(viewHolder.ivNewPostAvatar);
        Glide.with(mContext).load(Constants.BASE_URL_IMAGE+resultBean.getFigure()).into(viewHolder.ivHotFigure);
        viewHolder.tvHotSaying.setText(resultBean.getSaying());
        viewHolder.tvHotLikes.setText(resultBean.getLikes());
        viewHolder.tvHotComments.setText(resultBean.getComments());

        String is_top = resultBean.getIs_top();
        String is_hot = resultBean.getIs_hot();
        String is_essence = resultBean.getIs_essence();
        if("1".equals(is_top)) {
            LinearLayout.LayoutParams topParams = new LinearLayout.LayoutParams(-2,-2);

            TextView topTextView = new TextView(mContext);
            topTextView.setText("置顶");
            topTextView.setGravity(Gravity.CENTER);
            topTextView.setTextColor(Color.WHITE);

            topTextView.setBackgroundResource(R.drawable.is_top_shape);

            topTextView.setPadding(DensityUtil.dip2px(mContext, 5), DensityUtil.dip2px(mContext, 5), DensityUtil.dip2px(mContext, 5), DensityUtil.dip2px(mContext, 5));

            topParams.setMargins(DensityUtil.dip2px(mContext, 8), 0, DensityUtil.dip2px(mContext, 5), 0);

            viewHolder.llHotPost.addView(topTextView,topParams);

            viewHolder.llHotPost.removeAllViews();
        }

        if("1".equals(is_hot)) {
            LinearLayout.LayoutParams textViewLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            TextView textView = new TextView(mContext);
            textViewLp.setMargins(0, 0, DensityUtil.dip2px(mContext, 5), 0);
            textView.setText("热门");
            textView.setGravity(Gravity.CENTER);
            //文字为白色
            textView.setTextColor(Color.WHITE);
            //设置padding
            textView.setPadding(DensityUtil.dip2px(mContext, 5), DensityUtil.dip2px(mContext, 5), DensityUtil.dip2px(mContext, 5), DensityUtil.dip2px(mContext, 5));
            //设置橙色背景
            textView.setBackgroundResource(R.drawable.is_hot_shape);
            viewHolder.llHotPost.addView(textView, textViewLp);
        }
        if("1".equals(is_essence)) {
            LinearLayout.LayoutParams textViewLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            //距离右边
            textViewLp.setMargins(0, 0, DensityUtil.dip2px(mContext, 5), 0);
            TextView textView = new TextView(mContext);
            textView.setText("精华");
            textView.setGravity(Gravity.CENTER);
            //文字白色
            textView.setTextColor(Color.WHITE);
            textView.setPadding(DensityUtil.dip2px(mContext, 5), DensityUtil.dip2px(mContext, 5), DensityUtil.dip2px(mContext, 5), DensityUtil.dip2px(mContext, 5));
            //设置背景为亮橙色
            textView.setBackgroundResource(R.drawable.is_essence_shape);
            viewHolder.llHotPost.addView(textView, textViewLp);
        }
        return view;
    }

    static class ViewHolder {
        @BindView(R.id.tv_hot_username)
        TextView tvHotUsername;
        @BindView(R.id.tv_hot_addtime)
        TextView tvHotAddtime;
        @BindView(R.id.rl)
        RelativeLayout rl;
        @BindView(R.id.iv_new_post_avatar)
        ImageView ivNewPostAvatar;
        @BindView(R.id.iv_hot_figure)
        ImageView ivHotFigure;
        @BindView(R.id.ll_hot_post)
        LinearLayout llHotPost;
        @BindView(R.id.tv_hot_saying)
        TextView tvHotSaying;
        @BindView(R.id.tv_hot_likes)
        TextView tvHotLikes;
        @BindView(R.id.tv_hot_comments)
        TextView tvHotComments;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
