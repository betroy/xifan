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
import com.troy.xifan.http.request.BaseRequestParams;
import com.troy.xifan.model.response.StatusRes;
import com.troy.xifan.model.response.UserRes;
import com.troy.xifan.util.UIUtils;
import java.util.List;

/**
 * Created by chenlongfei on 2017/1/25.
 */

public class UserFavoriteFragment extends BaseFragment {
    @BindView(R.id.recycler_view) EasyRecyclerView mRecyclerView;

    private UserRes mUser;
    private int mPage;
    private StatusAdapter mStatusAdapter;
    private boolean isLoaded;
    private boolean isVisible;
    private boolean isPrepared;

    public static Fragment newInstance(Bundle bundle) {
        Fragment fragment = new UserFavoriteFragment();
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
            getFavorites(false);
        }

        isPrepared = true;
    }

    @Override
    protected void initViews() {
        mStatusAdapter = new StatusAdapter(getActivity());
        mStatusAdapter.setMore(R.layout.view_load_more, new RecyclerArrayAdapter.OnMoreListener() {
            @Override
            public void onMoreShow() {
                getFavorites(true);
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
        return R.layout.fragment_user_favorite;
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (!isLoaded && isPrepared) {
                getFavorites(false);
            }
            isVisible = true;
        }
    }

    private void getFavorites(final boolean loadMore) {
        isLoaded = true;

        BaseRequestParams request = new BaseRequestParams();
        request.setPage(String.valueOf(++mPage));
        request.setId(mUser == null ? null : mUser.getId());

        HttpRequestFactory.getInstance()
                .getUserFavorites(request, new SimpleHttpRequestCallback<List<StatusRes>>() {
                    @Override
                    public void onSuccess(List<StatusRes> responseData) {
                        if (!loadMore) {
                            mStatusAdapter.clear();
                        }
                        mStatusAdapter.addAll(responseData);
                        mStatusAdapter.notifyDataSetChanged();
                        mRecyclerView.setRefreshing(false);
                    }

                    @Override
                    public void onFail(ApiException apiException) {
                        UIUtils.showToast(getActivity(), apiException.getErrorMessage());
                        mRecyclerView.setRefreshing(false);
                    }
                });
    }
}
