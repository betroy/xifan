package com.troy.xifan.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import com.bumptech.glide.Glide;
import com.chenenyu.router.Router;
import com.troy.xifan.App;
import com.troy.xifan.R;
import com.troy.xifan.activity.ConversationActivity;
import com.troy.xifan.activity.ProfileActivity;
import com.troy.xifan.activity.UserListActivity;
import com.troy.xifan.adapter.ProfilePagerAdapter;
import com.troy.xifan.config.Constants;
import com.troy.xifan.http.HttpRequestFactory;
import com.troy.xifan.http.callback.SimpleHttpRequestCallback;
import com.troy.xifan.http.exception.ApiException;
import com.troy.xifan.http.request.BaseRequestParams;
import com.troy.xifan.http.request.FriendshipRequest;
import com.troy.xifan.model.response.UserRes;
import com.troy.xifan.util.SharedPreUtils;
import com.troy.xifan.util.UIUtils;
import de.hdodenhof.circleimageview.CircleImageView;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenlongfei on 2016/12/1.
 */

public class ProfileFragment extends BaseFragment {
    @BindView(R.id.collapsing_toolbar_layout) CollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.tab_layout) TabLayout mTabLayout;
    @BindView(R.id.view_pager) ViewPager mViewPager;
    @BindView(R.id.image_profile_bg) ImageView mImageProfileBackground;
    @BindView(R.id.text_user_info) TextView mTextUserInfo;
    @BindView(R.id.image_avatar) CircleImageView mImageAvatar;
    @BindView(R.id.view_dm_follow) View mViewDmFollow;
    @BindView(R.id.button_dm) Button mButtonDM;
    @BindView(R.id.button_follow) Button mButtonFollow;
    @BindView(R.id.layout_following) View mLayoutFriend;
    @BindView(R.id.layout_follower) View mLayoutFollower;
    @BindView(R.id.text_status_count) TextView mTextStatusCount;
    @BindView(R.id.text_following_count) TextView mTextFollowingCount;
    @BindView(R.id.text_follower_count) TextView mTextFollowerCount;

    private Bundle mBundle;
    private UserRes mUser;
    private String mUserId;
    private List<Fragment> mFragments = new ArrayList<>();
    private String[] mTabtitles;
    private boolean isSelf;
    private boolean isFollowing;

    public static Fragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBundle = getArguments();

        if (mBundle != null) {
            mUser = mBundle.getParcelable(ProfileActivity.BUNDLE_USER);
            mUserId = mBundle.getString(ProfileActivity.BUNDLE_USER_ID);
            isSelf = false;
        } else {
            mUser = App.getInstance().getUser();
            isSelf = true;
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if (mUser != null) {
            initFragments();
            initViews();
        } else {
            getUserInfo();
        }
    }

    private void initFragments() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(ProfileActivity.BUNDLE_USER, mUser);
        mFragments.add(UserTimelineFragment.newInstance(bundle));
        mFragments.add(UserFavoriteFragment.newInstance(bundle));
        mFragments.add(UserPhotoFragment.newInstance(bundle));
    }

    @Override
    public void initViews() {
        mToolbar.setTitle(mUser.getName());
        mCollapsingToolbarLayout.setCollapsedTitleTextColor(
                getResources().getColor(android.R.color.white));
        mCollapsingToolbarLayout.setExpandedTitleColor(
                getResources().getColor(android.R.color.transparent));
        mToolbar.inflateMenu(R.menu.menu_profile);
        mTabtitles = new String[] {
                getString(R.string.title_tab_profile_all),
                getString(R.string.title_tab_profile_favorite),
                getString(R.string.title_tab_profile_photo)
        };
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setOffscreenPageLimit(mTabtitles.length);
        mViewPager.setAdapter(
                new ProfilePagerAdapter(getChildFragmentManager(), mFragments, mTabtitles));

        Glide.with(this)
                .load(mUser.getProfile_background_image_url())
                .into(mImageProfileBackground);
        Glide.with(this).load(mUser.getProfile_image_url_large()).into(mImageAvatar);
        mTextUserInfo.setText(mUser.getGender() + " " + mUser.getLocation());
        mTextStatusCount.setText(String.valueOf(mUser.getStatuses_count()));
        mTextFollowingCount.setText(String.valueOf(mUser.getFriends_count()));
        mTextFollowerCount.setText(String.valueOf(mUser.getFollowers_count()));

        if (!isSelf) {
            Drawable backDrawable = getResources().getDrawable(R.drawable.abc_ic_ab_back_white);
            mToolbar.setNavigationIcon(backDrawable);
            mViewDmFollow.setVisibility(View.VISIBLE);
            isExistFriendship();
        } else {
            mViewDmFollow.setVisibility(View.GONE);
        }

        initListeners();
    }

    private void initListeners() {
        mButtonDM.setOnClickListener(mClickListener);
        mButtonFollow.setOnClickListener(mClickListener);
        mLayoutFriend.setOnClickListener(mClickListener);
        mLayoutFollower.setOnClickListener(mClickListener);
        mToolbar.setOnMenuItemClickListener(mOnMenuItemClickListener);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }

    @Override
    protected int getContentViewResId() {
        return R.layout.fragment_profile;
    }

    @Override
    public void onRefresh() {

    }

    private Toolbar.OnMenuItemClickListener mOnMenuItemClickListener =
            new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.action_settings:
                            Router.build(Constants.Router.SETTINGS).go(getActivity());
                            break;
                    }
                    return true;
                }
            };

    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.button_dm:
                    Bundle bundle = new Bundle();
                    if (mUser != null) {
                        bundle.putString(ConversationActivity.BUNDLE_OTHER_USER_ID, mUser.getId());
                    } else if (mUserId != null) {
                        bundle.putString(ConversationActivity.BUNDLE_OTHER_USER_ID, mUserId);
                    }
                    Router.build(Constants.Router.CONVERSATION).extras(bundle).go(getActivity());
                    break;
                case R.id.button_follow:
                    if (isFollowing) {
                        destroyFriendshiop();
                    } else {
                        createFriendshiop();
                    }
                    break;
                case R.id.layout_following:
                    Bundle followingBundle = new Bundle();
                    followingBundle.putParcelable(UserListActivity.BUNDLE_USER, mUser);
                    followingBundle.putString(UserListActivity.BUNLDE_TYPE,
                            UserListActivity.TYPE_GET_FOLLOWING);
                    Router.build(Constants.Router.USER_LIST)
                            .extras(followingBundle)
                            .go(getActivity());
                    break;
                case R.id.layout_follower:
                    Bundle followerBundle = new Bundle();
                    followerBundle.putParcelable(UserListActivity.BUNDLE_USER, mUser);
                    followerBundle.putString(UserListActivity.BUNLDE_TYPE,
                            UserListActivity.TYPE_GET_FOLLOWER);
                    Router.build(Constants.Router.USER_LIST)
                            .extras(followerBundle)
                            .go(getActivity());
                    break;
            }
        }
    };

    private void getUserInfo() {
        BaseRequestParams request = new BaseRequestParams();
        if (mUserId != null) {
            request.setId(mUserId);
        }

        HttpRequestFactory.getInstance()
                .getUserInfo(request, new SimpleHttpRequestCallback<UserRes>() {
                    @Override
                    public void onSuccess(UserRes userRes) {
                        mUser = userRes;
                        initFragments();
                        initViews();
                    }

                    @Override
                    public void onFail(ApiException apiException) {
                        UIUtils.showToast(getActivity(), apiException.getErrorMessage());
                    }
                });
    }

    private void isExistFriendship() {
        String selfUserId = SharedPreUtils.getString(Constants.SharedPreferences.KEY_USER_ID);
        FriendshipRequest request = new FriendshipRequest();
        request.setUser_a(selfUserId);
        request.setUser_b(mUser.getId());

        HttpRequestFactory.getInstance()
                .isExistFriendship(request, new SimpleHttpRequestCallback<String>() {
                    @Override
                    public void onSuccess(String responseData) {
                        try {
                            isFollowing = Boolean.valueOf(responseData);
                            if (isFollowing) {
                                mButtonFollow.setText(R.string.btn_text_following);
                            } else {
                                mButtonFollow.setText(R.string.btn_text_unfollowing);
                            }
                        } catch (IllegalArgumentException e) {
                        }
                    }

                    @Override
                    public void onFail(ApiException apiException) {
                        UIUtils.showToast(getActivity(), apiException.getErrorMessage());
                    }
                });
    }

    private void destroyFriendshiop() {
        BaseRequestParams request = new BaseRequestParams();
        if (mUser != null) {
            request.setId(mUser.getId());
        } else if (mUserId != null) {
            request.setId(mUserId);
        }
        request.setMode(Constants.RequestParams.MODE_LITE);

        HttpRequestFactory.getInstance()
                .destroyFriendship(request, new SimpleHttpRequestCallback<UserRes>() {
                    @Override
                    public void onSuccess(UserRes responseData) {
                        mButtonFollow.setText(R.string.btn_text_unfollowing);
                        isFollowing = false;
                    }

                    @Override
                    public void onFail(ApiException apiException) {
                        UIUtils.showToast(getActivity(), apiException.getErrorMessage());
                    }
                });
    }

    private void createFriendshiop() {
        BaseRequestParams request = new BaseRequestParams();
        if (mUser != null) {
            request.setId(mUser.getId());
        } else if (mUserId != null) {
            request.setId(mUserId);
        }
        request.setMode(Constants.RequestParams.MODE_LITE);

        HttpRequestFactory.getInstance()
                .createFriendship(request, new SimpleHttpRequestCallback<UserRes>() {
                    @Override
                    public void onSuccess(UserRes responseData) {
                        mButtonFollow.setText(R.string.btn_text_following);
                        isFollowing = true;
                    }

                    @Override
                    public void onFail(ApiException apiException) {
                        UIUtils.showToast(getActivity(), apiException.getErrorMessage());
                    }
                });
    }
}
