package com.troy.xifan.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import butterknife.BindView;
import com.chenenyu.router.Router;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.troy.xifan.R;
import com.troy.xifan.activity.StatusDetailActivity;
import com.troy.xifan.adapter.StatusAdapter;
import com.troy.xifan.config.Constants;
import com.troy.xifan.http.HttpRequestFactory;
import com.troy.xifan.http.callback.SimpleHttpRequestCallback;
import com.troy.xifan.http.exception.ApiException;
import com.troy.xifan.model.response.StatusRes;
import com.troy.xifan.util.UIUtils;
import java.util.List;

/**
 * Created by chenlongfei on 2017/1/10.
 */

public class PublicTimelineFragment extends BaseFragment {
    @BindView(R.id.recycler_view) EasyRecyclerView mRecyclerView;

    private StatusAdapter mStatusAdapter;

    public static Fragment newInstance() {
        return new PublicTimelineFragment();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getPublicTimeline();
    }

    private void getPublicTimeline() {
        HttpRequestFactory.getInstance()
                .getPublicTimeline(new SimpleHttpRequestCallback<List<StatusRes>>() {
                    @Override
                    public void onSuccess(List<StatusRes> responseData) {
                        mStatusAdapter.clear();
                        mStatusAdapter.addAll(responseData);
                        mStatusAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFail(ApiException apiException) {
                        UIUtils.showToast(getActivity(), apiException.getErrorMessage());
                    }
                });
    }

    @Override
    protected void initViews() {
        mStatusAdapter = new StatusAdapter(getActivity());
        mStatusAdapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                StatusRes statusRes = mStatusAdapter.getItem(position);
                Bundle bundle = new Bundle();
                bundle.putParcelable(StatusDetailActivity.EXTRA_STATUS, statusRes);
                Router.build(Constants.Router.STATUS_DETAIL).extras(bundle).go(getActivity());
            }
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapterWithProgress(mStatusAdapter);
        mRecyclerView.setRefreshListener(this);
        mRecyclerView.setRefreshingColor(getResources().getIntArray(R.array.refreshing_color));
    }

    @Override
    protected int getContentViewResId() {
        return R.layout.fragment_public_timeline;
    }

    @Override
    public void onRefresh() {
        getPublicTimeline();
    }
}
