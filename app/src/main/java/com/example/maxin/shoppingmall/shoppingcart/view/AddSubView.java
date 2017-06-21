package com.example.maxin.shoppingmall.shoppingcart.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.maxin.shoppingmall.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.maxin.shoppingmall.R.id.tv_value;

/**
 * Created by shkstart on 2017/6/13.
 */

public class AddSubView extends LinearLayout {
    @BindView(R.id.iv_sub)
    ImageView iv_sub;
    @BindView(tv_value)
    TextView tvValue;
    @BindView(R.id.iv_add)
    ImageView iv_add;
    private Context mContext;
    private int value = 1;
    private int minValue = 1;
    private int maxValue = 10;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
        //设置文本
        tvValue.setText(value+"");
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
        View view = View.inflate(mContext, R.layout.add_sub_view, AddSubView.this);
        ButterKnife.bind(this,view);

        /*if(attrs != null){
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
        }*/
    }

    @OnClick({R.id.iv_sub, R.id.iv_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_sub:
                subNumber();
                break;
            case R.id.iv_add:
                addNumber();
                break;
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
