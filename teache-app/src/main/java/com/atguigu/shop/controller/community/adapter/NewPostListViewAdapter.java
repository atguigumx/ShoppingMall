package com.atguigu.shop.controller.community.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atguigu.shop.R;
import com.atguigu.shop.model.communitybean.NewPostBean;
import com.atguigu.shop.utils.TimeUtils;
import com.atguigu.shop.model.NetManager;
import com.bumptech.glide.Glide;
import com.opendanmaku.DanmakuItem;
import com.opendanmaku.DanmakuView;
import com.opendanmaku.IDanmakuItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者：尚硅谷-杨光福 on 2016/12/24 15:43
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：新帖的适配器
 */
public class NewPostListViewAdapter extends BaseAdapter {
    private final Context mContext;
    private final List<NewPostBean.ResultBean> datas;
    private TimeUtils utils;

    public NewPostListViewAdapter(Context mContext, List<NewPostBean.ResultBean> result) {
        this.mContext = mContext;
        this.datas = result;

        utils = new TimeUtils();
    }

    @Override
    public int getCount() {
        return datas.size();
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
            convertView = View.inflate(mContext, R.layout.item_listview_new_post, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //根据位置得到对应的数据
        NewPostBean.ResultBean resultBean = datas.get(position);

        viewHolder.tvCommunityUsername.setText(resultBean.getUsername());
        //设置用户头像
        Glide.with(mContext).load(NetManager.BASE_URL_IMAGE+resultBean.getAvatar()).into(viewHolder.ibNewPostAvatar);

        //时间
        viewHolder.tvCommunityAddtime.setText(utils.getTimeFromMillisecond(Long.parseLong(resultBean.getAdd_time()))+"小时");
        //设置大图片
        Glide.with(mContext).load(NetManager.BASE_URL_IMAGE+resultBean.getFigure()).into(viewHolder.ivCommunityFigure);

        viewHolder.tvCommunitySaying.setText(resultBean.getSaying());

        viewHolder.tvCommunityLikes.setText(resultBean.getLikes());

        viewHolder.tvCommunityComments.setText(resultBean.getComments());

        List<String>  strings =   resultBean.getComment_list();

        if(strings != null && strings.size() >0){
            List<IDanmakuItem> list = initItems(strings,viewHolder.danmakuView);
            //把顺序打乱
            Collections.shuffle(list);
            //添加弹幕数据
            viewHolder.danmakuView.addItem(list, true);

            viewHolder.danmakuView.setVisibility(View.VISIBLE);
            //显示弹幕
            viewHolder.danmakuView.show();
        }else{
            viewHolder.danmakuView.hide();
            viewHolder.danmakuView.setVisibility(View.GONE);
        }

        return convertView;
    }

    /**
     * 构造弹幕
     * @param strings
     * @param danmakuView
     * @return
     */
    private List<IDanmakuItem> initItems(List<String> strings, DanmakuView danmakuView) {

        List<IDanmakuItem> list = new ArrayList<>();

        for (int i = 0; i < strings.size(); i++) {
            IDanmakuItem item = new DanmakuItem(mContext, strings.get(i), danmakuView.getWidth());
            list.add(item);
        }

        return list;
    }

    static class ViewHolder {
        @BindView(R.id.tv_community_username)
        TextView tvCommunityUsername;
        @BindView(R.id.tv_community_addtime)
        TextView tvCommunityAddtime;
        @BindView(R.id.rl)
        RelativeLayout rl;
        @BindView(R.id.ib_new_post_avatar)
        ImageButton ibNewPostAvatar;
        @BindView(R.id.iv_community_figure)
        ImageView ivCommunityFigure;
        @BindView(R.id.tv_community_saying)
        TextView tvCommunitySaying;
        @BindView(R.id.tv_community_likes)
        TextView tvCommunityLikes;
        @BindView(R.id.tv_community_comments)
        TextView tvCommunityComments;
        @BindView(R.id.danmakuView)
        DanmakuView danmakuView;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
