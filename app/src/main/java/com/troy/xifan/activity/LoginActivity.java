package com.troy.xifan.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.chenenyu.router.Router;
import com.chenenyu.router.annotation.Route;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.troy.xifan.R;
import com.troy.xifan.config.Constants;
import com.troy.xifan.http.callback.HttpRequestCallback;
import com.troy.xifan.http.HttpRequestFactory;
import com.troy.xifan.http.exception.ApiException;
import com.troy.xifan.manage.UserHolder;
import com.troy.xifan.model.request.User;
import com.troy.xifan.model.response.OAuthToken;
import com.troy.xifan.util.UIUtils;

import static com.troy.xifan.util.XAuthUtils.parseToken;

@Route(Constants.Router.LOGIN)
public class LoginActivity extends BaseActivity {
    @BindView(R.id.edit_username) EditText mEditUserName;
    @BindView(R.id.edit_password) EditText mEditPassword;
    @BindView(R.id.button_login) Button mButtonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.button_login)
    public void submit(View view) {
        final String username = mEditUserName.getText().toString().trim();
        final String password = mEditPassword.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            mEditUserName.requestFocus();
            mEditUserName.setError(getString(R.string.login_username_not_null));
            return;
        }

        if (TextUtils.isEmpty(password)) {
            mEditPassword.requestFocus();
            mEditPassword.setError(getString(R.string.login_password_not_null));
            return;
        }

        Constants.FanFou.USERNAME = username;
        Constants.FanFou.PASSWORD = password;

        HttpRequestFactory.getInstance()
                .getAccessToken(username, password, new HttpRequestCallback<String>() {
                    @Override
                    public void onStart() {
                        mButtonLogin.setText(getString(R.string.logining_text));
                    }

                    @Override
                    public void onSuccess(String responseData) {
                        mButtonLogin.setText(getString(R.string.login_text));

                        OAuthToken oAuthToken = parseToken(responseData);
                        User user = new User(username, password, oAuthToken);
                        UserHolder.getInstance().saveUser(user);
                        Logger.json(new Gson().toJson(user));

                        Router.build(Constants.Router.MAIN).go(LoginActivity.this);
                        finish();
                    }

                    @Override
                    public void onFail(ApiException apiException) {
                        mButtonLogin.setText(getString(R.string.login_text));
                        UIUtils.showToast(LoginActivity.this, apiException.getErrorMessage());
                    }
                });
    }

    @Override
    public void initViews() {

    }
}
