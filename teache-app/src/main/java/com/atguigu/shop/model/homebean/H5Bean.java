package com.atguigu.shop.model.homebean;

/**
 * 作者：尚硅谷-杨光福 on 2016/12/21 14:27
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：js调用java的数据
 */
public class H5Bean {

    /**
     * option : 1
     * type : 1
     * value : {"product_id":9707}
     */

    private int option;
    private int type;
    private ValueBean value;

    public int getOption() {
        return option;
    }

    public void setOption(int option) {
        this.option = option;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public ValueBean getValue() {
        return value;
    }

    public void setValue(ValueBean value) {
        this.value = value;
    }

    public static class ValueBean {
        /**
         * product_id : 9707
         */

        private int product_id;

        public int getProduct_id() {
            return product_id;
        }

        public void setProduct_id(int product_id) {
            this.product_id = product_id;
        }
    }
}
