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
import com.troy.xifan.activity.ConversationActivity;
import com.troy.xifan.adapter.ConversationListAdapter;
import com.troy.xifan.config.Constants;
import com.troy.xifan.http.HttpRequestFactory;
import com.troy.xifan.http.callback.SimpleHttpRequestCallback;
import com.troy.xifan.http.exception.ApiException;
import com.troy.xifan.http.request.BaseRequestParams;
import com.troy.xifan.model.response.DirectMessagesListRes;
import com.troy.xifan.util.UIUtils;
import java.util.List;

/**
 * Created by chenlongfei on 2016/12/16.
 */

public class DirectMessageFragment extends BaseFragment {
    @BindView(R.id.recycler_view) EasyRecyclerView mRecyclerView;

    private int mPage;
    private ConversationListAdapter mConversationListAdapter;

    public static Fragment newInstance() {
        return new DirectMessageFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDirectMessage(false);
    }

    private void getDirectMessage(final boolean loadMore) {
        BaseRequestParams request = new BaseRequestParams();
        request.setPage(String.valueOf(++mPage));

        HttpRequestFactory.getInstance()
                .getConversationList(request,
                        new SimpleHttpRequestCallback<List<DirectMessagesListRes>>() {
                            @Override
                            public void onSuccess(List<DirectMessagesListRes> responseData) {
                                if (!loadMore) {
                                    mConversationListAdapter.clear();
                                }
                                mConversationListAdapter.addAll(responseData);
                                mConversationListAdapter.notifyDataSetChanged();
                                mRecyclerView.setRefreshing(false);
                            }

                            @Override
                            public void onFail(ApiException apiException) {
                                UIUtils.showToast(getActivity(), apiException.getErrorMessage());
                            }
                        });
    }

    @Override
    protected void initViews() {
        mConversationListAdapter = new ConversationListAdapter(getActivity());
        mConversationListAdapter.setMore(R.layout.view_load_more,
                new RecyclerArrayAdapter.OnMoreListener() {
                    @Override
                    public void onMoreShow() {
                        getDirectMessage(true);
                    }

                    @Override
                    public void onMoreClick() {

                    }
                });
        mConversationListAdapter.setError(R.layout.view_load_more_error,
                new RecyclerArrayAdapter.OnErrorListener() {
                    @Override
                    public void onErrorShow() {

                    }

                    @Override
                    public void onErrorClick() {
                        mConversationListAdapter.resumeMore();
                    }
                });
        mConversationListAdapter.setOnItemClickListener(
                new RecyclerArrayAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        DirectMessagesListRes directMessagesList =
                                mConversationListAdapter.getItem(position);
                        String otherUserId = directMessagesList.getOtherid();
                        Bundle bundle = new Bundle();
                        bundle.putString(ConversationActivity.BUNDLE_OTHER_USER_ID, otherUserId);
                        Router.build(Constants.Router.CONVERSATION)
                                .extras(bundle)
                                .go(getActivity());
                    }
                });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapterWithProgress(mConversationListAdapter);
        mRecyclerView.setRefreshListener(this);
        mRecyclerView.setRefreshingColor(getResources().getIntArray(R.array.refreshing_color));
    }

    @Override
    protected int getContentViewResId() {
        return R.layout.fragment_direct_message;
    }

    @Override
    public void onRefresh() {
        getDirectMessage(false);
    }
}
