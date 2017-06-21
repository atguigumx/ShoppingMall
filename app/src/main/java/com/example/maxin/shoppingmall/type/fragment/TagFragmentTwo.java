package com.example.maxin.shoppingmall.type.fragment;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.example.maxin.shoppingmall.base.BaseFragment;

/**
 * Created by shkstart on 2017/6/16.
 */

public class TagFragmentTwo extends BaseFragment {
    private TextView textView;
    @Override
    public View initView() {
        textView=new TextView(mContext);
        return textView;
    }

    @Override
    public void initData() {
        super.initData();
        textView.setText("TagFragment");
        textView.setTextColor(Color.RED);
    }
}
