package com.example.maxin.shoppingmall.community.fragment;

import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.example.maxin.shoppingmall.R;
import com.example.maxin.shoppingmall.base.BaseFragment;
import com.example.maxin.shoppingmall.community.adapter.NewPostListViewAdapter;
import com.example.maxin.shoppingmall.community.bean.NewPostBean;
import com.example.maxin.shoppingmall.utils.Constants;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * Created by shkstart on 2017/6/17.
 */

public class NewPostFragment extends BaseFragment {
    @BindView(R.id.lv_new_post)
    ListView lvNewPost;
    private List<NewPostBean.ResultBean> result;
    private NewPostListViewAdapter adapter;

    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.fragment_new_post, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        getDataFromNet(Constants.NEW_POST_URL);
    }

    private void getDataFromNet(String url) {
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("TAG", "NewPostFragment联网失败" + e.getMessage());

                    }

                    @Override
                    public void onResponse(String response, int id) {
                       // Log.e("TAG", "NewPostFragment联网成功" + response);
                        processData(response);

                    }


                });
    }

    private void processData(String response) {
        NewPostBean newPostBean = JSON.parseObject(response,NewPostBean.class);
        result = newPostBean.getResult();
        if(result != null && result.size() >0){
            //设置适配器
            adapter = new NewPostListViewAdapter(mContext,result);
            lvNewPost.setAdapter(adapter);

        }
    }
}
