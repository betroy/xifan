package com.troy.xifan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.chenenyu.router.Router;
import com.chenenyu.router.annotation.Route;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.easyrecyclerview.decoration.DividerDecoration;
import com.troy.xifan.R;
import com.troy.xifan.adapter.FollowingAdapter;
import com.troy.xifan.config.Constants;
import com.troy.xifan.http.HttpRequestFactory;
import com.troy.xifan.http.callback.SimpleHttpRequestCallback;
import com.troy.xifan.http.exception.ApiException;
import com.troy.xifan.http.request.UserRequest;
import com.troy.xifan.model.response.UserRes;
import com.troy.xifan.util.UIUtils;
import java.util.List;

/**
 * Created by chenlongfei on 2017/1/3.
 */
@Route(Constants.Router.USER_LIST)
public class UserListActivity extends BaseActivity {
    public static final String EXTRAS_USER_NAME = "user_name";
    public static final String BUNDLE_USER = "user";
    public static final String BUNLDE_TYPE = "type";
    public static final String TYPE_GET_FOLLOWING = "get_following";
    public static final String TYPE_GET_FOLLOWER = "get_follower";
    public static final String TYPE_GET_AT_FOLLOWING = "get_at_following";

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.recycler_view) EasyRecyclerView mRecyclerView;

    private String mType;
    private UserRes mUser;
    private int mPage;
    private FollowingAdapter mFollowingAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mUser = bundle.getParcelable(BUNDLE_USER);
            mType = bundle.getString(BUNLDE_TYPE);
        }

        setContentView(R.layout.activity_following);
        ButterKnife.bind(this);
        initViews();

        if (TYPE_GET_FOLLOWER.equals(mType)) {
            getFollowers();
        } else {
            getFriends();
        }
    }

    @Override
    protected void initViews() {
        if (TYPE_GET_FOLLOWER.equals(mType)) {
            mToolbar.setTitle(getString(R.string.title_follower));
        } else {
            mToolbar.setTitle(getString(R.string.title_following));
        }
        mToolbar.setElevation(getResources().getDimension(R.dimen.elevation));
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mFollowingAdapter = new FollowingAdapter(this);
        mFollowingAdapter.setMore(R.layout.view_load_more,
                new RecyclerArrayAdapter.OnMoreListener() {
                    @Override
                    public void onMoreShow() {
                        getFriends();
                    }

                    @Override
                    public void onMoreClick() {
                    }
                });
        mFollowingAdapter.setError(R.layout.view_load_more_error,
                new RecyclerArrayAdapter.OnErrorListener() {
                    @Override
                    public void onErrorShow() {

                    }

                    @Override
                    public void onErrorClick() {
                        mFollowingAdapter.resumeMore();
                    }
                });
        mFollowingAdapter.setNoMore(R.layout.view_no_more);
        mFollowingAdapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                UserRes userRes = mFollowingAdapter.getItem(position);
                if (TYPE_GET_AT_FOLLOWING.equals(mType)) {
                    Intent intent = new Intent();
                    intent.putExtra(EXTRAS_USER_NAME, userRes.getName());
                    setResult(RESULT_OK, intent);
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(ProfileActivity.BUNDLE_USER, userRes);
                    Router.build(Constants.Router.PROFILE).extras(bundle).go(UserListActivity.this);
                }
                finish();
            }
        });

        DividerDecoration itemDecoration =
                new DividerDecoration(getResources().getColor(R.color.divider_line_color),
                        Math.round(getResources().getDimension(R.dimen.divider_line_size)),
                        Math.round(getResources().getDimension(R.dimen.margin_huge)), 0);
        itemDecoration.setDrawLastItem(true);
        itemDecoration.setDrawHeaderFooter(false);
        mRecyclerView.addItemDecoration(itemDecoration);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapterWithProgress(mFollowingAdapter);
    }

    private void getFriends() {
        UserRequest request = new UserRequest();
        request.setMode(Constants.RequestParams.MODE_LITE);
        request.setPage(String.valueOf(++mPage));
        request.setId(mUser == null ? null : mUser.getId());

        HttpRequestFactory.getInstance()
                .getFriends(request, new SimpleHttpRequestCallback<List<UserRes>>() {
                    @Override
                    public void onSuccess(List<UserRes> responseData) {
                        mFollowingAdapter.addAll(responseData);
                        mFollowingAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFail(ApiException apiException) {
                        UIUtils.showToast(UserListActivity.this, apiException.getErrorMessage());
                    }
                });
    }

    private void getFollowers() {
        UserRequest request = new UserRequest();
        request.setMode(Constants.RequestParams.MODE_LITE);
        request.setPage(String.valueOf(++mPage));
        request.setId(mUser == null ? null : mUser.getId());

        HttpRequestFactory.getInstance()
                .getFollowers(request, new SimpleHttpRequestCallback<List<UserRes>>() {
                    @Override
                    public void onSuccess(List<UserRes> responseData) {
                        mFollowingAdapter.addAll(responseData);
                        mFollowingAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFail(ApiException apiException) {
                        UIUtils.showToast(UserListActivity.this, apiException.getErrorMessage());
                    }
                });
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
