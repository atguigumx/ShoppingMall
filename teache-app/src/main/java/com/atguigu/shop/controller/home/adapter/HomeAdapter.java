package com.atguigu.shop.controller.home.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.atguigu.shop.R;
import com.atguigu.shop.controller.home.activity.GoodsInfoActivity;
import com.atguigu.shop.controller.home.activity.GoodsListActivity;
import com.atguigu.shop.controller.home.activity.WebViewActivity;
import com.atguigu.shop.model.homebean.GoodsBean;
import com.atguigu.shop.model.homebean.HomeBean;
import com.atguigu.shop.model.homebean.WebViewBean;
import com.atguigu.shop.model.NetManager;
import com.atguigu.shop.utils.DensityUtil;
import com.bumptech.glide.Glide;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerClickListener;
import com.youth.banner.loader.ImageLoader;
import com.youth.banner.transformer.AccordionTransformer;
import com.zhy.magicviewpager.transformer.ScaleInTransformer;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.iwgang.countdownview.CountdownView;

/**
 * 作者：尚硅谷-杨光福 on 2016/12/20 13:54
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：主页面的RecyclerView的适配器
 */
public class HomeAdapter extends RecyclerView.Adapter {
    public static final String GOODS_BEAN = "goods_Bean";
    public static final String FIGURE = "figure";
    public static final String WEBVIEW_BEAN = "webview_bean";
    private final Context mContext;
    private final HomeBean.ResultBean result;

    /**
     * 六种类型
     */
    /**
     * 横幅广告
     */
    public static final int BANNER = 0;
    /**
     * 频道
     */
    public static final int CHANNEL = 1;

    /**
     * 活动
     */
    public static final int ACT = 2;

    /**
     * 秒杀
     */
    public static final int SECKILL = 3;
    /**
     * 推荐
     */
    public static final int RECOMMEND = 4;
    /**
     * 热卖
     */
    public static final int HOT = 5;

    /**
     * 当前类型
     */
    public int currentType = BANNER;

    LayoutInflater inflater;

    public HomeAdapter(Context mContext, HomeBean.ResultBean result) {
        this.mContext = mContext;
        this.result = result;
        inflater = LayoutInflater.from(mContext);
    }

    /**
     * 返回总数据
     * @return
     */
    @Override
    public int getItemCount() {
        //1-->6
        return 6;
    }


    /**
     * 根据位置得到不同的类型
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {

        if (position == BANNER) {
            currentType = BANNER;
        } else if (position == CHANNEL) {
            currentType = CHANNEL;
        } else if (position == ACT) {
            currentType = ACT;
        } else if (position == SECKILL) {
            currentType = SECKILL;
        } else if (position == RECOMMEND) {
            currentType = RECOMMEND;
        } else if (position == HOT) {
            currentType = HOT;
        }

        return currentType;
    }

    /**
     * 创建ViewHolder
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == BANNER) {
            return new BannerViewHolder(inflater.inflate(R.layout.banner_viewpager, null));
        } else if (viewType == CHANNEL) {
            return new ChannelViewHolder(mContext, inflater.inflate(R.layout.channel_item, null));
        } else if (viewType == ACT) {
            return new ActViewHolder(mContext, inflater.inflate(R.layout.act_item, null));
        } else if (viewType == SECKILL) {
            return new SeckillViewHolder(mContext, inflater.inflate(R.layout.seckill_item, null));
        } else if (viewType == RECOMMEND) {
            return new RecommendViewHolder(mContext, inflater.inflate(R.layout.recommend_item, null));
        } else if (viewType == HOT) {
            return new HotViewHolder(mContext, inflater.inflate(R.layout.hot_item, null));
        }

        return null;
    }

    /**
     * 绑定数据
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (getItemViewType(position) == BANNER) {
            BannerViewHolder viewHolder = (BannerViewHolder) holder;
            //设置Banner的数据
            viewHolder.setData(result.getBanner_info());
        } else if (getItemViewType(position) == CHANNEL) {
            ChannelViewHolder channelViewHolder = (ChannelViewHolder) holder;
            channelViewHolder.setData(result.getChannel_info());
        } else if (getItemViewType(position) == ACT) {
            ActViewHolder actViewHolder = (ActViewHolder) holder;
            actViewHolder.setData(result.getAct_info());
        } else if (getItemViewType(position) == SECKILL) {
            SeckillViewHolder seckillViewHolder = (SeckillViewHolder) holder;
            Log.e("Test", "SECKILL===" + position);
            seckillViewHolder.setData(result.getSeckill_info());
        } else if (getItemViewType(position) == RECOMMEND) {
            RecommendViewHolder recommendViewHolder = (RecommendViewHolder) holder;
            recommendViewHolder.setData(result.getRecommend_info());
        } else if (getItemViewType(position) == HOT) {
            HotViewHolder hotViewHolder = (HotViewHolder) holder;
            hotViewHolder.setData(result.getHot_info());
        }
    }

    class HotViewHolder extends RecyclerView.ViewHolder {

        private final Context mContext;
        private TextView tvMoreHot;
        private GridView gvHot;

        private HotGridViewAdapter adapter;

        public HotViewHolder(Context mContext, View itemView) {
            super(itemView);
            this.mContext = mContext;
            tvMoreHot = (TextView) itemView.findViewById(R.id.tv_more_hot);
            gvHot = (GridView) itemView.findViewById(R.id.gv_hot);

            //创建构造方法
            adapter = new HotGridViewAdapter(mContext);
            gvHot.setAdapter(adapter);
        }

        public void setData(final List<HomeBean.ResultBean.HotInfoBean> hot_info) {
            //1.已经有数据
            //2.设置GridView的适配器

            adapter.setData(hot_info);

            //3.设置item的点击事件
            gvHot.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    HomeBean.ResultBean.HotInfoBean infoBean = hot_info.get(position);
                    //商品或者产品，货物
                    GoodsBean goodsBean = new GoodsBean();
                    goodsBean.setProduct_id(infoBean.getProduct_id());//产品id
                    goodsBean.setCover_price(infoBean.getCover_price());//价格
                    goodsBean.setName(infoBean.getName());//名称
                    goodsBean.setFigure(infoBean.getFigure());//图片地址

                    Intent intent = new Intent(mContext, GoodsInfoActivity.class);
                    intent.putExtra(GOODS_BEAN, goodsBean);
                    mContext.startActivity(intent);
                }
            });
        }
    }

    class RecommendViewHolder extends RecyclerView.ViewHolder {

        private final Context mContext;
        @BindView(R.id.tv_more_recommend)
        TextView tvMoreRecommend;
        @BindView(R.id.gv_recommend)
        GridView gvRecommend;
        RecommendGridViewAdapter adapter;

        public RecommendViewHolder(Context mContext, View itemView) {
            super(itemView);

            this.mContext = mContext;

            ButterKnife.bind(this, itemView);
        }

        public void setData(final List<HomeBean.ResultBean.RecommendInfoBean> recommend_info) {
            //1.已经有数据
            //2.设置适配器了
            adapter = new RecommendGridViewAdapter(mContext, recommend_info);
            gvRecommend.setAdapter(adapter);
            //3.设置item的点击事件
            gvRecommend.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    //根据位置取对应的数据
                    HomeBean.ResultBean.RecommendInfoBean infoBean = recommend_info.get(position);

                    //商品或者产品，货物
                    GoodsBean goodsBean = new GoodsBean();
                    goodsBean.setProduct_id(infoBean.getProduct_id());//产品id
                    goodsBean.setCover_price(infoBean.getCover_price());//价格
                    goodsBean.setName(infoBean.getName());//名称
                    goodsBean.setFigure(infoBean.getFigure());//图片地址

                    Intent intent = new Intent(mContext, GoodsInfoActivity.class);
                    intent.putExtra(GOODS_BEAN, goodsBean);
                    mContext.startActivity(intent);
                }
            });
        }
    }

    /**
     * 是否是第一次启动倒计时
     */
    private boolean isFrist = false;

    class SeckillViewHolder extends RecyclerView.ViewHolder {

        private final Context mContext;
        @BindView(R.id.countdownview)
        CountdownView countdownview;
        @BindView(R.id.tv_more_seckill)
        TextView tvMoreSeckill;
        @BindView(R.id.rv_seckill)
        RecyclerView rvSeckill;

        SeckillRecyclerViewAdapter adapter;

        Handler mHandler = new Handler();
        HomeBean.ResultBean.SeckillInfoBean seckillInfo;

        /**
         * 开始刷新
         */
        void startRefreshTime() {
            mHandler.postDelayed(mRefreshTimeRunnable, 1);
        }

        Runnable mRefreshTimeRunnable = new Runnable() {
            @Override
            public void run() {
                //得到当前时间
                long currentTime = System.currentTimeMillis();

                if (currentTime >= Long.parseLong(seckillInfo.getEnd_time())) {
                    // 倒计时结束
                    mHandler.removeCallbacksAndMessages(null);
                } else {
                    //更新时间
                    countdownview.updateShow(Long.parseLong(seckillInfo.getEnd_time()) - currentTime);
                    //每隔1000毫秒更新一次
                    mHandler.postDelayed(mRefreshTimeRunnable, 1000);
                }
            }
        };


        public SeckillViewHolder(Context mContext, View itemView) {
            super(itemView);

            this.mContext = mContext;

            ButterKnife.bind(this, itemView);
        }

        public void setData(final HomeBean.ResultBean.SeckillInfoBean seckill_info) {

            this.seckillInfo = seckill_info;
            //1.已经有数据
            //2.设置RecyclerView的适配器
            adapter = new SeckillRecyclerViewAdapter(mContext, seckill_info);
            rvSeckill.setAdapter(adapter);
            //布局管理器
            rvSeckill.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));

            //3.设置item的点击事件
            //接口
            //回调
            adapter.setOnItemClickListener(new SeckillRecyclerViewAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {

                    HomeBean.ResultBean.SeckillInfoBean.ListBean infoBean = seckill_info.getList().get(position);
                    GoodsBean goodsBean = new GoodsBean();
                    goodsBean.setProduct_id(infoBean.getProduct_id());
                    goodsBean.setCover_price(infoBean.getCover_price());
                    goodsBean.setFigure(infoBean.getFigure());
                    goodsBean.setName(infoBean.getName());
                    Intent intent = new Intent(mContext, GoodsInfoActivity.class);
                    intent.putExtra(GOODS_BEAN, goodsBean);
                    mContext.startActivity(intent);
                }
            });

            if (!isFrist) {
                isFrist = true;
                //计算倒计时持续的时间
                long totalTime = Long.parseLong(seckill_info.getEnd_time()) - Long.parseLong(seckill_info.getStart_time());

                // 校对倒计时
                long curTime = System.currentTimeMillis();
                //重新设置结束数据时间
                seckillInfo.setEnd_time((curTime + totalTime + ""));

                //开始刷新
                startRefreshTime();
            }
        }
    }

    class ActViewHolder extends RecyclerView.ViewHolder {
        private final Context mContext;
        @BindView(R.id.act_viewpager)
        ViewPager actViewpager;
        ViewPagerAdapter adapter;

        public ActViewHolder(Context mContext, View itemView) {
            super(itemView);
            this.mContext = mContext;
            ButterKnife.bind(this, itemView);
        }

        public void setData(final List<HomeBean.ResultBean.ActInfoBean> act_info) {
            //1.准备数据了
            //2.设置适配器
            adapter = new ViewPagerAdapter(mContext, act_info);
            actViewpager.setAdapter(adapter);

            //美化ViewPager
            actViewpager.setPageMargin(DensityUtil.dip2px(mContext, 20));
            actViewpager.setOffscreenPageLimit(3);//>=3

            //setPageTransformer 决定动画效果
            actViewpager.setPageTransformer(true, new ScaleInTransformer());

            //3.设置item的点击事件
            adapter.setOnItemClickListener(new ViewPagerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {

                    HomeBean.ResultBean.ActInfoBean actInfoBean = act_info.get(position);
                    //Bean对象
                    WebViewBean webViewBean = new WebViewBean();
                    webViewBean.setUrl(actInfoBean.getUrl());
                    webViewBean.setName(actInfoBean.getName());
                    webViewBean.setIcon_url(actInfoBean.getIcon_url());

                    Intent intent = new Intent(mContext, WebViewActivity.class);
                    intent.putExtra(WEBVIEW_BEAN, webViewBean);
                    mContext.startActivity(intent);
                }
            });
        }
    }

    class ChannelViewHolder extends RecyclerView.ViewHolder {
        private Context mContext;
        @BindView(R.id.gv_channel)
        GridView gvChannel;
        ChannelAdapter adapter;

        public ChannelViewHolder(Context mContext, View itemView) {
            super(itemView);

            this.mContext = mContext;
            ButterKnife.bind(this, itemView);
        }

        /**
         * 设置数据
         * @param channel_info
         */
        public void setData(List<HomeBean.ResultBean.ChannelInfoBean> channel_info) {

            //1.已经有数据
            //2.设置适配器
            adapter = new ChannelAdapter(mContext, channel_info);
            gvChannel.setAdapter(adapter);

            //3.设置item点击事件
            gvChannel.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    if(position <=8){
                        Intent intent  = new Intent(mContext, GoodsListActivity.class);
                        intent.putExtra("position",position);
                        mContext.startActivity(intent);
                    }
                }
            });
        }
    }

    class BannerViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.banner)
        Banner banner;

        public BannerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(final List<HomeBean.ResultBean.BannerInfoBean> banner_info) {

            //准备图片集合
            List<String> imageUrls = new ArrayList<>();
            for (int i = 0; i < banner_info.size(); i++) {
                imageUrls.add(NetManager.BASE_URL_IMAGE + banner_info.get(i).getImage());
            }

            //简单使用
            banner.setImages(imageUrls)
                    .setImageLoader(new ImageLoader() {
                        @Override
                        public void displayImage(Context context, Object path, ImageView imageView) {
                            Glide.with(context)
                                    .load(path)
                                    .crossFade()
                                    .into(imageView);
                        }
                    })
                    .start();

            //设置动画效果-手风琴效果
            banner.setBannerAnimation(AccordionTransformer.class);

            //设置点击事件
            banner.setOnBannerClickListener(new OnBannerClickListener() {
                @Override
                public void OnBannerClick(int position) {
                    int realPostion = position - 1;

                    if (realPostion < banner_info.size()) {
                        String product_id = "";
                        String name = "";
                        String cover_price = "";
                        String image = banner_info.get(realPostion).getImage();
                        if (realPostion == 0) {
                            product_id = "627";
                            cover_price = "32.00";
                            name = "剑三T恤批发";
                        } else if (realPostion == 1) {
                            product_id = "21";
                            cover_price = "8.00";
                            name = "同人原创】剑网3 剑侠情缘叁 Q版成男 口袋胸针";
                        } else {
                            product_id = "1341";
                            cover_price = "50.00";
                            name = "【蓝诺】《天下吾双》 剑网3同人本";
                        }

                        //商品或者产品，货物
                        GoodsBean goodsBean = new GoodsBean();
                        goodsBean.setProduct_id(product_id);//产品id
                        goodsBean.setCover_price(cover_price);//价格
                        goodsBean.setName(name);//名称
                        goodsBean.setFigure(image);//图片地址

                        Intent intent = new Intent(mContext, GoodsInfoActivity.class);
                        intent.putExtra(GOODS_BEAN, goodsBean);
                        mContext.startActivity(intent);
                    }
                }
            });
        }
    }
}
