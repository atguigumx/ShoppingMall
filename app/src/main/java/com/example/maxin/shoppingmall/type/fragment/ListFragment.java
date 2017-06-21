package com.example.maxin.shoppingmall.type.fragment;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.example.maxin.shoppingmall.R;
import com.example.maxin.shoppingmall.base.BaseFragment;
import com.example.maxin.shoppingmall.type.adapter.TypeLeftAdapter;
import com.example.maxin.shoppingmall.type.adapter.TypeRightAdapter;
import com.example.maxin.shoppingmall.type.bean.TypeBean;
import com.example.maxin.shoppingmall.utils.Constants;
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
    private TypeLeftAdapter leftAdapter;

    private String[] urls = new String[]{Constants.SKIRT_URL, Constants.JACKET_URL, Constants.PANTS_URL, Constants.OVERCOAT_URL,
            Constants.ACCESSORY_URL, Constants.BAG_URL, Constants.DRESS_UP_URL, Constants.HOME_PRODUCTS_URL, Constants.STATIONERY_URL,
            Constants.DIGIT_URL, Constants.GAME_URL};

    private List<TypeBean.ResultBean> result;
    private TypeRightAdapter rightAdapter;

    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.fragment_list, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        //设置适配器
        leftAdapter=new TypeLeftAdapter(mContext);
        lvLeft.setAdapter(leftAdapter);

        lvLeft.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //1.设置点击的位置
                leftAdapter.changeSelected(i);

                //2.刷新适配器--getView
                leftAdapter.notifyDataSetChanged();

                getDateFromNet(urls[i]);
            }
        });

        getDateFromNet(urls[0]);
    }

    private void getDateFromNet(String url) {
        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.e("TAG", "ListFragment联网失败" + e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
              //  Log.e("TAG", "ListFragment联网成功");
                processData(response);
            }
        });
    }

    private void processData(String response) {
        TypeBean typeBean = JSON.parseObject(response, TypeBean.class);
        result = typeBean.getResult();
        if(result!=null&&result.size()>0) {
            rightAdapter=new TypeRightAdapter(mContext,result);
            rvRight.setAdapter(rightAdapter);

            GridLayoutManager manager = new GridLayoutManager(mContext, 3);
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
            rvRight.setLayoutManager(manager);
        }

    }


}
