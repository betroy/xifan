package com.troy.xifan.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
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
 * Created by chenlongfei on 2017/1/11.
 */
@Route({
        Constants.Router.TREND_STATUSES, Constants.Router.SCHEME + Constants.Router.TREND_STATUSES
})
public class TrendStatusesActivity extends BaseActivity
        implements SwipeRefreshLayout.OnRefreshListener {
    public static final String BUNDLE_SEARCH_KEYWORD = "keyword";

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.recycler_view) EasyRecyclerView mRecyclerView;

    private String mMaxId;
    private String mKeyword;
    private StatusAdapter mStatusAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trend_statuses);
        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mKeyword = bundle.getString(BUNDLE_SEARCH_KEYWORD);
        }
        if (mKeyword == null) {
            return;
        }

        initViews();
        searchPublicTimeline(false);
    }

    @Override
    protected void initViews() {
        mToolbar.setTitle(mKeyword);
        mToolbar.setElevation(getResources().getDimension(R.dimen.elevation));
        setSupportActionBar(mToolbar);
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
                Router.build(Constants.Router.STATUS_DETAIL)
                        .extras(bundle)
                        .go(TrendStatusesActivity.this);
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapterWithProgress(mStatusAdapter);
        mRecyclerView.setRefreshListener(this);
        mRecyclerView.setRefreshingColor(getResources().getIntArray(R.array.refreshing_color));
    }

    private void searchPublicTimeline(final boolean loadMore) {
        SearchRequest request = new SearchRequest();
        request.setQ(mKeyword);
        request.setMax_id(loadMore ? mMaxId : null);

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
                        UIUtils.showToast(TrendStatusesActivity.this,
                                apiException.getErrorMessage());
                    }
                });
    }

    @Override
    public void onRefresh() {
        searchPublicTimeline(false);
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
