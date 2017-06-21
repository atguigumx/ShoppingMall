package com.atguigu.shop.controller.shoppingcart.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.SparseArray;

import com.atguigu.shop.model.homebean.GoodsBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：尚硅谷-杨光福 on 2016/12/21 14:52
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：存储购物信息
 */
public class CartStorage {

    private static final String JSON_CART = "json_cart";
    private static CartStorage instance;
    private final Context mContext;
    /**
     * 相当于HashMap集合，性能好于HashMap
     */
    private SparseArray<GoodsBean> sparseArray;

    private CartStorage(Context context) {
        this.mContext = context;
        sparseArray = new SparseArray<>();
        //从本地加载-->List-->SparseArray
        listToSparseArray();
    }

    /**
     * List-->SparseArray
     */
    private void listToSparseArray() {

        List<GoodsBean> list = getAllData();

        for (int i = 0; i < list.size(); i++) {
            GoodsBean goodsBean = list.get(i);
            sparseArray.put(Integer.parseInt(goodsBean.getProduct_id()), goodsBean);
        }
    }

    /**
     * 得到本地保存所有数据
     * 从本地的json数组-->List
     *
     * @return
     */
    public List<GoodsBean> getAllData() {

        //从本地加载-->List
        List<GoodsBean> list = new ArrayList<>();
        //本地
        String savejson = CacheUtils.getString(mContext, JSON_CART);

        if (!TextUtils.isEmpty(savejson)) {
            //解析json数据
            list = new Gson().fromJson(savejson, new TypeToken<List<GoodsBean>>() {
            }.getType());
        }

        return list;
    }

    /**
     * @param context
     * @return
     */
    public static CartStorage getInstance(Context context) {

        if (instance == null) {

            synchronized (CartStorage.class) {
                if (instance == null) {
                    instance = new CartStorage(context);
                }
            }
        }

        return instance;
    }

    /**
     * 添加数据
     *
     * @param goodsBean
     */
    public void addData(GoodsBean goodsBean) {

        //添加数据
        GoodsBean temp = sparseArray.get(Integer.parseInt(goodsBean.getProduct_id()));

        if (temp != null) {
            //有数据
            temp.setNumber(temp.getNumber() + goodsBean.getNumber());
        } else {
            //列表中还不存在，没有数据
            temp = goodsBean;
            temp.setNumber(1);
        }

        //添加到内存中
        sparseArray.put(Integer.parseInt(temp.getProduct_id()), temp);

        //保存数据到本地
        commit();
    }

    /**
     * 删除数据
     *
     * @param goodsBean
     */
    public void deleteData(GoodsBean goodsBean) {
        //删除数据-内存
        sparseArray.delete(Integer.parseInt(goodsBean.getProduct_id()));

        //保存数据到本地-把本地保持的删除
        commit();
    }

    /**
     * 修改数据
     *
     * @param goodsBean
     */
    public void updateData(GoodsBean goodsBean) {
        //修改数据
        sparseArray.put(Integer.parseInt(goodsBean.getProduct_id()), goodsBean);

        //保存数据到本地
        commit();
    }

    /**
     * SparseArray-->List-->json保存到本地
     */
    private void commit() {

        List<GoodsBean> list = sparseArrayToList();
        //List-->json保存到本地
        //把List转换成json字符串的数据
        String json = new Gson().toJson(list);
        CacheUtils.putString(mContext,JSON_CART,json);
    }

    /**
     * SparseArray-->List
     * @return
     */
    private List<GoodsBean> sparseArrayToList() {

        List<GoodsBean> list= new ArrayList<>();

        for (int i = 0; i < sparseArray.size(); i++) {

            GoodsBean goodsBean = sparseArray.valueAt(i);
            list.add(goodsBean);
        }

        return list;
    }

    /**
     * 查找数据-购物车是否存在该商品
     * @param product_id
     * @return
     */
    public GoodsBean findDete(String product_id) {
        GoodsBean goodsBean = sparseArray.get(Integer.parseInt(product_id));
        return goodsBean;
    }
}
