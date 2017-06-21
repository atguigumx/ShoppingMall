package com.example.maxin.shoppingmall.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.maxin.shoppingmall.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.ib_login_back)
    ImageButton ibLoginBack;
    @BindView(R.id.et_login_phone)
    EditText etLoginPhone;
    @BindView(R.id.et_login_pwd)
    EditText etLoginPwd;
    @BindView(R.id.ib_login_visible)
    ImageButton ibLoginVisible;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.tv_login_register)
    TextView tvLoginRegister;
    @BindView(R.id.tv_login_forget_pwd)
    TextView tvLoginForgetPwd;
    @BindView(R.id.ib_weibo)
    ImageButton ibWeibo;
    @BindView(R.id.ib_qq)
    ImageButton ibQq;
    @BindView(R.id.ib_wechat)
    ImageButton ibWechat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.ib_login_back, R.id.btn_login, R.id.tv_login_register, R.id.tv_login_forget_pwd, R.id.ib_weibo, R.id.ib_qq, R.id.ib_wechat})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_login_back:
                finish();
                break;
            case R.id.btn_login:
                break;
            case R.id.tv_login_register:
                break;
            case R.id.tv_login_forget_pwd:
                break;
            case R.id.ib_weibo:
                break;
            case R.id.ib_qq:
                break;
            case R.id.ib_wechat:
                break;
        }
    }
    private boolean isShowPassword;
    @OnClick(R.id.ib_login_visible)
    public void onViewClicked() {
        isShowPassword = !isShowPassword;
        if(isShowPassword){
            //显示
            ibLoginVisible.setBackgroundResource(R.drawable.new_password_drawable_visible);
            etLoginPwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            //光标定位到末尾
            etLoginPwd.setSelection(etLoginPwd.length());

        }else{
            //隐藏
            ibLoginVisible.setBackgroundResource(R.drawable.new_password_drawable_invisible);
            etLoginPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            //光标定位到末尾
            etLoginPwd.setSelection(etLoginPwd.length());
        }

    }
}
