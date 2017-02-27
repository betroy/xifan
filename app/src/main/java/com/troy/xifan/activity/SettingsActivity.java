package com.troy.xifan.activity;

import android.content.res.Resources;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.chenenyu.router.Router;
import com.chenenyu.router.annotation.Route;
import com.troy.xifan.App;
import com.troy.xifan.R;
import com.troy.xifan.config.Constants;
import com.troy.xifan.manage.UserHolder;
import com.troy.xifan.util.UIUtils;
import com.troy.xifan.util.Utils;

/**
 * Created by chenlongfei on 2017/1/25.
 */
@Route(Constants.Router.SETTINGS)
public class SettingsActivity extends BaseActivity {
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.layout_logout) View mViewLogout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        initViews();
    }

    @Override
    protected void initViews() {
        mToolbar.setTitle(getString(R.string.title_settings));
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initFragment();
        initListeners();
    }

    private void initListeners() {
        mViewLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIUtils.showDialog(SettingsActivity.this, getString(R.string.text_logout_tips),
                        new UIUtils.OnDialogListener() {
                            @Override
                            public void onConfirm() {
                                UserHolder.getInstance().cleanUser();
                                Router.build(Constants.Router.LOGIN).go(SettingsActivity.this);
                                App.getInstance().cleanActivityList();
                            }

                            @Override
                            public void onCancel() {

                            }
                        });
            }
        });
    }

    private void initFragment() {
        getFragmentManager().beginTransaction()
                .replace(R.id.content, new SettingsFragment())
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

    public static class SettingsFragment extends PreferenceFragment
            implements Preference.OnPreferenceClickListener {
        private Resources mResources;
        private Preference mCleanCachePre;
        private Preference mFeedbackPre;
        private Preference mCheckUpdatePre;
        private Preference mAboutPre;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            initPreference();
        }

        private void initPreference() {
            mResources = getResources();
            addPreferencesFromResource(R.xml.preference);
            //mCleanCachePre = findPreference(mResources.getString(R.string.text_clean_cache));
            //mFeedbackPre = findPreference(mResources.getString(R.string.text_feedback));
            //mCheckUpdatePre = findPreference(mResources.getString(R.string.text_check_update));
            mAboutPre = findPreference(getString(R.string.text_about));
            mAboutPre.setOnPreferenceClickListener(this);
        }

        @Override
        public boolean onPreferenceClick(Preference preference) {
            String key = preference.getKey();
            if (getString(R.string.text_about).equals(key)) {
                UIUtils.showToast(getActivity(), Utils.getVersionName());
            }
            return false;
        }
    }
}
