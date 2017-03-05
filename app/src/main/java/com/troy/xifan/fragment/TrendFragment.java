package com.troy.xifan.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import butterknife.BindView;
import com.chenenyu.router.Router;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.easyrecyclerview.decoration.DividerDecoration;
import com.troy.xifan.R;
import com.troy.xifan.activity.TrendStatusesActivity;
import com.troy.xifan.adapter.TrendAdapter;
import com.troy.xifan.config.Constants;
import com.troy.xifan.http.HttpRequestFactory;
import com.troy.xifan.http.callback.SimpleHttpRequestCallback;
import com.troy.xifan.http.exception.ApiException;
import com.troy.xifan.model.response.TrendsRes;
import com.troy.xifan.util.UIUtils;

/**
 * Created by chenlongfei on 2017/1/10.
 */

public class TrendFragment extends BaseFragment {
    @BindView(R.id.recycler_view) EasyRecyclerView mRecyclerView;

    private TrendAdapter mTrendAdapter;
    private boolean isLoaded;

    public static Fragment newInstance() {
        return new TrendFragment();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser && !isLoaded) {
            getTrends();
            isLoaded = true;
        }
    }

    private void getTrends() {
        HttpRequestFactory.getInstance().getTrends(new SimpleHttpRequestCallback<TrendsRes>() {
            @Override
            public void onSuccess(TrendsRes responseData) {
                mTrendAdapter.clear();
                mTrendAdapter.addAll(responseData.getTrends());
                mTrendAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFail(ApiException apiException) {
                UIUtils.showToast(getActivity(), apiException.getErrorMessage());
            }
        });
    }

    @Override
    protected void initViews() {
        mTrendAdapter = new TrendAdapter(getActivity());
        mTrendAdapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                TrendsRes.Trends trend = mTrendAdapter.getAllData().get(position);
                Bundle bundle = new Bundle();
                bundle.putString(TrendStatusesActivity.BUNDLE_SEARCH_KEYWORD, trend.getName());
                Router.build(Constants.Router.TREND_STATUSES).extras(bundle).go(getActivity());
            }
        });
        DividerDecoration itemDecoration =
                new DividerDecoration(getResources().getColor(R.color.divider_line_color),
                        Math.round(getResources().getDimension(R.dimen.divider_line_size)), 0, 0);
        itemDecoration.setDrawLastItem(true);
        itemDecoration.setDrawHeaderFooter(false);
        mRecyclerView.addItemDecoration(itemDecoration);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapterWithProgress(mTrendAdapter);
        mRecyclerView.setRefreshListener(this);
        mRecyclerView.setRefreshingColor(getResources().getIntArray(R.array.refreshing_color));
    }

    @Override
    protected int getContentViewResId() {
        return R.layout.fragment_trend;
    }

    @Override
    public void onRefresh() {
        getTrends();
    }
}
