package com.atguigu.shop.controller.shoppingcart.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.atguigu.shop.R;
import com.atguigu.shop.controller.shoppingcart.adapter.PaymentAdapter;
import com.atguigu.shop.model.cartbean.AddressBean;
import com.atguigu.shop.model.homebean.GoodsBean;
import com.atguigu.shop.controller.shoppingcart.pay.PayResult;
import com.atguigu.shop.controller.shoppingcart.pay.SignUtils;
import com.atguigu.shop.controller.shoppingcart.utils.CartStorage;
import com.atguigu.shop.view.NoScrollListView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PaymentActivtiy extends AppCompatActivity {

    @BindView(R.id.title_right)
    ImageView titleRight;
    @BindView(R.id.tvAddresseeName)
    TextView tvAddresseeName;
    @BindView(R.id.tvSmeAredAddress)
    TextView tvSmeAredAddress;
    @BindView(R.id.tvAddressNext)
    TextView tvAddressNext;
    @BindView(R.id.detailAddress)
    TextView detailAddress;
    @BindView(R.id.addressRelative)
    RelativeLayout addressRelative;
    @BindView(R.id.checkOutListView)
    NoScrollListView checkOutListView;
    @BindView(R.id.tvCheckOutAllPrice)
    TextView tvCheckOutAllPrice;
    @BindView(R.id.btnSureCheckOut)
    Button btnSureCheckOut;
    @BindView(R.id.tv_left_back)
    TextView tvLeftBack;
    @BindView(R.id.rg_paymode)
    RadioGroup rgPaymode;
    private ArrayList<AddressBean> addressList;
    private List<GoodsBean> listData;
    private PaymentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_activtiy);
        ButterKnife.bind(this);
        getAddressData();
        showAddressInfo(0);
        setData();
        showTotalPrice();
    }

    private void setData() {

        rgPaymode.check(R.id.alipay);
        listData = CartStorage.getInstance(this).getAllData();

        if (listData != null && listData.size() > 0) {
            adapter = new PaymentAdapter(this, listData);
            checkOutListView.setAdapter(adapter);
        }
    }

    public void showTotalPrice() {
        tvCheckOutAllPrice.setText("合计:" + getTotalPrice());
    }

    /**
     * 得到总价格
     *
     * @return
     */
    public double getTotalPrice() {
        double totalPrice = 0.0;

        if (listData != null && listData.size() > 0) {
            for (int i = 0; i < listData.size(); i++) {

                GoodsBean goodsBean = listData.get(i);
                if (goodsBean.isChecked()) {
                    //被选中的
                    totalPrice = totalPrice + goodsBean.getNumber() * Double.parseDouble(goodsBean.getCover_price());
                }
            }
        }

        return totalPrice;
    }

    private void getAddressData() {
        addressList = new ArrayList<AddressBean>();

        AddressBean bean = new AddressBean();
        bean.setName("杨先生");
        bean.setSmearedAddress("贵州省黎平县");
        bean.setDetailAddress("黎平一中  15527196048");
        addressList.add(bean);

        AddressBean bean2 = new AddressBean();
        bean2.setName("杨先生");
        bean2.setSmearedAddress("北京昌平");
        bean2.setDetailAddress("北京昌平xxx家园  18140549110");
        addressList.add(bean2);
    }

    //显示收货人姓名地址等信息
    private void showAddressInfo(int index) {

        AddressBean bean = addressList.get(index);
        tvAddresseeName.setText(bean.getName());
        tvSmeAredAddress.setText(bean.getSmearedAddress());
        detailAddress.setText(bean.getDetailAddress());
    }

    @OnClick({R.id.tv_left_back, R.id.tvAddressNext, R.id.btnSureCheckOut})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_left_back:
                finish();
                break;

            case R.id.tvAddressNext:
                 // 修改收货地址
                updateAddress();
                break;

            case R.id.btnSureCheckOut:
                int checkId = rgPaymode.getCheckedRadioButtonId();
                if (checkId == R.id.alipay) {
                    //支付
                    pay(null);
                } else if (checkId == R.id.weixing) {
                    //微信支付
                    Toast.makeText(this, "微信暂未完成", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private static int REQUESTCODE = 1;          //跳转请求码

    //修改地址
    private void updateAddress() {
        Intent intent = new Intent(PaymentActivtiy.this, UpdateAddressActivity.class);
        intent.putExtra("AddressLiist", addressList);
        startActivityForResult(intent, REQUESTCODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUESTCODE && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            int addressIndex = bundle.getInt("addressIndex");
            showAddressInfo(addressIndex);
        }
    }

    // 商户PID
    public static final String PARTNER = "2088911876712776";
    // 商户收款账号
    public static final String SELLER = "chenlei@atguigu.com";
    // 商户私钥，pkcs8格式
    public static final String RSA_PRIVATE = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAMpjX58B45FWljDZ\n" +
            "3MhEf/ZqJiYiXtGDwx8ne74jSSYztfdC67YKtnx6T0afJLNZxWY4MME3bwgJ366c\n" +
            "Ky2EypBI+uC50p8k0HfiMSjM0Uw5onQK+tpFr00cNJIU+MC1txS2cNGMnqa8jzUn\n" +
            "QMbQJ4nC/k5sZqG/hWJ31jyY1LUrAgMBAAECgYBQH0EmuN+3lPjGhClUm1GxYtqR\n" +
            "dD/nX+tqNP5XCq8V0ZjzN2oLWnheFSm3Qp+L5Tkxu4MJqwFoxEfqX2b0kUKDYYOj\n" +
            "VBLAQwYLb1otKyisIoNUyeihIW/Iuc0HDDEbuJLOyjp6fR3pZKzHBtbgbZKx/2sW\n" +
            "zqvVe8cXsow+bjGhkQJBAPicWMBTQjMBodH7eKBFdgGeXk5CVSKeCi7mjIzJd/QQ\n" +
            "+yM5bNa69gejOsao/9TO2+oLZ22WtW0APYNpLa4/XskCQQDQZ1MAWrQpU6HIrXf/\n" +
            "aoDJy/LLsd6KnONb6qR1P5NVpiVJcB8pbafmSOsi9FtgoVWQ6Kh68WP7zZBKtrdA\n" +
            "3CpTAkBvz8luvkNgs1Q3H3VyB6t6MOLTKawmhJOp4lDGJPpJD7YTcolLzgxHBRez\n" +
            "L2DYMizgBz7+H7D7FAeWaaB85M1ZAkBAaN63SlFKQIXM/wgKUcFSHQ2CNQwBrTF7\n" +
            "tA52Clsf6oS1qMiIxlJRExJRMFKZj+NhMPb5YGe1aDgDT2tdyW3vAkAP/SASgTwT\n" +
            "Dqem0cZenq/vcnuWEPldxssaJJwUXC2FgR9tEnrWYnhFY8f4VxKDxvuNnbUIiv3L\n" +
            "/coNQ57KGWRH";
    // 支付宝公钥
    public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDKY1+fAeORVpYw2dzIRH/2aiYm\n" +
            "Il7Rg8MfJ3u+I0kmM7X3Quu2CrZ8ek9GnySzWcVmODDBN28ICd+unCsthMqQSPrg\n" +
            "udKfJNB34jEozNFMOaJ0CvraRa9NHDSSFPjAtbcUtnDRjJ6mvI81J0DG0CeJwv5O\n" +
            "bGahv4Vid9Y8mNS1KwIDAQAB";
    private static final int SDK_PAY_FLAG = 1;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息

                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(PaymentActivtiy.this, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(PaymentActivtiy.this, "支付结果确认中", Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(PaymentActivtiy.this, "支付失败", Toast.LENGTH_SHORT).show();

                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };

    /**
     * call alipay sdk pay. 调用SDK支付
     */
    public void pay(View v) {
        if (TextUtils.isEmpty(PARTNER) || TextUtils.isEmpty(RSA_PRIVATE) || TextUtils.isEmpty(SELLER)) {
            new AlertDialog.Builder(PaymentActivtiy.this).setTitle("警告").setMessage("需要配置PARTNER | RSA_PRIVATE| SELLER")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {

//                            finish();
                        }
                    }).show();
            return;
        }

        String orderInfo = getOrderInfo("硅谷商城", "硅谷商城商品的详细描述", getTotalPrice() + "");

        /**
         * 特别注意，这里的签名逻辑需要放在服务端，切勿将私钥泄露在代码中！
         */
        String sign = sign(orderInfo);
        try {
            /**
             * 仅需对sign 做URL编码
             */
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        /**
         * 完整的符合支付宝参数规范的订单信息
         */
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + getSignType();

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                //子线程
                // 构造PayTask 对象
                PayTask alipay = new PayTask(PaymentActivtiy.this);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo, true);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * create the order info. 创建订单信息
     */
    private String getOrderInfo(String subject, String body, String price) {

        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + PARTNER + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo() + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + "http://notify.msp.hk/notify.htm" + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

        return orderInfo;
    }

    /**
     * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
     */
    private String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);

        Random r = new Random();
        key = key + r.nextInt();
        key = key.substring(0, 15);

        return key;
    }

    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param content 待签名订单信息
     */
    private String sign(String content) {
        return SignUtils.sign(content, RSA_PRIVATE);
    }

    /**
     * get the sign type we use. 获取签名方式
     */
    private String getSignType() {
        return "sign_type=\"RSA\"";
    }
}
