package com.troy.xifan.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import com.chenenyu.router.annotation.Route;
import com.troy.xifan.R;
import com.troy.xifan.config.Constants;
import com.troy.xifan.fragment.ProfileFragment;

/**
 * Created by chenlongfei on 2017/2/14.
 */
@Route({ Constants.Router.PROFILE, Constants.Router.SCHEME + Constants.Router.PROFILE })
public class ProfileActivity extends BaseActivity {
    public static final String BUNDLE_USER = "user";
    public static final String BUNDLE_USER_ID = "userid";
    private Bundle mUserBundle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserBundle = getIntent().getExtras();
        setContentView(R.layout.activity_profile);
        initFragment();
    }

    @Override
    protected void initViews() {

    }

    private void initFragment() {
        ProfileFragment profileFragment = new ProfileFragment();
        profileFragment.setArguments(mUserBundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, profileFragment)
                .commit();
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
