package com.atguigu.shop.controller.type.fragment;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.atguigu.shop.R;
import com.atguigu.shop.base.BaseFragment;
import com.atguigu.shop.controller.type.adapter.TypeLeftAdapter;
import com.atguigu.shop.controller.type.adapter.TypeRightAdapter;
import com.atguigu.shop.model.typebean.TypeBean;
import com.atguigu.shop.model.NetManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * 作者：尚硅谷-杨光福 on 2016/12/24 09:41
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：ListFragmen
 */
public class ListFragment extends BaseFragment {
    @BindView(R.id.lv_left)
    ListView lvLeft;
    @BindView(R.id.rv_right)
    RecyclerView rvRight;

    private String[] titles = new String[]{"小裙子", "上衣", "下装", "外套", "配件", "包包", "装扮", "居家宅品",
            "办公文具", "数码周边", "游戏专区"};

    private String[] urls = new String[]{NetManager.SKIRT_URL, NetManager.JACKET_URL, NetManager.PANTS_URL, NetManager.OVERCOAT_URL,
            NetManager.ACCESSORY_URL, NetManager.BAG_URL, NetManager.DRESS_UP_URL, NetManager.HOME_PRODUCTS_URL, NetManager.STATIONERY_URL,
            NetManager.DIGIT_URL, NetManager.GAME_URL};

    private TypeLeftAdapter leftAdapter;
    private TypeRightAdapter rightAdapter;
    private List<TypeBean.ResultBean> result;

    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.fragment_list, null);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void initData() {
        super.initData();

        //设置ListView的适配器
        leftAdapter = new TypeLeftAdapter(mContext,titles);
        lvLeft.setAdapter(leftAdapter);

        //设置item的点击事件
        lvLeft.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //1.设置点击的位置
                leftAdapter.changeSelected(position);

                //2.刷新适配器--getView
                leftAdapter.notifyDataSetChanged();

                //根据位置得到不同的url，联网请求
                getDataFromNet(urls[position]);
            }
        });

        getDataFromNet(urls[0]);
    }

    /**
     * 根据url联网请求数据
     * @param url
     */
    private void getDataFromNet(String url) {

        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(mContext, "ListFragment联网失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        processData(response);
                    }
                });
    }

    private void processData(String response) {

        TypeBean typeBean = JSON.parseObject(response,TypeBean.class);
        result = typeBean.getResult();

        if(result != null && result.size() >0){
            //有数据
            rightAdapter = new TypeRightAdapter(mContext,result);
            rvRight.setAdapter(rightAdapter);

            //设置布局管理器
            GridLayoutManager manager = new GridLayoutManager(mContext,3);

            manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if(position ==0){
                        return  3;
                    }else{
                        return  1;
                    }
                }
            });

            //设置布局管理器
            rvRight.setLayoutManager(manager);
        }
    }
}
