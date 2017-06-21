package com.atguigu.shop.utils.zxing.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atguigu.shop.R;
import com.atguigu.shop.controller.home.activity.GoodsInfoActivity;
import com.atguigu.shop.controller.home.adapter.HomeAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 自己写
 * 处理扫描结果
 * 1.展示
 * 2.跳转
 */
public class ScanResultActivity extends AppCompatActivity {

    @BindView(R.id.scan_webview)
    WebView scanWebview;
    @BindView(R.id.pb_loading)
    ProgressBar pbLoading;
    @BindView(R.id.tv_isloading)
    TextView tvIsloading;
    @BindView(R.id.iv_error)
    ImageView ivError;
    @BindView(R.id.tv_error)
    TextView tvError;
    @BindView(R.id.ibn_back)
    ImageButton ibnBack;
    @BindView(R.id.activity_sacn_result)
    RelativeLayout activitySacnResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_result);
        ButterKnife.bind(this);
        getData();

    }

    @OnClick(R.id.ibn_back)
    public void onClick() {
        finish();
    }


    private WebSettings mSetting;

    protected void getData() {

        String content = this.getIntent().getBundleExtra("data").getString("result");//http://xxxxxxxxsllllsl.png 图片地址
        if (content == null || TextUtils.isEmpty(content) || !content.startsWith("http://") || !content.startsWith("https://")) {
            Intent intent = new Intent(this, GoodsInfoActivity.class);
            intent.putExtra(HomeAdapter.FIGURE, content);
            startActivity(intent);
            finish();
            return;
        } else if (content.startsWith("http://") || content.startsWith("https://")) {
            mSetting = scanWebview.getSettings();
            mSetting.setJavaScriptEnabled(true);
            mSetting.setBuiltInZoomControls(true);

            scanWebview.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    tvIsloading.setVisibility(View.GONE);
                    ivError.setVisibility(View.GONE);
                    pbLoading.setVisibility(View.GONE);
                    tvError.setVisibility(View.GONE);
                    scanWebview.setVisibility(View.VISIBLE);
                }
            });

            scanWebview.loadUrl(content);
        }
    }

}
