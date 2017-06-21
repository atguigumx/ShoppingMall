package com.example.maxin.shoppingmall.type.fragment;

import android.view.View;
import android.widget.GridView;

import com.alibaba.fastjson.JSON;
import com.example.maxin.shoppingmall.R;
import com.example.maxin.shoppingmall.base.BaseFragment;
import com.example.maxin.shoppingmall.type.adapter.TagGridViewAdapter;
import com.example.maxin.shoppingmall.type.bean.TagBean;
import com.example.maxin.shoppingmall.utils.Constants;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;

/**
 * Created by shkstart on 2017/6/15.
 */

public class TagFragment extends BaseFragment {


    @BindView(R.id.gv_tag)
    GridView gvTag;
    Unbinder unbinder;
    private TagGridViewAdapter adapter;
    private List<TagBean.ResultBean> result;

    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.fragment_tag, null);
        unbinder = ButterKnife.bind(this, view);
        getDataFromNet();
        return view;
    }

    public void getDataFromNet() {
        OkHttpUtils
                .get()
                .url(Constants.TAG_URL)
                .id(100)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        switch (id) {
                            case 100:
                                if (response != null) {
                                    processData(response);
                                    adapter = new TagGridViewAdapter(mContext, result);
                                    gvTag.setAdapter(adapter);
                                }
                                break;
                    }
                }
                });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     * 解析json数据
     *
     * @param json
     */
    private void processData(String json) {
        TagBean tagBean = JSON.parseObject(json, TagBean.class);
        result = tagBean.getResult();
    }


    @Override
    public void initData() {
        super.initData();

    }
}
