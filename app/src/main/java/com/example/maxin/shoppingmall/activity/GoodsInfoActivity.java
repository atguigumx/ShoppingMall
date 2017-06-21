package com.example.maxin.shoppingmall.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.maxin.shoppingmall.R;
import com.example.maxin.shoppingmall.home.bean.GoodsBean;
import com.example.maxin.shoppingmall.shoppingcart.utils.CartStorage;
import com.example.maxin.shoppingmall.shoppingcart.utils.VirtualkeyboardHeight;
import com.example.maxin.shoppingmall.shoppingcart.view.AddSubView;
import com.example.maxin.shoppingmall.utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.maxin.shoppingmall.MyApplication.getContext;
import static com.example.maxin.shoppingmall.home.adapter.HomeAdapter.GOODSBEAN;

public class GoodsInfoActivity extends AppCompatActivity {
    public static final String CHECK_ID = "checkId";
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
    private GoodsBean goodsBean;
    private boolean isExist=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_info);
        ButterKnife.bind(this);
        getData();
        setData();

    }

    private void setData() {
        String product_id = goodsBean.getProduct_id();
        tvGoodInfoName.setText(goodsBean.getName());
        tvGoodInfoPrice.setText("$"+goodsBean.getCover_price());
        Glide.with(this).load(Constants.BASE_URL_IMAGE+goodsBean.getFigure()).into(ivGoodInfoImage);

        //设置webView的数据
        setWebViewData(product_id);
    }

    private void setWebViewData(String product_id) {
        WebSettings webSettings = wbGoodInfoMore.getSettings();
        //设置支持js
        webSettings.setJavaScriptEnabled(true);
        //设置支持双击变大变小
        webSettings.setUseWideViewPort(true);

        //设置检索缓存的
        webSettings.setCacheMode(webSettings.LOAD_CACHE_ELSE_NETWORK);

        //设置不跳转到系统的浏览器
        wbGoodInfoMore.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    view.loadUrl(request.getUrl().toString());
                }
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });

        wbGoodInfoMore.loadUrl("http://mp.weixin.qq.com/s/Cf3DrW2lnlb-w4wYaxOEZg");
    }

    private void getData() {
        goodsBean= (GoodsBean) getIntent().getSerializableExtra(GOODSBEAN);
    }

    @OnClick({R.id.ib_good_info_back, R.id.ib_good_info_more, R.id.tv_good_info_callcenter, R.id.tv_good_info_collection, R.id.tv_good_info_cart, R.id.btn_good_info_addcart, R.id.tv_more_share, R.id.tv_more_search, R.id.tv_more_home, R.id.btn_more})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_good_info_back:
                finish();
                break;
            case R.id.ib_good_info_more:
//                Toast.makeText(GoodsInfoActivity.this, "更多", Toast.LENGTH_SHORT).show();
                llRoot.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_good_info_callcenter:

                startActivity(new Intent(getContext(),CallCenterActivity.class));
                break;
            case R.id.tv_good_info_collection:

                break;
            case R.id.tv_good_info_cart:
                // Toast.makeText(GoodsInfoActivity.this, "购物车", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.putExtra(CHECK_ID,R.id.rb_cart);
                startActivity(intent);
                break;
            case R.id.btn_good_info_addcart:
                /*CartStorage.getInstance(getContext()).addData(goodsBean);
                Toast.makeText(GoodsInfoActivity.this, "已经添加到购物车", Toast.LENGTH_SHORT).show();*/
                showPopwindow();  //弹出选择框 使用POpwindow实现
                break;
            case R.id.tv_more_share:
                Toast.makeText(GoodsInfoActivity.this, "分享", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_more_search:
                Toast.makeText(GoodsInfoActivity.this, "搜索", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_more_home:
               // Toast.makeText(GoodsInfoActivity.this, "主页", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(GoodsInfoActivity.this, MainActivity.class);
                startActivity(intent1);
                break;
            case R.id.btn_more:
//                Toast.makeText(GoodsInfoActivity.this, "退出", Toast.LENGTH_SHORT).show();
                llRoot.setVisibility(View.GONE);

                break;
        }
    }
    //显示Popwindow
    private void showPopwindow() {
        //tmpeGoodsBean = CartStorage.getInstance(getContext()).findDete(goodsBean.getProduct_id());
        /*if(tmpeGoodsBean == null){
            isExist = false;
            tmpeGoodsBean = goodsBean;
        }else {
            isExist  =true;
        }*/
        LayoutInflater inflater =
                (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view=inflater.inflate(R.layout.popupwindow_add_product,null);

        final PopupWindow window = new PopupWindow(view,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);


        // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
        window.setFocusable(true);

        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xFFFFFFFF);
        window.setBackgroundDrawable(dw);

        // 设置popWindow的显示和消失动画
       // window.setAnimationStyle(R.style.mypopwindow_anim_style);

        // 4 控件处理
        ImageView iv_goodinfo_photo = (ImageView) view.findViewById(R.id.iv_goodinfo_photo);
        TextView tv_goodinfo_name = (TextView) view.findViewById(R.id.tv_goodinfo_name);
        TextView tv_goodinfo_price = (TextView) view.findViewById(R.id.tv_goodinfo_price);
        AddSubView nas_goodinfo_num = (AddSubView) view.findViewById(R.id.nas_goodinfo_num);
        Button bt_goodinfo_cancel = (Button) view.findViewById(R.id.bt_goodinfo_cancel);
        Button bt_goodinfo_confim = (Button) view.findViewById(R.id.bt_goodinfo_confim);

        Glide.with(getContext()).load(Constants.BASE_URL_IMAGE+goodsBean.getFigure()).into(iv_goodinfo_photo);
        tv_goodinfo_name.setText(goodsBean.getName());
        tv_goodinfo_price.setText(goodsBean.getCover_price());
        nas_goodinfo_num.setMaxValue(100);
        goodsBean.setNumber(1);
        nas_goodinfo_num.setValue(goodsBean.getNumber());

        nas_goodinfo_num.setOnNumberChangeListener(new AddSubView.OnNumberChangeListener() {
            @Override
            public void numberChange(int value) {
                goodsBean.setNumber(value);
            }
        });

        bt_goodinfo_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //取消
                window.dismiss();
            }
        });

        bt_goodinfo_confim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //确定
                window.dismiss();

                CartStorage.getInstance(getContext()).addData(goodsBean);
                Toast.makeText(GoodsInfoActivity.this, "添加购物车成功", Toast.LENGTH_SHORT).show();
            }
        });

        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                window.dismiss();
            }
        });
        //在底部显示
        window.showAtLocation(GoodsInfoActivity.this.findViewById(R.id.ll_goods_root),
                Gravity.BOTTOM, 0, VirtualkeyboardHeight.getBottomStatusHeight(GoodsInfoActivity.this));
    }
}
