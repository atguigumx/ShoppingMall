package com.example.maxin.shoppingmall.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.maxin.shoppingmall.R;
import com.example.maxin.shoppingmall.utils.Constants;

public class CallCenterActivity extends AppCompatActivity {
    private WebView  webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_center);
        webView = (WebView)findViewById(R.id.webView);
        setWebView(Constants.CALLCENTER_WEB_URL);
    }

    private void setWebView(String url) {
        if(url!=null) {
            webView.loadUrl(url);
            WebSettings settings = webView.getSettings();
            settings.setJavaScriptEnabled(true);
            // 先使用缓存
            settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            webView.setWebViewClient(new WebViewClient(){
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                    //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                    view.loadUrl(request.getUrl().toString());
                    return true;
                }
            });

        }

    }
}
