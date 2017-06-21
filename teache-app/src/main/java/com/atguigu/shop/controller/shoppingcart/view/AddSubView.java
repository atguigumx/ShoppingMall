package com.atguigu.shop.controller.shoppingcart.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.TintTypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.atguigu.shop.R;

/**
 * 作者：尚硅谷-杨光福 on 2016/12/21 15:55
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：自定义曾减按钮
 */
public class AddSubView extends LinearLayout implements View.OnClickListener {
    private final Context mContext;
    private ImageView iv_sub;
    private ImageView iv_add;
    private TextView tv_value;

    private int value = 1;
    private int minValue = 1;
    private int maxValue = 10;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
        //设置文本
        tv_value.setText(value+"");
    }

    public int getMinValue() {
        return minValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public AddSubView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        //加载布局--把布局实例化成View,并且认AddSubView.this做父层视图
        View.inflate(context, R.layout.add_sub_view,AddSubView.this);
        iv_sub = (ImageView) findViewById(R.id.iv_sub);
        iv_add = (ImageView) findViewById(R.id.iv_add);
        tv_value = (TextView) findViewById(R.id.tv_value);

        //设置点击事件
        iv_sub.setOnClickListener(this);
        iv_add.setOnClickListener(this);

        if(attrs != null){
            //取出属性
            TintTypedArray tintTypedArray = TintTypedArray.obtainStyledAttributes(context, attrs, R.styleable.AddSubView);
            int value = tintTypedArray.getInt(R.styleable.AddSubView_value, 0);
            if (value > 0) {//来自自定义属性
                setValue(value);
            }
            int minValue = tintTypedArray.getInt(R.styleable.AddSubView_minValue, 0);
            if (value > 0) {
                setMinValue(minValue);
            }
            int maxValue = tintTypedArray.getInt(R.styleable.AddSubView_maxValue, 0);
            if (value > 0) {
                setMaxValue(maxValue);
            }
            Drawable addDrawable = tintTypedArray.getDrawable(R.styleable.AddSubView_numberAddBackground);
            if (addDrawable != null) {
                iv_add.setImageDrawable(addDrawable);
            }
            Drawable subDrawable = tintTypedArray.getDrawable(R.styleable.AddSubView_numberSubBackground);
            if (subDrawable != null) {
                iv_sub.setImageDrawable(subDrawable);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.iv_sub){
            //减
            subNumber();
        }else if(v.getId()==R.id.iv_add){
            addNumber();
        }
    }

    /**
     * 减
     */
    private void subNumber() {

        if(value > minValue){
            value--;
        }

        setValue(value);

        if(listener != null){
            listener.numberChange(value);
        }
    }

    /**
     * 加
     */
    private void addNumber() {

        if(value < maxValue){
            value++;
        }

        setValue(value);

        if(listener != null){
            //MyOnNumberChangeListener的实例，里面有numberChange
            listener.numberChange(value);
        }
    }

    public interface OnNumberChangeListener {
        /**
         *  当按钮被点击的时候回调
         */
        public void numberChange(int value);
    }

    //MyOnNumberChangeListener的实例
    private OnNumberChangeListener listener;

    /**
     * 设置数字变化的监听
     * @param l
     */
    public void setOnNumberChangeListener(OnNumberChangeListener l){//l是MyOnNumberChangeListener的实例
        this.listener = l;
    }
}
