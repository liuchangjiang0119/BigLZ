package com.starsutdio.biglz.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.starsutdio.biglz.R;
import com.starsutdio.biglz.contract.UserContract;
import com.starsutdio.biglz.presenter.UserPresenter;
import com.starsutdio.biglz.utils.ProveCodeUtil;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterActivity extends AppCompatActivity implements UserContract.View,View.OnClickListener{

    @BindView(R.id.provecode_image)
    ImageView provecode_image;
    @BindView(R.id.editText_username)
    EditText username;
    @BindView(R.id.editText_email)
    EditText email;
    @BindView(R.id.editText_pwd)
    EditText pwd;
    @BindView(R.id.confirm_pwd)
    EditText confirm_pwd;
    @BindView(R.id.editText_pc)
    EditText pc;
    @BindView(R.id.register_btn)
    Button register_btn;

    private String username_str,email_str,pwd_str,pwdconfirm ,pc_str;
    private UserPresenter mPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        setTitle("注册");
        provecode_image.setImageBitmap(ProveCodeUtil.createRandomBitmap());
        mPresenter = new UserPresenter(this);
        provecode_image.setOnClickListener(this);
        register_btn.setOnClickListener(this);


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.provecode_image:
                provecode_image.setImageBitmap(ProveCodeUtil.createRandomBitmap());
                break;
            case R.id.register_btn:
                username_str = username.getText().toString();
                email_str = email.getText().toString();
                pwd_str = pwd.getText().toString();
                pc_str = pc.getText().toString();
                pwdconfirm = confirm_pwd.getText().toString();
                if (pwd_str!=pwdconfirm) Toast.makeText(RegisterActivity.this,"两次密码输入不一致",Toast.LENGTH_LONG).show();
                mPresenter.register(username_str,email_str,pwd_str,pc_str);
                break;
        }
    }

    @Override
    public void showFailureDialog() {

    }

    @Override
    public void showSuccessDialog() {

    }
}
