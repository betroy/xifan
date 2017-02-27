package com.troy.xifan.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.chenenyu.router.Router;
import com.chenenyu.router.annotation.Route;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.troy.xifan.R;
import com.troy.xifan.adapter.StatusAdapter;
import com.troy.xifan.config.Constants;
import com.troy.xifan.http.HttpRequestFactory;
import com.troy.xifan.http.callback.SimpleHttpRequestCallback;
import com.troy.xifan.http.exception.ApiException;
import com.troy.xifan.http.request.SearchRequest;
import com.troy.xifan.model.response.StatusRes;
import com.troy.xifan.util.UIUtils;
import com.troy.xifan.util.Utils;
import java.util.List;

/**
 * Created by chenlongfei on 2016/12/17.
 */
@Route(Constants.Router.SEARCH)
public class SearchActivity extends BaseActivity {
    @BindView(R.id.search_bar) Toolbar mSearchBar;
    @BindView(R.id.edit_search_bar) EditText mEditSearchBar;
    @BindView(R.id.recycler_view) EasyRecyclerView mRecyclerView;

    private String mMaxId;
    private String mKeyword;
    private StatusAdapter mStatusAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        initViews();
    }

    @Override
    public void initViews() {
        mSearchBar.setTitle("");
        Drawable backDrawable = getResources().getDrawable(R.drawable.abc_ic_ab_back);
        mSearchBar.setNavigationIcon(backDrawable);
        setSupportActionBar(mSearchBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mStatusAdapter = new StatusAdapter(this);
        mStatusAdapter.setMore(R.layout.view_load_more, new RecyclerArrayAdapter.OnMoreListener() {
            @Override
            public void onMoreShow() {
                searchPublicTimeline(true);
            }

            @Override
            public void onMoreClick() {

            }
        });
        mStatusAdapter.setError(R.layout.view_load_more_error,
                new RecyclerArrayAdapter.OnErrorListener() {
                    @Override
                    public void onErrorShow() {

                    }

                    @Override
                    public void onErrorClick() {
                        mStatusAdapter.resumeMore();
                    }
                });
        mStatusAdapter.setNoMore(R.layout.view_no_more);
        mStatusAdapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                StatusRes statusRes = mStatusAdapter.getItem(position);
                Bundle bundle = new Bundle();
                bundle.putParcelable(StatusDetailActivity.EXTRA_STATUS, statusRes);
                Router.build(Constants.Router.STATUS_DETAIL).extras(bundle).go(SearchActivity.this);
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mStatusAdapter);
        mRecyclerView.setRefreshingColor(getResources().getIntArray(R.array.refreshing_color));
        mRecyclerView.setRefreshing(false);

        initLiseners();
    }

    private void initLiseners() {
        mEditSearchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mKeyword = charSequence.toString().trim();
                invalidateOptionsMenu();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mEditSearchBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (textView.getText().length() == 0) {
                        return false;
                    }
                    searchPublicTimeline(false);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem menuItem = menu.findItem(R.id.action_clear);
        menuItem.setVisible(mEditSearchBar.length() > 0);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_clear:
                mEditSearchBar.getText().clear();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void searchPublicTimeline(final boolean loadMore) {
        SearchRequest request = new SearchRequest();
        request.setQ(mKeyword);
        request.setMax_id(loadMore ? mMaxId : null);
        mRecyclerView.setRefreshing(loadMore ? false : true);

        HttpRequestFactory.getInstance()
                .searchPublicTimeline(request, new SimpleHttpRequestCallback<List<StatusRes>>() {
                    @Override
                    public void onSuccess(List<StatusRes> responseData) {
                        if (!loadMore) {
                            mStatusAdapter.clear();
                        }
                        mStatusAdapter.addAll(responseData);
                        mStatusAdapter.notifyDataSetChanged();
                        mRecyclerView.setRefreshing(false);

                        mMaxId = Utils.getMaxId(mStatusAdapter.getAllData());
                    }

                    @Override
                    public void onFail(ApiException apiException) {
                        UIUtils.showToast(SearchActivity.this, apiException.getErrorMessage());
                    }
                });
    }
}
