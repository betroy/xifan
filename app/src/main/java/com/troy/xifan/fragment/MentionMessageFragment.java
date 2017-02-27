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
import com.troy.xifan.http.request.StatusesRequest;
import com.troy.xifan.model.response.StatusRes;
import com.troy.xifan.util.UIUtils;
import com.troy.xifan.util.Utils;
import java.util.List;

/**
 * Created by chenlongfei on 2016/12/16.
 */

public class MentionMessageFragment extends BaseFragment {
    @BindView(R.id.recycler_view) EasyRecyclerView mRecyclerView;

    private String mMaxId;
    private StatusAdapter mStatusAdapter;

    public static Fragment newInstance() {
        return new MentionMessageFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getMentions(false);
    }

    private void getMentions(final boolean loadMore) {
        StatusesRequest request = new StatusesRequest();
        request.setMax_id(loadMore ? mMaxId : null);

        HttpRequestFactory.getInstance()
                .getMentions(request, new SimpleHttpRequestCallback<List<StatusRes>>() {
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
                        UIUtils.showToast(getActivity(), apiException.getErrorMessage());
                        mRecyclerView.setRefreshing(false);
                    }
                });
    }

    @Override
    protected void initViews() {
        mStatusAdapter = new StatusAdapter(getActivity());
        mStatusAdapter.setMore(R.layout.view_load_more, new RecyclerArrayAdapter.OnMoreListener() {
            @Override
            public void onMoreShow() {
                getMentions(true);
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
        return R.layout.fragment_mention_message;
    }

    @Override
    public void onRefresh() {
        getMentions(false);
    }
}
