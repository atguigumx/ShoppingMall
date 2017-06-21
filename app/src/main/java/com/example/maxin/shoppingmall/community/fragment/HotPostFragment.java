package com.example.maxin.shoppingmall.community.fragment;

import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.example.maxin.shoppingmall.R;
import com.example.maxin.shoppingmall.base.BaseFragment;
import com.example.maxin.shoppingmall.community.adapter.HotPostListViewAdapter;
import com.example.maxin.shoppingmall.community.bean.HotPostBean;
import com.example.maxin.shoppingmall.utils.Constants;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;

/**
 * Created by shkstart on 2017/6/17.
 */

public class HotPostFragment extends BaseFragment {
    @BindView(R.id.lv_hot_post)
    ListView lvHotPost;
    Unbinder unbinder;
    private HotPostListViewAdapter adapter;

    @Override
    public View initView() {
        View rootView = View.inflate(mContext, R.layout.fragment_hot_post, null);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void initData() {
        super.initData();
        getDataFromNet(Constants.HOT_POST_URL);
    }

    private void getDataFromNet(String url) {
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("TAG", "HotPostFragment联网失败" + e.getMessage());

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("TAG", "HotPostFragment联网成功" );
                        processData(response);

                    }


                });
    }

    private void processData(String response) {
        HotPostBean hotPostBean = JSON.parseObject(response, HotPostBean.class);
        adapter=new HotPostListViewAdapter(mContext,hotPostBean.getResult());
        lvHotPost.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
