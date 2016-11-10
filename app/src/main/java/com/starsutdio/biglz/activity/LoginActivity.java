package com.starsutdio.biglz.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.starsutdio.biglz.R;
import com.starsutdio.biglz.contract.UserContract;
import com.starsutdio.biglz.presenter.UserPresenter;
import com.starsutdio.biglz.utils.ProveCodeUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindFloat;
import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener,UserContract.View{
    @BindView(R.id.login_un)
    TextInputLayout login_un;
    @BindView(R.id.login_pwd)
    TextInputLayout login_pwd;
    @BindView(R.id.login_provecode)
    EditText login_provecode;
    @BindView(R.id.login_btn)
    Button login_btn;
    @BindView(R.id.codeimage_btn)
    ImageView codeimage;

    private String un,pwd,provecode;
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;
    private Map<String,String> mMap;
    private UserPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        setTitle("登录");

        mPresenter = new UserPresenter(this);

        login_btn.setOnClickListener(this);
        codeimage.setOnClickListener(this);
        codeimage.setImageBitmap(ProveCodeUtil.createRandomBitmap());


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.login_btn:
                un = login_un.getEditText().getText().toString();
                pwd = login_pwd.getEditText().getText().toString();
                provecode = login_provecode.getText().toString();
                mMap = new HashMap<>();
                mMap.put("un",un);
                mMap.put("pd",pwd);
                mMap.put("pc","9a3b");
                mPresenter.login(mMap);
                break;
            case R.id.codeimage_btn:
                codeimage.setImageBitmap(ProveCodeUtil.createRandomBitmap());
                break;

        }
    }

    void saveUserInfo(){
        mPreferences = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        mEditor = mPreferences.edit();

    }

    @Override
    public void showSuccessDialog() {

    }

    @Override
    public void showFailureDialog() {

    }
}
