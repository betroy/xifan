package com.troy.xifan.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import butterknife.BindView;
import com.chenenyu.router.Router;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.troy.xifan.R;
import com.troy.xifan.activity.StatusDetailActivity;
import com.troy.xifan.adapter.StatusAdapter;
import com.troy.xifan.config.Constants;
import com.troy.xifan.eventbus.SendStatusEvent;
import com.troy.xifan.eventbus.StatusRefreshEvent;
import com.troy.xifan.http.HttpRequestFactory;
import com.troy.xifan.http.callback.SimpleHttpRequestCallback;
import com.troy.xifan.http.exception.ApiException;
import com.troy.xifan.http.request.StatusesRequest;
import com.troy.xifan.model.response.StatusRes;
import com.troy.xifan.util.UIUtils;
import com.troy.xifan.util.Utils;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by chenlongfei on 2016/12/1.
 */

public class HomeFragment extends BaseFragment {
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.recycler_view) EasyRecyclerView mRecyclerView;

    private String mMaxId;
    private StatusAdapter mStatusAdapter;

    public static Fragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getHomeTimeline(false);
    }

    private void getHomeTimeline(final boolean loadMore) {
        StatusesRequest request = new StatusesRequest();
        request.setMax_id(loadMore ? mMaxId : null);

        HttpRequestFactory.getInstance()
                .getHomeTimeline(request, new SimpleHttpRequestCallback<List<StatusRes>>() {
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
    public void initViews() {
        mToolbar.setTitle(getString(R.string.bottom_navigation_home));
        mToolbar.inflateMenu(R.menu.menu_home);
        mToolbar.setOnMenuItemClickListener(mOnMenuItemClickListener);
        mToolbar.setElevation(getResources().getDimension(R.dimen.elevation));

        mStatusAdapter = new StatusAdapter(getActivity());
        mStatusAdapter.setMore(R.layout.view_load_more, new RecyclerArrayAdapter.OnMoreListener() {
            @Override
            public void onMoreShow() {
                getHomeTimeline(true);
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
        return R.layout.fragment_home;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onRefresh() {
        getHomeTimeline(false);
    }

    //刷新timeline
    @Subscribe
    public void onStatusRefresh(StatusRefreshEvent statusRefreshEvent) {
        int position = -1;
        StatusRes statusRes = statusRefreshEvent.getStatusRes();
        List<StatusRes> statusResList = mStatusAdapter.getAllData();

        for (int i = 0; i < statusResList.size(); i++) {
            if (statusResList.get(i).getId().equals(statusRes.getId())) {
                position = i;
                break;
            }
        }
        if (position < 0) {
            return;
        }
        if (statusRefreshEvent.getType().equals(StatusRefreshEvent.TYPE_DELETE_STATUS)) {
            mStatusAdapter.remove(position);
        } else if (statusRefreshEvent.getType().equals(StatusRefreshEvent.TYPE_FAVORITE_STATUS)) {
            mStatusAdapter.update(statusRes, position);
        }
    }

    //消息发送成功刷新timeline
    @Subscribe
    public void onSendStatus(SendStatusEvent sendStatusEvent) {
        if (SendStatusEvent.TYPE_SUCCESS.equals(sendStatusEvent.getType())) {
            UIUtils.showToast(getActivity(), getString(R.string.text_send_status_success));
            getHomeTimeline(false);
        } else if (SendStatusEvent.TYPE_FAIL.equals(sendStatusEvent.getType())) {
            UIUtils.showToast(getActivity(), getString(R.string.text_send_status_fail));
        }
    }

    private Toolbar.OnMenuItemClickListener mOnMenuItemClickListener =
            new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.action_write:
                            Router.build(Constants.Router.WRITE_STATUS).go(getActivity());
                            break;
                    }
                    return true;
                }
            };
}