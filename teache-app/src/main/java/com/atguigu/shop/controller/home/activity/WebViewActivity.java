package com.atguigu.shop.controller.home.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.atguigu.shop.R;
import com.atguigu.shop.controller.home.adapter.HomeAdapter;
import com.atguigu.shop.model.homebean.GoodsBean;
import com.atguigu.shop.model.homebean.H5Bean;
import com.atguigu.shop.model.homebean.WebViewBean;
import com.atguigu.shop.model.NetManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.atguigu.shop.controller.home.adapter.HomeAdapter.GOODS_BEAN;

public class WebViewActivity extends AppCompatActivity {

    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ib_more)
    ImageButton ibMore;
    @BindView(R.id.webview)
    WebView webview;

    @BindView(R.id.progressbar)
    ProgressBar progressbar;
    @BindView(R.id.activity_web_view)
    LinearLayout activityWebView;

    private WebViewBean webViewBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_web_view);
        ButterKnife.bind(this);
        getData();
        setData();
    }

    /**
     * 设置数据
     */
    private void setData() {

        //设置标题
        tvTitle.setText(webViewBean.getName());

        //设置加载网页

        //1.设置支持js
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);//设置支持js
        webSettings.setUseWideViewPort(true);//设置用户双击单击
        webSettings.setBuiltInZoomControls(true);

        //添加js接口,调用java
        webview.addJavascriptInterface(new MyJavascriptInterface(),"cyc");

        //2.设置客户端监听
        webview.setWebViewClient(new WebViewClient(){
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
        webview.loadUrl(NetManager.BASE_URL_IMAGE+webViewBean.getUrl());
    }

    class MyJavascriptInterface{

        @JavascriptInterface
        public void jumpForAndroid(String json){
            if(!TextUtils.isEmpty(json)){
                H5Bean h5Bean = JSON.parseObject(json,H5Bean.class);

                GoodsBean goodsBean = new GoodsBean();

                //产品id
                goodsBean.setProduct_id(h5Bean.getValue().getProduct_id()+"");

                //传递价格
                goodsBean.setCover_price("10080");
                //设置图片
                goodsBean.setFigure("http://android.atguigu.com/pic/logo.jpg");
                //设置名称
                goodsBean.setName("尚硅谷Android");
                Intent intent = new Intent(WebViewActivity.this, GoodsInfoActivity.class);
                intent.putExtra(GOODS_BEAN,goodsBean);
                startActivity(intent);
            }
        }
    }

    /**
     * 得到数据
     */
    private void getData() {
        webViewBean = (WebViewBean) getIntent().getSerializableExtra(HomeAdapter.WEBVIEW_BEAN);
    }

    @OnClick({R.id.ib_back, R.id.ib_more})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.ib_back:
                finish();
                break;

            case R.id.ib_more:
                Toast.makeText(this, "更多", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
