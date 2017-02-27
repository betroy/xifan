package com.troy.xifan.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import butterknife.BindView;
import com.chenenyu.router.Router;
import com.troy.xifan.R;
import com.troy.xifan.adapter.SearchPagerAdapter;
import com.troy.xifan.config.Constants;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenlongfei on 2016/12/1.
 */

public class SearchFragment extends BaseFragment {
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.tab_layout) TabLayout mTabLayout;
    @BindView(R.id.view_pager) ViewPager mViewPager;

    private List<Fragment> mFragments = new ArrayList<>();
    private String[] mTabTitles;

    public static Fragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initFragments();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void initFragments() {
        mFragments.add(TrendFragment.newInstance());
        mFragments.add(PublicTimelineFragment.newInstance());
    }

    @Override
    protected void initViews() {
        mToolbar.setTitle(getString(R.string.title_search));
        mToolbar.setNavigationIcon(R.mipmap.ic_action_search);
        mTabTitles = new String[] {
                getString(R.string.title_tab_topic), getString(R.string.title_tab_public_timeline)
        };
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(
                new SearchPagerAdapter(getChildFragmentManager(), mFragments, mTabTitles));
        mTabLayout.setupWithViewPager(mViewPager);
        initListeners();
    }

    private void initListeners() {
        mToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Router.build(Constants.Router.SEARCH).go(getActivity());
            }
        });
    }

    @Override
    protected int getContentViewResId() {
        return R.layout.fragment_search;
    }

    @Override
    public void onRefresh() {

    }
}
