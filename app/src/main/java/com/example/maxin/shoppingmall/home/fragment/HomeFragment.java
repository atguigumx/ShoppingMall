package com.example.maxin.shoppingmall.home.fragment;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.maxin.shoppingmall.R;
import com.example.maxin.shoppingmall.base.BaseFragment;
import com.example.maxin.shoppingmall.home.adapter.HomeAdapter;
import com.example.maxin.shoppingmall.home.bean.HomeBean;
import com.example.maxin.shoppingmall.utils.Constants;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.CaptureActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by shkstart on 2017/6/11.
 */

public class HomeFragment extends BaseFragment {
    @BindView(R.id.tv_search_home)
    TextView tvSearchHome;
    @BindView(R.id.tv_message_home)
    TextView tvMessageHome;
    @BindView(R.id.rv_home)
    RecyclerView rvHome;
    @BindView(R.id.ib_top)
    ImageButton ibTop;
    private HomeBean.ResultBean resultBean;
    private HomeAdapter adapter;


    @Override
    public View initView() {
        Log.e("TAG","主页视图被初始化了");
        View view = View.inflate(mContext, R.layout.fragment_home, null);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void initData() {
        super.initData();
        getDataFromNet();
    }

    private void getDataFromNet() {

       OkHttpUtils
                .get()
                .url(Constants.HOME_URL)
                .id(100)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("TAG","onError"+e.getMessage());
                        Toast.makeText(mContext, "请求网络失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        //Log.e("TAG","onResponse");
                        processDate(response);
                    }
                });
    }


    

    @OnClick({R.id.tv_search_home, R.id.tv_message_home, R.id.ib_top})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_search_home:
                Toast.makeText(mContext, "搜索", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_message_home:
               // Toast.makeText(mContext, "进入消息中心",Toast.LENGTH_SHORT).show();
                customScan();
                break;
            case R.id.ib_top:
                rvHome.scrollToPosition(0);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(intentResult != null) {
            if(intentResult.getContents() == null) {
                Toast.makeText(mContext,"内容为空",Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(mContext,"扫描成功",Toast.LENGTH_LONG).show();
                // ScanResult 为 获取到的字符串
                String ScanResult = intentResult.getContents();
            }
        } else {
            super.onActivityResult(requestCode,resultCode,data);
        }
    }

    public void customScan(){
   /*     new IntentIntegrator(getActivity())
                .setOrientationLocked(false)
                .setCaptureActivity(CustomScanActivity.class) // 设置自定义的activity是CustomActivity
                .initiateScan(); // 初始化扫描*/
        startActivity(new Intent(mContext,CaptureActivity.class));
    }

    private void processDate(String response) {
        HomeBean homeBean = JSON.parseObject(response, HomeBean.class);
        resultBean = homeBean.getResult();

        if(resultBean != null){
            adapter = new HomeAdapter(mContext,resultBean);
            //设置RecyclerView的适配器
            rvHome.setAdapter(adapter);


            //设置布局管理器
            GridLayoutManager manager = new GridLayoutManager(mContext,1);
            //设置监听跨度
            manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if(position <=3){
                        //隐藏按钮
                        ibTop.setVisibility(View.GONE);
                    }else{
                        //显示按钮
                        ibTop.setVisibility(View.VISIBLE);
                    }
                    return 1;//这个只能是1
                }
            });
            rvHome.setLayoutManager(manager);



        }else{
            Toast.makeText(mContext, "没有请求到数据", Toast.LENGTH_SHORT).show();
        }
    }
}
