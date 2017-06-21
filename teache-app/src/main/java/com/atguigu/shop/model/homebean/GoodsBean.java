package com.atguigu.shop.model.homebean;

import java.io.Serializable;

/**
 * 作者：尚硅谷-杨光福 on 2016/12/21 11:25
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：商品信息
 */
public class GoodsBean implements Serializable {


    /**
     * cover_price : 159.00
     * figure : /1477984921265.jpg
     * name : 现货【一方尘寰】剑侠情缘三剑三七秀 干将莫邪 90橙武仿烧蓝复古对簪
     * product_id : 9356
     */

    private String cover_price;
    private String figure;
    private String name;
    /**
     * 唯一标识
     */
    private String product_id;
    /**
     * 在购物车的购买数量
     */
    private int number = 1;

    /**
     * 默认是选择
     */
    private boolean isChecked = true;

    private String seller = "尚硅谷";

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getCover_price() {
        return cover_price;
    }

    public void setCover_price(String cover_price) {
        this.cover_price = cover_price;
    }

    public String getFigure() {
        return figure;
    }

    public void setFigure(String figure) {
        this.figure = figure;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    @Override
    public String toString() {
        return "GoodsBean{" +
                "cover_price='" + cover_price + '\'' +
                ", figure='" + figure + '\'' +
                ", name='" + name + '\'' +
                ", product_id='" + product_id + '\'' +
                ", number=" + number +
                ", isChecked=" + isChecked +
                ", seller='" + seller + '\'' +
                '}';
    }
}
