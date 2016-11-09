package com.starsutdio.biglz.contract;

import com.google.gson.JsonObject;
import com.starsutdio.biglz.presenter.BasePresenter;
import com.starsutdio.biglz.view.BaseView;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;

/**
 * Created by windfall on 16-11-2.
 */

public class UserContract {
    public interface Presenter extends BasePresenter {
        void login(Map map);
        void register(String username,String email,String password,String provecode);
        void logout();
        void imgupload(List<MultipartBody.Part> list);
        void post(String acckey,JsonObject jsonObject);
    }

    public interface View extends BaseView<Presenter>{

    }
}
