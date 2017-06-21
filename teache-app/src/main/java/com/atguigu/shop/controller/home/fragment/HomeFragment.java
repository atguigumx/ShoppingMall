package com.atguigu.shop.controller.home.fragment;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.atguigu.shop.R;
import com.atguigu.shop.controller.home.activity.SearchActivity;
import com.atguigu.shop.base.BaseFragment;
import com.atguigu.shop.controller.home.adapter.HomeAdapter;
import com.atguigu.shop.model.homebean.HomeBean;
import com.atguigu.shop.model.NetManager;
import com.atguigu.shop.utils.zxing.activity.CaptureActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 作者：尚硅谷-杨光福 on 2016/12/20 10:22
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：主页Fragment
 */
public class HomeFragment extends BaseFragment {

    private static final String TAG = HomeFragment.class.getSimpleName();
    @BindView(R.id.tv_search)
    TextView tvSearchHome;
    @BindView(R.id.rv_home)
    RecyclerView rvHome;
    @BindView(R.id.ib_top)
    ImageButton ibTop;

    /**
     * 请求后得到的数据
     */
    private HomeBean.ResultBean result;

    private HomeAdapter adapter;

    @Override
    public View initView() {
        Log.e(TAG, "主页Fragment初始化ui了");
        View view = View.inflate(mContext, R.layout.fragment_home, null);
        //让当前类和视图绑定起来
        ButterKnife.bind(HomeFragment.this, view);

        return view;
    }

    @Override
    public void initData() {
        super.initData();
        Log.e(TAG, "主页Fragment数据绑定了");
        getDataFromNet();
    }

    private void getDataFromNet() {
        //主页路径
        String url = NetManager.HOME_URL;

        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("TAG", "主页联网失败" + e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("TAG", "主页联网成功");
                        processData(response);
                    }
                });
    }

    private void processData(String response) {
        HomeBean homeBean = paraseJson(response);
        result = homeBean.getResult();

        if(result != null){
            adapter = new HomeAdapter(mContext,result);

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

    /**
     * 使用fastjson解决json数据
     * @param response
     * @return
     */
    private HomeBean paraseJson(String response) {
        return JSON.parseObject(response, HomeBean.class);
    }

    @OnClick({R.id.ll_main_scan,R.id.tv_search,  R.id.ib_top})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_search:
                Intent intent = new Intent(mContext, SearchActivity.class);
                startActivity(intent);
                break;

            case R.id.ib_top:
                rvHome.scrollToPosition(0);//滚动到第0条位置
                break;

            case R.id.ll_main_scan://打开扫描
                openScan();
                break;
        }
    }

    /**
     * 打开二维码扫描
     */
    private void openScan() {
        startActivityForResult(new Intent(getActivity(), CaptureActivity.class), 0);
    }

    /**
     * 提高屏幕亮度
     */
    private void config() {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.screenBrightness = 1.0f;
        getActivity().getWindow().setAttributes(lp);
    }
}
