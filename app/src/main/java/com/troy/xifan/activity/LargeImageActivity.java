package com.troy.xifan.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.chenenyu.router.annotation.Route;
import com.troy.xifan.R;
import com.troy.xifan.config.Constants;

/**
 * Created by Lenovo on 2017/3/8.
 */
@Route(Constants.Router.LARGE_IMAGE)
public class LargeImageActivity extends BaseActivity {
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.image_large_image) ImageView mImageLargeImage;

    public static final String BUNDLE_IMAGE_URL = "image_url";
    private String mUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_large_image);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mUrl = bundle.getString(BUNDLE_IMAGE_URL);
        }
        initViews();
    }

    @Override
    protected void initViews() {
        mToolbar.setTitle(R.string.title_large_image);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Glide.with(this).load(mUrl).into(mImageLargeImage);
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
