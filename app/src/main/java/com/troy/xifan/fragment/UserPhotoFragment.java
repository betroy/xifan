package com.troy.xifan.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import butterknife.BindView;
import com.chenenyu.router.Router;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.easyrecyclerview.decoration.SpaceDecoration;
import com.troy.xifan.R;
import com.troy.xifan.activity.ProfileActivity;
import com.troy.xifan.activity.StatusDetailActivity;
import com.troy.xifan.adapter.PhotoAdapter;
import com.troy.xifan.config.Constants;
import com.troy.xifan.http.HttpRequestFactory;
import com.troy.xifan.http.callback.SimpleHttpRequestCallback;
import com.troy.xifan.http.exception.ApiException;
import com.troy.xifan.http.request.StatusesRequest;
import com.troy.xifan.model.response.StatusRes;
import com.troy.xifan.model.response.UserRes;
import com.troy.xifan.util.UIUtils;
import java.util.Iterator;
import java.util.List;

/**
 * Created by chenlongfei on 2017/1/25.
 */

public class UserPhotoFragment extends BaseFragment {
    @BindView(R.id.recycler_view) EasyRecyclerView mRecyclerView;

    private UserRes mUser;
    private int mPage;
    private PhotoAdapter mPhotoAdapter;

    public static Fragment newInstance(Bundle bundle) {
        Fragment fragment = new UserPhotoFragment();
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
    }

    @Override
    protected void initViews() {
        mPhotoAdapter = new PhotoAdapter(getActivity());
        mPhotoAdapter.setMore(R.layout.view_load_more, new RecyclerArrayAdapter.OnMoreListener() {
            @Override
            public void onMoreShow() {
                getUserPhotos(true);
            }

            @Override
            public void onMoreClick() {

            }
        });
        mPhotoAdapter.setError(R.layout.view_load_more_error,
                new RecyclerArrayAdapter.OnErrorListener() {
                    @Override
                    public void onErrorShow() {

                    }

                    @Override
                    public void onErrorClick() {
                        mPhotoAdapter.resumeMore();
                    }
                });
        mPhotoAdapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                StatusRes statusRes = mPhotoAdapter.getItem(position);
                Bundle bundle = new Bundle();
                bundle.putParcelable(StatusDetailActivity.EXTRA_STATUS, statusRes);
                Router.build(Constants.Router.STATUS_DETAIL).extras(bundle).go(getActivity());
            }
        });

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        gridLayoutManager.setSpanSizeLookup(mPhotoAdapter.obtainGridSpanSizeLookUp(3));
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setAdapterWithProgress(mPhotoAdapter);

        SpaceDecoration itemDecoration =
                new SpaceDecoration((int) UIUtils.dip2px(getActivity(), 8));
        itemDecoration.setPaddingEdgeSide(true);
        itemDecoration.setPaddingStart(true);
        itemDecoration.setPaddingHeaderFooter(false);
        mRecyclerView.addItemDecoration(itemDecoration);
    }

    @Override
    protected int getContentViewResId() {
        return R.layout.fragment_user_photo;
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getUserPhotos(false);
    }

    private void getUserPhotos(final boolean loadMore) {
        mPage = loadMore ? ++mPage : 0;
        StatusesRequest request = new StatusesRequest();
        request.setPage(String.valueOf(++mPage));
        request.setId(mUser == null ? null : mUser.getId());

        HttpRequestFactory.getInstance()
                .getUserPhotos(request, new SimpleHttpRequestCallback<List<StatusRes>>() {
                    @Override
                    public void onSuccess(List<StatusRes> responseData) {
                        if (!loadMore) {
                            mPhotoAdapter.clear();
                        }

                        Iterator<StatusRes> resIterator = responseData.iterator();
                        while (resIterator.hasNext()) {
                            StatusRes statusRes = resIterator.next();
                            if (statusRes.getPhoto() == null) {
                                resIterator.remove();
                            }
                        }
                        mPhotoAdapter.addAll(responseData);
                    }

                    @Override
                    public void onFail(ApiException apiException) {
                        UIUtils.showToast(getActivity(), apiException.getErrorMessage());
                    }
                });
    }
}
