package com.troy.xifan.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import butterknife.BindView;
import com.troy.xifan.R;
import com.troy.xifan.adapter.MessagePagerAdapter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenlongfei on 2016/12/1.
 */

public class MessageFragment extends BaseFragment {
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.tab_layout) TabLayout mTabLayout;
    @BindView(R.id.view_pager) ViewPager mViewPager;

    private List<Fragment> mFragments = new ArrayList<>();
    private String[] mTabTitles;

    public static Fragment newInstance() {
        return new MessageFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initFragments();
    }

    private void initFragments() {
        mFragments.add(MentionMessageFragment.newInstance());
        mFragments.add(DirectMessageFragment.newInstance());
    }

    @Override
    public void initViews() {
        mToolbar.setTitle(getString(R.string.title_message));
        mTabTitles = new String[] {
                getString(R.string.title_tab_mention_msg), getString(R.string.title_tab_direct_msg)
        };

        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setAdapter(
                new MessagePagerAdapter(getChildFragmentManager(), mFragments, mTabTitles));
    }

    @Override
    protected int getContentViewResId() {
        return R.layout.fragment_message;
    }

    @Override
    public void onRefresh() {

    }
}
