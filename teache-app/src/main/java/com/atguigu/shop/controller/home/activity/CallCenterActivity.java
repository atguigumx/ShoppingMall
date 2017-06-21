package com.atguigu.shop.controller.home.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.atguigu.shop.R;

import butterknife.BindView;
import butterknife.ButterKnife;

// 客服
public class CallCenterActivity extends AppCompatActivity {

    @BindView(R.id.webview)
    WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_center);
        ButterKnife.bind(this);

        //设置加载网页

        //1.设置支持js
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);//设置支持js
        webSettings.setUseWideViewPort(true);//设置用户双击单击
        webSettings.setBuiltInZoomControls(true);

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
            }
        });

        //3.加载路径
        webview.loadUrl("http://www6.53kf.com/webCompany.php?arg=10007377&style=2&kflist=off&kf=info@atguigu.com,video@atguigu.com,public@atguigu.com,3069368606@qq.com,215648937@qq.com,sudan@atguigu.com,sszhang@atguigu.com&zdkf_type=1&language=zh-cn&charset=gbk&referer=http%3A%2F%2Fatguigu.com%2F&keyword=&tfrom=1&tpl=crystal_blue&uid=94453f39187416b3d434b974b8dccc0c&timeStamp=1482482675132&ucust_id=");
    }
}
