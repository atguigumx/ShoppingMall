package com.example.maxin.shoppingmall.home.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.maxin.shoppingmall.R;
import com.example.maxin.shoppingmall.activity.GoodsInfoActivity;
import com.example.maxin.shoppingmall.activity.GoodsListActivity;
import com.example.maxin.shoppingmall.activity.WebViewActivity;
import com.example.maxin.shoppingmall.home.bean.GoodsBean;
import com.example.maxin.shoppingmall.home.bean.HomeBean;
import com.example.maxin.shoppingmall.home.view.NoScrollGridView;
import com.example.maxin.shoppingmall.utils.Constants;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.iwgang.countdownview.CountdownView;

/**
 * Created by shkstart on 2017/6/11.
 */

public class HomeAdapter extends RecyclerView.Adapter {

    //六种类型

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
    public static final String GOODSBEAN = "GoodsBean";
    public static final String WEBVIEW = "webView";
    public static final String POSITION = "position";
    public static final String FIGURE = "figure";
    /**
     * 当前类型
     */
    public int currentType = BANNER;

    private final Context mContext;
    private final HomeBean.ResultBean resultBean;
    private final LayoutInflater inflater;


    public HomeAdapter(Context mContext, HomeBean.ResultBean resultBean) {
        this.mContext = mContext;
        this.resultBean = resultBean;
        inflater = LayoutInflater.from(mContext);

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == BANNER) {
            return new BannerViewHolder(mContext, inflater.inflate(R.layout.banner_viewpager, null));
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

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == BANNER) {
            BannerViewHolder bannerViewHolder = (BannerViewHolder) holder;
            //设置数据Banner的数据
            bannerViewHolder.setData(resultBean.getBanner_info());
        } else if (getItemViewType(position) == CHANNEL) {
            ChannelViewHolder channelViewHolder = (ChannelViewHolder) holder;
            channelViewHolder.setData(resultBean.getChannel_info());
        } else if (getItemViewType(position) == ACT) {
            ActViewHolder actViewHolder = (ActViewHolder) holder;
            actViewHolder.setData(resultBean.getAct_info());
        } else if (getItemViewType(position) == SECKILL) {
            SeckillViewHolder seckillViewHolder = (SeckillViewHolder) holder;
            seckillViewHolder.setData(resultBean.getSeckill_info());
        } else if (getItemViewType(position) == RECOMMEND) {
            RecommendViewHolder recommendViewHolder = (RecommendViewHolder) holder;
            recommendViewHolder.setData(resultBean.getRecommend_info());
        } else if (getItemViewType(position) == HOT) {
            HotViewHolder hotViewHolder = (HotViewHolder) holder;
            hotViewHolder.setData(resultBean.getHot_info());
        }


    }


    @Override
    public int getItemCount() {
        return 6;
    }

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

    class HotViewHolder extends RecyclerView.ViewHolder {
        private HotGridViewAdapter adapter;
        @BindView(R.id.tv_more_hot)
        TextView tvMoreHot;
        @BindView(R.id.gv_hot)
        NoScrollGridView gvHot;
        private final Context mContext;

        public HotViewHolder(Context mContext, View itemView) {
            super(itemView);
            this.mContext = mContext;
            ButterKnife.bind(this, itemView);
        }


        public void setData(final List<HomeBean.ResultBean.HotInfoBean> hot_info) {
            //创建构造方法
            adapter = new HotGridViewAdapter(mContext, hot_info);
            gvHot.setAdapter(adapter);
            gvHot.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    HomeBean.ResultBean.HotInfoBean hotInfoBean = hot_info.get(i);

                    GoodsBean goodsBean = new GoodsBean();
                    goodsBean.setName(hotInfoBean.getName());
                    goodsBean.setProduct_id(hotInfoBean.getProduct_id());
                    goodsBean.setFigure(hotInfoBean.getFigure());
                    goodsBean.setCover_price(hotInfoBean.getCover_price());

                    Intent intent = new Intent(mContext, GoodsInfoActivity.class);
                    intent.putExtra(GOODSBEAN,goodsBean);
                    mContext.startActivity(intent);
                }
            });
        }

    }


    class SeckillViewHolder extends RecyclerView.ViewHolder {
        //是否是第一次启动倒计时
        private boolean isFrist = false;
        private final Context mContext;
        Handler mHandler = new Handler();
        @BindView(R.id.countdownview)
        CountdownView countdownview;
        @BindView(R.id.tv_more_seckill)
        TextView tvMoreSeckill;
        @BindView(R.id.rv_seckill)
        RecyclerView rvSeckill;
        private HomeBean.ResultBean.SeckillInfoBean seckillInfo;
        private SeckillRecyclerViewAdapter adapter;

        public SeckillViewHolder(Context mContext, View itemView) {
            super(itemView);
            this.mContext = mContext;
            ButterKnife.bind(this, itemView);
        }

        public void setData(final HomeBean.ResultBean.SeckillInfoBean seckill_info) {
            this.seckillInfo = seckill_info;
            adapter = new SeckillRecyclerViewAdapter(mContext, seckill_info.getList());
            rvSeckill.setAdapter(adapter);
            //设置RecyclerView的适配器
            rvSeckill.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));

            tvMoreSeckill.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(mContext, "并没有更多", Toast.LENGTH_SHORT).show();
                }
            });
            adapter.setOnItemClickListener(new SeckillRecyclerViewAdapter.OnItemClickListener() {
                @Override
                public void OnItemClick(int position) {
                    HomeBean.ResultBean.SeckillInfoBean.ListBean listBean = seckill_info.getList().get(position);
                    GoodsBean goodsBean = new GoodsBean();

                    goodsBean.setProduct_id(listBean.getProduct_id());
                    goodsBean.setName(listBean.getName());
                    goodsBean.setFigure(listBean.getFigure());
                    goodsBean.setCover_price(listBean.getCover_price());
                    Intent intent = new Intent(mContext, GoodsInfoActivity.class);
                    intent.putExtra(GOODSBEAN,goodsBean);
                    mContext.startActivity(intent);
                }
            });

            if (!isFrist) {
                isFrist = true;
                //计算倒计时持续的时间
                long totalTime = Long.parseLong(seckill_info.getEnd_time())
                        - Long.parseLong(seckill_info.getStart_time());
                // 校对倒计时
                long curTime = System.currentTimeMillis();
                //重新设置结束数据时间
                seckillInfo.setEnd_time((curTime + totalTime + ""));
                //开始刷新
                startRefreshTime();
            }
        }

        /**
         * 开始刷新
         */
        public void startRefreshTime() {
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

    }

    class RecommendViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_more_recommend)
        TextView tvMoreRecommend;
        @BindView(R.id.gv_recommend)
        GridView gvRecommend;
        private final Context mContext;
        private RecommendGridViewAdapter adapter;

        public RecommendViewHolder(Context mContext, View itemView) {
            super(itemView);
            this.mContext = mContext;
            ButterKnife.bind(this, itemView);
        }

        public void setData(final List<HomeBean.ResultBean.RecommendInfoBean> recommend_info) {
            adapter = new RecommendGridViewAdapter(mContext, recommend_info);
            gvRecommend.setAdapter(adapter);
            gvRecommend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    HomeBean.ResultBean.RecommendInfoBean bean = recommend_info.get(i);
                    GoodsBean goodsBean = new GoodsBean();

                    goodsBean.setName(bean.getName());
                    goodsBean.setCover_price(bean.getCover_price());
                    goodsBean.setFigure(bean.getFigure());
                    goodsBean.setProduct_id(bean.getProduct_id());

                    Intent intent = new Intent(mContext, GoodsInfoActivity.class);
                    intent.putExtra(GOODSBEAN,goodsBean);
                    mContext.startActivity(intent);
                }
            });
            tvMoreRecommend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(mContext, "没了", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    class ActViewHolder extends RecyclerView.ViewHolder {

        private final Context mContext;
        @BindView(R.id.banner_act)
        Banner banner;
        private ViewPagerAdapter adapter;

        public ActViewHolder(Context mContext, View itemView) {
            super(itemView);
            this.mContext = mContext;
            ButterKnife.bind(this, itemView);

        }

        public void setData(final List<HomeBean.ResultBean.ActInfoBean> act_info) {
            //设置循环指标点
            banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
            List<String> imageUris = new ArrayList<>();
            for (int i = 0; i < act_info.size(); i++) {
                imageUris.add(Constants.BASE_URL_IMAGE + act_info.get(i).getIcon_url());
            }
            //设置动画
            banner.setBannerAnimation(Transformer.CubeIn);
            //设置加载图片
            banner.setImages(imageUris).setImageLoader(new ImageLoader() {
                @Override
                public void displayImage(Context context, Object path, ImageView imageView) {
                    Glide.with(context)
                            .load(path)
                            .crossFade()
                            .into(imageView);
                }
            }).start();
            banner.setOnBannerListener(new OnBannerListener() {
                @Override
                public void OnBannerClick(int position) {
                    HomeBean.ResultBean.ActInfoBean actInfoBean = act_info.get(position);
                    Intent intent = new Intent(mContext, WebViewActivity.class);
                    intent.putExtra(WEBVIEW,actInfoBean);
                    mContext.startActivity(intent);
                }
            });
        }
    }

    class BannerViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.banner)
        Banner banner;
        private final Context mContext;

        public BannerViewHolder(Context mContext, View itemView) {
            super(itemView);
            this.mContext = mContext;
            ButterKnife.bind(this, itemView);

        }

        public void setData(final List<HomeBean.ResultBean.BannerInfoBean> banner_info) {
            //设置循环指标点
            banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
            //如果你想用自己项目的图片加载,那么----->自定义图片加载框架
            List<String> imageUris = new ArrayList<>();
            //  Log.e("TAG", Constants.BASE_URL_IMAGE +banner_info.get(0).getImage());
            for (int i = 0; i < banner_info.size(); i++) {
                imageUris.add(Constants.BASE_URL_IMAGE + banner_info.get(i).getImage());
            }

            //设置动画
            banner.setBannerAnimation(Transformer.Default);
            //设置加载图片
            banner.setImages(imageUris).setImageLoader(new ImageLoader() {
                @Override
                public void displayImage(Context context, Object path, ImageView imageView) {
                    Glide.with(context)
                            .load(path)
                            .crossFade()
                            .into(imageView);
                }
            }).start();
            //设置点击事件
            banner.setOnBannerListener(new OnBannerListener() {
                @Override
                public void OnBannerClick(int position) {
                    int realPosition = position - 1;
                    if (realPosition < banner_info.size()) {
                        String product_id = "";
                        String name = "";
                        String cover_price = "";
                        if (realPosition == 0) {
                            product_id = "627";
                            cover_price = "32.00";
                            name = "剑三T恤批发";
                        } else if (realPosition == 1) {
                            product_id = "21";
                            cover_price = "8.00";
                            name = "同人原创】剑网3 剑侠情缘叁 Q版成男 口袋胸针";
                        } else {
                            product_id = "1341";
                            cover_price = "50.00";
                            name = "【蓝诺】《天下吾双》 剑网3同人本";
                        }
                        String image = banner_info.get(position).getImage();
                        GoodsBean goodsBean = new GoodsBean();
                        goodsBean.setName(name);
                        goodsBean.setCover_price(cover_price);
                        goodsBean.setFigure(image);
                        goodsBean.setProduct_id(product_id);

                        Intent intent = new Intent(mContext, GoodsInfoActivity.class);
                        intent.putExtra(GOODSBEAN, goodsBean);
                        mContext.startActivity(intent);}
                }

            });

        }
    }

    class ChannelViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.gv_channel)
        GridView gvChannel;
        private final Context mContext;
        private ChannelAdapter adapter;

        public ChannelViewHolder(Context mContext, View itemView) {
            super(itemView);
            this.mContext = mContext;
            ButterKnife.bind(this, itemView);
        }

        public void setData(List<HomeBean.ResultBean.ChannelInfoBean> channel_info) {
            //1.已经有数据
            //2.设置适配器
            adapter = new ChannelAdapter(mContext, channel_info);
            gvChannel.setAdapter(adapter);
            //3.设置item点击事件
            gvChannel.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if(position<=8) {
                        Intent intent = new Intent(mContext, GoodsListActivity.class);
                        intent.putExtra(POSITION,position);
                        mContext.startActivity(intent);
                    }
                }
            });
        }
    }


}
