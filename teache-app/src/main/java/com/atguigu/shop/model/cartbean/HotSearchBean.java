package com.atguigu.shop.model.cartbean;

import java.util.List;

/**
 * 作者：尚硅谷-杨光福 on 2017/1/10 23:54
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：搜索Bean对象
 */
public class HotSearchBean {
    /**
     * code : 200
     * msg : 请求成功
     * result : [{"url":"","title":"lolita"},{"url":"","title":"Honest"},{"url":"","title":"人"},{"url":"","title":"画影"},{"url":"","title":"风荷举"},{"url":"","title":"剑三"},{"url":"","title":"鹤归"},{"url":"","title":"烟昔泠"},{"url":"","title":"鲛人歌"},{"url":"","title":"举"},{"url":"","title":"风荷"}]
     */

    private int code;
    private String msg;
    private List<ResultBean> result;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * url :
         * title : lolita
         */
        private String url;
        private String title;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
