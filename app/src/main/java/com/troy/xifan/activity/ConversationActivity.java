package com.troy.xifan.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.chenenyu.router.annotation.Route;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.troy.xifan.R;
import com.troy.xifan.adapter.ConversationAdapter;
import com.troy.xifan.config.Constants;
import com.troy.xifan.http.HttpRequestFactory;
import com.troy.xifan.http.callback.SimpleHttpRequestCallback;
import com.troy.xifan.http.exception.ApiException;
import com.troy.xifan.http.request.BaseRequestParams;
import com.troy.xifan.http.request.DirectMessagesRequest;
import com.troy.xifan.model.response.DirectMessagesRes;
import com.troy.xifan.util.UIUtils;
import com.troy.xifan.util.Utils;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by chenlongfei on 2017/1/14.
 */
@Route(Constants.Router.CONVERSATION)
public class ConversationActivity extends BaseActivity {
    public static String BUNDLE_OTHER_USER_ID = "other_user_id";

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.recycler_view) EasyRecyclerView mRecyclerView;
    @BindView(R.id.edit_msg) EditText mEditMsg;
    @BindView(R.id.button_send_dm) Button mButtonSend;

    private int mPage;
    private String mMsg;
    private String mOtherUserId;
    private ConversationAdapter mConversationAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        }

        mOtherUserId = bundle.getString(BUNDLE_OTHER_USER_ID);
        if (mOtherUserId == null) {
            return;
        }

        initViews();
    }

    @Override
    protected void initViews() {
        mToolbar.setTitle(R.string.title_dm);
        mToolbar.setElevation(getResources().getDimension(R.dimen.elevation));
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mConversationAdapter = new ConversationAdapter(this, mOtherUserId);
        mConversationAdapter.setMore(R.layout.view_load_more,
                new RecyclerArrayAdapter.OnMoreListener() {
                    @Override
                    public void onMoreShow() {
                        getConversation(true);
                    }

                    @Override
                    public void onMoreClick() {

                    }
                });
        mConversationAdapter.setError(R.layout.view_load_more_error,
                new RecyclerArrayAdapter.OnErrorListener() {
                    @Override
                    public void onErrorShow() {

                    }

                    @Override
                    public void onErrorClick() {
                        mConversationAdapter.resumeMore();
                    }
                });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapterWithProgress(mConversationAdapter);
        mButtonSend.setEnabled(false);

        initListener();
        getConversation(false);
    }

    private void initListener() {
        mEditMsg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mButtonSend.setEnabled(s.length() > 0 && !TextUtils.isEmpty(s.toString()));
                mMsg = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mButtonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendDirectMessage();
                insertDirectMessage();
            }
        });
    }

    private void getConversation(final boolean isLoadMore) {
        BaseRequestParams request = new BaseRequestParams();
        request.setPage(String.valueOf(++mPage));
        request.setId(mOtherUserId);

        HttpRequestFactory.getInstance()
                .getConversation(request, new SimpleHttpRequestCallback<List<DirectMessagesRes>>() {
                    @Override
                    public void onSuccess(List<DirectMessagesRes> responseData) {
                        Collections.reverse(responseData);
                        mConversationAdapter.addAll(responseData);
                        mConversationAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFail(ApiException apiException) {
                        UIUtils.showToast(ConversationActivity.this,
                                apiException.getErrorMessage());
                    }
                });
    }

    private void insertDirectMessage() {
        DirectMessagesRes directMessages = new DirectMessagesRes();
        directMessages.setRecipient_id(mOtherUserId);
        directMessages.setText(mMsg);
        directMessages.setCreated_at(Utils.formatFanFouDate(new Date()));
        mConversationAdapter.add(directMessages);
        mConversationAdapter.notifyDataSetChanged();
        mEditMsg.setText(null);
    }

    private void sendDirectMessage() {
        DirectMessagesRequest request = new DirectMessagesRequest();
        request.setUser(mOtherUserId);
        request.setText(mMsg);

        HttpRequestFactory.getInstance()
                .sendDirectMessage(request, new SimpleHttpRequestCallback<DirectMessagesRes>() {
                    @Override
                    public void onSuccess(DirectMessagesRes responseData) {
                    }

                    @Override
                    public void onFail(ApiException apiException) {
                        UIUtils.showToast(ConversationActivity.this,
                                apiException.getErrorMessage());
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
