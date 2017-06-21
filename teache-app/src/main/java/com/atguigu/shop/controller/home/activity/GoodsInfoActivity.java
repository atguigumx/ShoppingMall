package com.atguigu.shop.controller.home.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.atguigu.shop.R;
import com.atguigu.shop.controller.home.adapter.HomeAdapter;
import com.atguigu.shop.controller.main.MainActivity;
import com.atguigu.shop.model.homebean.GoodsBean;
import com.atguigu.shop.model.homebean.HomeBean;
import com.atguigu.shop.controller.shoppingcart.utils.CartStorage;
import com.atguigu.shop.controller.shoppingcart.utils.VirtualkeyboardHeight;
import com.atguigu.shop.controller.shoppingcart.view.AddSubView;
import com.atguigu.shop.model.NetManager;
import com.atguigu.shop.utils.zxing.activity.CreateQRCodeActivity;
import com.bumptech.glide.Glide;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
// 商品详情
public class GoodsInfoActivity extends AppCompatActivity {

    @BindView(R.id.ib_good_info_back)
    ImageButton ibGoodInfoBack;
    @BindView(R.id.ib_good_info_more)
    ImageButton ibGoodInfoMore;
    @BindView(R.id.iv_good_info_image)
    ImageView ivGoodInfoImage;
    @BindView(R.id.tv_good_info_name)
    TextView tvGoodInfoName;
    @BindView(R.id.tv_good_info_desc)
    TextView tvGoodInfoDesc;
    @BindView(R.id.tv_good_info_price)
    TextView tvGoodInfoPrice;
    @BindView(R.id.tv_good_info_store)
    TextView tvGoodInfoStore;
    @BindView(R.id.tv_good_info_style)
    TextView tvGoodInfoStyle;
    @BindView(R.id.wb_good_info_more)
    WebView wbGoodInfoMore;
    @BindView(R.id.tv_good_info_callcenter)
    TextView tvGoodInfoCallcenter;
    @BindView(R.id.tv_good_info_collection)
    TextView tvGoodInfoCollection;
    @BindView(R.id.tv_good_info_cart)
    TextView tvGoodInfoCart;
    @BindView(R.id.btn_good_info_addcart)
    Button btnGoodInfoAddcart;
    @BindView(R.id.ll_goods_root)
    LinearLayout llGoodsRoot;
    @BindView(R.id.tv_more_share)
    TextView tvMoreShare;
    @BindView(R.id.tv_more_search)
    TextView tvMoreSearch;
    @BindView(R.id.tv_more_home)
    TextView tvMoreHome;
    @BindView(R.id.btn_more)
    Button btnMore;
    @BindView(R.id.ll_root)
    LinearLayout llRoot;
    @BindView(R.id.progressbar)
    ProgressBar progressbar;
    private GoodsBean goodsBean;

    //缓存数据
    private GoodsBean tmpeGoodsBean;
    private String shareUrl;
    /**
     * 得到主页数据
     */
    private HomeBean.ResultBean result;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_info);
        ButterKnife.bind(this);
        getData();//接收数据

    }

    /**
     * 绑定数据到ui上
     */
    private void setData() {
        String cover_price = goodsBean.getCover_price();
        String figure = goodsBean.getFigure();
        String name = goodsBean.getName();
        String product_id = goodsBean.getProduct_id();

        if (figure.contains("http")) {
            Glide.with(GoodsInfoActivity.this).load(figure).into(ivGoodInfoImage);
        } else {
            //设置图片
            Glide.with(GoodsInfoActivity.this).load(NetManager.BASE_URL_IMAGE + figure).into(ivGoodInfoImage);
        }

        //设置发货信息
        tvGoodInfoStore.setText(goodsBean.getSeller());
        //设置名称
        tvGoodInfoName.setText(name);
        //设置价格
        tvGoodInfoPrice.setText("￥" + cover_price);

        //设置webView显示
        setWebViewData(product_id);

    }

    /**
     * 加载网页
     *
     * @param product_id
     */
    private void setWebViewData(String product_id) {

        //1.设置支持js
        WebSettings webSettings = wbGoodInfoMore.getSettings();
        webSettings.setJavaScriptEnabled(true);//设置支持js
        webSettings.setUseWideViewPort(true);//设置用户双击单击
        webSettings.setBuiltInZoomControls(true);

        //2.设置客户端监听
        wbGoodInfoMore.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    view.loadUrl(request.getUrl().toString());
                }
                return true;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressbar.setVisibility(View.GONE);
            }
        });

        //3.加载路径
        wbGoodInfoMore.loadUrl("http://mp.weixin.qq.com/s/Cf3DrW2lnlb-w4wYaxOEZg");


    }

    /**
     * 得到数据
     */
    private void getData() {
        goodsBean = (GoodsBean) getIntent().getSerializableExtra(HomeAdapter.GOODS_BEAN);
        //得到图片地址
        shareUrl = getIntent().getStringExtra(HomeAdapter.FIGURE);

        if (!TextUtils.isEmpty(shareUrl)) {
            //是扫描进来的
            goodsBean = new GoodsBean();
            goodsBean.setFigure(shareUrl);//图片地址
            setShareData();

        }else{
            //默认正常点击
            setData();
        }

    }

    private void setShareData() {
        /**
         * 重新联网请求数据
         */
        OkHttpUtils
                .get()
                .url(NetManager.HOME_URL)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("TAG", "服务器异常,请重试" + e.getMessage());

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("TAG", "联网成功");
                        processData(response);
                        findGoodsBean();
                        setData();
                    }


                });
    }

    /**
     * 解析数据
     * @param response
     */
    private void processData(String response) {

        HomeBean homeBean = JSON.parseObject(response, HomeBean.class);
        //得到resultBean的数据
        result = homeBean.getResult();

    }

    /**
     * 所有的数据中查找对应的url
     */
    private void findGoodsBean() {
        List<HomeBean.ResultBean.BannerInfoBean> act_info = result.getBanner_info();
        for (int i = 0; i < act_info.size(); i++) {
            if (act_info.get(i).getImage().equals(shareUrl)) {
                if (i == 0) {
                    goodsBean.setName("尚硅谷在线课堂");
                    goodsBean.setCover_price("320.00");
                    goodsBean.setProduct_id("627");
                } else if (i == 1) {
                    goodsBean.setName("尚硅谷抢座");
                    goodsBean.setCover_price("800.00");
                    goodsBean.setProduct_id("21");
                } else if (i == 2) {
                    goodsBean.setName("尚硅谷讲座");
                    goodsBean.setCover_price("150.00");
                    goodsBean.setProduct_id("1341");
                }

            }
        }
        List<HomeBean.ResultBean.HotInfoBean> hot_info = result.getHot_info();
        for (int i = 0; i < hot_info.size(); i++) {
            if (hot_info.get(i).getFigure().equals(shareUrl)) {
                goodsBean.setName(hot_info.get(i).getName());
                goodsBean.setCover_price(hot_info.get(i).getCover_price());
                goodsBean.setProduct_id(hot_info.get(i).getProduct_id());
            }
        }
        List<HomeBean.ResultBean.RecommendInfoBean> recommend_info = result.getRecommend_info();
        for (int i = 0; i < recommend_info.size(); i++) {
            if (recommend_info.get(i).getFigure().equals(shareUrl)) {
                goodsBean.setName(recommend_info.get(i).getName());
                goodsBean.setCover_price(recommend_info.get(i).getCover_price());
                goodsBean.setProduct_id(recommend_info.get(i).getProduct_id());
            }
        }
        HomeBean.ResultBean.SeckillInfoBean seckill_info = result.getSeckill_info();
        List<HomeBean.ResultBean.SeckillInfoBean.ListBean> seckill_infoList = seckill_info.getList();
        for (int i = 0; i < seckill_infoList.size(); i++) {
            if (seckill_infoList.get(i).getFigure().equals(shareUrl)) {
                goodsBean.setName(seckill_infoList.get(i).getName());
                goodsBean.setCover_price(seckill_infoList.get(i).getCover_price());
                goodsBean.setProduct_id(seckill_infoList.get(i).getProduct_id());
            }
        }
    }


    @OnClick({R.id.ib_good_info_back, R.id.ib_good_info_more, R.id.tv_good_info_callcenter, R.id.tv_good_info_collection, R.id.tv_good_info_cart, R.id.btn_good_info_addcart, R.id.tv_more_share, R.id.tv_more_search, R.id.tv_more_home, R.id.btn_more})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_good_info_back:
                finish();
                break;
            case R.id.ib_good_info_more:
//                Toast.makeText(this, "更多", Toast.LENGTH_SHORT).show();
                if (llRoot.getVisibility() == View.VISIBLE) {
                    llRoot.setVisibility(View.GONE);
                } else {
                    llRoot.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.tv_good_info_callcenter:
                Intent intent = new Intent(this, CallCenterActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_good_info_collection:
                Toast.makeText(this, "收藏", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_good_info_cart:
                intent = new Intent(GoodsInfoActivity.this, MainActivity.class);
                intent.putExtra("checkedid", R.id.rb_cart);//跳转到主页面
                startActivity(intent);
                break;
            case R.id.btn_good_info_addcart:
//                Toast.makeText(this, "添加商品到购物车", Toast.LENGTH_SHORT).show();
//                CartStorage cartStorage =  CartStorage.getInstance(GoodsInfoActivity.this);
//                cartStorage.addData(goodsBean);
                //简单写法
//                CartStorage.getInstance(GoodsInfoActivity.this).addData(goodsBean);
                showPopwindow();
                break;
            case R.id.tv_more_share:
//               //把图片生成--生成二维码
                intent = new Intent(this, CreateQRCodeActivity.class);
                intent.putExtra(HomeAdapter.FIGURE, goodsBean.getFigure());
                startActivity(intent);
                break;
            case R.id.tv_more_search:
//                Toast.makeText(this, "搜索", Toast.LENGTH_SHORT).show();
                 intent = new Intent(GoodsInfoActivity.this, SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_more_home:
//                Toast.makeText(this, "跳转到主页", Toast.LENGTH_SHORT).show();
                intent = new Intent(this, MainActivity.class);
                intent.putExtra("checkedid", R.id.rb_home);//跳转到主页面
                startActivity(intent);
                finish();
                break;
            case R.id.btn_more:
//                Toast.makeText(this, "隐藏更多的布局", Toast.LENGTH_SHORT).show();
                llRoot.setVisibility(View.GONE);
                break;
        }
    }

    private boolean isExist = false;

    /**
     * 显示popupWindow
     */
    private void showPopwindow() {

        //查找是否存在
        tmpeGoodsBean = CartStorage.getInstance(this).findDete(goodsBean.getProduct_id());
        if (tmpeGoodsBean == null) {
            //之前在购物车里面没有
            isExist = false;
            tmpeGoodsBean = goodsBean;
        } else {
            isExist = true;
        }


        // 1 利用layoutInflater获得View
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popupwindow_add_product, null);

        // 2下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()
        final PopupWindow window = new PopupWindow(view,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);

        // 3 参数设置
        // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
        window.setFocusable(true);

        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xFFFFFFFF);
        window.setBackgroundDrawable(dw);

        // 设置popWindow的显示和消失动画
        window.setAnimationStyle(R.style.mypopwindow_anim_style);


        // 4 控件处理
        ImageView iv_goodinfo_photo = (ImageView) view.findViewById(R.id.iv_goodinfo_photo);
        TextView tv_goodinfo_name = (TextView) view.findViewById(R.id.tv_goodinfo_name);
        TextView tv_goodinfo_price = (TextView) view.findViewById(R.id.tv_goodinfo_price);
        AddSubView nas_goodinfo_num = (AddSubView) view.findViewById(R.id.nas_goodinfo_num);
        Button bt_goodinfo_cancel = (Button) view.findViewById(R.id.bt_goodinfo_cancel);
        Button bt_goodinfo_confim = (Button) view.findViewById(R.id.bt_goodinfo_confim);

        // 加载图片
        Glide.with(GoodsInfoActivity.this).load(NetManager.BASE_URL_IMAGE + goodsBean.getFigure()).into(iv_goodinfo_photo);

        // 名称
        tv_goodinfo_name.setText(tmpeGoodsBean.getName());
        // 显示价格
        tv_goodinfo_price.setText(tmpeGoodsBean.getCover_price());

        // 设置最大值和当前值
        nas_goodinfo_num.setMaxValue(100);//库存100
        nas_goodinfo_num.setValue(tmpeGoodsBean.getNumber());


        nas_goodinfo_num.setOnNumberChangeListener(new AddSubView.OnNumberChangeListener() {
            @Override
            public void numberChange(int value) {
                tmpeGoodsBean.setNumber(value);
            }
        });

        bt_goodinfo_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
            }
        });

        if (!isExist && tmpeGoodsBean.getNumber() == 1) {
            Toast.makeText(GoodsInfoActivity.this, "请选择您要购买的数量", Toast.LENGTH_SHORT).show();
        }
        bt_goodinfo_confim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();

                //添加购物车
                CartStorage.getInstance(GoodsInfoActivity.this).updateData(tmpeGoodsBean);
                Log.e("TAG", "66:" + tmpeGoodsBean.toString());

            }
        });

        window.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                window.dismiss();
            }
        });

        // 5 在底部显示
        window.showAtLocation(GoodsInfoActivity.this.findViewById(R.id.ll_goods_root),
                Gravity.BOTTOM, 0, VirtualkeyboardHeight.getBottomStatusHeight(GoodsInfoActivity.this));

    }

}
