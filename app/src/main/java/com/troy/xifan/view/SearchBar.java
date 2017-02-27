package com.troy.xifan.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import com.troy.xifan.R;

/**
 * Created by chenlongfei on 2017/1/12.
 */

public class SearchBar extends Toolbar {
    public SearchBar(Context context) {
        super(context);
    }

    public SearchBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    private void initViews() {
        inflate(getContext(), R.layout.merge_searchbar, this);
    }
}
