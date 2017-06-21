package com.atguigu.shop.controller.user;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.atguigu.shop.R;
import com.atguigu.shop.model.NetManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

// 注册页面
public class RegistActivity extends Activity {

    @BindView(R.id.ib_login_back)
    ImageButton ibLoginBack;
    @BindView(R.id.et_regist_name)
    EditText etRegistName;
    @BindView(R.id.et_regist_phone)
    EditText etRegistPhone;
    @BindView(R.id.et_regist_pwd)
    EditText etRegistPwd;
    @BindView(R.id.et_regist_pwdagain)
    EditText etRegistPwdagain;
    @BindView(R.id.btn_regist)
    Button btnRegist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_regist);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.ib_login_back)
    public void back(View view){
        finish();
    }

    @OnClick(R.id.btn_regist)
    public void regist(View view){

        String name = etRegistName.getText().toString().trim();
        String phone = etRegistPhone.getText().toString().trim();
        String pwd = etRegistPwd.getText().toString().trim();
        String pwdAgain = etRegistPwdagain.getText().toString().trim();

        if(TextUtils.isEmpty(name) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(pwd) || TextUtils.isEmpty(pwdAgain)){
            Toast.makeText(RegistActivity.this, "输入的信息不能为空", Toast.LENGTH_SHORT).show();
        }else if(!pwd.equals(pwdAgain)){
            Toast.makeText(RegistActivity.this, "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
            etRegistPwd.setText("");
            etRegistPwdagain.setText("");
        }else{

            OkHttpUtils.post()
                    .url(NetManager.REGIST)
                    .addParams("username",name)
                    .addParams("phone",phone)
                    .addParams("password", pwd)
//                    .addParams("password", MD5Utils.MD5(pwd))
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            Toast.makeText(RegistActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            Toast.makeText(RegistActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
        }
    }
}
