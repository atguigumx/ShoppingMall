package com.example.maxin.shoppingmall.type.fragment;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.example.maxin.shoppingmall.R;
import com.example.maxin.shoppingmall.base.BaseFragment;
import com.example.maxin.shoppingmall.type.adapter.TypeLeftAdapter;
import com.example.maxin.shoppingmall.type.adapter.TypeRightAdapterTwo;
import com.example.maxin.shoppingmall.type.bean.TypeBean;
import com.example.maxin.shoppingmall.utils.Constants;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;

/**
 * Created by shkstart on 2017/6/16.
 */

public class ListFragmentTwo extends BaseFragment {
    @BindView(R.id.lv_left)
    ListView lvLeft;
    @BindView(R.id.rv_right)
    RecyclerView rvRight;
    Unbinder unbinder;
    private String[] urls = new String[]{Constants.SKIRT_URL, Constants.JACKET_URL, Constants.PANTS_URL, Constants.OVERCOAT_URL,
            Constants.ACCESSORY_URL, Constants.BAG_URL, Constants.DRESS_UP_URL, Constants.HOME_PRODUCTS_URL, Constants.STATIONERY_URL,
            Constants.DIGIT_URL, Constants.GAME_URL};
    private TypeLeftAdapter adapter;
    private List<TypeBean.ResultBean> result;
    private TypeRightAdapterTwo rightAdapter;

    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.fragment_list, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void initData() {
        adapter=new TypeLeftAdapter(mContext);
        lvLeft.setAdapter(adapter);

        lvLeft.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                adapter.changeSelected(i);
                adapter.notifyDataSetChanged();

                getDataFromNet(urls[i]);
            }
        });
        getDataFromNet(urls[0]);
    }

    private void getDataFromNet(String url) {
        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                processData(response);
            }
        });
    }

    private void processData(String response) {
        TypeBean typeBean = JSON.parseObject(response, TypeBean.class);
        result=typeBean.getResult();
        if(result!=null&&result.size()>0) {
            rightAdapter=new TypeRightAdapterTwo(mContext,result);
            rvRight.setAdapter(rightAdapter);

            GridLayoutManager manager=new GridLayoutManager(mContext,3);
            manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if(position==0) {
                        return 3;
                    }else {
                        return 1;
                    }

                }
            });
            rvRight.setLayoutManager(manager);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
