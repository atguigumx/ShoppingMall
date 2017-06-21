package com.atguigu.shop.controller.shoppingcart.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.atguigu.shop.R;
import com.atguigu.shop.controller.main.MainActivity;
import com.atguigu.shop.controller.shoppingcart.activity.PaymentActivtiy;
import com.atguigu.shop.base.BaseFragment;
import com.atguigu.shop.model.homebean.GoodsBean;
import com.atguigu.shop.controller.shoppingcart.adapter.ShoppingCartAdapter;
import com.atguigu.shop.controller.shoppingcart.utils.CartStorage;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 作者：尚硅谷-杨光福 on 2016/12/20 10:22
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：购物车Fragment
 */
public class ShoppingCartFragment extends BaseFragment {

    private static final String TAG = ShoppingCartFragment.class.getSimpleName();
    @BindView(R.id.tv_shopcart_edit)
    TextView tvShopcartEdit;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.checkbox_all)
    CheckBox checkboxAll;
    @BindView(R.id.tv_shopcart_total)
    TextView tvShopcartTotal;
    @BindView(R.id.btn_check_out)
    Button btnCheckOut;
    @BindView(R.id.ll_check_all)
    LinearLayout llCheckAll;
    @BindView(R.id.checkbox_delete_all)
    CheckBox checkboxDeleteAll;
    @BindView(R.id.btn_delete)
    Button btnDelete;
    @BindView(R.id.btn_collection)
    Button btnCollection;
    @BindView(R.id.ll_delete)
    LinearLayout llDelete;
    @BindView(R.id.iv_empty)
    ImageView ivEmpty;
    @BindView(R.id.tv_empty_cart_tobuy)
    TextView tvEmptyCartTobuy;
    @BindView(R.id.ll_empty_shopcart)
    LinearLayout llEmptyShopcart;

    ShoppingCartAdapter adapter;
    /**
     * 购物车里面的数据
     */
    private List<GoodsBean> datas;
    /**
     * 编辑
     */
    private static final  int ACTION_EDIT = 1;
    /***
     * 完成状态
     */
    private static final  int ACTION_COMPLETE = 2;

    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.fragment_shopping_cart, null);
        ButterKnife.bind(this, view);

        //编辑
        tvShopcartEdit.setText("编辑");
        tvShopcartEdit.setTag(ACTION_EDIT);
        tvShopcartEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int action = (int) tvShopcartEdit.getTag();//ACTION_EDIT
                if(action ==ACTION_EDIT){
                    //显示删除按钮--完成
                    showDelete();
                }else{
                    //隐藏删除按钮--编辑
                    hideDelete();
                }
            }
        });

        return view;
    }

    private void hideDelete() {
        tvShopcartEdit.setText("编辑");
        tvShopcartEdit.setTag(ACTION_EDIT);

        //显示结算布局
        llCheckAll.setVisibility(View.VISIBLE);
        //隐藏删除布局
        llDelete.setVisibility(View.GONE);

        if(adapter != null){
            //改为非勾选
            adapter.checkAll_none(true);
            adapter.checkAll();
            adapter.showTotalPrice();
        }
    }

    /**
     * 显示删除按钮-完成
     */
    private void showDelete() {
        tvShopcartEdit.setText("完成");
        tvShopcartEdit.setTag(ACTION_COMPLETE);

        //隐藏结算布局
        llCheckAll.setVisibility(View.GONE);

        //显示删除布局
        llDelete.setVisibility(View.VISIBLE);

        if(adapter != null){
            //改为非勾选
            adapter.checkAll_none(false);
            adapter.checkAll();
        }
    }

    @Override
    public void initData() {
        super.initData();
        Log.e(TAG, "购物车Fragment数据绑定了");
    }

    /**
     * 获得焦点的时候执行
     */
    @Override
    public void onResume() {
        super.onResume();

        hideDelete();//设置编辑状态

        datas = CartStorage.getInstance(mContext).getAllData();

        if(datas != null && datas.size() >0){
            //有数据-设置适配器
            adapter  = new ShoppingCartAdapter(mContext,datas,checkboxAll,tvShopcartTotal,checkboxDeleteAll);
            recyclerview.setAdapter(adapter);
            //设置布局管理器
            recyclerview.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
            //把显示没有数据的页面-隐藏
            llEmptyShopcart.setVisibility(View.GONE);

            //设置item的点击事件
            MyOnItemClickListener itemClickListener = new MyOnItemClickListener();
            adapter.setOnItemClickListener(itemClickListener);

            //默认校验
            adapter.checkAll();
        }else{
            //没有数据
            //把显示没有数据的页面-显示
            llEmptyShopcart.setVisibility(View.VISIBLE);
        }
    }

    class MyOnItemClickListener implements ShoppingCartAdapter.OnItemClickListener{

        @Override
        public void onItemClick(View view, int position) {
            //1.得到CheckBox的状态
            GoodsBean  goodsBean = datas.get(position);
            boolean isChecked = goodsBean.isChecked();
            //2.设置状态取反-设置Bean对象中
            goodsBean.setChecked(!isChecked);

            //3.显示总价格
            adapter.showTotalPrice();
            //4.刷新适配器
            adapter.notifyItemChanged(position);

            //5.校验全选CheckBox和删除的CheckBox
            adapter.checkAll();
        }
    }

    @OnClick({R.id.tv_shopcart_edit, R.id.checkbox_all, R.id.btn_check_out, R.id.checkbox_delete_all, R.id.btn_delete, R.id.btn_collection, R.id.tv_empty_cart_tobuy})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.tv_shopcart_edit:
                Toast.makeText(mContext, "编辑", Toast.LENGTH_SHORT).show();
                break;

            case R.id.checkbox_all:
                //1.得到CheckBox的状态:true和false
                boolean isChecked = checkboxAll.isChecked();

                //2.把列表中所有的数据搜都设置true或者fals
                adapter.checkAll_none(isChecked);

                //3.刷新适配器

                //4.重新计算总价格
                adapter.showTotalPrice();
                break;

            case R.id.btn_check_out:
//                pay(view);
                Intent intent = new Intent(mContext, PaymentActivtiy.class);
                startActivity(intent);
                break;

            case R.id.checkbox_delete_all:
                 isChecked = checkboxDeleteAll.isChecked();
                //2.把列表中所有的数据搜都设置true或者fals
                adapter.checkAll_none(isChecked);

                //3.刷新适配器

                //4.重新计算总价格
                adapter.showTotalPrice();
                break;

            case R.id.btn_delete:
                adapter.deleteData();
                //显示空数据
                adapter.checkAll();
                //显示空页面
                showEempty();
                break;

            case R.id.btn_collection:
                Toast.makeText(mContext, "收藏", Toast.LENGTH_SHORT).show();
                break;

            case R.id.tv_empty_cart_tobuy:
                intent = new Intent(mContext, MainActivity.class);
                intent.putExtra("checkedid",R.id.rb_home);//跳转到主页面
                startActivity(intent);
                break;
        }
    }

    private void showEempty() {

        if(datas == null || datas.size()==0){
            llEmptyShopcart.setVisibility(View.VISIBLE);
        }
    }
}
