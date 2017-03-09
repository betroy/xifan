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
import com.troy.xifan.activity.ProfileActivity;
import com.troy.xifan.activity.StatusDetailActivity;
import com.troy.xifan.adapter.StatusAdapter;
import com.troy.xifan.config.Constants;
import com.troy.xifan.http.HttpRequestFactory;
import com.troy.xifan.http.callback.SimpleHttpRequestCallback;
import com.troy.xifan.http.exception.ApiException;
import com.troy.xifan.http.request.StatusesRequest;
import com.troy.xifan.model.response.StatusRes;
import com.troy.xifan.model.response.UserRes;
import com.troy.xifan.util.UIUtils;
import com.troy.xifan.util.Utils;
import java.util.List;

/**
 * Created by chenlongfei on 2017/1/25.
 */

public class UserTimelineFragment extends BaseFragment {
    @BindView(R.id.recycler_view) EasyRecyclerView mRecyclerView;

    private String mMaxId;
    private StatusAdapter mStatusAdapter;
    private UserRes mUser;
    private boolean isLoaded;
    private boolean isVisible;
    private boolean isPrepared;

    public static Fragment newInstance(Bundle bundle) {
        Fragment fragment = new UserTimelineFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            mUser = bundle.getParcelable(ProfileActivity.BUNDLE_USER);
        }

        if (isVisible) {
            getUserTimeline(false);
        }

        isPrepared = true;
    }

    @Override
    protected void initViews() {
        mStatusAdapter = new StatusAdapter(getActivity());
        mStatusAdapter.setMore(R.layout.view_load_more, new RecyclerArrayAdapter.OnMoreListener() {
            @Override
            public void onMoreShow() {
                getUserTimeline(true);
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
    }

    @Override
    protected int getContentViewResId() {
        return R.layout.fragment_user_timeline;
    }

    @Override
    public void onRefresh() {

    }

    //Fragment 从不可见变为可见，isVisibleToUser参数由false-->true 再调用onAttach()-->onCreate()
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (!isLoaded && isPrepared) {
                getUserTimeline(false);
            }
            isVisible = true;
        }
    }

    private void getUserTimeline(final boolean loadMore) {
        isLoaded = true;

        StatusesRequest request = new StatusesRequest();
        request.setMax_id(loadMore ? mMaxId : null);
        request.setId(mUser == null ? null : mUser.getId());

        HttpRequestFactory.getInstance()
                .getUserHomeTimeline(request, new SimpleHttpRequestCallback<List<StatusRes>>() {
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
}
